package com.example.infrastructure;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.common.customcontrol.customalertdialog.AlertDialogModel;
import com.example.common.customcontrol.inputdialogfragment.InputDialogModel;
import com.example.common.customcontrol.snackbar.SnackbarModel;
import com.example.user.repository.AuthRepos;

/**
 * BaseViewModel là một abstract class định nghĩa các LiveData và các phương thức cơ bản của ViewModel.
 * Các ViewModel khác trong ứng dụng cần kế thừa từ BaseViewModel này
 * để sử dụng các LiveData và phương thức đã được định nghĩa sẵn.
 *
 * Tác giả: Trần Văn An
 */
public abstract class BaseViewModel extends ViewModel {

    /**
     * Các LiveData bao gồm:
     * - successToastMessage: LiveData để hiển thị thông báo thành công dưới dạng Toast.
     * - errorToastMessage: LiveData để hiển thị thông báo lỗi dưới dạng Toast.
     * - snackbarModel: LiveData để hiển thị Snackbar với các thông tin cụ thể.
     * - alertDialogModel: LiveData để hiển thị AlertDialog với các thông tin cụ thể.
     * - inputDialogModel: LiveData để hiển thị InputDialog với các thông tin cụ thể.
     */
    protected final MutableLiveData<String> successToastMessage = new MutableLiveData<>();
    protected final MutableLiveData<String> errorToastMessage = new MutableLiveData<>();
    protected final MutableLiveData<SnackbarModel> snackbarModel = new MutableLiveData<>();
    protected final MutableLiveData<AlertDialogModel> alertDialogModel = new MutableLiveData<>();
    protected final MutableLiveData<InputDialogModel> inputDialogModel = new MutableLiveData<>();

    protected AuthRepos authRepos;

    /**
     * Phương thức getter cho LiveData successToastMessage.
     *
     * @return LiveData để hiển thị thông báo thành công dưới dạng Toast.
     *
     * Tác giả: Trần Văn An
     */
    public MutableLiveData<String> getSuccessToastMessage() {
        return successToastMessage;
    }

    /**
     * Phương thức getter cho LiveData errorToastMessage.
     *
     * @return LiveData để hiển thị thông báo lỗi dưới dạng Toast.
     *
     * Tác giả: Trần Văn An
     */
    public MutableLiveData<String> getErrorToastMessage() {
        return errorToastMessage;
    }

    /**
     * Phương thức getter cho LiveData snackbarModel.
     *
     * @return LiveData để hiển thị Snackbar với các thông tin cụ thể.
     *
     * Tác giả: Trần Văn An
     */
    public LiveData<SnackbarModel> getSnackbarModel() {
        return snackbarModel;
    }

    /**
     * Phương thức getter cho LiveData alertDialogModel.
     *
     * @return LiveData để hiển thị AlertDialog với các thông tin cụ thể.
     *
     * Tác giả: Trần Văn An
     */
    public LiveData<AlertDialogModel> getAlertDialogModel() {
        return alertDialogModel;
    }

    /**
     * Phương thức getter cho LiveData inputDialogModel.
     *
     * @return LiveData để hiển thị InputDialog với các thông tin cụ thể.
     *
     * Tác giả: Trần Văn An
     */
    public LiveData<InputDialogModel> getInputDialogModel() {
        return inputDialogModel;
    }

    /**
     * Constructor mặc định.
     *
     * Tác giả: Trần Văn An
     */
    public BaseViewModel() {
    }

    /**
     * Được gọi khi phương thức onCreate() của Activity hoặc Fragment kết thúc.
     *
     * Tác giả: Trần Văn An
     */
    protected void onCreate() {
    }

    /**
     * Được gọi khi phương thức onStart() của Activity hoặc Fragment kết thúc.
     *
     * Tác giả: Trần Văn An
     */
    protected void onStart() {
    }

    /**
     * Được gọi khi phương thức onResume() của Activity hoặc Fragment kết thúc.
     *
     * Tác giả: Trần Văn An
     */
    protected void onResume() {
    }

    /**
     * Được gọi khi phương thức onPause() của Activity hoặc Fragment kết thúc.
     *
     * Tác giả: Trần Văn An
     */
    protected void onPause() {
    }

    /**
     * Được gọi khi phương thức onStop() của Activity hoặc Fragment kết thúc.
     *
     * Tác giả: Trần Văn An
     */
    protected void onStop() {
    }

    /**
     * Được gọi khi phương thức onDestroy() của Activity hoặc Fragment kết thúc.
     *
     * Tác giả: Trần Văn An
     */
    protected void onDestroy() {
    }
}
