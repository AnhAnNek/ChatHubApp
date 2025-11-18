package com.example.chat.message.callback;

/**
 * Interface định nghĩa phương thức callback cho việc tải ảnh lên.
 * Được sử dụng để thông báo khi ảnh đã được tải lên thành công.
 * Tác giả: Văn Hoàng
 */
public interface UploadImageCallBack {
    /**
     * Phương thức được gọi khi ảnh được tải lên thành công.
     *
     * @param url URL của ảnh đã được tải lên.
     * Tác giả: Văn Hoàng
     */
    void onSuccess(String url);
}
