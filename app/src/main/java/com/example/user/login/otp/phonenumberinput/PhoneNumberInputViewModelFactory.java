package com.example.user.login.otp.phonenumberinput;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.user.repository.AuthRepos;
import com.example.user.repository.UserRepos;

/**
 * Factory để tạo ra PhoneNumberInputViewModel.
 *
 * Tác giả: Trần Văn An
 */
public class PhoneNumberInputViewModelFactory implements ViewModelProvider.Factory {

    private final AuthRepos authRepos;

    public PhoneNumberInputViewModelFactory(AuthRepos authRepos) {
        this.authRepos = authRepos;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PhoneNumberInputViewModel.class)) {
            return (T) new PhoneNumberInputViewModel(authRepos);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
