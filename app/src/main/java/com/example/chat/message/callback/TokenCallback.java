package com.example.chat.message.callback;

/**
 * Interface callback để nhận kết quả lấy token fcm của người được gửi tin nhắn.
 * Tác giả: Văn Hoàng
 */
public interface TokenCallback {
    /**
     * Phương thức được gọi khi lấy token fcm thành công.
     *
     * @param token Chuỗi token.
     * Tác giả: Văn Hoàng
     */
    void onTokenReceived(String token);
}
