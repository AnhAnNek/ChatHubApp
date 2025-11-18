package com.example.chat.textanalyzer;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.IOException;

public class RecognizeUtils {
    private static final String TAG = RecognizeUtils.class.getSimpleName();

    /**

     Chuyển đổi Uri thành Bitmap.
     @param context Context của ứng dụng.
     @param uri Uri của hình ảnh cần chuyển đổi.
     @return Bitmap được tạo từ Uri.
     Tác giả: Văn Hoàng
     */
    public static Bitmap uriToBitmap(Context context, Uri uri) {
        try {
            ContentResolver resolver = context.getContentResolver();
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(resolver, uri);
            return bitmap;
        } catch (IOException e) {
            Log.e(TAG, "Error converting Uri to Bitmap: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
