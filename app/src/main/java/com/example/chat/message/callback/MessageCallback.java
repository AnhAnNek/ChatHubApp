package com.example.chat.message.callback;

import com.example.chat.message.MessageDTO;

/**
 * Interface callback để nhận kết quả trả về từ response của api gửi tin nhắn bên Backend.
 * Tác giả: Văn Hoàng
 */
public interface MessageCallback {
    /**
     * Phương thức được gọi khi lấy dữ liệu thành công.
     *
     * @param message Đối tượng MessageDTO chứa thông tin chi tiết của một tin nhắn.
     * Tác giả: Văn Hoàng
     */
    void onResponse(MessageDTO message);
}
