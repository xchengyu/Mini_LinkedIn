package com.example.chengyu.mini_linkedin.util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

/**
 * Created by Chengyu on 8/24/2016.
 */
public class ImageUtils {
    public static void loadImage(@NonNull Context context,
                                 @NonNull String path,
                                 @NonNull ImageView imageView) {
        Bitmap bitmap = ModelUtils.loadImageFromStorage(path);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }
    }//test

    public static void loadImage(@NonNull Context context,
                                 @NonNull Uri uri,
                                 @NonNull ImageView imageView) {
        Bitmap bitmap = BitmapFactory.decodeFile(uri.getPath());
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

}
