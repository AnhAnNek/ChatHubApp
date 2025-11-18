package com.example.chat.message;

import org.jetbrains.annotations.NotNull;

/**
 * Đối tượng dữ liệu DTO để truyền thông tin tin nhắn giữa Backend và Frontend App.
 *
 * Tác giả: Văn Hoàng
 */
public class MessageDTO {
    private String message;

    private String sendingTime;

    private Message.EType type;

    private Message.EVisible visibility;

    @NotNull
    private String senderId;

    @NotNull
    private String recipientId;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSendingTime() {
        return sendingTime;
    }

    public void setSendingTime(String sendingTime) {
        this.sendingTime = sendingTime;
    }

    public Message.EType getType() {
        return type;
    }

    public void setType(Message.EType type) {
        this.type = type;
    }

    public Message.EVisible getVisibility() {
        return visibility;
    }

    public void setVisibility(Message.EVisible visibility) {
        this.visibility = visibility;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }
}
