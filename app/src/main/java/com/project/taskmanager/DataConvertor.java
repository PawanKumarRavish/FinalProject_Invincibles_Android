package com.project.taskmanager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DataConvertor {
    public static byte[] convertImageToByteArray(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100,stream);
        return  stream.toByteArray();
    }
    public static Bitmap convertByteArrayToImage(byte [] array)
    {
        return BitmapFactory.decodeByteArray(array,0,array.length);
    }
    public static byte[] convertUriToByteArray(Uri uri, Context context) throws IOException {
        InputStream iStream =   context.getContentResolver().openInputStream(uri);

        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = iStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
}
