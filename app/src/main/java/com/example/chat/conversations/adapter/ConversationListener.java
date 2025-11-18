package com.example.chat.conversations.adapter;

/**
 * Interface định nghĩa phương thức lắng nghe sự kiện click lên đối tượng cuộc trò chuyện.
 * Sử dụng để chuyển sang khung trò chuyện.
 *
 * Tác giả: Nguyễn Hà Quỳnh Giao
 */
public interface ConversationListener {

    /**
     * Bắt sự kiện khi click vào đối tượng cuộc trò chuyện.
     *
     * @param position : Vị trí cuộc cuộc trò chuyện trong danh sách trò chuyện.
     *
     * Tác giả: Nguyễn Hà Quỳnh Giao
     */
    void onItemClick(int position);
}
