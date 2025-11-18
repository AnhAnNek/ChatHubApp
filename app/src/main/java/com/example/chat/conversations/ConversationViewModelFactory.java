package com.example.chat.conversations;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.chat.conversations.repos.ConversationRepos;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.UserRepos;
/**
 * Factory class để tạo ra ConversationViewModel.

 * Tác giả: Nguyễn Hà Quỳnh Giao.
 */
public class ConversationViewModelFactory implements ViewModelProvider.Factory{
    protected final AuthRepos authRepos;
    protected final ConversationRepos conversationRepos;
    private final UserRepos userRepos;
    public ConversationViewModelFactory(AuthRepos authRepos,
                                        ConversationRepos conversationRepos,
                                        UserRepos userRepos) {
        this.authRepos = authRepos;
        this.conversationRepos = conversationRepos;
        this.userRepos = userRepos;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ConversationViewModel.class)) {
            return (T) new ConversationViewModel(authRepos, conversationRepos, userRepos);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
