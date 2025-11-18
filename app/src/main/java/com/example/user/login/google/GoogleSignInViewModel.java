package com.example.user.login.google;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.infrastructure.AppExecutors;
import com.example.user.login.LoginViewModel;
import com.example.user.repository.AuthRepos;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * GoogleSignInViewModel là một ViewModel dùng để xử lý đăng nhập bằng Google.
 *
 * Tác giả: Trần Văn An
 */
public class GoogleSignInViewModel extends LoginViewModel {

    private static final String TAG = GoogleSignInViewModel.class.getSimpleName();

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToHome = new MutableLiveData<>();

    /**
     * Phương thức để lấy trạng thái của quá trình tải.
     *
     * @return LiveData của isLoading.
     *
     * Tác giả: Trần Văn An
     */
    public LiveData<Boolean> isLoading() {
        return isLoading;
    }

    /**
     * Phương thức để lấy trạng thái của quá trình chuyển hướng đến màn hình chính.
     *
     * @return LiveData của navigateToHome.
     *
     * Tác giả: Trần Văn An
     */
    public LiveData<Boolean> getNavigateToHome() {
        return navigateToHome;
    }

    /**
     * Constructor để khởi tạo ViewModel với AuthRepos.
     *
     * @param authRepos AuthRepos để thực hiện các thao tác liên quan đến xác thực.
     *
     * Tác giả: Trần Văn An
     */
    public GoogleSignInViewModel(AuthRepos authRepos) {
        super(authRepos);

        isLoading.postValue(true);
    }

    /**
     * Phương thức để xử lý kết quả đăng nhập Google.
     *
     * @param completedTask Task chứa kết quả đăng nhập.
     *
     * Tác giả: Trần Văn An
     */
    public void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            // Lấy kết quả của quá trình đăng nhập từ Task.
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Lấy idToken từ GoogleSignInAccount.
            String idToken = account.getIdToken();
            // Thực hiện đăng nhập bằng idToken trên một luồng khác để tránh chặn luồng chính.
            AppExecutors.getIns().networkIO().execute(() -> {
                signInWithIdToken(idToken);
            });
        } catch (ApiException e) {
            isLoading.postValue(false);
        }
    }

    /**
     * Phương thức để đăng nhập bằng idToken từ Google.
     *
     * @param idToken idToken từ Google.
     *
     * Tác giả: Trần Văn An
     */
    private void signInWithIdToken(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        authRepos.signInWithCredential(credential)
                .thenAccept(aVoid -> {
                    isLoading.postValue(false);
                    navigateToHome();
                })
                .exceptionally(e -> {
                    isLoading.postValue(false);
                    Log.e(TAG, "Error: ", e);
                    return null;
                });
    }

    /**
     * Phương thức để chuyển hướng đến màn hình chính.
     *
     * Tác giả: Trần Văn An
     */
    public void navigateToHome() {
        navigateToHome.postValue(true);
    }
}
