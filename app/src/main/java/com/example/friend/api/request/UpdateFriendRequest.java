package com.example.friend.api.request;

public class UpdateFriendRequest {
    private String friendRequestId;
    private String status;

    public UpdateFriendRequest() {
    }

    public UpdateFriendRequest(String friendRequestId, String status) {
        this.friendRequestId = friendRequestId;
        this.status = status;
    }

    public String getFriendRequestId() {
        return friendRequestId;
    }

    public void setFriendRequestId(String friendRequestId) {
        this.friendRequestId = friendRequestId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
