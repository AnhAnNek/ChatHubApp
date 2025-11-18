package com.example.common.navigation;

import android.os.Bundle;

public interface NavigationManager {
    int DEFAULT_FLAGS = 0;

    void navigateToHome(EAnimationType animationType);

    void navigateToUserProfile(EAnimationType animationType);

    void navigateToLogin(EAnimationType animationType);

    void navigateToForgotPassword(EAnimationType animationType);

    void navigateToSignUp(EAnimationType animationType);

    void navigateToGoogleSignIn(EAnimationType animationType);

    void navigateToAccountLinking(EAnimationType animationType);

    void navigateToPhoneNumberInput(EAnimationType animationType);

    void navigateToVerifyOtp(Bundle data, EAnimationType animationType);

    void navigateToProfileViewer(Bundle data, EAnimationType animationType);

    void navigateBack(Bundle data, EAnimationType animationType);

    void navigateToFriends(EAnimationType animationType);

    void navigateToFriendSuggestions(EAnimationType animationType);

    void getNavigateToSentRequests(EAnimationType animationType);

    void navigateToChat(Bundle data, EAnimationType animationType);
}
