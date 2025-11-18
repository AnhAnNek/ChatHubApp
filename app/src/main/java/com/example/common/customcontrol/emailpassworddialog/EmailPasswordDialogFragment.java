package com.example.common.customcontrol.emailpassworddialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.R;
import com.example.user.Validator;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * Fragment để hiển thị hộp thoại nhập email và mật khẩu.
 *
 * Tác giả: Trần Văn An
 */
public class EmailPasswordDialogFragment extends AppCompatDialogFragment {
    public static final String TAG = EmailPasswordDialogFragment.class.getSimpleName();

    private final EmailPasswordDialogModel model;
    private TextView txvTitle;
    private TextView txvSubTitle;
    private TextInputLayout tilEmail;
    private TextInputEditText edtEmail;
    private TextInputLayout tilPassword;
    private TextInputEditText edtPassword;
    private MaterialButton btnSubmit;

    /**
     * Constructor nhận vào EmailPasswordDialogModel để thiết lập nội dung hộp thoại.
     *
     * @param model Dữ liệu cho hộp thoại
     *
     * Tác giả: Trần Văn An
     */
    public EmailPasswordDialogFragment(EmailPasswordDialogModel model) {
        super();
        this.model = model;
    }

    /**
     * Phương thức này được gọi khi hộp thoại được tạo ra.
     * Nó tạo và trả về một Dialog tùy chỉnh.
     *
     * @param savedInstanceState Nếu không null, fragment này được tạo lại từ một trạng thái lưu trữ trước đó.
     * @return Đối tượng Dialog đại diện cho hộp thoại được tạo.
     *
     * Tác giả: Trần Văn An
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.email_password_dialog, null);

        AlertDialog dialog = new AlertDialog.Builder(requireActivity())
                .setView(view)
                .create();

        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Khởi tạo các view từ layout
        txvTitle = view.findViewById(R.id.txv_title);
        txvSubTitle = view.findViewById(R.id.txv_sub_title);
        tilEmail = view.findViewById(R.id.til_email);
        edtEmail = view.findViewById(R.id.edit_email);
        tilPassword = view.findViewById(R.id.til_password);
        edtPassword = view.findViewById(R.id.edt_password);
        btnSubmit = view.findViewById(R.id.btn_submit);

        // Thiết lập dữ liệu cho các view
        txvTitle.setText(model.getTitle());
        txvSubTitle.setText(model.getSubTitle());
        edtEmail.setText(model.getEmail());
        edtPassword.setText(model.getPassword());

        // Lắng nghe sự thay đổi của email để kiểm tra tính hợp lệ
        edtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String email = s.toString();
                String error = Validator.validateEmail(email);
                if (error != null) {
                    tilEmail.setError(error);
                    return;
                }
                tilEmail.setError(null);

                Log.i(TAG, "onTextChanged (email): " + email);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Lắng nghe sự thay đổi của password để kiểm tra tính hợp lệ
        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = s.toString();
                String error = Validator.validatePassword(password);
                if (error != null) {
                    tilPassword.setError(error);
                    return;
                }
                tilPassword.setError(null);

                Log.i(TAG, "onTextChanged (password): " + password);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Thiết lập hành động cho nút submit
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence emailText = edtEmail.getText();
                CharSequence passwordText = edtPassword.getText();

                String email = emailText != null ? emailText.toString() : "";
                String password = passwordText != null ? passwordText.toString() : "";

                model.getSubmitButtonClickListener().apply(email, password);
                dialog.dismiss();
            }
        });

        return dialog;
    }
}
