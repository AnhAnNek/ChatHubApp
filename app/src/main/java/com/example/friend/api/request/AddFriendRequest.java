package com.example.friend.api.request;

import java.util.Date;

public class AddFriendRequest {
    private String senderId;
    private String recipientId;
    private Date createdTime;

    public AddFriendRequest(String senderId, String recipientId, Date createdTime) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.createdTime = createdTime;
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

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
