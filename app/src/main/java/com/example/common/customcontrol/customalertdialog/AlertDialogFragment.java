package com.example.common.customcontrol.customalertdialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.R;
import com.google.android.material.button.MaterialButton;

import java.util.function.Consumer;

/**
 * AlertDialogFragment là một lớp tùy chỉnh để hiển thị một hộp thoại cảnh báo với tiêu đề,
 * thông điệp và hai nút tùy chọn.
 *
 * Tác giả: Trần Văn An
 */
public class AlertDialogFragment extends AppCompatDialogFragment {

    public static final String TAG = AlertDialogFragment.class.getSimpleName();

    // Model chứa các thông tin hiển thị trong dialog
    private final AlertDialogModel model;

    private TextView txvTitle;
    private TextView txvMessage;
    private MaterialButton btnPositive;
    private MaterialButton btnNegative;

    /**
     * Hàm khởi tạo cho AlertDialogFragment.
     *
     * @param model Model chứa dữ liệu cho dialog
     *
     * Tác giả: Trần Văn An
     */
    public AlertDialogFragment(AlertDialogModel model) {
        super();
        this.model = model;
    }

    /**
     * Phương thức onCreateDialog tạo và thiết lập dialog.
     *
     * @param savedInstanceState Trạng thái đã lưu của fragment, nếu có
     * @return Đối tượng Dialog đã được tạo và thiết lập
     *
     * Tác giả: Trần Văn An
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_alert_dialog, null);

        // Tạo AlertDialog và thiết lập view
        AlertDialog dialog = new AlertDialog.Builder(requireActivity())
                .setView(view)
                .create();
        dialog.setCanceledOnTouchOutside(false);

        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        initializeViews(view);
        setupContents();

        return dialog;
    }

    /**
     * Phương thức initializeViews khởi tạo các view thành phần của dialog.
     *
     * @param view View gốc của dialog
     *
     * Tác giả: Trần Văn An
     */
    private void initializeViews(View view) {
        txvTitle = view.findViewById(R.id.txv_title);
        txvMessage = view.findViewById(R.id.txv_message);
        btnPositive = view.findViewById(R.id.btn_positive);
        btnNegative = view.findViewById(R.id.btn_negative);
    }

    /**
     * Phương thức setupContents thiết lập nội dung cho các thành phần của dialog.
     *
     * Tác giả: Trần Văn An
     */
    private void setupContents() {
        txvTitle.setText(model.getTitle());
        txvMessage.setText(model.getMessage());
        setupButton(btnPositive, model.getPositiveBtnTitle(), model.getPositiveButtonClickListener());
        setupButton(btnNegative, model.getNegativeBtnTitle(), model.getNegativeButtonClickListener());
    }

    /**
     * Phương thức setupButton thiết lập nội dung và sự kiện cho một nút bấm.
     *
     * @param button Nút cần thiết lập
     * @param title Tiêu đề của nút
     * @param clickListener Sự kiện khi nút được bấm
     *
     * Tác giả: Trần Văn An
     */
    private void setupButton(Button button, String title, Consumer<Void> clickListener) {
        if (title != null) {
            button.setVisibility(View.VISIBLE);
            button.setText(title);
            button.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.accept(null);
                }
                dismiss();
            });
        }
    }
}
