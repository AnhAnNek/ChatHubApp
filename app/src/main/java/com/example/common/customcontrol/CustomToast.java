package com.example.common.customcontrol;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;

import com.example.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;

import java.util.function.Consumer;

/**
 * Lớp CustomToast cung cấp các phương thức tiện ích để hiển thị thông báo tùy chỉnh
 * dưới dạng Toast với các biểu tượng và văn bản khác nhau.
 *
 * Tác giả: Trần Văn An
 */
public class CustomToast {

    private static final int SHORT_MESSAGE_LENGTH_THRESHOLD = 20;

    /**
     * Hiển thị một Toast thành công với thông báo và biểu tượng thành công.
     *
     * @param activity Activity hiện tại để hiển thị Toast
     * @param message  Thông báo muốn hiển thị
     *
     * Tác giả: Trần Văn An
     */
    public static void showSuccessToast(Activity activity, String message) {
        CustomToast.showToastMessage(activity, message, icon -> {
            icon.setBackground(ResourcesCompat.getDrawable(activity.getResources(),
                    R.drawable.ic_circle_check_solid,
                    activity.getTheme()));
        });
    }

    /**
     * Hiển thị một Toast lỗi với thông báo và biểu tượng lỗi.
     *
     * @param activity Activity hiện tại để hiển thị Toast
     * @param message  Thông báo muốn hiển thị
     *
     * Tác giả: Trần Văn An
     */
    public static void showErrorToast(Activity activity, String message) {
        CustomToast.showToastMessage(activity, message, icon -> {
            icon.setBackground(ResourcesCompat.getDrawable(activity.getResources(),
                    R.drawable.ic_circle_xmark_solid,
                    activity.getTheme()));
        });
    }

    /**
     * Hiển thị một Toast tùy chỉnh với thông báo và biểu tượng được cung cấp.
     *
     * @param activity   Activity hiện tại để hiển thị Toast
     * @param message    Thông báo muốn hiển thị
     * @param customIcon Consumer để tùy chỉnh biểu tượng của Toast
     *
     * Tác giả: Trần Văn An
     */
    public static void showToastMessage(Activity activity,
                                        String message,
                                        Consumer<ImageView> customIcon) {
        int duration = getDurationByMessageLength(message);
        showToastMessage(activity, duration, customIcon, messageTxv -> messageTxv.setText(message));
    }

    /**
     * Hiển thị một Toast tùy chỉnh với thời lượng, biểu tượng và thông báo được cung cấp.
     *
     * @param activity     Activity hiện tại để hiển thị Toast
     * @param duration     Thời lượng của Toast
     * @param customIcon   Consumer để tùy chỉnh biểu tượng của Toast
     * @param customMessage Consumer để tùy chỉnh thông báo của Toast
     *
     * Tác giả: Trần Văn An
     */
    public static void showToastMessage(Activity activity,
                                        @BaseTransientBottomBar.Duration int duration,
                                        Consumer<ImageView> customIcon,
                                        Consumer<TextView> customMessage) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast_message,
                activity.findViewById(R.id.toastLayoutRoot));

        ImageView icon = layout.findViewById(R.id.icon);
        customIcon.accept(icon);

        TextView txvMessage = layout.findViewById(R.id.txv_message);
        customMessage.accept(txvMessage);

        Toast toast = new Toast(activity.getApplication());
        toast.setView(layout);
        toast.setDuration(duration);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 100);
        toast.setMargin(0, 0.05f);
        toast.show();
    }

    /**
     * Xác định thời lượng của Toast dựa trên độ dài của thông báo.
     *
     * @param message Thông báo muốn hiển thị
     * @return Thời lượng của Toast
     *
     * Tác giả: Trần Văn An
     */
    private static @BaseTransientBottomBar.Duration int getDurationByMessageLength(String message) {
        if (isShortMessage(message)) {
            return Toast.LENGTH_SHORT;
        } else {
            return Toast.LENGTH_LONG;
        }
    }

    /**
     * Kiểm tra xem thông báo có phải là ngắn hay không dựa trên độ dài của nó.
     *
     * @param message Thông báo muốn kiểm tra
     * @return true nếu thông báo ngắn, false nếu dài
     *
     * Tác giả: Trần Văn An
     */
    private static boolean isShortMessage(String message) {
        return message.length() <= SHORT_MESSAGE_LENGTH_THRESHOLD;
    }
}
