package com.example.friend;

public class FriendRequestView {
    private FriendRequest friendRequest;
    private String displayedUserId;
    private String displayName;
    private boolean isLoading;

    public FriendRequestView() {
    }

    public FriendRequest getFriendRequest() {
        return friendRequest;
    }

    public void setFriendRequest(FriendRequest friendRequest) {
        this.friendRequest = friendRequest;
    }

    public String getDisplayedUserId() {
        return displayedUserId;
    }

    public void setDisplayedUserId(String displayedUserId) {
        this.displayedUserId = displayedUserId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }
}
