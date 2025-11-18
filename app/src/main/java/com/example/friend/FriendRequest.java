package com.example.friend;

import com.example.user.User;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class FriendRequest {
    private String id;
    private User sender;
    private User recipient;
    private EStatus status;
    private Date createdTime;
    private int mutualFriends;

    public FriendRequest() {
    }

    public FriendRequest(String id, User sender, User recipient,
                         EStatus status, Date createdTime, int mutualFriends) {
        this.id = id;
        this.sender = sender;
        this.recipient = recipient;
        this.status = status;
        this.createdTime = createdTime;
        this.mutualFriends = mutualFriends;
    }

    public FriendRequest(User sender, User recipient, EStatus status, Date createdTime) {
        this.sender = sender;
        this.recipient = recipient;
        this.status = status;
        this.createdTime = createdTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public EStatus getStatus() {
        return status;
    }

    public void setStatus(EStatus status) {
        this.status = status;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public int getMutualFriends() {
        return mutualFriends;
    }

    public void setMutualFriends(int mutualFriends) {
        this.mutualFriends = mutualFriends;
    }

    public enum EStatus {
        @SerializedName("ACCEPTED")
        ACCEPTED,
        @SerializedName("REJECTED")
        REJECTED,
        @SerializedName("PENDING")
        PENDING,
        @SerializedName("NOT_FOUND")
        NOT_FOUND
    }
}
