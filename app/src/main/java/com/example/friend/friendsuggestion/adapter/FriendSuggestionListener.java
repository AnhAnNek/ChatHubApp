package com.example.friend.friendsuggestion.adapter;

/**
 * Giao diện để lắng nghe sự kiện trên một item trong danh sách gợi ý bạn bè.
 *
 * Tác giả: Trần Văn An
 */
public interface FriendSuggestionListener {
    /**
     * Được gọi khi một item trong danh sách được nhấp vào.
     *
     * @param position Vị trí của item trong danh sách.
     *
     * Tác giả: Trần Văn An
     */
    void onItemClick(int position);

    /**
     * Được gọi khi người dùng nhấp vào nút thêm bạn bè trên một item.
     *
     * @param position Vị trí của item trong danh sách.
     *
     * Tác giả: Trần Văn An
     */
    void onAddFriendClick(int position);

    /**
     * Được gọi khi người dùng nhấp vào nút xóa trên một item.
     *
     * @param position Vị trí của item trong danh sách.
     *
     * Tác giả: Trần Văn An
     */
    void onRemoveClick(int position);
}