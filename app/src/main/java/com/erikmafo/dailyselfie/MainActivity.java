package com.erikmafo.dailyselfie;

import android.app.AlarmManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements
        SelfieGalleryFragment.Listener, ShowSelfieFragment.Listener {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String TAG = MainActivity.class.getSimpleName();

    private SelfieGalleryFragment mSelfieGalleryFragment;
    private AlarmManager mAlarmManager;

    private SelfieDatabaseHelper mSelfieDatabaseHelper;

    private long mAlarmInterval =  2 * 60 * 1000L;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSelfieDatabaseHelper = setUpDatabase();

        mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        createRepeatingAlarm(mAlarmInterval);

        if (savedInstanceState == null) {
            Selfie[] selfieArray = mSelfieDatabaseHelper.getSelfies();

            mSelfieGalleryFragment = SelfieGalleryFragment.newInstance(selfieArray);

            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.main_content, mSelfieGalleryFragment)
                    .commit();
        }

    }

    private SelfieDatabaseHelper setUpDatabase() {
        SelfieDatabaseHelper dbHelper = new SelfieDatabaseHelper(getApplicationContext());
        for (Selfie selfie : dbHelper.getSelfies()) {
            if (!selfie.hasImageResource()) {
                dbHelper.delete(selfie);
            }
        }
        return dbHelper;
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0 ){
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    private void createRepeatingAlarm(long interval) {

        Intent intent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        mAlarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + interval,
                interval,
                pendingIntent);
    }


    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id){
            case R.id.action_selfie:
                dispatchTakePictureIntent();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            Date date = new Date();
            File imageFile= null;
            FileOutputStream out = null;
            try {
                imageFile = createImageFile(date);
                out = new FileOutputStream(imageFile.getAbsolutePath());
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            } catch (FileNotFoundException e) {
                Log.i(TAG, "File not found: " + e.getMessage());
            }catch (IOException e) {
                Log.i(TAG, "Unable to create image file");
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (imageFile != null) {
                addSelfie(new Selfie(imageFile.getAbsolutePath(), date.getTime()));
            }
        }
    }

    private File createImageFile(Date date) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        return image;
    }



    private void addSelfie(Selfie selfie) {
        if (selfie == null) {
            throw new NullPointerException();
        }
        mSelfieDatabaseHelper.add(selfie);
        mSelfieGalleryFragment.add(selfie);
    }

    private void removeSelfie(Selfie selfie) {
        if (selfie == null) {
            throw new NullPointerException();
        }
        mSelfieDatabaseHelper.delete(selfie);
        mSelfieGalleryFragment.remove(selfie);
        boolean isDeleted = selfie.deleteResource();
        if(!isDeleted) {
            Log.i(TAG, "Unable to delete file: " + selfie.getPath());
        }
    }

    @Override
    public void onSelfieItemClick(Selfie selfieItem) {
        ShowSelfieFragment showSelfieFragment = ShowSelfieFragment.newInstance(selfieItem);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.main_content, showSelfieFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onDeleteButtonClicked(Selfie selfie) {
        getFragmentManager().popBackStack();
        removeSelfie(selfie);
    }


}
