package com.example.common.customcontrol.customalertdialog;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Lớp CustomAlertDialog cung cấp phương thức tiện ích để hiển thị một hộp thoại cảnh báo tùy chỉnh.
 *
 * Tác giả: Trần Văn An
 */
public class CustomAlertDialog {

    /**
     * Phương thức tĩnh để hiển thị một AlertDialogFragment.
     *
     * @param activity Activity hiện tại từ đó hộp thoại sẽ được hiển thị
     * @param model    AlertDialogModel chứa thông tin hiển thị cho hộp thoại
     *
     * Tác giả: Trần Văn An
     */
    public static void show(AppCompatActivity activity, AlertDialogModel model) {
        AlertDialogFragment dialog = new AlertDialogFragment(model);
        dialog.show(activity.getSupportFragmentManager(), AlertDialogFragment.TAG);
    }
}
