package com.example.infrastructure;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;

import com.example.BR;
import com.example.common.navigation.NavigationManager;
import com.example.common.navigation.NavigationManagerImpl;

/**
 * BaseActivity là một abstract class định nghĩa các phương thức cơ bản và hành vi chung
 * cho tất cả các Activity trong ứng dụng.Các Activity khác trong ứng dụng cần kế thừa
 * từ BaseActivity này để sử dụng các phương thức đã được định nghĩa sẵn.
 *
 * Tác giả: Trần Văn An
 */
public abstract class BaseActivity<VM extends BaseViewModel, B extends ViewDataBinding> extends AppCompatActivity {

    protected VM viewModel;
    protected B binding;
    protected NavigationManager navigationManager;

    /**
     * Phương thức abstract getLayout() cần được cài đặt trong các class con
     * để cung cấp layout cho Activity.
     *
     * @return ID của layout được sử dụng trong Activity
     *
     * Tác giả: Trần Văn An
     */
    protected abstract @LayoutRes int getLayout();

    /**
     * Phương thức abstract getViewModelClass() cần được cài đặt trong các class con
     * để cung cấp lớp ViewModel tương ứng.
     *
     * @return Lớp ViewModel được sử dụng trong Activity
     *
     * Tác giả: Trần Văn An
     */
    protected abstract Class<VM> getViewModelClass();

    /**
     * Phương thức getViewModelFactory() trả về Factory để tạo ViewModel.
     * Mặc định là null, có thể override nếu cần thiết.
     *
     * @return Factory để tạo ViewModel
     *
     * Tác giả: Trần Văn An
     */
    protected ViewModelProvider.Factory getViewModelFactory() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(this);
        init();

        viewModel.onCreate();
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

        binding = DataBindingUtil.setContentView(this, getLayout());
        binding.setLifecycleOwner(this);
        binding.setVariable(BR.viewModel, viewModel);

        navigationManager = new NavigationManagerImpl(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewModel.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewModel.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        viewModel.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.onDestroy();
    }
}
