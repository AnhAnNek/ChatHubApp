package com.example.chat;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.chat.conversations.Conversation;
import com.example.chat.conversations.repos.ConversationRepos;
import com.example.chat.message.repos.MessageRepos;
import com.example.chat.message.repos.PhotoRepos;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.UserRepos;

/**
 * Factory class để tạo ra ChatViewModel trong ứng dụng Chat.

 * Tác giả: Văn Hoàng
 */
public class ChatViewModelFactory implements ViewModelProvider.Factory{
    protected final AuthRepos authRepos;
    protected final UserRepos userRepos;
    protected final MessageRepos messageRepos;
    protected final ConversationRepos conversationRepos;

    protected final Conversation conversation;
    protected final PhotoRepos photoRepos;
    public ChatViewModelFactory(AuthRepos authRepos,
                                UserRepos userRepos,
                                MessageRepos messageRepos,
                                ConversationRepos conversationRepos,
                                Conversation conversation,
                                PhotoRepos photoRepos) {
        this.authRepos = authRepos;
        this.userRepos = userRepos;
        this.messageRepos = messageRepos;
        this.conversationRepos = conversationRepos;
        this.conversation = conversation;
        this.photoRepos = photoRepos;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ChatViewModel.class)) {
            return (T) new ChatViewModel(userRepos, authRepos, conversationRepos, messageRepos, conversation, photoRepos);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }

}
