package com.example.chat.conversations;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.chat.ChatViewModel;
import com.example.chat.Utils;
import com.example.chat.conversations.adapter.ConversationListener;
import com.example.chat.conversations.repos.ConversationRepos;
import com.example.chat.message.Message;
import com.example.infrastructure.BaseViewModel;
import com.example.user.User;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.UserRepos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class ConversationViewModel extends BaseViewModel implements ConversationListener {

    private static final String TAG = ConversationViewModel.class.getSimpleName();

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isShowMessageError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> navigateToFriends = new MutableLiveData<>();
    private final MutableLiveData<Conversation> targetConversation = new MutableLiveData<>();
    private static final MutableLiveData<List<Conversation>> conversationsLiveData = new MutableLiveData<>(new ArrayList<>());
    private static List<Conversation> conversations = new ArrayList<>();
    private static AuthRepos authRepos;
    private static ConversationRepos conversationRepos;
    private static UserRepos userRepos;

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<Boolean> getIsShowMessageError() {
        return isShowMessageError;
    }

    public LiveData<Boolean> getNavigateToFriends() {
        return navigateToFriends;
    }

    public LiveData<Conversation> getTargetConversation() {
        return targetConversation;
    }


    public LiveData<List<Conversation>> getConversations() {
        return conversationsLiveData;
    }

    public ConversationViewModel(AuthRepos authRepos, ConversationRepos conversationRepos, UserRepos userRepos) {
        this.authRepos = authRepos;
        this.conversationRepos = conversationRepos;
        this.userRepos = userRepos;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadConversations();

    }

    public void navigateToFriends() {
        this.navigateToFriends.postValue(true);
    }

    /**
     * Lấy danh sách hội thoại và tải lên view.
     * <p>
     * Tác giả: Nguyễn Hà Quỳnh Giao - 21110171
     */

    public void loadConversations() {
        // Hiển thị Progress bar trong quá trình lấy dữ liệu từ API
        this.isLoading.postValue(true);
        // Lấy danh sách các cuộc hội thoại.
        CompletableFuture<List<Conversation>> futureConversations = conversationRepos.getConversations(authRepos.getCurrentUid());
        futureConversations.thenAccept(newConversations -> {
            // Xóa dữ liệu danh sách hội thoại củ và cập nhật mới
            this.conversations.clear();
            this.conversations.addAll(newConversations);
            // Lấy thông tin tham gia cuộc trò chuyện
            CompletableFuture<?>[] userFutures = new CompletableFuture<?>[conversations.size()];

            for (int i = 0; i < this.conversations.size(); i++) {
                Conversation conversation = this.conversations.get(i);
                // Lấy Id của người tham gia cuộc trò chuyện còn lại (không phải người dùng hiện tại
                String userId = authRepos.getCurrentUid().equals(conversation.getSenderId()) ? conversation.getRecipientId() : conversation.getSenderId();
                // Lấy dữ liệu thành công thì thiết lập ảnh hiển thị và tên cho cuộc trò chuyên
                CompletableFuture<Void> setUserFuture = userRepos.getUserByUid(userId).thenAccept(user -> {
                    conversation.setConversationName(user.getFullName());
                    conversation.setUri(user.getUri());
                    // Chuyển dữ liệu thời gian gửi từ String sang LocalDateTime
                    conversation.setSendingTimeFormatted(Utils.getLocalDateTime(conversation.getSendingTime()));
                }).exceptionally(e -> {
                    return null;
                });
                // Lấy dữ liệu người dùng vừa tìm được
                userFutures[i] = setUserFuture;
            }
            // Khi tất cả việc lấy dữ liệu hoàn thành thì cập nhật giao diện
            CompletableFuture<Void> allFutures = CompletableFuture.allOf(userFutures);

            allFutures.thenRun(() -> {
                // Tắt progress bar
                isLoading.postValue(false);
                if (this.conversations.isEmpty()) {
                    // Nếu không có dữ liệu cuộc trò chuyện nào thì hiển thị thông báo
                    isShowMessageError.postValue(true);
                } else {
                    // Nếu có dữ liệu thì tiến hành sắp xếp cuộc trò chuyện theo thời gian
                    this.conversations.sort(Comparator.comparing(Conversation::getSendingTimeFormatted).reversed());
                    conversationsLiveData.postValue(this.conversations);
                }
            });
        // Xử lý ngoại lệ
        }).exceptionally(throwable -> {
            isLoading.postValue(false);
            isShowMessageError.postValue(true);
            return null;
        });
    }

    /**
     * Quan sát sự kiện khi hình ảnh được nhấp để xem chi tiết.
     * Nếu một URL hình ảnh được trả về, một Intent được tạo để mở ImageActivity với URL hình ảnh được chuyển đi.
     * @param position Vị trí của mục trong danh sách đã được nhấp.
     * Tác giả: Văn Hoàng
     */
    @Override
    public void onItemClick(int position) {
        Conversation conversation = conversations.get(position);
        targetConversation.postValue(conversation);
    }


    /**
     * Cập nhật giao diện conversations
     *
     * @param message   :   Tin nhắn mới nhận
     *
     * Tác giả: Nguyễn Hà Quỳnh Giao
     */
    public static void updateConversations(Message message) {
        if(isExistConversation(message)){
            updateConversation(message);
        } else {
            addConversation(message);
        }
    }

    /**
     * Thêm mới conversation vào view
     *
     * @param message   :   Tin nhắn mới nhận.
     *
     * Tác giả: Nguyễn Hà Quỳnh Giao
     */
    public static void addConversation(Message message){
        Log.i(TAG, "Add view: new conversation");

        Conversation conversation = new Conversation(
                message.getSenderId(),
                message.getRecipientId(),
                message.getMessage(),
                message.getSendingTime());

        // Lấy thông tin người gửi để cập nhật vào giao diện
        CompletableFuture<User> userFuture = userRepos.getUserByUid(conversation.getSenderId());

        // Nếu lấy dữ liệu thành công
        userFuture.thenAccept(sender -> {
            if (sender != null) {
                conversation.setUri(sender.getUri());
                conversation.setConversationName(sender.getFullName());
                conversation.setSendingTimeFormatted(Utils.getLocalDateTime(conversation.getSendingTime()));
            } else {
                Log.e(TAG, "User not found");
            }
        }).exceptionally(ex -> {
            Log.e(TAG, "Failed to get user", ex);
            return null;
        }).thenRun(() -> {
            conversations.add(conversation);
            // Sắp xếp lại danh sách và thông báo cập nhật giao diện
            conversations.sort(Comparator.comparing(Conversation::getSendingTimeFormatted).reversed());
            conversationsLiveData.postValue(conversations);
        }).exceptionally(ex -> {
            Log.e(TAG, "Failed to add conversation", ex);
            return null;
        });
    }

    /**
     * Cập nhật conversation vào view
     *
     * @param message   :   Tin nhắn mới nhận được.
     *
     * Tác giả: Nguyễn Hà Quỳnh Giao
     */
    public static void updateConversation(Message message){
        Log.i(TAG, "Update view: new conversation");

        // Cập nhật thông tin cuộc trò chuyện đã tồn tại với tin nhắn mới nhất
        for(int i = 0; i < conversations.size(); i++){
            String senderId = message.getSenderId();
            String recipientId = message.getRecipientId();

            if((conversations.get(i).getSenderId().equals(senderId) && conversations.get(i).getRecipientId().equals(recipientId))
                    || (conversations.get(i).getRecipientId().equals(senderId) && conversations.get(i).getSenderId().equals(recipientId))){
                conversations.get(i).setLastMessage(message.getMessage());
                conversations.get(i).setSendingTime(message.getSendingTime());

                conversations.get(i).setSendingTimeFormatted(Utils.getLocalDateTime(message.getSendingTime()));
            }
        }

        // Sắp xếp lại danh sách và thông báo cập nhật giao diện
        conversations.sort(Comparator.comparing(Conversation::getSendingTimeFormatted).reversed());
        conversationsLiveData.postValue(conversations);
    }

    /**
     * Kiểm tra xem conversation đã tồn tại trong danh sách trước đó
     *
     * @param message   :   Tin nhắn mới gửi.
     * @return          :   Một boolean kết quả. 0 - không tồn tại, 1 - tồn tại.
     *
     * Tác giả: Nguyễn Hà Quỳnh Giao
     */
    private static boolean isExistConversation(Message message){
        if (message == null) {
            return false;
        }

        String senderId = message.getSenderId();
        String recipientId = message.getRecipientId();

        for (Conversation conversation : conversations) {
            if ((Objects.equals(conversation.getSenderId(), senderId) && Objects.equals(conversation.getRecipientId(), recipientId)) ||
                    (Objects.equals(conversation.getRecipientId(), senderId) && Objects.equals(conversation.getSenderId(), recipientId))) {
                return true;
            }
        }

        return false;
    }
}
