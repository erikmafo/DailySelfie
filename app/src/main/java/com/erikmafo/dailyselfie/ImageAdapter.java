package com.erikmafo.dailyselfie;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by erikmafo on 16.11.15.
 */
public class ImageAdapter extends BaseAdapter {

    private List<Selfie> mItemList = new ArrayList<>();
    private LayoutInflater mInflater;

    public ImageAdapter(Context context, List<Selfie> itemList) {
        mItemList = itemList;
        Collections.sort(itemList);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void add(Selfie selfie) {
        mItemList.add(selfie);
    }

    public void remove(Selfie selfie) {
        mItemList.remove(selfie);
    }

    public ArrayList<Selfie> getList() {
        return new ArrayList<>(mItemList);
    }


    @Override
    public int getCount() {
        return mItemList.size();
    }

    @Override
    public Selfie getItem(int position) {
        return mItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        if (convertView == null) {
            view = mInflater.inflate(R.layout.selfie_gallery_element, null);
        } else {
            view = convertView;
        }

        ImageView imageView = (ImageView) view.findViewById(R.id.selfie_gridview_image);
        Selfie selfie = mItemList.get(position);
        selfie.loadOnTo(imageView, 220, 220);

        return view;
    }

    private Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight) {

        Bitmap bm = null;
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(path, options);

        return bm;
    }

    private int calculateInSampleSize(

            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float)height / (float)reqHeight);
            } else {
                inSampleSize = Math.round((float)width / (float)reqWidth);
            }
        }

        return inSampleSize;
    }

}

