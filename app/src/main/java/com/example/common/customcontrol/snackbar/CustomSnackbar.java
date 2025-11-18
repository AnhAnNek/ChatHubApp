package com.example.common.customcontrol.snackbar;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.core.content.ContextCompat;

import com.example.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

/**
 * Lớp CustomSnackbar dùng để hiển thị một Snackbar tùy chỉnh trên màn hình.
 *
 * Tác giả: Trần Văn An
 */
public class CustomSnackbar {

    /**
     * Hiển thị Snackbar tùy chỉnh dựa trên model được cung cấp.
     *
     * @param activity Activity hiện tại để lấy context.
     * @param model Đối tượng SnackbarModel chứa thông tin cấu hình cho Snackbar.
     */
    public static void show(Activity activity, SnackbarModel model) {
        Snackbar.Callback callback = getSnackbarCallback(model);

        View view = activity.getWindow().getDecorView().getRootView();
        Snackbar snackbar = Snackbar.make(view, model.getMessage(), model.getDuration())
                .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE)
                .setBackgroundTint(ContextCompat.getColor(activity, R.color.color_toast_background))
                .addCallback(callback)
                .setActionTextColor(ContextCompat.getColor(activity, R.color.color_button_tertiary_highlight_text))
                .setAction(model.getActionText(), v -> {
                    if (model.getCustomAction() != null) {
                        model.getCustomAction().accept(null);
                    }
                });

        View snackbarView = snackbar.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackbarView.getLayoutParams();
        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        params.bottomMargin = getNavigationBarHeight(activity) + 200;
        snackbarView.setLayoutParams(params);

        snackbar.show();
    }

    /**
     * Tạo và trả về đối tượng Snackbar.Callback dựa trên model được cung cấp.
     *
     * @param model Đối tượng SnackbarModel chứa thông tin cấu hình callback.
     * @return Đối tượng Snackbar.Callback tùy chỉnh.
     */
    private static Snackbar.Callback getSnackbarCallback(SnackbarModel model) {
        return new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                if (model.getOnDismissedAction() != null) {
                    model.getOnDismissedAction().accept(event);
                }
            }

            @Override
            public void onShown(Snackbar sb) {
                super.onShown(sb);
                if (model.getOnShownAction() != null) {
                    model.getOnShownAction().accept(null);
                }
            }
        };
    }

    /**
     * Lấy chiều cao của thanh điều hướng (bottom navigation bar).
     *
     * @param context Context để lấy tài nguyên.
     * @return Chiều cao của thanh điều hướng tính bằng pixel. Trả về 0 nếu có lỗi xảy ra.
     */
    private static int getNavigationBarHeight(Context context) {
        try {
            TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(android.R.attr.actionBarSize, typedValue, true);
            return context.getResources().getDimensionPixelSize(typedValue.resourceId);
        } catch (Exception e) {
            return 0;
        }
    }
}
