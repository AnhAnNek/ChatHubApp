package com.example.user.signup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.user.repository.AuthRepos;

/**
 * Factory để tạo ra các đối tượng ViewModel.
 * Đối với ViewModel cụ thể này, nó sẽ tạo một instance của SignUpViewModel với AuthRepos được cung cấp.
 *
 * Tác giả: Trần Văn An
 */
public class SignUpViewModelFactory implements ViewModelProvider.Factory {

    private final AuthRepos authRepos;

    /**
     * Constructor, khởi tạo SignUpViewModelFactory với AuthRepos được cung cấp.
     *
     * @param authRepos AuthRepos là một interface đại diện cho tất cả các thao tác xác thực.
     *
     * Tác giả: Trần Văn An
     */
    public SignUpViewModelFactory(AuthRepos authRepos) {
        this.authRepos = authRepos;
    }

    /**
     * Phương thức này được gọi bởi ViewModelProvider khi cần tạo một ViewModel mới.
     *
     * @param modelClass Lớp của ViewModel cần được tạo.
     * @param <T>        Kiểu của ViewModel.
     * @return ViewModel mới được tạo.
     *
     * Tác giả: Trần Văn An
     * @throws IllegalArgumentException Nếu modelClass không khớp với SignUpViewModel.
     */
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        // Kiểm tra xem lớp được chỉ định có thể gán với lớp SignUpViewModel không
        if (modelClass.isAssignableFrom(SignUpViewModel.class)) {
            // Trả về một instance của SignUpViewModel với AuthRepos được cung cấp
            return (T) new SignUpViewModel(authRepos);
        }
        // Nếu lớp không khớp, ném một ngoại lệ IllegalArgumentException
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
