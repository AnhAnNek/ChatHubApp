package com.example.user.profile;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.common.customcontrol.customalertdialog.AlertDialogModel;
import com.example.common.customcontrol.inputdialogfragment.InputDialogModel;
import com.example.infrastructure.AppExecutors;
import com.example.infrastructure.BaseViewModel;
import com.example.infrastructure.Utils;
import com.example.user.EGender;
import com.example.user.User;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.UserRepos;

import java.util.Calendar;
import java.util.Date;

/**
 * ViewModel cho UserProfileActivity, quản lý dữ liệu và logic của giao diện người dùng.
 *
 * Tác giả: Trần Văn An
 */
public class UserProfileViewModel extends BaseViewModel {

    private static final String TAG = UserProfileViewModel.class.getSimpleName();

    private final MutableLiveData<Uri> imageUri = new MutableLiveData<>();
    private final MutableLiveData<String> fullName = new MutableLiveData<>();
    private final MutableLiveData<EGender> gender = new MutableLiveData<>();
    private final MutableLiveData<String> birthdayStr = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateBack = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isUserInitializing = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isDataChanged = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isUserUpdating = new MutableLiveData<>();
    private final MutableLiveData<Boolean> openImagePicker = new MutableLiveData<>();
    private final MutableLiveData<Calendar> openDatePickerDialog = new MutableLiveData<>();
    private final MutableLiveData<Integer> openSingleChoiceGender = new MutableLiveData<>();
    private final UserRepos userRepos;
    private User originalUser;
    private int selectedGenderIndex = 0;

    /**
     * Lấy LiveData của ảnh đại diện người dùng.
     *
     * @return LiveData của Uri ảnh đại diện.
     *
     * Tác giả: Trần Văn An
     */
    public LiveData<Uri> getImageUri() {
        return imageUri;
    }

    /**
     * Cập nhật ảnh đại diện người dùng.
     *
     * @param imageUri Uri của ảnh đại diện mới.
     *
     * Tác giả: Trần Văn An
     */
    public void setImageUri(Uri imageUri) {
        this.imageUri.postValue(imageUri);
        this.isDataChanged.postValue(true);
    }

    /**
     * Lấy LiveData của tên đầy đủ người dùng.
     *
     * @return LiveData của tên đầy đủ.
     *
     * Tác giả: Trần Văn An
     */
    public LiveData<String> getFullName() {
        return fullName;
    }

    /**
     * Lấy LiveData của giới tính người dùng.
     *
     * @return LiveData của giới tính.
     *
     * Tác giả: Trần Văn An
     */
    public LiveData<EGender> getGender() {
        return gender;
    }

    /**
     * Cập nhật giới tính người dùng.
     *
     * @param gender Giới tính mới.
     *
     * Tác giả: Trần Văn An
     */
    public void setGender(EGender gender) {
        this.gender.postValue(gender);
        if (gender.equals(originalUser.getGender())) {
            this.isDataChanged.postValue(false);
        } else {
            this.isDataChanged.postValue(true);
        }
    }

    /**
     * Lấy LiveData của ngày sinh người dùng dưới dạng chuỗi.
     *
     * @return LiveData của ngày sinh.
     *
     * Tác giả: Trần Văn An
     */
    public LiveData<String> getBirthdayStr() {
        return birthdayStr;
    }

    /**
     * Cập nhật ngày sinh người dùng.
     *
     * @param birthday Ngày sinh mới.
     *
     * Tác giả: Trần Văn An
     */
    public void setBirthday(Date birthday) {
        String originalBirthdayStr = Utils.dateToString(originalUser.getBirthday());
        String birthdayStr = Utils.dateToString(birthday);
        this.birthdayStr.postValue(birthdayStr);
        if (birthdayStr.equals(originalBirthdayStr)) {
            this.isDataChanged.postValue(false);
        } else {
            this.isDataChanged.postValue(true);
        }
    }

    /**
     * Lấy LiveData của trạng thái điều hướng về màn hình trước.
     *
     * @return LiveData của trạng thái điều hướng.
     *
     * Tác giả: Trần Văn An
     */
    public LiveData<Boolean> getNavigateBack() {
        return navigateBack;
    }

    /**
     * Lấy LiveData của trạng thái khởi tạo người dùng.
     *
     * @return LiveData của trạng thái khởi tạo.
     *
     * Tác giả: Trần Văn An
     */
    public MutableLiveData<Boolean> getIsUserInitializing() {
        return isUserInitializing;
    }

    /**
     * Lấy LiveData của trạng thái dữ liệu có thay đổi hay không.
     *
     * @return LiveData của trạng thái dữ liệu.
     *
     * Tác giả: Trần Văn An
     */
    public MutableLiveData<Boolean> getIsDataChanged() {
        return isDataChanged;
    }

    /**
     * Lấy LiveData của trạng thái cập nhật người dùng.
     *
     * @return LiveData của trạng thái cập nhật.
     *
     * Tác giả: Trần Văn An
     */
    public MutableLiveData<Boolean> getIsUserUpdating() {
        return isUserUpdating;
    }

    /**
     * Lấy LiveData để mở bộ chọn ảnh.
     *
     * @return LiveData để mở bộ chọn ảnh.
     *
     * Tác giả: Trần Văn An
     */
    public LiveData<Boolean> getOpenImagePicker() {
        return openImagePicker;
    }

    /**
     * Lấy LiveData để mở bộ chọn ngày.
     *
     * @return LiveData để mở bộ chọn ngày.
     *
     * Tác giả: Trần Văn An
     */
    public MutableLiveData<Calendar> getOpenDatePickerDialog() {
        return openDatePickerDialog;
    }

    /**
     * Lấy LiveData để mở bộ chọn giới tính một lựa chọn.
     *
     * @return LiveData để mở bộ chọn giới tính.
     *
     * Tác giả: Trần Văn An
     */
    public LiveData<Integer> getOpenSingleChoiceGender() {
        return openSingleChoiceGender;
    }

    /**
     * Khởi tạo UserProfileViewModel với các repository cần thiết.
     *
     * @param userRepos Repository cho người dùng
     * @param authRepos Repository cho xác thực
     *
     * Tác giả: Trần Văn An
     */
    public UserProfileViewModel(UserRepos userRepos, AuthRepos authRepos) {
        this.userRepos = userRepos;
        this.authRepos = authRepos;
    }

    /**
     * Phương thức gọi khi ViewModel được tạo.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    protected void onCreate() {
        super.onCreate();

        isDataChanged.postValue(false);

        AppExecutors.getIns().networkIO().execute(() -> {
            fetchUserProfile();
        });
    }

    /**
     * Điều hướng về màn hình chính.
     *
     * Tác giả: Trần Văn An
     */
    public void navigateToHome() {
        navigateBack.postValue(true);
    }

    /**
     * Mở bộ chọn ảnh.
     *
     * Tác giả: Trần Văn An
     */
    public void openImagePicker() {
        openImagePicker.postValue(true);
    }

    /**
     * Mở hộp thoại nhập tên người dùng.
     *
     * Tác giả: Trần Văn An
     */
    public void openCustomInputDialog() {
        InputDialogModel model = new InputDialogModel.Builder()
                .setTitle("Full name")
                .setCurrentContent(fullName.getValue())
                .setSubmitButtonClickListener(newName -> {
                    if (newName.isEmpty()) {
                        return;
                    }
                    fullName.postValue(newName);
                    if (newName.equals(originalUser.getFullName())) {
                        this.isDataChanged.postValue(false);
                    } else {
                        this.isDataChanged.postValue(true);
                    }
                })
                .build();
        inputDialogModel.postValue(model);
    }

    /**
     * Mở bộ chọn ngày sinh.
     *
     * Tác giả: Trần Văn An
     */
    public void openDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        Date selectedBirthday = Utils.stringToDate(birthdayStr.getValue());
        calendar.setTime(selectedBirthday != null ? selectedBirthday : originalUser.getBirthday());
        openDatePickerDialog.postValue(calendar);
    }

    /**
     * Mở hộp thoại xác nhận cập nhật thông tin.
     *
     * Tác giả: Trần Văn An
     */
    public void openUpdateDialog() {
        AlertDialogModel model = new AlertDialogModel.Builder()
                .setTitle("Update!")
                .setMessage("When you click \"OK\", your information will be updated.")
                .setPositiveButton("Ok", aVoid -> {
                    updateUser();
                })
                .setNegativeButton("Reset", aVoid -> {
                    resetAllFields();
                })
                .build();
        alertDialogModel.postValue(model);
    }

    /**
     * Mở bộ chọn giới tính (một lựa chọn).
     *
     * Tác giả: Trần Văn An
     */
    public void openSingleChoiceGender() {
        EGender curGenderSelected = gender.getValue();
        int curIndexSelected = EGender.getCurrentIndex(curGenderSelected);
        openSingleChoiceGender.postValue(curIndexSelected);
    }

    /**
     * Cập nhật thông tin người dùng.
     *
     * Tác giả: Trần Văn An
     */
    private void updateUser() {
        isUserUpdating.postValue(true);

        AppExecutors.getIns().networkIO().execute(() -> {
            originalUser.setUri(imageUri.getValue());
            originalUser.setFullName(fullName.getValue());
            originalUser.setGender(gender.getValue());
            Date birthday = Utils.stringToDate(birthdayStr.getValue());
            originalUser.setBirthday(birthday);

            String uid = authRepos.getCurrentUid();
            userRepos.updateBasicUser(uid, originalUser)
                    .thenAccept(aVoid -> {
                        successToastMessage.postValue("Update successfully");
                        isDataChanged.postValue(false);
                        isUserUpdating.postValue(false);
                    })
                    .exceptionally(e -> {
                        setUser(originalUser);
                        errorToastMessage.postValue("Update unsuccessfully");
                        isUserUpdating.postValue(false);
                        Log.e(TAG, "Error" + e);
                        return null;
                    });
        });
    }

    /**
     * Lấy thông tin hồ sơ người dùng từ repository.
     *
     * Tác giả: Trần Văn An
     */
    private void fetchUserProfile() {
        isUserInitializing.postValue(true);
        authRepos.getCurrentUser()
                .thenAccept(user -> {
                    if (user != null) {
                        setUser(user);
                    }
                    isUserInitializing.postValue(false);
                })
                .exceptionally(e -> {
                    Log.e(TAG, String.valueOf(e));
                    isUserInitializing.postValue(false);
                    return null;
                });
    }

    /**
     * Cập nhật dữ liệu người dùng trong ViewModel.
     *
     * @param user Người dùng cần cập nhật.
     *
     * Tác giả: Trần Văn An
     */
    private void setUser(User user) {
        originalUser = user;

        imageUri.postValue(user.getUri());
        fullName.postValue(user.getFullName());
        gender.postValue(user.getGender());
        Date birthday = user.getBirthday();
        birthdayStr.postValue(Utils.dateToString(birthday));
    }

    /**
     * Đặt lại tất cả các trường về giá trị ban đầu.
     *
     * Tác giả: Trần Văn An
     */
    private void resetAllFields() {
        setUser(originalUser);
        isDataChanged.postValue(false);
    }

    /**
     * Lấy chỉ số giới tính được chọn hiện tại.
     *
     * @return Chỉ số giới tính được chọn.
     *
     * Tác giả: Trần Văn An
     */
    public int getSelectedGenderIndex() {
        return selectedGenderIndex;
    }

    /**
     * Đặt chỉ số giới tính được chọn.
     *
     * @param selectedGenderIndex Chỉ số giới tính được chọn.
     *
     * Tác giả: Trần Văn An
     */
    public void setSelectedGenderIndex(int selectedGenderIndex) {
        this.selectedGenderIndex = selectedGenderIndex;
    }
}