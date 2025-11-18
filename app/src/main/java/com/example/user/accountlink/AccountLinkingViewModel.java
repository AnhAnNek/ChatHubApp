package com.example.user.accountlink;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.common.customcontrol.emailpassworddialog.EmailPasswordDialogModel;
import com.example.common.customcontrol.phonecredential.PhoneCredentialDialogModel;
import com.example.infrastructure.BaseViewModel;
import com.example.user.repository.AuthRepos;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthProvider;

/**
 * ViewModel cho AccountLinkingActivity, quản lý dữ liệu và logic của liên kết tài khoản.
 *
 * Tác giả: Trần Văn An
 */
public class AccountLinkingViewModel extends BaseViewModel {

    private static final String TAG = AccountLinkingViewModel.class.getSimpleName();

    private final MutableLiveData<Boolean> navigateBack = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isInAppPasswordLinked = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isInAppAdding = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isGoogleAccountLinked = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isGoogleAdding = new MutableLiveData<>();
    private final MutableLiveData<Boolean> openGoogleSignIn = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isSmsAccountLinked = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isSmsAdding = new MutableLiveData<>();
    private final MutableLiveData<EmailPasswordDialogModel> openEmailPasswordDialog = new MutableLiveData<>();
    private final MutableLiveData<PhoneCredentialDialogModel> openPhoneCredentialDialog = new MutableLiveData<>();
    private final AuthRepos authRepos;

    public LiveData<Boolean> getNavigateBack() {
        return navigateBack;
    }

    public LiveData<Boolean> getIsInAppPasswordLinked() {
        return isInAppPasswordLinked;
    }

    public LiveData<Boolean> getIsInAppAdding() {
        return isInAppAdding;
    }

    public LiveData<Boolean> getIsGoogleAccountLinked() {
        return isGoogleAccountLinked;
    }

    public LiveData<Boolean> getIsGoogleAdding() {
        return isGoogleAdding;
    }

    public LiveData<Boolean> getOpenGoogleSignIn() {
        return openGoogleSignIn;
    }

    public LiveData<Boolean> getIsSmsAccountLinked() {
        return isSmsAccountLinked;
    }

    public LiveData<Boolean> getIsSmsAdding() {
        return isSmsAdding;
    }

    public LiveData<EmailPasswordDialogModel> getOpenEmailPasswordDialog() {
        return openEmailPasswordDialog;
    }

    public LiveData<PhoneCredentialDialogModel> getOpenPhoneCredentialDialog() {
        return openPhoneCredentialDialog;
    }

    /**
     * Khởi tạo AccountLinkingViewModel với repository xác thực.
     *
     * @param authRepos Repository xác thực
     *
     * Tác giả: Trần Văn An
     */
    public AccountLinkingViewModel(AuthRepos authRepos) {
        this.authRepos = authRepos;

        loadProviders();
    }

    /**
     * Tải thông tin các phương thức liên kết tài khoản.
     *
     * Tác giả: Trần Văn An
     */
    public void loadProviders() {
        // Đặt giá trị mặc định ban đầu cho các trạng thái liên kết
        isInAppPasswordLinked.postValue(false);
        isGoogleAccountLinked.postValue(false);
        isSmsAccountLinked.postValue(false);

        // Lấy danh sách các phương thức đăng nhập đã liên kết với tài khoản từ AuthRepos
        authRepos.fetchSignInMethods()
                .thenAccept(providerIds -> {
                    // Duyệt qua danh sách các phương thức đăng nhập và cập nhật trạng thái tương ứng
                    for (String providerId : providerIds) {
                        switch (providerId) {
                            case EmailAuthProvider.PROVIDER_ID:
                                isInAppPasswordLinked.postValue(true);
                                break;
                            case GoogleAuthProvider.PROVIDER_ID:
                                isGoogleAccountLinked.postValue(true);
                                break;
                            case PhoneAuthProvider.PROVIDER_ID:
                                isSmsAccountLinked.postValue(true);
                                break;
                        }
                    }
                })
                .exceptionally(e -> {
                    // Xử lý lỗi khi lấy danh sách phương thức đăng nhập
                    Log.e(TAG, "Error: ", e);
                    return null;
                });
    }

    /**
     * Điều hướng về màn hình cài đặt.
     *
     * Tác giả: Trần Văn An
     */
    public void navigateToSettings() {
        // Đặt giá trị navigateBack thành true để yêu cầu điều hướng về màn hình cài đặt
        navigateBack.postValue(true);
    }

    /**
     * Mở hộp thoại nhập mật khẩu để thêm mật khẩu vào tài khoản.
     *
     * Tác giả: Trần Văn An
     */
    public void addInAppPassword() {
        // Tạo mô hình dữ liệu cho hộp thoại nhập mật khẩu
        EmailPasswordDialogModel model = new EmailPasswordDialogModel.Builder()
                .setTitle("In-app password")
                .setSubTitle("Enter your information for password setup")
                .setEmail(authRepos.getCurrentEmail())
                .setPassword("")
                .setSubmitButtonClickListener((email, password) -> {
                    // Bắt đầu quá trình thêm mật khẩu vào tài khoản
                    isInAppAdding.postValue(true);
                    if (!authRepos.isCurrentUserEmail(email)) {
                        // Kiểm tra email nhập vào có khớp với email hiện tại của tài khoản không
                        errorToastMessage.postValue("Your typed email mismatch");
                        isInAppAdding.postValue(false);
                        return;
                    }

                    // Cập nhật mật khẩu mới cho tài khoản
                    authRepos.updatePassword(password)
                            .thenAccept(aVoid -> {
                                // Tải lại các phương thức đăng nhập sau khi cập nhật thành công
                                loadProviders();
                                isInAppAdding.postValue(false);
                                successToastMessage.postValue("Add password successfully");
                            })
                            .exceptionally(e -> {
                                // Xử lý lỗi khi cập nhật mật khẩu
                                errorToastMessage.postValue("Add password unsuccessfully");
                                isInAppAdding.postValue(false);
                                Log.e(TAG, "Error during in-App password linking: " + e.getMessage(), e);
                                return null;
                            });
                })
                .build();

        // Hiển thị hộp thoại nhập mật khẩu
        openEmailPasswordDialog.postValue(model);
    }

    /**
     * Mở giao diện đăng nhập Google để thêm tài khoản Google.
     *
     * Tác giả: Trần Văn An
     */
    public void addGoogleAccount() {
        openGoogleSignIn();
    }

    /**
     * Mở giao diện đăng nhập Google.
     *
     * Tác giả: Trần Văn An
     */
    private void openGoogleSignIn() {
        // Đặt giá trị openGoogleSignIn thành true để yêu cầu mở giao diện đăng nhập Google
        openGoogleSignIn.postValue(true);
    }

    /**
     * Xử lý kết quả đăng nhập Google và liên kết tài khoản Google với tài khoản hiện tại.
     *
     * @param completedTask Nhiệm vụ hoàn tất từ kết quả đăng nhập Google
     *
     * Tác giả: Trần Văn An
     */
    public void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
        isGoogleAdding.postValue(true);
        try {
            // Lấy tài khoản Google từ kết quả đăng nhập
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String idToken = account.getIdToken();
            AuthCredential googleCredential = GoogleAuthProvider.getCredential(idToken, null);

            // Liên kết tài khoản Google với tài khoản hiện tại
            authRepos.linkCurrentUserWithCredential(googleCredential)
                    .thenAccept(authResult -> {
                        // Tải lại các phương thức đăng nhập sau khi liên kết thành công
                        loadProviders();
                        successToastMessage.postValue("Link google account successfully");
                        isGoogleAdding.postValue(false);
                    })
                    .exceptionally(e -> {
                        // Xử lý lỗi khi liên kết tài khoản Google
                        isGoogleAdding.postValue(false);
                        errorToastMessage.postValue("Link google account unsuccessfully");
                        Log.e(TAG, "Error during Google linking: " + e.getMessage(), e);
                        return null;
                    });
        } catch (ApiException e) {
            // Xử lý lỗi khi không thể lấy tài khoản Google
            errorToastMessage.postValue("Link google account unsuccessfully");
            isGoogleAdding.postValue(false);
            Log.e(TAG, "Error: ", e);
        }
    }

    /**
     * Mở hộp thoại xác thực số điện thoại để thêm tài khoản SMS.
     *
     * Tác giả: Trần Văn An
     */
    public void addSmsAccount() {
        // Tạo mô hình dữ liệu cho hộp thoại xác thực số điện thoại
        PhoneCredentialDialogModel model = new PhoneCredentialDialogModel.Builder()
                .setVerifyButtonClickListener(phoneAuthCredential -> {
                    isSmsAdding.postValue(true);
                    // Liên kết số điện thoại với tài khoản hiện tại
                    authRepos.linkCurrentUserWithPhoneAuthCredential(phoneAuthCredential)
                            .thenAccept(authResult -> {
                                // Tải lại các phương thức đăng nhập sau khi liên kết thành công
                                loadProviders();
                                isSmsAdding.postValue(false);
                                successToastMessage.postValue("Link phone number successfully");
                            })
                            .exceptionally(e -> {
                                // Xử lý lỗi khi liên kết số điện thoại
                                isSmsAdding.postValue(false);
                                errorToastMessage.postValue("Link phone number unsuccessfully");
                                Log.e(TAG, "Error during SMS linking: " + e.getMessage(), e);
                                return null;
                            });
                })
                .build();

        // Hiển thị hộp thoại xác thực số điện thoại
        openPhoneCredentialDialog.postValue(model);
    }
}
