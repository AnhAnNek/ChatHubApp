package com.example.friend.myfriend.adapter;

/**
 * Interface để xử lý sự kiện khi người dùng tương tác với các mục trong RecyclerView.
 *
 * Tác giả: Trần Văn An
 */
public interface FriendListener {
    /**
     * Xử lý sự kiện khi một mục trong RecyclerView được nhấp.
     *
     * @param position Vị trí của mục trong RecyclerView.
     *
     * Tác giả: Trần Văn An
     */
    void onItemClick(int position);

    /**
     * Xử lý sự kiện khi người dùng muốn mở cuộc trò chuyện với một mục trong RecyclerView.
     *
     * @param position Vị trí của mục trong RecyclerView.
     *
     * Tác giả: Trần Văn An
     */
    void onOpenChatClick(int position);
}
