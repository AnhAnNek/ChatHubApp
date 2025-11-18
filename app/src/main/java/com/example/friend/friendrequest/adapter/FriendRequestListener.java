package com.example.friend.friendrequest.adapter;

/**
 * Interface định nghĩa các sự kiện khi người dùng tương tác với các yêu cầu kết bạn.
 *
 * Tác giả: Trần Văn An
 */
public interface FriendRequestListener {
    /**
     * Xử lý sự kiện khi người dùng nhấn vào một item trong danh sách.
     *
     * @param position Vị trí của item trong danh sách.
     *
     * Tác giả: Trần Văn An
     */
    void onItemClick(int position);


    /**
     * Xử lý sự kiện khi người dùng nhấn vào nút chấp nhận yêu cầu kết bạn.
     *
     * @param position Vị trí của item trong danh sách.
     *
     * Tác giả: Trần Văn An
     */
    void onAcceptClick(int position);

    /**
     * Xử lý sự kiện khi người dùng nhấn vào nút từ chối yêu cầu kết bạn.
     *
     * @param position Vị trí của item trong danh sách.
     *
     * Tác giả: Trần Văn An
     */
    void onRejectClick(int position);
}
