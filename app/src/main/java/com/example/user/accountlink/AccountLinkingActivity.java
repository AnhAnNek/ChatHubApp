package com.example.user.accountlink;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.LayoutRes;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.common.customcontrol.emailpassworddialog.EmailPasswordDialogFragment;
import com.example.common.customcontrol.emailpassworddialog.EmailPasswordDialogModel;
import com.example.common.customcontrol.phonecredential.PhoneCredentialDialogFragment;
import com.example.common.customcontrol.phonecredential.PhoneCredentialDialogModel;
import com.example.common.navigation.EAnimationType;
import com.example.common.repository.MediaRepos;
import com.example.common.repository.MediaReposImpl;
import com.example.databinding.ActivityAccountLinkingBinding;
import com.example.infrastructure.BaseActivity;
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
 * Activity quản lý việc liên kết tài khoản.
 *
 * Tác giả: Trần Văn An
 */
public class AccountLinkingActivity extends BaseActivity<AccountLinkingViewModel, ActivityAccountLinkingBinding> {

    private UserRepos userRepos;
    private AuthRepos authRepos;

    /**
     * Trả về layout cho Activity.
     *
     * @return Layout resource ID.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    protected @LayoutRes int getLayout() {
        return R.layout.activity_account_linking;
    }

    /**
     * Trả về lớp ViewModel cho Activity.
     *
     * @return Class của ViewModel.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    protected Class<AccountLinkingViewModel> getViewModelClass() {
        return AccountLinkingViewModel.class;
    }

    /**
     * Trả về ViewModelProvider.Factory cho ViewModel.
     *
     * @return Factory cho ViewModel.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    protected ViewModelProvider.Factory getViewModelFactory() {
        MediaRepos mediaRepos = new MediaReposImpl();
        userRepos = new UserReposImpl(mediaRepos);
        authRepos = new AuthReposImpl(userRepos);
        return new AccountLinkingViewModelFactory(authRepos);
    }

    /**
     * Phương thức onCreate của Activity.
     *
     * @param savedInstanceState Trạng thái đã lưu của instance.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupObservers();
    }

    /**
     * Thiết lập các observer cho ViewModel.
     *
     * Tác giả: Trần Văn An
     */
    private void setupObservers() {
        viewModel.getNavigateBack().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateBack(null, EAnimationType.FADE_OUT);
            }
        });

        viewModel.getOpenGoogleSignIn().observe(this, open -> {
            if (open) {
                displayOneTapSignInUI();
            }
        });

        viewModel.getOpenEmailPasswordDialog().observe(this, this::openEmailPasswordDialog);

        viewModel.getOpenPhoneCredentialDialog().observe(this, this::openPhoneCredentialDialog);
    }

    /**
     * Hiển thị giao diện đăng nhập bằng Google.
     *
     * Tác giả: Trần Văn An
     */
    private void displayOneTapSignInUI() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(AccountLinkingActivity.this, gso);
        googleSignInClient.signOut();
        Intent intent = googleSignInClient.getSignInIntent();
        loginWithGoogle.launch(intent);
    }

    /**
     * Đăng ký ActivityResultLauncher cho kết quả đăng nhập Google.
     *
     * Tác giả: Trần Văn An
     */
    private ActivityResultLauncher<Intent> loginWithGoogle = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    viewModel.handleGoogleSignInResult(task);
                }
            }
    );

    /**
     * Mở dialog nhập email và mật khẩu.
     *
     * @param model Model cho dialog.
     *
     * Tác giả: Trần Văn An
     */
    private void openEmailPasswordDialog(EmailPasswordDialogModel model) {
        EmailPasswordDialogFragment dialog = new EmailPasswordDialogFragment(model);
        dialog.show(getSupportFragmentManager(), EmailPasswordDialogFragment.TAG);
    }

    /**
     * Mở dialog nhập thông tin xác thực qua điện thoại.
     *
     * @param model Model cho dialog.
     *
     * Tác giả: Trần Văn An
     */
    private void openPhoneCredentialDialog(PhoneCredentialDialogModel model) {
        PhoneCredentialDialogFragment dialog =
                new PhoneCredentialDialogFragment(userRepos, authRepos, model);
        dialog.show(getSupportFragmentManager(), PhoneCredentialDialogFragment.TAG);
    }
}