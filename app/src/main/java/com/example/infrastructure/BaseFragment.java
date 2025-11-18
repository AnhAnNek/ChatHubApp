package com.example.infrastructure;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.BR;
import com.example.common.navigation.NavigationManager;
import com.example.common.navigation.NavigationManagerImpl;

/**
 * BaseFragment là một abstract class định nghĩa các phương thức cơ bản và hành vi chung
 * cho tất cả các Fragment trong ứng dụng. Các Fragment khác trong ứng dụng cần kế thừa
 * từ BaseFragment này để sử dụng các phương thức đã được định nghĩa sẵn.
 *
 * Tác giả: Trần Văn An
 */
public abstract class BaseFragment<VM extends BaseViewModel, B extends ViewDataBinding> extends Fragment {

    protected VM viewModel;
    protected B binding;
    protected NavigationManager navigationManager;

    /**
     * Phương thức abstract getViewDataBinding() cần được cài đặt trong các class con
     * để cung cấp ViewDataBinding cho Fragment.
     *
     * @param inflater  Inflater để inflate layout
     * @param container Container cha cho layout
     * @return ViewDataBinding được sử dụng trong Fragment
     *
     * Tác giả: Trần Văn An
     */
    protected abstract B getViewDataBinding(LayoutInflater inflater, ViewGroup container);

    /**
     * Phương thức abstract getViewModelClass() cần được cài đặt trong các class con
     * để cung cấp lớp ViewModel tương ứng.
     *
     * @return Lớp ViewModel được sử dụng trong Fragment
     *
     * Tác giả: Trần Văn An
     */
    protected abstract Class<VM> getViewModelClass();

    protected ViewModelProvider.Factory getViewModelFactory() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = getViewDataBinding(inflater, container);
        init();
        viewModel.onCreate();
        return binding.getRoot();
    }

    /**
     * Phương thức init() khởi tạo ViewModel, DataBinding và NavigationManager.
     *
     * Tác giả: Trần Văn An
     */
    protected void init() {
        ViewModelProvider.Factory factory = getViewModelFactory();
        Class<VM> vmClass = getViewModelClass();
        viewModel = factory == null
                ? new ViewModelProvider(this).get(vmClass)
                : new ViewModelProvider(this, factory).get(vmClass);

        binding.setLifecycleOwner(this);
        binding.setVariable(BR.viewModel, viewModel);

        navigationManager = new NavigationManagerImpl(requireActivity());
    }

    @Override
    public void onStart() {
        super.onStart();
        viewModel.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        viewModel.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        viewModel.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModel.onDestroy();
    }
}