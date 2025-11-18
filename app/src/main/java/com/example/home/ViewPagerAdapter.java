package com.example.home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter cho ViewPager2 để quản lý các Fragment.
 *
 * Tác giả: Trần Văn An
 */
public class ViewPagerAdapter extends FragmentStateAdapter {
    private final List<Fragment> fragmentList = new ArrayList<>();

    /**
     * Khởi tạo ViewPagerAdapter với FragmentActivity.
     *
     * @param fragmentActivity FragmentActivity chứa adapter
     *
     * Tác giả: Trần Văn An
     */
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    /**
     * Tạo Fragment tại vị trí cụ thể.
     *
     * @param position Vị trí của Fragment trong adapter
     * @return Fragment tại vị trí cụ thể
     *
     * Tác giả: Trần Văn An
     */
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }

    /**
     * Trả về số lượng Fragment trong adapter.
     *
     * @return Số lượng Fragment
     *
     * Tác giả: Trần Văn An
     */
    @Override
    public int getItemCount() {
        return fragmentList.size();
    }

    /**
     * Thêm một Fragment vào adapter.
     *
     * @param fragment Fragment cần thêm vào
     *
     * Tác giả: Trần Văn An
     */
    public void addFragment(Fragment fragment) {
        fragmentList.add(fragment);
    }
}
