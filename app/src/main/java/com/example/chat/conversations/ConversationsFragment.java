package com.example.chat.conversations;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.chat.ChatActivity;
import com.example.chat.Utils;
import com.example.chat.conversations.adapter.ConversationsAdapter;
import com.example.chat.conversations.repos.ConversationRepos;
import com.example.chat.conversations.repos.ConversationReposImpl;
import com.example.chat.message.repos.MessageRepos;
import com.example.chat.message.repos.MessageReposImpl;
import com.example.common.navigation.EAnimationType;
import com.example.common.repository.MediaRepos;
import com.example.common.repository.MediaReposImpl;
import com.example.databinding.FragmentConversationsBinding;
import com.example.infrastructure.BaseFragment;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.AuthReposImpl;
import com.example.user.repository.UserRepos;
import com.example.user.repository.UserReposImpl;

import java.util.ArrayList;
public class ConversationsFragment extends BaseFragment<ConversationViewModel, FragmentConversationsBinding> {

    private static final String TAG = ConversationsFragment.class.getSimpleName();
    private ConversationsAdapter conversationsAdapter;
    private AuthRepos authRepos;
    private MessageRepos messageRepos;

    public ConversationsFragment() {
    }

    @Override
    protected FragmentConversationsBinding getViewDataBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentConversationsBinding.inflate(inflater, container, false);
    }

    @Override
    protected Class<ConversationViewModel> getViewModelClass() {
        return ConversationViewModel.class;
    }

    @Override
    protected ViewModelProvider.Factory getViewModelFactory() {
        MediaRepos mediaRepos = new MediaReposImpl();
        UserRepos userRepos = new UserReposImpl(mediaRepos);
        this.authRepos = new AuthReposImpl(userRepos);
        messageRepos = new MessageReposImpl();
        ConversationRepos conversationRepos = new ConversationReposImpl();
        return new ConversationViewModelFactory(authRepos, conversationRepos, userRepos);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getToken();
        setupConversationsAdapter();
        setupObservers();
    }

    private void setupConversationsAdapter() {
        conversationsAdapter = new ConversationsAdapter(new ArrayList<>(), viewModel);
        binding.setConversationsAdapter(conversationsAdapter);
    }

    /**
     * Thiết lập observer để theo dõi các thay đổi từ ViewModel và cập nhật lên giao diện người dùng
     * <p>
     * - Theo dõi và chuyển đến trang danh sách bạn bè.
     * - Theo dõi và cập nhật giao diện danh sách hội thoại.
     * - Theo dõi và xử lý dữ liệu, sau đó chuyển đến khung trò chuyện.
     *</p>
     *
     * Tác giả: Nguyễn Hà Quỳnh Giao - 21110171
     */

    private void setupObservers() {
        viewModel.getNavigateToFriends().observe(getViewLifecycleOwner(), navigate -> {
            if (navigate != null && navigate) {
                //Điều hướng đến trang danh sách bạn bè
                navigationManager.navigateToFriends(EAnimationType.FADE_IN);
            }
        });

        viewModel.getConversations().observe(getViewLifecycleOwner(), conversations -> {
            if (conversations != null) {
                //Cập nhật adapter với danh sách hội thoại mới
                Log.d(TAG, "Load Conversations View");
                conversationsAdapter.setConversations(conversations);
            }
        });

        viewModel.getTargetConversation().observe(getViewLifecycleOwner(), conversation -> {
            if (conversation != null) {
                //Tạo Intent để chuyển đến khung trò chuyện
                Intent intent = new Intent(getContext(), ChatActivity.class);
                //Người gửi là người dùng hiện tại
                String senderId = authRepos.getCurrentUid();
                String recipientId;
                //Người nhận là người còn lại
                if (senderId.equals(conversation.getSenderId()))
                    recipientId = conversation.getRecipientId();
                else recipientId = conversation.getSenderId();
                //Truyền thông tin người gửi và người nhận
                intent.putExtra(Utils.KEY_SENDER_ID, senderId);
                intent.putExtra(Utils.KEY_RECIPIENT_ID, recipientId);
                //Đặt cờ ưu tiên cho ChatActivity lên đầu stack
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                //Chuyển đến khung trò chuyển
                startActivity(intent);
            }
        });
    }

    private void getToken(){
        String uid = authRepos.getCurrentUid();
        messageRepos.updateToken(uid);
    }
}