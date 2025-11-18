package com.example.home;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.R;
import com.example.chat.ChatActivity;
import com.example.chat.Utils;
import com.example.chat.conversations.ConversationsFragment;
import com.example.databinding.ActivityHomeBinding;
import com.example.friend.friendrequest.FriendRequestsFragment;
import com.example.infrastructure.BaseActivity;
import com.example.setting.SettingsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * HomeActivity quản lý giao diện chính của ứng dụng với ViewPager và BottomNavigationView.
 *
 * Tác giả: Trần Văn An
 */
public class HomeActivity extends BaseActivity<HomeViewModel, ActivityHomeBinding> {

    private MenuItem prevMenuItem;

    /**
     * Trả về layout resource ID cho HomeActivity.
     *
     * @return layout resource ID
     *
     * Tác giả: Trần Văn An
     */
    @Override
    protected @LayoutRes int getLayout() {
        return R.layout.activity_home;
    }

    /**
     * Trả về class của ViewModel.
     *
     * @return Class của HomeViewModel
     *
     * Tác giả: Trần Văn An
     */
    @Override
    protected Class<HomeViewModel> getViewModelClass() {
        return HomeViewModel.class;
    }

    /**
     * Xử lý khi Activity được tạo.
     *
     * @param savedInstanceState Bundle chứa trạng thái trước đó
     *
     * Tác giả: Trần Văn An
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Thiết lập sự kiện chọn item trên BottomNavigationView
        binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.bottom_messages) {
                    binding.viewPager.setCurrentItem(0);
                    return true;
                }
                if (itemId == R.id.bottom_friends) {
                    binding.viewPager.setCurrentItem(1);
                    return true;
                }
                if (itemId == R.id.bottom_settings) {
                    binding.viewPager.setCurrentItem(2);
                    return true;
                }
                return false;
            }
        });

        // Thiết lập callback thay đổi trang cho ViewPager2
        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    binding.bottomNavigation.getMenu().getItem(0).setChecked(false);
                }
                binding.bottomNavigation.getMenu().getItem(position).setChecked(true);
                prevMenuItem = binding.bottomNavigation.getMenu().getItem(position);
            }
        });

        setupViewPager();
        requestPermissions();
    }

    /**
     * Thiết lập ViewPager với các Fragment.
     *
     * Tác giả: Trần Văn An
     */
    private void setupViewPager() {
        ConversationsFragment conversationsFragment = new ConversationsFragment();
        FriendRequestsFragment friendRequestsFragment = new FriendRequestsFragment();
        SettingsFragment settingsFragment = new SettingsFragment();

        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        adapter.addFragment(conversationsFragment);
        adapter.addFragment(friendRequestsFragment);
        adapter.addFragment(settingsFragment);

        binding.setViewPagerAdapter(adapter);
    }

    /**
     * Yêu cầu thiết bị cấp quyền hiển thị thông báo
     *
     * Tác giả: Nguyễn Hà Quỳnh Giao
     */

    public void requestPermissions(){
        //Kiểm tra phiên bảng SDK của thiết bị
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            // Kiểm tra quyền gửi thông báo đã được cấp chưa
            if(ContextCompat.checkSelfPermission(HomeActivity.this,
                    Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // Yêu cầu quyền nếu chưa được cấp
                ActivityCompat.requestPermissions(HomeActivity.this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }

    }
}