package com.example.common.customcontrol.phonecredential;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.R;
import com.example.common.customcontrol.CustomToast;
import com.example.infrastructure.Utils;
import com.example.user.Validator;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.UserRepos;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

/**
 * `PhoneCredentialDialogFragment` là một `DialogFragment`
 * dùng để xác minh số điện thoại của người dùng thông qua OTP.
 *
 * Tác giả: Trần Văn An
 */
public class PhoneCredentialDialogFragment extends DialogFragment {
    public static final String TAG = PhoneCredentialDialogFragment.class.getSimpleName();

    private LinearLayout llInputPhoneNumber;
    private AutoCompleteTextView actvCountryCode;
    private TextInputLayout tilLocalNumber;
    private TextInputEditText edtLocalNumber;
    private MaterialButton btnSendOtp;
    private ProgressBar pgbSendOtp;

    private LinearLayout llVerifyOtp;
    private TextInputEditText edtOpt;
    private TextView txvResend;
    private MaterialButton btnVerify;
    private ProgressBar pgbVerify;

    private final UserRepos userRepos;
    private final AuthRepos authRepos;
    private final PhoneCredentialDialogModel model;
    private long timeoutSeconds = Utils.OTP_TIME_OUT_SECONDS;
    private String verificationId;
    private PhoneAuthProvider.ForceResendingToken resendingToken;

    /**
     * Constructor của `PhoneCredentialDialogFragment`.
     *
     * @param userRepos Đối tượng `UserRepos` để kiểm tra tồn tại của số điện thoại.
     * @param authRepos Đối tượng `AuthRepos` để gửi và xác minh OTP.
     * @param model Đối tượng `PhoneCredentialDialogModel` chứa cấu hình của dialog.
     *
     * Tác giả: Trần Văn An
     */
    public PhoneCredentialDialogFragment(UserRepos userRepos,
                                         AuthRepos authRepos,
                                         PhoneCredentialDialogModel model) {
        super();
        this.userRepos = userRepos;
        this.authRepos = authRepos;
        this.model = model;
    }

    /**
     * Tạo và trả về một `Dialog` tùy chỉnh khi `DialogFragment` được tạo.
     *
     * @param savedInstanceState Trạng thái lưu lại của `DialogFragment`.
     * @return `Dialog` được tạo.
     *
     * Tác giả: Trần Văn An
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.phone_credential_dialog, null);

        AlertDialog dialog = new AlertDialog.Builder(requireActivity())
                .setView(view)
                .create();

        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        initializeViews(view);
        setupPhoneNumberInput();
        setupVerifyOtp();

        return dialog;
    }

    /**
     * Khởi tạo các view trong `DialogFragment`.
     *
     * @param view View gốc của `DialogFragment`.
     *
     * Tác giả: Trần Văn An
     */
    public void initializeViews(View view) {
        llInputPhoneNumber = view.findViewById(R.id.ll_input_phone_number);
        actvCountryCode = view.findViewById(R.id.actv_country_code);
        tilLocalNumber = view.findViewById(R.id.til_local_number);
        edtLocalNumber = view.findViewById(R.id.edt_local_number);
        btnSendOtp = view.findViewById(R.id.btn_send_otp);
        pgbSendOtp = view.findViewById(R.id.pgb_send_otp);

        llVerifyOtp = view.findViewById(R.id.ll_verify_otp);
        edtOpt = view.findViewById(R.id.edt_otp);
        txvResend = view.findViewById(R.id.txv_resend);
        btnVerify = view.findViewById(R.id.btn_verify);
        pgbVerify = view.findViewById(R.id.pgb_verify);

        llInputPhoneNumber.setVisibility(View.VISIBLE);
        llVerifyOtp.setVisibility(View.GONE);
    }

    /**
     * Cài đặt các sự kiện và chức năng cho phần nhập số điện thoại.
     *
     * Tác giả: Trần Văn An
     */
    private void setupPhoneNumberInput() {
        actvCountryCode.setText("+84");

        edtLocalNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkPhoneNumber(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnSendOtp.setOnClickListener(v -> {
            verifyPhoneNumberAndSendOtp();
        });
    }

    /**
     * Kiểm tra sự hợp lệ của số điện thoại và gửi OTP.
     *
     * Tác giả: Trần Văn An
     */
    public void verifyPhoneNumberAndSendOtp() {
        setPhoneNumberInputInProgress(true);
        String phoneNumber = getFullPhoneNumber();
        userRepos.existsByPhoneNumber(phoneNumber)
                .thenAccept(isExists -> {
                    setPhoneNumberInputInProgress(false);
                    if (isExists) {
                        tilLocalNumber.setError("This phone number is already in use");
                    } else {
                        sendOtp(phoneNumber, false);

                        llInputPhoneNumber.setVisibility(View.GONE);
                        llVerifyOtp.setVisibility(View.VISIBLE);
                    }
                })
                .exceptionally(e -> {
                    setPhoneNumberInputInProgress(false);
                    CustomToast.showErrorToast(requireActivity(), "Verify phone number unsuccessfully");
                    Log.e(TAG, "Error: ", e);
                    return null;
                });
    }

    /**
     * Lấy số điện thoại đầy đủ từ mã quốc gia và số địa phương.
     *
     * @return Số điện thoại đầy đủ.
     *
     * Tác giả: Trần Văn An
     */
    private String getFullPhoneNumber() {
        String countryCode = actvCountryCode.getText().toString();
        String number = edtLocalNumber.getText().toString();
        return Utils.getFullPhoneNumber(countryCode, number);
    }

    /**
     * Kiểm tra sự hợp lệ của số điện thoại và hiển thị lỗi nếu có.
     *
     * @param s Số điện thoại nhập vào.
     *
     * Tác giả: Trần Văn An
     */
    public void checkPhoneNumber(CharSequence s) {
        String countryCode = actvCountryCode.getText().toString();
        String localNumber = s.toString();
        String phoneNumber = Utils.getFullPhoneNumber(countryCode, localNumber);
        String error = Validator.validPhoneNumber(phoneNumber);
        tilLocalNumber.setError(error);
    }

    /**
     * Cài đặt các sự kiện và chức năng cho phần xác minh OTP.
     *
     * Tác giả: Trần Văn An
     */
    private void setupVerifyOtp() {
        txvResend.setOnClickListener(v -> {
            resendOtp();
        });

        btnVerify.setOnClickListener(v -> verifyOtp());
    }

    /**
     * Gửi lại OTP.
     *
     * Tác giả: Trần Văn An
     */
    public void resendOtp() {
        String phoneNumber = getFullPhoneNumber();
        sendOtp(phoneNumber, true);
    }

    /**
     * Gửi OTP đến số điện thoại.
     *
     * @param phoneNumber Số điện thoại nhận OTP.
     * @param isResend Flag xác định là gửi lại OTP hay không.
     *
     * Tác giả: Trần Văn An
     */
    private void sendOtp(String phoneNumber, boolean isResend) {
        startResendTimer();
        txvResend.setEnabled(false);
        authRepos.sendOtp(requireActivity(),
                phoneNumber,
                isResend,
                resendingToken,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        applyCredentialAndDismiss(phoneAuthCredential);
                        setVerifyOtpInProgress(false);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        CustomToast.showErrorToast(requireActivity(), "OTP verification failed");
                        setVerifyOtpInProgress(false);
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        verificationId = s;
                        resendingToken = forceResendingToken;
                        CustomToast.showSuccessToast(requireActivity(), "OTP sent successfully");
                        setVerifyOtpInProgress(false);
                    }
                });
    }

    /**
     * Xác minh OTP.
     *
     * Tác giả: Trần Văn An
     */
    public void verifyOtp() {
        String enteredOtp = edtOpt.getText().toString();
        try {
            PhoneAuthCredential phoneCredential = PhoneAuthProvider.getCredential(verificationId, enteredOtp);
            applyCredentialAndDismiss(phoneCredential);
        } catch (IllegalArgumentException e) {
            CustomToast.showErrorToast(requireActivity(), "Failed to verify OTP");
            Log.e(TAG, "Error: " + e.getMessage(), e);
        }
    }

    /**
     * Áp dụng thông tin xác thực và đóng dialog.
     *
     * @param phoneCredential Thông tin xác thực điện thoại.
     *
     * Tác giả: Trần Văn An
     */
    private void applyCredentialAndDismiss(PhoneAuthCredential phoneCredential) {
        if (model.getVerifyButtonClickListener() != null) {
            model.getVerifyButtonClickListener().apply(phoneCredential);
        }
        dismiss();
    }

    /**
     * Bắt đầu đếm ngược cho thời gian gửi lại OTP.
     *
     * Tác giả: Trần Văn An
     */
    private void startResendTimer() {
        CountDownTimer timer = new CountDownTimer(Utils.OTP_TIME_OUT_SECONDS * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeoutSeconds = (int) (millisUntilFinished / 1000);
                String content = String.format("Resend OTP in " + timeoutSeconds + " seconds");
                txvResend.setText(content);
            }

            @Override
            public void onFinish() {
                timeoutSeconds = Utils.OTP_TIME_OUT_SECONDS;
                txvResend.setText("Resend");
                txvResend.setEnabled(true);
            }
        };
        timer.start();
    }

    /**
     * Cập nhật giao diện khi đang gửi số điện thoại.
     *
     * @param inProgress Flag xác định đang gửi số điện thoại hay không.
     *
     * Tác giả: Trần Văn An
     */
    private void setPhoneNumberInputInProgress(boolean inProgress) {
        if (inProgress) {
            btnSendOtp.setVisibility(View.GONE);
            pgbSendOtp.setVisibility(View.VISIBLE);
        } else {
            btnSendOtp.setVisibility(View.VISIBLE);
            pgbSendOtp.setVisibility(View.GONE);
        }
    }

    /**
     * Cập nhật giao diện khi đang xác minh OTP.
     *
     * @param inProgress Flag xác định đang xác minh OTP hay không.
     *
     * Tác giả: Trần Văn An
     */
    private void setVerifyOtpInProgress(boolean inProgress) {
        if (inProgress) {
            btnVerify.setVisibility(View.GONE);
            pgbVerify.setVisibility(View.VISIBLE);
        } else {
            btnVerify.setVisibility(View.VISIBLE);
            pgbVerify.setVisibility(View.GONE);
        }
    }
}
