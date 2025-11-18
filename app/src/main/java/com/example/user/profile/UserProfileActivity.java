package com.example.user.profile;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.DatePicker;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.common.navigation.EAnimationType;
import com.example.common.repository.MediaRepos;
import com.example.common.repository.MediaReposImpl;
import com.example.databinding.ActivityUserProfileBinding;
import com.example.infrastructure.BaseActivity;
import com.example.user.EGender;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.AuthReposImpl;
import com.example.user.repository.UserRepos;
import com.example.user.repository.UserReposImpl;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Calendar;

/**
 * Activity cho phép người dùng chỉnh sửa thông tin cá nhân của mình.
 *
 * Tác giả: Trần Văn An
 */
public class UserProfileActivity extends BaseActivity<UserProfileViewModel, ActivityUserProfileBinding> {

    /**
     * Lấy layout cho activity từ tài nguyên.
     *
     * @return ID của layout activity_user_profile
     *
     * Tác giả: Trần Văn An
     */
    @Override
    protected int getLayout() {
        return R.layout.activity_user_profile;
    }

    /**
     * Trả về lớp ViewModel tương ứng.
     *
     * @return Lớp UserProfileViewModel.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    protected Class<UserProfileViewModel> getViewModelClass() {
        return UserProfileViewModel.class;
    }

    /**
     * Tạo một ViewModelProvider.Factory để tạo ViewModel.
     *
     * @return Một ViewModelProvider.Factory để tạo ViewModel
     *
     * Tác giả: Trần Văn An
     */
    @Override
    protected ViewModelProvider.Factory getViewModelFactory() {
        MediaRepos mediaRepos = new MediaReposImpl();
        UserRepos userRepos = new UserReposImpl(mediaRepos);
        AuthRepos authRepos = new AuthReposImpl(userRepos);
        return new UserProfileViewModelFactory(userRepos, authRepos);
    }

    /**
     * Thực hiện các cài đặt và gắn các quan sát viên (observers).
     *
     * Tác giả: Trần Văn An
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupObservers();
    }

    /**
     * Thiết lập các quan sát viên cho các LiveData trong ViewModel.
     *
     * Tác giả: Trần Văn An
     */
    private void setupObservers() {
        viewModel.getNavigateBack().observe(this, navigate -> {
            if (navigate) {
                navigationManager.navigateBack(null, EAnimationType.FADE_IN);
            }
        });

        viewModel.getOpenImagePicker().observe(this, pick -> {
            if (pick) {
                openImagePicker();
            }
        });

        viewModel.getOpenDatePickerDialog().observe(this, this::openDatePickerDialog);

        viewModel.getOpenSingleChoiceGender().observe(this, this::openSingleChoiceGender);
    }

    /**
     * Mở màn hình chọn ảnh từ thư viện ảnh.
     *
     * Tác giả: Trần Văn An
     */
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        pickImage.launch(intent);
    }

    // Launcher để nhận kết quả khi người dùng chọn hình ảnh từ bộ sưu tập.
    private final ActivityResultLauncher pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // Kiểm tra xem kết quả trả về có thành công không
                if (result.getResultCode() != RESULT_OK) {
                    return;
                }

                // Kiểm tra xem dữ liệu trả về có rỗng không
                if (result.getData() == null) {
                    return;
                }
                // Lấy URI của hình ảnh đã chọn từ dữ liệu trả về
                Uri imgUri = result.getData().getData();

                // Đặt URI của hình ảnh vào ViewModel để hiển thị hình ảnh đã chọn
                viewModel.setImageUri(imgUri);
            }
    );

    /**
     * Khởi tạo và hiển thị hộp thoại chọn ngày sinh.
     *
     * @param currentDate Ngày hiện tại để hiển thị trên DatePickerDialog
     *
     * Tác giả: Trần Văn An
     */
    private void openDatePickerDialog(Calendar currentDate) {
        // Lấy thông tin về năm, tháng và ngày từ currentDate
        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH);
        int dayOfMonth = currentDate.get(Calendar.DAY_OF_MONTH);

        // Tạo một DatePickerDialog với ngày ban đầu là currentDate
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Tạo một Calendar mới để lưu trữ ngày được chọn
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);

                // Đặt ngày sinh mới vào ViewModel
                viewModel.setBirthday(selectedDate.getTime());
            }
        }, year, month, dayOfMonth);

        // Hiển thị DatePickerDialog lên màn hình
        datePickerDialog.show();
    }

    /**
     * Mở hộp thoại chọn giới tính.
     *
     * @param selectedItem Chỉ mục của mục đã chọn trước đó
     *
     * Tác giả: Trần Văn An
     */
    private void openSingleChoiceGender(int selectedItem) {
        viewModel.setSelectedGenderIndex(selectedItem);
        String[] genderStrs = EGender.getAllDisplays();

        // Tạo một MaterialAlertDialogBuilder để hiển thị hộp thoại chọn giới tính
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this)
                .setIcon(R.drawable.ic_gender)
                .setTitle("Gender")
                .setSingleChoiceItems(genderStrs, selectedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Đặt chỉ mục của mục giới tính được chọn vào ViewModel
                        viewModel.setSelectedGenderIndex(which);
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Lấy giới tính được chọn từ ViewModel và đặt nó vào ViewModel
                        EGender curSelected = EGender.values()[viewModel.getSelectedGenderIndex()];
                        viewModel.setGender(curSelected);

                        // Đóng hộp thoại
                        dialog.dismiss();
                    }
                });
        // Hiển thị hộp thoại lên màn hình
        builder.show();
    }
}