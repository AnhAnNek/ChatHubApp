package com.example.user.repository;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.infrastructure.Utils;
import com.example.user.User;
import com.example.user.UserNotAuthenticatedException;
import com.example.user.login.SignInRequest;
import com.example.user.signup.SignUpRequest;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * AuthReposImpl là một lớp triển khai của AuthRepos,
 * sử dụng Firebase Authentication để thực hiện các thao tác xác thực.
 *
 * Tác giả: Trần Văn An
 */
public class AuthReposImpl implements AuthRepos {

    private static final String TAG = AuthReposImpl.class.getSimpleName();

    private final UserRepos userRepos;
    private final FirebaseAuth auth;

    /**
     * Khởi tạo AuthReposImpl với kho lưu trữ người dùng và FirebaseAuth.
     *
     * @param userRepos kho người dùng.
     *
     * Tác giả: Trần Văn An
     */
    public AuthReposImpl(UserRepos userRepos) {
        this.userRepos = userRepos;
        auth = FirebaseAuth.getInstance();
    }

    /**
     * Đăng ký người dùng mới.
     *
     * @param signUpRequest đối tượng SignUpRequest chứa thông tin đăng ký.
     * @return CompletableFuture<Void> đại diện cho kết quả của thao tác bất đồng bộ.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    public CompletableFuture<Void> signUp(SignUpRequest signUpRequest) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        String fullName = signUpRequest.getFullName();
        String email = signUpRequest.getEmail();
        String password = signUpRequest.getPassword();

        // Tạo người dùng mới với email và mật khẩu và lưu vào Firebase Authentication.
        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    String uid = firebaseUser.getUid();
                    User user = new User(uid, fullName, email);
                    userRepos.addUser(user)
                            .thenAccept(future::complete)
                            .exceptionally(e -> {
                                future.completeExceptionally(e);
                                firebaseUser.delete();
                                return null;
                            });
                })
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }

    /**
     * Đăng nhập bằng email và mật khẩu.
     *
     * @param signInRequest đối tượng SignInRequest chứa thông tin đăng nhập.
     * @return CompletableFuture<Void> đại diện cho kết quả của thao tác bất đồng bộ.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    public CompletableFuture<Void> signInWithEmailPassword(SignInRequest signInRequest) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        String email = signInRequest.getEmail();
        String password = signInRequest.getPassword();

        // Đăng nhập bằng email và mật khẩu.
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    future.complete(null);
                })
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }

    /**
     * Lấy UID của người dùng hiện tại.
     *
     * @return String UID của người dùng hiện tại.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    public String getCurrentUid() {
        FirebaseUser firebaseUser = auth.getCurrentUser();
        return firebaseUser != null ? firebaseUser.getUid() : "";
    }

    /**
     * Lấy thông tin người dùng hiện tại.
     *
     * @return CompletableFuture<User> đại diện cho kết quả của thao tác bất đồng bộ.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    public CompletableFuture<User> getCurrentUser() {
        String uid = getCurrentUid();
        return userRepos.getUserByUid(uid);
    }

    /**
     * Đăng xuất người dùng hiện tại.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    public void signOut() {
        auth.signOut();
    }

    /**
     * Gửi email đặt lại mật khẩu.
     *
     * @param email địa chỉ email của người dùng.
     * @return CompletableFuture<Void> đại diện cho kết quả của thao tác bất đồng bộ.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    public CompletableFuture<Void> sendPasswordResetEmail(String email) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        // Gửi email đặt lại mật khẩu đến địa chỉ email đã cung cấp.
        auth.sendPasswordResetEmail(email)
                .addOnSuccessListener(future::complete)
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }

    /**
     * Kiểm tra xem người dùng hiện tại đã đăng nhập hay chưa.
     *
     * @return boolean true nếu người dùng đã đăng nhập, false nếu không.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    public boolean isLoggedIn() {
        FirebaseUser firebaseUser = auth.getCurrentUser();
        return firebaseUser != null;
    }

    /**
     * Cập nhật mật khẩu mới cho người dùng hiện tại.
     *
     * @param newPassword mật khẩu mới.
     * @return CompletableFuture<Void> đại diện cho kết quả của thao tác bất đồng bộ.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    public CompletableFuture<Void> updatePassword(String newPassword) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        if (!isLoggedIn()) {
            future.completeExceptionally(new UserNotAuthenticatedException("User is not authenticated"));
            return future;
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            future.completeExceptionally(new UserNotAuthenticatedException("User is not authenticated"));
            return future;
        }

        user.updatePassword(newPassword)
                .addOnSuccessListener(future::complete)
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }

    /**
     * Kiểm tra mật khẩu cũ của người dùng.
     *
     * @param email địa chỉ email của người dùng.
     * @param oldPassword mật khẩu cũ.
     * @return CompletableFuture<Void> đại diện cho kết quả của thao tác bất đồng bộ.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    public CompletableFuture<Void> checkOldPassword(String email, String oldPassword) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        if (!isLoggedIn()) {
            future.completeExceptionally(new UserNotAuthenticatedException("User is not authenticated"));
            return future;
        }

        FirebaseUser user = auth.getCurrentUser();
        try {
            AuthCredential credential = EmailAuthProvider.getCredential(email, oldPassword);
            user.reauthenticate(credential)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            future.complete(null);
                        } else {
                            Exception exception = task.getException();
                            future.completeExceptionally(exception);
                        }
                    });
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Error: " + e.getMessage(), e);
            future.completeExceptionally(e);
        }

        return future;
    }

    /**
     * Lấy các phương thức đăng nhập có sẵn cho người dùng.
     *
     * @return CompletableFuture<List<String>> đại diện cho kết quả của thao tác bất đồng bộ.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    public CompletableFuture<List<String>> fetchSignInMethods() {
        CompletableFuture<List<String>> future = new CompletableFuture<>();

        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            future.completeExceptionally(new UserNotAuthenticatedException("User is not authenticated"));
            return future;
        }

        List<String> providerIds = new ArrayList<>();
        for (UserInfo userInfo : user.getProviderData()) {
            String providerId = userInfo.getProviderId();
            providerIds.add(providerId);
        }

        future.complete(providerIds);
        return future;
    }

    /**
     * Liên kết người dùng hiện tại với thông tin xác thực.
     *
     * @param authCredential thông tin xác thực.
     * @return CompletableFuture<Void> đại diện cho kết quả của thao tác bất đồng bộ.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    public CompletableFuture<Void> linkCurrentUserWithCredential(AuthCredential authCredential) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            future.completeExceptionally(new UserNotAuthenticatedException("User is not authenticated"));
            return future;
        }

        user.linkWithCredential(authCredential)
                .addOnSuccessListener(authResult -> future.complete(null))
                .addOnFailureListener(future::completeExceptionally);
        return future;
    }

    /**
     * Liên kết người dùng hiện tại với thông tin xác thực từ OTP.
     *
     * @param phoneAuthCredential thông tin xác thực từ OTP.
     * @return CompletableFuture<Void> đại diện cho kết quả của thao tác bất đồng bộ.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    public CompletableFuture<Void> linkCurrentUserWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            future.completeExceptionally(new UserNotAuthenticatedException("User is not authenticated"));
            return future;
        }

        user.linkWithCredential(phoneAuthCredential)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser linkedUser = authResult.getUser();
                    String phoneNumber = linkedUser.getPhoneNumber();
                    userRepos.updatePhoneNumber(user.getUid(), phoneNumber)
                            .thenAccept(future::complete)
                            .exceptionally(e -> {
                                future.completeExceptionally(e);
                                return null;
                            });
                })
                .addOnFailureListener(e -> {
                    future.completeExceptionally(e);
                });
        return future;
    }

    /**
     * Kiểm tra xem email hiện tại của người dùng có khớp với email đã cung cấp hay không.
     *
     * @param email địa chỉ email để kiểm tra.
     * @return boolean true nếu khớp, false nếu không.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    public boolean isCurrentUserEmail(@Nullable String email) {
        FirebaseUser user = auth.getCurrentUser();
        return user != null && user.getEmail() != null && user.getEmail().equals(email);
    }

    /**
     * Lấy email của người dùng hiện tại.
     *
     * @return String email của người dùng hiện tại.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    public String getCurrentEmail() {
        FirebaseUser user = auth.getCurrentUser();
        return user != null ? user.getEmail() : "";
    }

    /**
     * Đăng nhập bằng thông tin xác thực.
     *
     * @param authCredential thông tin xác thực.
     * @return CompletableFuture<Void> đại diện cho kết quả của thao tác bất đồng bộ.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    public CompletableFuture<Void> signInWithCredential(AuthCredential authCredential) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        auth.signInWithCredential(authCredential)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        future.completeExceptionally(task.getException());
                        return;
                    }

                    FirebaseUser curUser = auth.getCurrentUser();
                    if (curUser == null) {
                        future.completeExceptionally(new Exception("Current user is null"));
                        return;
                    }

                    String email = curUser.getEmail() != null
                            ? curUser.getEmail() : "example@gmail.com";
                    userRepos.checkUserExistsByEmail(email)
                            .thenAccept(exists -> {
                                if (exists) {
                                    future.complete(null);
                                    return;
                                }

                                String uid = curUser.getUid();
                                User user = new User(uid, curUser.getEmail());
                                userRepos.addUser(user)
                                        .thenAccept(future::complete)
                                        .exceptionally(e -> {
                                            future.completeExceptionally(e);
                                            curUser.delete();
                                            return null;
                                        });
                            })
                            .exceptionally(e -> {
                                Log.e(TAG, "Error fetching user data: " + e);
                                future.completeExceptionally(e);
                                return null;
                            });
                });

        return future;
    }

    /**
     * Gửi OTP tới số điện thoại của người dùng.
     *
     * @param activity activity hiện tại.
     * @param phoneNumber số điện thoại của người dùng.
     * @param isResend true nếu là gửi lại OTP, false nếu không.
     * @param resendingToken token để gửi lại OTP.
     * @param callbacks callback cho trạng thái xác minh.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    public void sendOtp(Activity activity,
                        String phoneNumber,
                        boolean isResend,
                        PhoneAuthProvider.ForceResendingToken resendingToken,
                        PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks) {
        PhoneAuthOptions.Builder builder = PhoneAuthOptions
                .newBuilder(auth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(Utils.OTP_TIME_OUT_SECONDS, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(callbacks);
        if (isResend) {
            PhoneAuthProvider.verifyPhoneNumber(builder
                    .setForceResendingToken(resendingToken)
                    .build());
        } else {
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }
    }
}
