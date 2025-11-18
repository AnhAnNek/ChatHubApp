package com.example.chat.conversations;

import android.net.Uri;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Conversation implements Serializable {
    private String id;
    private String senderId;
    private String recipientId;
    private String sendingTime;
    private String conversationName;
    private Uri uri;
    private  String lastMessage;
    private LocalDateTime sendingTimeFormatted;

    public Conversation(){};

    public Conversation(String senderId, String receiptId,
                        String lastMessage, String sendingTime) {
        this.senderId = senderId;
        this.recipientId = receiptId;
        this.lastMessage = lastMessage;
        this.sendingTime = sendingTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public void setRecipientId(String receiptId) {
        this.recipientId = receiptId;
    }

    public String getSendingTime() {
        return sendingTime;
    }

    public void setSendingTime(String sendingTime) {
        this.sendingTime = sendingTime;
    }

    public String getConversationName() {
        return conversationName;
    }

    public void setConversationName(String conversationName) {
        this.conversationName = conversationName;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public LocalDateTime getSendingTimeFormatted() {
        return sendingTimeFormatted;
    }

    public void setSendingTimeFormatted(LocalDateTime sendingTimeFormatted) {
        this.sendingTimeFormatted = sendingTimeFormatted;
    }
}
