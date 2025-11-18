package com.example.common.customcontrol;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chaos.view.PinView;
import com.example.common.customcontrol.customalertdialog.AlertDialogModel;
import com.example.common.customcontrol.customalertdialog.CustomAlertDialog;
import com.example.common.customcontrol.inputdialogfragment.CustomInputDialog;
import com.example.common.customcontrol.inputdialogfragment.InputDialogModel;
import com.example.common.customcontrol.snackbar.CustomSnackbar;
import com.example.common.customcontrol.snackbar.SnackbarModel;
import com.example.infrastructure.Utils;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.Date;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/**
 * Lớp CustomBindingAdapters chứa các phương thức binding adapter cho các thành phần UI khác nhau.
 *
 * Tác giả: Trần Văn An
 */
public class CustomBindingAdapters {

    /**
     * Hiển thị thông báo toast thành công.
     *
     * @param view    View hiện tại.
     * @param message Tin nhắn cần hiển thị.
     *
     * Tác giả: Trần Văn An
     */
    @BindingAdapter({"successToastMessage"})
    public static void showSuccessToast(View view, String message) {
        if (message != null && view.getContext() instanceof Activity) {
            Activity activity = (Activity) view.getContext();
            CustomToast.showSuccessToast(activity, message);
        }
    }

    /**
     * Hiển thị thông báo toast lỗi.
     *
     * @param view    View hiện tại.
     * @param message Tin nhắn cần hiển thị.
     *
     * Tác giả: Trần Văn An
     */
    @BindingAdapter({"errorToastMessage"})
    public static void showErrorToast(View view, String message) {
        if (message != null && view.getContext() instanceof Activity) {
            Activity activity = (Activity) view.getContext();
            CustomToast.showErrorToast(activity, message);
        }
    }

    /**
     * Hiển thị snackbar tùy chỉnh.
     *
     * @param view  View hiện tại.
     * @param model Model chứa dữ liệu snackbar.
     *
     * Tác giả: Trần Văn An
     */
    @BindingAdapter("customSnackbar")
    public static void showCustomSnackbar(View view, SnackbarModel model) {
        if (model != null && view.getContext() instanceof Activity) {
            Activity activity = (Activity) view.getContext();
            CustomSnackbar.show(activity, model);
        }
    }

    /**
     * Hiển thị hộp thoại cảnh báo tùy chỉnh.
     *
     * @param view  View hiện tại.
     * @param model Model chứa dữ liệu hộp thoại.
     *
     * Tác giả: Trần Văn An
     */
    @BindingAdapter("customAlertDialog")
    public static void showCustomAlertDialog(View view, AlertDialogModel model) {
        if (model != null && view.getContext() instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            CustomAlertDialog.show(activity, model);
        }
    }

    /**
     * Hiển thị hộp thoại nhập liệu tùy chỉnh.
     *
     * @param view  View hiện tại.
     * @param model Model chứa dữ liệu hộp thoại.
     *
     * Tác giả: Trần Văn An
     */
    @BindingAdapter("customInputDialog")
    public static void showCustomInputDialog(View view, InputDialogModel model) {
        if (model != null && view.getContext() instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            CustomInputDialog.show(activity, model);
        }
    }

    /**
     * Đặt thời gian đã trôi qua cho TextView.
     *
     * @param textView TextView cần đặt.
     * @param date     Ngày cần tính.
     *
     * Tác giả: Trần Văn An
     */
    @BindingAdapter("timeAgo")
    public static void setTimeAgo(TextView textView, Date date) {
        String timeAgo = Utils.calculateTimeAgo(date);
        textView.setText(timeAgo);
    }

    /**
     * Kích hoạt animation cho PinView.
     *
     * @param pinView   PinView cần đặt.
     * @param animation True để kích hoạt animation.
     *
     * Tác giả: Trần Văn An
     */
    @BindingAdapter("animation")
    public static void setAnimation(PinView pinView, boolean animation) {
        if (animation) {
            pinView.setAnimationEnable(true);
        }
    }

    /**
     * Tự động hiển thị hoặc ẩn bàn phím.
     *
     * @param view             View hiện tại.
     * @param isKeyboardVisible True để hiển thị bàn phím, false để ẩn.
     *
     * Tác giả: Trần Văn An
     */
    @BindingAdapter("app:showKeyboardIfTrue")
    public static void autoShowKeyboard(View view, boolean isKeyboardVisible) {
        if (isKeyboardVisible) {
            showKeyboard(view);
        } else {
            hideKeyboard(view);
        }
    }

    /**
     * Thiết lập over-scroll cho RecyclerView.
     *
     * @param recyclerView RecyclerView cần đặt.
     * @param setup        True để thiết lập over-scroll.
     *
     * Tác giả: Trần Văn An
     */
    @BindingAdapter("setupOverScroll")
    public static void setupOverScroll(RecyclerView recyclerView, boolean setup) {
        if (setup) {
            OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
        }
    }

    /**
     * Thiết lập over-scroll cho GridView.
     *
     * @param gridView GridView cần đặt.
     * @param setup    True để thiết lập over-scroll.
     *
     * Tác giả: Trần Văn An
     */
    @BindingAdapter("setupOverScroll")
    public static void setupOverScroll(GridView gridView, boolean setup) {
        if (setup) {
            OverScrollDecoratorHelper.setUpOverScroll(gridView);
        }
    }

    /**
     * Thiết lập over-scroll cho ListView.
     *
     * @param listView ListView cần đặt.
     * @param setup    True để thiết lập over-scroll.
     *
     * Tác giả: Trần Văn An
     */
    @BindingAdapter("setupOverScroll")
    public static void setupOverScroll(ListView listView, boolean setup) {
        if (setup) {
            OverScrollDecoratorHelper.setUpOverScroll(listView);
        }
    }

    /**
     * Thiết lập over-scroll cho ScrollView.
     *
     * @param scrollView ScrollView cần đặt.
     * @param setup      True để thiết lập over-scroll.
     *
     * Tác giả: Trần Văn An
     */
    @BindingAdapter("setupOverScroll")
    public static void setupOverScroll(ScrollView scrollView, boolean setup) {
        if (setup) {
            OverScrollDecoratorHelper.setUpOverScroll(scrollView);
        }
    }

    /**
     * Thiết lập over-scroll cho HorizontalScrollView.
     *
     * @param horizontalScrollView HorizontalScrollView cần đặt.
     * @param setup                True để thiết lập over-scroll.
     *
     * Tác giả: Trần Văn An
     */
    @BindingAdapter("setupOverScroll")
    public static void setupOverScroll(HorizontalScrollView horizontalScrollView, boolean setup) {
        if (setup) {
            OverScrollDecoratorHelper.setUpOverScroll(horizontalScrollView);
        }
    }

    /**
     * Tải hình ảnh từ Uri vào RoundedImageView.
     *
     * @param roundedImageView RoundedImageView cần đặt.
     * @param uri              Uri của hình ảnh.
     *
     * Tác giả: Trần Văn An
     */
    @BindingAdapter("loadImageWithUri")
    public static void loadImage(RoundedImageView roundedImageView, Uri uri) {
        Context context = roundedImageView.getContext();
        if (uri != null && context instanceof AppCompatActivity) {
            Glide
                    .with(context)
                    .load(uri)
                    .into(roundedImageView);
        }
    }

    /**
     * Hiển thị bàn phím.
     *
     * @param view View hiện tại.
     *
     * Tác giả: Trần Văn An
     */
    private static void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * Ẩn bàn phím.
     *
     * @param view View hiện tại.
     *
     * Tác giả: Trần Văn An
     */
    private static void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
