package com.example.user.repository;

import android.app.Activity;

import androidx.annotation.Nullable;

import com.example.user.User;
import com.example.user.login.SignInRequest;
import com.example.user.signup.SignUpRequest;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * AuthRepos là một giao diện đại diện cho kho lưu trữ xác thực người dùng.
 * Các phương thức trong giao diện này sử dụng CompletableFuture để thực hiện các thao tác bất đồng bộ.
 *
 * Tác giả: Trần Văn An
 */
public interface AuthRepos {

    /**
     * Đăng ký người dùng mới.
     *
     * @param signUpRequest đối tượng SignUpRequest chứa thông tin đăng ký.
     * @return CompletableFuture<Void> đại diện cho kết quả của thao tác bất đồng bộ.
     *
     * Tác giả: Trần Văn An
     */
    CompletableFuture<Void> signUp(SignUpRequest signUpRequest);

    /**
     * Đăng nhập bằng email và mật khẩu.
     *
     * @param signInRequest đối tượng SignInRequest chứa thông tin đăng nhập.
     * @return CompletableFuture<Void> đại diện cho kết quả của thao tác bất đồng bộ.
     *
     * Tác giả: Trần Văn An
     */
    CompletableFuture<Void> signInWithEmailPassword(SignInRequest signInRequest);

    /**
     * Lấy UID của người dùng hiện tại.
     *
     * @return String UID của người dùng hiện tại.
     *
     * Tác giả: Trần Văn An
     */
    String getCurrentUid();

    /**
     * Lấy thông tin người dùng hiện tại.
     *
     * @return CompletableFuture<User> đại diện cho kết quả của thao tác bất đồng bộ.
     *
     * Tác giả: Trần Văn An
     */
    CompletableFuture<User> getCurrentUser();

    /**
     * Đăng xuất người dùng hiện tại.
     *
     * Tác giả: Trần Văn An
     */
    void signOut();

    /**
     * Gửi email đặt lại mật khẩu.
     *
     * @param email địa chỉ email của người dùng.
     * @return CompletableFuture<Void> đại diện cho kết quả của thao tác bất đồng bộ.
     *
     * Tác giả: Trần Văn An
     */
    CompletableFuture<Void> sendPasswordResetEmail(String email);

    /**
     * Kiểm tra xem người dùng hiện tại đã đăng nhập hay chưa.
     *
     * @return boolean true nếu người dùng đã đăng nhập, false nếu không.
     *
     * Tác giả: Trần Văn An
     */
    boolean isLoggedIn();

    /**
     * Cập nhật mật khẩu mới cho người dùng hiện tại.
     *
     * @param newPassword mật khẩu mới.
     * @return CompletableFuture<Void> đại diện cho kết quả của thao tác bất đồng bộ.
     *
     * Tác giả: Trần Văn An
     */
    CompletableFuture<Void> updatePassword(String newPassword);

    /**
     * Kiểm tra mật khẩu cũ của người dùng có đúng hay không.
     *
     * @param email địa chỉ email của người dùng.
     * @param password mật khẩu cũ.
     * @return CompletableFuture<Void> đại diện cho kết quả của thao tác bất đồng bộ.
     *
     * Tác giả: Trần Văn An
     */
    CompletableFuture<Void> checkOldPassword(String email, String password);

    /**
     * Lấy các phương thức đăng nhập có sẵn cho người dùng.
     *
     * @return CompletableFuture<List<String>> đại diện cho kết quả của thao tác bất đồng bộ.
     *
     * Tác giả: Trần Văn An
     */
    CompletableFuture<List<String>> fetchSignInMethods();

    /**
     * Liên kết người dùng hiện tại với chứng thực (credential) khác.
     *
     * @param authCredential đối tượng AuthCredential để liên kết.
     * @return CompletableFuture<Void> đại diện cho kết quả của thao tác bất đồng bộ.
     *
     * Tác giả: Trần Văn An
     */
    CompletableFuture<Void> linkCurrentUserWithCredential(AuthCredential authCredential);

    /**
     * Liên kết người dùng hiện tại với chứng thực bằng số điện thoại.
     *
     * @param phoneAuthCredential đối tượng PhoneAuthCredential để liên kết.
     * @return CompletableFuture<Void> đại diện cho kết quả của thao tác bất đồng bộ.
     *
     * Tác giả: Trần Văn An
     */
    CompletableFuture<Void> linkCurrentUserWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential);

    /**
     * Kiểm tra email hiện tại của người dùng.
     *
     * @param email địa chỉ email cần kiểm tra.
     * @return boolean true nếu email hiện tại của người dùng trùng khớp với email đưa vào, false nếu không.
     *
     * Tác giả: Trần Văn An
     */
    boolean isCurrentUserEmail(@Nullable String email);

    /**
     * Lấy email hiện tại của người dùng.
     *
     * @return String email hiện tại của người dùng.
     *
     * Tác giả: Trần Văn An
     */
    String getCurrentEmail();

    /**
     * Đăng nhập bằng chứng thực (credential).
     *
     * @param authCredential đối tượng AuthCredential để đăng nhập.
     * @return CompletableFuture<Void> đại diện cho kết quả của thao tác bất đồng bộ.
     *
     * Tác giả: Trần Văn An
     */
    CompletableFuture<Void> signInWithCredential(AuthCredential authCredential);

    /**
     * Gửi mã OTP tới số điện thoại của người dùng.
     *
     * @param activity đối tượng Activity hiện tại.
     * @param phoneNumber số điện thoại của người dùng.
     * @param isResend cờ để xác định xem có phải gửi lại mã hay không.
     * @param resendingToken token để gửi lại mã.
     * @param callbacks các callback để xử lý các trạng thái xác minh.
     *
     * Tác giả: Trần Văn An
     */
    void sendOtp(Activity activity,
                 String phoneNumber,
                 boolean isResend,
                 PhoneAuthProvider.ForceResendingToken resendingToken,
                 PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks);
}
