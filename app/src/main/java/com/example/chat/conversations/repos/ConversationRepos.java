package com.example.chat.conversations.repos;


import com.example.chat.conversations.Conversation;
import com.example.chat.message.Message;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Interface định nghĩa các phuơng thức tương tác với dữ liệu thông tin cuộc hội thoại.
 *
 * Tác giả: Nguyễn Hà Quỳnh Giao
 */

public interface ConversationRepos {
    /**
     * Thêm cuộc trò chuyện mới.
     *
     * @param message   : Tin nhắn mới được gửi.
     * @return          : Một CompletableFuture chứa cuộc hội thoại.
     *
     * Tác giả: Nguyễn Hà Quỳnh Giao.
     */
    CompletableFuture<Conversation> add(Message message);
    /**
     * Cập nhật cuộc trò chuyện.
     *
     * @param message   : Tin nhắn mới được gửi.
     * @return          : Một CompletableFuture chứa cuộc hội thoại.
     *
     * Tác giả: Nguyễn Hà Quỳnh Giao.
     */
    CompletableFuture<Conversation> update(Message message);
    /**
     * Tìm cuộc hội thoại có sender và recipient
     *
     * @param senderId      : Id người gửi
     * @param recipientId   : Id người nhận
     * @return              :Một CompletableFuture chứa cuộc hội thoại.
     *
     * Tác giả: Nguyễn Hà Quỳnh Giao.
     */
    CompletableFuture<Conversation> getConversationBySenderAndRecipient(String senderId, String recipientId);

    /**
     * Lấy danh sách cuộc hội thoại của userId
     *
     * @param userId    : mã người dùng muốn lấy danh sách cuộc hội thoại
     * @return          : một CompletableFuture chứa danh sách cuộc hội thoại.
     *
     * Tác giả: Nguyễn Hà Quỳnh Giao.
     */
    CompletableFuture<List<Conversation>> getConversations(String userId);
}
