package com.example.habitrack;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by sshussai on 11/17/17.
 */

/**
 * Handles the acceptance and compression of Images
 */
public class ImageHandler {

    //public static class Compressor extends AsyncTask<Bitmap, Void, String> {
    public static class Compressor extends AsyncTask<HabitEvent, Void, Void> {
        private final int COMPRESSION_QUALITY = 100;
        private String encodedImage;
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();

        @Override
        protected Void doInBackground(HabitEvent... habitEvents) {
            for(HabitEvent he : habitEvents){
                Bitmap photo = he.getDecodedPhoto();
                he.setDecodedPhoto(null);
                photo.compress(Bitmap.CompressFormat.PNG, COMPRESSION_QUALITY, byteArrayBitmapStream);
                byte[] b = byteArrayBitmapStream.toByteArray();
                encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                he.setEncodedPhoto(encodedImage);
            }
            return null;
        }
    }

    /**
     * Decompresses the image
     */
    public static class Decompressor extends AsyncTask<String, Void, Bitmap>{

        private Bitmap decodedByte;

        // Code taken from: http://mobile.cs.fsu.edu/converting-images-to-json-objects/
        @Override
        protected Bitmap doInBackground(String... strings) {
            for (String stringPicture : strings) {
                byte[] decodedString = Base64.decode(stringPicture, Base64.DEFAULT);

                decodedByte = BitmapFactory.decodeByteArray(decodedString,
                        0, decodedString.length);
            }
            return decodedByte;
        }
    }
}
