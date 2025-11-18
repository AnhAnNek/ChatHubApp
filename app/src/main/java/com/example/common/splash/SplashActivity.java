package com.example.common.splash;

import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.R;
import com.example.common.navigation.EAnimationType;
import com.example.common.navigation.NavigationManager;
import com.example.common.navigation.NavigationManagerImpl;
import com.example.common.repository.MediaRepos;
import com.example.common.repository.MediaReposImpl;
import com.example.infrastructure.PreferenceManagerRepos;
import com.example.infrastructure.Utils;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.AuthReposImpl;
import com.example.user.repository.UserRepos;
import com.example.user.repository.UserReposImpl;

/**
 * SplashActivity là màn hình khởi đầu của ứng dụng, hiển thị logo
 * và kiểm tra trạng thái đăng nhập của người dùng. Nếu người dùng đã đăng nhập,
 * chuyển hướng đến màn hình chính. Ngược lại, chuyển hướng đến màn hình đăng nhập.
 *
 * Tác giả: Trần Văn An
 */
public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 1500;
    private NavigationManager navigationManager;
    private PreferenceManagerRepos preferenceManagerRepos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(SplashActivity.this);
        setContentView(R.layout.activity_splash);

        navigationManager = new NavigationManagerImpl(this);

        MediaRepos mediaRepos = new MediaReposImpl();
        UserRepos userRepos = new UserReposImpl(mediaRepos);
        AuthRepos authRepos = new AuthReposImpl(userRepos);

        preferenceManagerRepos = new PreferenceManagerRepos(getApplicationContext());

        // Đặt thời gian chờ trước khi chuyển hướng
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Kiểm tra trạng thái đăng nhập của người dùng
                if (!authRepos.isLoggedIn()) {
                    // Nếu chưa đăng nhập, chuyển hướng đến màn hình đăng nhập
                    navigationManager.navigateToLogin(EAnimationType.FADE_IN);
                    finish();
                    return;
                }

                // Chuyển hướng đến màn hình chính
                navigationManager.navigateToHome(EAnimationType.FADE_IN);
                finish();
            }
        }, SPLASH_DURATION);
    }
}