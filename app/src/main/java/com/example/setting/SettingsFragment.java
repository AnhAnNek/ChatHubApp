package com.example.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.common.navigation.EAnimationType;
import com.example.common.repository.MediaRepos;
import com.example.common.repository.MediaReposImpl;
import com.example.databinding.FragmentSettingsBinding;
import com.example.infrastructure.BaseFragment;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.AuthReposImpl;
import com.example.user.repository.UserRepos;
import com.example.user.repository.UserReposImpl;

/**
 * SettingsFragment quản lý giao diện cài đặt của người dùng.
 *
 * Tác giả: Trần Văn An
 */
public class SettingsFragment extends BaseFragment<SettingsViewModel, FragmentSettingsBinding> {

    /**
     * Tạo và trả về FragmentSettingsBinding.
     *
     * @param inflater  LayoutInflater để tạo layout
     * @param container ViewGroup chứa fragment
     * @return FragmentSettingsBinding
     *
     * Tác giả: Trần Văn An
     */
    @Override
    protected FragmentSettingsBinding getViewDataBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentSettingsBinding.inflate(inflater, container, false);
    }

    /**
     * Trả về class của ViewModel.
     *
     * @return Class của SettingsViewModel
     *
     * Tác giả: Trần Văn An
     */
    @Override
    protected Class<SettingsViewModel> getViewModelClass() {
        return SettingsViewModel.class;
    }

    /**
     * Trả về ViewModelProvider.Factory cho SettingsViewModel.
     *
     * @return ViewModelProvider.Factory
     *
     * Tác giả: Trần Văn An
     */
    @Override
    protected ViewModelProvider.Factory getViewModelFactory() {
        MediaRepos mediaRepos = new MediaReposImpl();
        UserRepos userRepos = new UserReposImpl(mediaRepos);
        AuthRepos authRepos = new AuthReposImpl(userRepos);
        return new SettingsViewModelFactory(authRepos);
    }

    /**
     * Xử lý khi View đã được tạo.
     *
     * @param view View đã được tạo
     * @param savedInstanceState Bundle chứa trạng thái trước đó
     *
     * Tác giả: Trần Văn An
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupObservers();
    }

    /**
     * Thiết lập các observer cho ViewModel.
     *
     * Tác giả: Trần Văn An
     */
    private void setupObservers() {
        viewModel.getNavigateToUserProfile().observe(requireActivity(), navigate -> {
            if (navigate) {
                navigationManager.navigateToUserProfile(EAnimationType.FADE_IN);
            }
        });

        viewModel.getNavigateToAccountLinking().observe(requireActivity(), navigate -> {
            if (navigate) {
                navigationManager.navigateToAccountLinking(EAnimationType.FADE_IN);
            }
        });

        viewModel.getNavigateToLogin().observe(requireActivity(), navigate -> {
            if (navigate) {
                navigationManager.navigateToLogin(EAnimationType.FADE_OUT);
            }
        });
    }
}