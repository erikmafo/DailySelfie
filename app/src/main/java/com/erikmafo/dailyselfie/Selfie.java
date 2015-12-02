package com.erikmafo.dailyselfie;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import java.io.File;
import java.util.Date;

/**
 * Created by erikmafo on 18.11.15.
 */
public class Selfie implements Parcelable, Comparable<Selfie> {

    private final String mPath;
    private final Long mTimeStamp;

    public Selfie(String path, Long timeStamp) {
        mPath = path;
        mTimeStamp = timeStamp;
    }

    public String getPath() {
        return mPath;
    }

    public Long getTimeStamp() {
        return mTimeStamp;
    }

    public void loadOnTo(ImageView imageView, int reqWidth, int reqHeight) {
        // Get the dimensions of the View
        int targetW = Math.max(imageView.getWidth(), reqWidth);
        int targetH = Math.max(imageView.getHeight(), reqHeight);

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mPath, bmOptions);
        imageView.setImageBitmap(bitmap);
    }

    public boolean deleteResource() {
        File file = new File(mPath);
        return file.delete();
    }

    public boolean hasImageResource() {
        File file = new File(mPath);
        return file.exists();
    }


    private Selfie(Parcel in) {
        mPath = in.readString();
        mTimeStamp = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mPath);
        dest.writeLong(mTimeStamp);
    }

    public static final Creator CREATOR = new Creator() {

        public Selfie createFromParcel(Parcel in) {
            return new Selfie(in);
        }
        public Selfie[] newArray(int size) {
            return new Selfie[size];
        }
    };

    @Override
    public int compareTo(Selfie another) {
        int cmp = mTimeStamp.compareTo(another.getTimeStamp());
        if (cmp == 0) {
            cmp =  mPath.compareTo(another.mPath);
        }
        return cmp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Selfie selfie = (Selfie) o;

        if (!mPath.equals(selfie.mPath)) return false;
        return mTimeStamp.equals(selfie.mTimeStamp);

    }

    @Override
    public int hashCode() {
        int result = mPath.hashCode();
        result = 31 * result + mTimeStamp.hashCode();
        return result;
    }
}
