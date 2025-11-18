package com.example.common.customcontrol;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;

import com.example.R;

/**
 * Đây là một custom dialog dùng để hiển thị một hộp thoại loading.
 *
 * Tác giả: Trần Văn An
 */
public class LoadingDialog {

    private AlertDialog dialog;

    /**
     * Hàm khởi tạo của LoadingDialog.
     * Thiết lập dialog với giao diện từ layout custom_loading_dialog.
     *
     * @param activity Activity nơi mà dialog sẽ được hiển thị
     *
     * Tác giả: Trần Văn An
     */
    public LoadingDialog(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_loading_dialog, null));

        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    /**
     * Phương thức hiển thị dialog loading.
     *
     * Tác giả: Trần Văn An
     */
    public void show() {
        dialog.show();
    }

    /**
     * Phương thức ẩn dialog loading.
     *
     * Tác giả: Trần Văn An
     */
    public void dismiss() {
        dialog.dismiss();
    }
}
