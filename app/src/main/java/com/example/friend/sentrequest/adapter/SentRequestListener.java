package com.example.friend.sentrequest.adapter;

/**
 * Giao diện này định nghĩa các phương thức để lắng nghe sự kiện khi một yêu cầu kết bạn đã được gửi.
 *
 * Tác giả: Trần Văn An
 */
public interface SentRequestListener {

    /**
     * Được gọi khi người dùng nhấn vào một yêu cầu kết bạn trong danh sách.
     *
     * @param position Vị trí của yêu cầu kết bạn trong danh sách.
     *
     * Tác giả: Trần Văn An
     */
    void onItemClick(int position);

    /**
     * Được gọi khi người dùng nhấn vào nút thu hồi trên một yêu cầu kết bạn đã gửi.
     *
     * @param position Vị trí của yêu cầu kết bạn trong danh sách.
     *
     * Tác giả: Trần Văn An
     */
    void onRecallClick(int position);
}
