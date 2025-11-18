package com.example.user.login.otp.phonenumberinput;

import android.os.Bundle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.infrastructure.BaseViewModel;
import com.example.infrastructure.Utils;
import com.example.user.Validator;
import com.example.user.repository.AuthRepos;

/**
 * Lớp ViewModel để xử lý logic của việc nhập số điện thoại để xác minh OTP.
 *
 * Tác giả: Trần Văn An
 */
public class PhoneNumberInputViewModel extends BaseViewModel {

    private final MutableLiveData<String> countryCode = new MutableLiveData<>("+84");
    private final MutableLiveData<String> localNumber = new MutableLiveData<>();
    private final MutableLiveData<String> phoneNumberError = new MutableLiveData<>("");
    private final MutableLiveData<Bundle> navigateToVerifyOtpWithPhoneNumber = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToSignUp = new MutableLiveData<>();

    /**
     * Phương thức getter để lấy mã quốc gia.
     *
     * @return LiveData của mã quốc gia.
     *
     * Tác giả: Trần Văn An
     */
    public MutableLiveData<String> getCountryCode() {
        return countryCode;
    }


    /**
     * Phương thức getter để lấy số điện thoại cục bộ.
     *
     * @return LiveData của số điện thoại cục bộ.
     *
     * Tác giả: Trần Văn An
     */
    public MutableLiveData<String> getLocalNumber() {
        return localNumber;
    }

    /**
     * Phương thức getter để lấy lỗi số điện thoại.
     *
     * @return LiveData của lỗi số điện thoại.
     *
     * Tác giả: Trần Văn An
     */
    public LiveData<String> getPhoneNumberError() {
        return phoneNumberError;
    }

    /**
     * Phương thức getter để lấy dữ liệu để chuyển hướng tới màn hình xác minh OTP với số điện thoại.
     *
     * @return LiveData của dữ liệu để chuyển hướng tới màn hình xác minh OTP với số điện thoại.
     *
     * Tác giả: Trần Văn An
     */
    public LiveData<Bundle> getNavigateToVerifyOtpWithPhoneNumber() {
        return navigateToVerifyOtpWithPhoneNumber;
    }

    /**
     * Phương thức getter để lấy dữ liệu để chuyển hướng tới màn hình đăng ký.
     *
     * @return LiveData của dữ liệu để chuyển hướng tới màn hình đăng ký.
     *
     * Tác giả: Trần Văn An
     */
    public LiveData<Boolean> getNavigateToSignUp() {
        return navigateToSignUp;
    }

    /**
     * Constructor để khởi tạo ViewModel với AuthRepos.
     *
     * @param authRepos AuthRepos để thực hiện các thao tác liên quan đến xác thực.
     *
     * Tác giả: Trần Văn An
     */
    public PhoneNumberInputViewModel(AuthRepos authRepos) {
        this.authRepos = authRepos;
    }

    /**
     * Phương thức này được gọi để kiểm tra số điện thoại được nhập.
     *
     * @param s số điện thoại được nhập.
     *
     * Tác giả: Trần Văn An
     */
    public void checkPhoneNumber(CharSequence s) {
        String countryCode = this.countryCode.getValue();
        String localNumber = s.toString();
        String phoneNumber = Utils.getFullPhoneNumber(countryCode, localNumber);
        String error = Validator.validPhoneNumber(phoneNumber);
        this.phoneNumberError.postValue(error);
    }

    /**
     * Phương thức này được gọi để điều hướng tới màn hình xác minh OTP với số điện thoại đã nhập.
     *
     * Tác giả: Trần Văn An
     */
    public void navigateToVerifyOtpWithPhoneNumber() {
        String phoneNumber = getFullPhoneNumber();
        Bundle data = new Bundle();
        data.putString(Utils.EXTRA_PHONE_NUMBER, phoneNumber);
        navigateToVerifyOtpWithPhoneNumber.postValue(data);
    }

    /**
     * Phương thức để lấy số điện thoại đầy đủ.
     *
     * @return Số điện thoại đầy đủ kết hợp giữa mã quốc gia và số điện thoại cục bộ.
     *
     * Tác giả: Trần Văn An
     */
    private String getFullPhoneNumber() {
        String countryCode = this.countryCode.getValue();
        String number = localNumber.getValue();
        return Utils.getFullPhoneNumber(countryCode, number);
    }

    /**
     * Phương thức này được gọi để điều hướng tới màn hình đăng ký.
     *
     * Tác giả: Trần Văn An
     */
    public void navigateToSignUp() {
        navigateToSignUp.postValue(true);
    }
}
