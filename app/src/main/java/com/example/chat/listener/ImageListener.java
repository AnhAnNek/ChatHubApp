package com.example.chat.listener;

/**
 * Interface định nghĩa phương thức lắng nghe sự kiện click trên một hình ảnh.
 * Được sử dụng để thông báo khi một hình ảnh tại một vị trí cụ thể trong danh sách được click.

 * Tác giả: Văn Hoàng
 */
public interface ImageListener {
    /**
     * Phương thức này được gọi khi một hình ảnh được click.
     *
     * @param position Vị trí của hình ảnh được click trong danh sách trong ChatAdapter.
     * Tác giả: Văn Hoàng
     */
    void onImageClick(int position);
}

