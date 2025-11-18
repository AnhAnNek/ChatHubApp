package com.example.common.customcontrol.inputdialogfragment;

import androidx.appcompat.app.AppCompatActivity;

import com.example.common.customcontrol.customalertdialog.AlertDialogFragment;

/**
 * Lớp tiện ích CustomInputDialog để hiển thị hộp thoại input tùy chỉnh.
 *
 * Tác giả: Trần Văn An
 */
public class CustomInputDialog {

    /**
     * Phương thức hiển thị một hộp thoại input tùy chỉnh.
     *
     * @param activity Activity hiện tại, nơi hộp thoại sẽ được hiển thị
     * @param model    Model chứa cấu hình của hộp thoại
     *
     * Tác giả: Trần Văn An
     */
    public static void show(AppCompatActivity activity, InputDialogModel model) {
        InputDialogFragment dialog = new InputDialogFragment(model);
        dialog.show(activity.getSupportFragmentManager(), AlertDialogFragment.TAG);
    }
}
