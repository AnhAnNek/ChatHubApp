package com.example.user.login.google;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.common.customcontrol.CustomToast;
import com.example.common.customcontrol.LoadingDialog;
import com.example.common.navigation.EAnimationType;
import com.example.common.repository.MediaRepos;
import com.example.common.repository.MediaReposImpl;
import com.example.user.login.LoginActivity;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.AuthReposImpl;
import com.example.user.repository.UserRepos;
import com.example.user.repository.UserReposImpl;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;

/**
 * GoogleSignInActivity là hoạt động cho phép người dùng đăng nhập bằng tài khoản Google.
 *
 * Tác giả: Trần Văn An
 */
public class GoogleSignInActivity extends LoginActivity {

    private GoogleSignInViewModel viewModel;
    private LoadingDialog loadingDialog;

    /**
     * Phương thức onCreate được gọi khi activity được tạo.
     *
     * @param savedInstanceState trạng thái được lưu trước đó của activity.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Khởi tạo các repository và ViewModel.
        MediaRepos mediaRepos = new MediaReposImpl();
        UserRepos userRepos = new UserReposImpl(mediaRepos);
        AuthRepos authRepos = new AuthReposImpl(userRepos);
        GoogleSignInViewModelFactory factory = new GoogleSignInViewModelFactory(authRepos);
        viewModel = new ViewModelProvider(this, factory).get(GoogleSignInViewModel.class);

        // Khởi tạo LoadingDialog để hiển thị tiến trình tải.
        loadingDialog = new LoadingDialog(GoogleSignInActivity.this);
        setupObservers();

        // Hiển thị giao diện đăng nhập Google.
        displayOneTapSignInUI();
    }

    /**
     * Thiết lập các observer để theo dõi thay đổi trong ViewModel.
     *
     * Tác giả: Trần Văn An
     */
    private void setupObservers() {
        viewModel.isLoading().observe(this, isLoading -> {
            if (isLoading) {
                loadingDialog.show();
            } else {
                loadingDialog.dismiss();
            }
        });

        viewModel.getNavigateToHome().observe(this, navigate -> {
            if (navigate) {
                CustomToast.showSuccessToast(this, "Login successfully");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        navigationManager.navigateToHome(EAnimationType.FADE_IN);
                    }
                }, 100);
            }
        });
    }

    /**
     * Hiển thị giao diện đăng nhập Google bằng cách tạo intent và khởi chạy launcher.
     *
     * Tác giả: Trần Văn An
     */
    private void displayOneTapSignInUI() {
        // Cấu hình tùy chọn đăng nhập Google.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Tạo một đối tượng GoogleSignInClient và yêu cầu đăng xuất trước đó.
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(GoogleSignInActivity.this, gso);
        googleSignInClient.signOut();

        // Tạo một intent để mở giao diện đăng nhập Google.
        Intent intent = googleSignInClient.getSignInIntent();
        loginWithGoogleLauncher.launch(intent);
    }

    /**
     * ActivityResultLauncher để xử lý kết quả từ giao diện đăng nhập Google.
     *
     * Tác giả: Trần Văn An
     */
    private ActivityResultLauncher<Intent> loginWithGoogleLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    viewModel.handleSignInResult(task);
                }
            }
    );
}