package com.example.setting;

import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.infrastructure.BaseViewModel;
import com.example.user.User;
import com.example.user.repository.AuthRepos;

/**
 * ViewModel quản lý dữ liệu và logic cho SettingsFragment.
 *
 * Tác giả: Trần Văn An
 */
public class SettingsViewModel extends BaseViewModel {

    private static final String TAG = SettingsViewModel.class.getSimpleName();

    private final MutableLiveData<Uri> profileImg = new MutableLiveData<>();
    private final MutableLiveData<String> fullName = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToUserProfile = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToAccountLinking = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToLogin = new MutableLiveData<>();
    private User originalUser;

    /**
     * Trả về LiveData của profile image Uri.
     *
     * @return LiveData của Uri
     *
     * Tác giả: Trần Văn An
     */
    public LiveData<Uri> getProfileImg() {
        return profileImg;
    }

    /**
     * Trả về LiveData của tên đầy đủ người dùng.
     *
     * @return LiveData của String
     *
     * Tác giả: Trần Văn An
     */
    public LiveData<String> getFullName() {
        return fullName;
    }

    /**
     * Trả về MutableLiveData để quan sát điều hướng tới trang profile.
     *
     * @return MutableLiveData của Boolean
     *
     * Tác giả: Trần Văn An
     */
    public MutableLiveData<Boolean> getNavigateToUserProfile() {
        return navigateToUserProfile;
    }

    /**
     * Trả về MutableLiveData để quan sát điều hướng tới trang liên kết tài khoản.
     *
     * @return MutableLiveData của Boolean
     *
     * Tác giả: Trần Văn An
     */
    public MutableLiveData<Boolean> getNavigateToAccountLinking() {
        return navigateToAccountLinking;
    }

    /**
     * Trả về LiveData để quan sát điều hướng tới trang đăng nhập.
     *
     * @return LiveData của Boolean
     *
     * Tác giả: Trần Văn An
     */
    public LiveData<Boolean> getNavigateToLogin() {
        return navigateToLogin;
    }

    /**
     * Trả về người dùng hiện tại.
     *
     * @return User hiện tại
     *
     * Tác giả: Trần Văn An
     */
    public User getUser() {
        return originalUser;
    }

    /**
     * Khởi tạo SettingsViewModel với AuthRepos.
     *
     * @param authRepos AuthRepos để tương tác với dữ liệu xác thực
     *
     * Tác giả: Trần Văn An
     */
    public SettingsViewModel(AuthRepos authRepos) {
        this.authRepos = authRepos;
    }

    /**
     * Gọi khi ViewModel bắt đầu.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    protected void onStart() {
        super.onStart();

        fetchUser();
    }

    /**
     * Điều hướng tới trang profile của người dùng.
     *
     * Tác giả: Trần Văn An
     */
    public void navigateToUserProfile() {
        navigateToUserProfile.postValue(true);
    }

    /**
     * Điều hướng tới trang liên kết tài khoản.
     *
     * Tác giả: Trần Văn An
     */
    public void navigateToAccountLinking() {
        navigateToAccountLinking.postValue(true);
    }

    /**
     * Đăng xuất người dùng.
     *
     * Tác giả: Trần Văn An
     */
    public void signOut() {
        authRepos.signOut();
        successToastMessage.postValue("Sign out");
        new Handler().postDelayed(() -> navigateToLogin(), 100);
    }

    /**
     * Lấy thông tin người dùng hiện tại và cập nhật lên giao diện.
     *
     * Tác giả: Trần Văn An
     */
    public void fetchUser() {
        this.authRepos.getCurrentUser()
                .thenAccept(user -> {
                    if (user != null) {
                        originalUser = user;

                        profileImg.postValue(user.getUri());
                        fullName.postValue(user.getFullName());
                    }
                })
                .exceptionally(e -> {
                    Log.e(TAG, "Error: ", e);
                    return null;
                });
    }

    /**
     * Điều hướng tới trang đăng nhập.
     *
     * Tác giả: Trần Văn
     */
    private void navigateToLogin() {
        this.navigateToLogin.postValue(true);
    }
}
