package com.example.chat.image;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.infrastructure.BaseViewModel;

/**
 * ViewModel quản lý dữ liệu và logic liên quan đến việc chọn và hiển thị hình ảnh.
 *
 * - Kế thừa từ BaseViewModel để tận dụng các chức năng cơ bản của một ViewModel.
 * - Sử dụng MutableLiveData để lưu trữ URL của hình ảnh được chọn.
 *
 * Tác giả: Văn Hoàng
 */
public class ImageViewModel extends BaseViewModel {
    private static final String TAG = ImageViewModel.class.getSimpleName();

    // MutableLiveData để lưu trữ URL của hình ảnh được chọn.
    private final MutableLiveData<String> imagePickedUrl = new MutableLiveData<>(null);

    /**
     * Khởi tạo ImageViewModel với URL của hình ảnh.
     *
     * @param imageUrl URL của hình ảnh ban đầu.
     * Tác giả: Văn Hoàng
     */
    public ImageViewModel(String imageUrl) {
        // Đặt giá trị ban đầu cho imagePickedUrl.
        imagePickedUrl.postValue(imageUrl);
    }

    /**
     * Trả về LiveData cho imagePickedUrl để các thành phần giao diện có thể quan sát.
     *
     * @return LiveData chứa URL của hình ảnh được chọn.
     * Tác giả: Văn Hoàng
     */
    public LiveData<String> getImagePickedUrl() {
        return imagePickedUrl;
    }
}

