package com.example.chat.message.callback;

import com.example.chat.message.MessageDTO;

import java.util.List;

/**
 * Interface callback để nhận kết quả lấy danh sách tin nhắn từ bên Backend.
 * Tác giả: Văn Hoàng
 */
public interface GetRemoteMessageCallBack {
    void onResponse(List<MessageDTO> messageDTOs);
}
