package com.example.chat;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.chat.conversations.Conversation;
import com.example.chat.conversations.ConversationViewModel;
import com.example.chat.conversations.repos.ConversationRepos;
import com.example.chat.conversations.repos.ConversationReposImpl;
import com.example.chat.image.ImageActivity;
import com.example.chat.message.repos.MessageRepos;
import com.example.chat.message.repos.MessageReposImpl;
import com.example.chat.message.repos.PhotoRepos;
import com.example.chat.message.repos.PhotoReposImpl;
import com.example.chat.notify.FCMSend;
import com.example.common.repository.MediaRepos;
import com.example.common.repository.MediaReposImpl;
import com.example.databinding.ActivityChatBinding;
import com.example.home.HomeActivity;
import com.example.infrastructure.BaseActivity;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.AuthReposImpl;
import com.example.user.repository.UserRepos;
import com.example.user.repository.UserReposImpl;

import java.util.ArrayList;
import java.util.Objects;


public class ChatActivity extends BaseActivity<ChatViewModel,ActivityChatBinding> {
    private static final String TAG = ChatActivity.class.getSimpleName();

    private ChatAdapter chatAdapter;
    private String senderId;
    private Uri recipientImageUrl;
    ActivityResultLauncher<PickVisualMediaRequest> pickMultipleMedia;
    ActivityResultLauncher<PickVisualMediaRequest> pickSingleMedia;

    @Override
    protected int getLayout() {
        return R.layout.activity_chat;
    }

    @Override
    protected Class<ChatViewModel> getViewModelClass() {
        return ChatViewModel.class;
    }

    @Override
    protected ViewModelProvider.Factory getViewModelFactory() {
        Conversation conversation = new Conversation();

        MediaRepos mediaRepos = new MediaReposImpl();
        UserRepos userRepos = new UserReposImpl(mediaRepos);
        AuthRepos authRepos = new AuthReposImpl(userRepos);
        MessageRepos messageRepos = new MessageReposImpl();
        PhotoRepos photoRepos = new PhotoReposImpl();
        ConversationRepos conversationRepos = new ConversationReposImpl();

        String senderId = (String) getIntent().getStringExtra(Utils.KEY_SENDER_ID);
        String recipientId = (String) getIntent().getStringExtra(Utils.KEY_RECIPIENT_ID);

        userRepos.getUserByUid(recipientId).thenAccept(user -> {
            conversation.setConversationName(user.getFullName());
            conversation.setUri(user.getUri());
        });
        conversation.setSenderId(senderId);
        conversation.setRecipientId(recipientId);

        this.senderId = conversation.getSenderId();
        this.recipientImageUrl = conversation.getUri();

        return new ChatViewModelFactory(authRepos, userRepos, messageRepos, conversationRepos, conversation, photoRepos);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        chatAdapter = new ChatAdapter(
                new ArrayList<>(),
                senderId,
                recipientImageUrl,
                viewModel
        );
        binding.chatRecyclerView.setAdapter(chatAdapter);

        setToasts();
        setObservers();
        setRegisterForActivityResult();
        binding.setLifecycleOwner(this);
    }

    /**

     Thiết lập các thông báo Toast từ LiveData errorToastMessage của ViewModel.
     Khi errorToastMessage thay đổi, hiển thị Toast tương ứng.
     Tác giả: Văn Hoàng
     */
    private void setToasts() {
        viewModel.getErrorToastMessage().observe(this, text -> {
            showToast(text);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        extractIntentData();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void extractIntentData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String selectedUserId = extras.getString(com.example.infrastructure.Utils.EXTRA_SELECTED_USER_ID, "");
            String selectedFriendRequestId = extras.getString(com.example.infrastructure.Utils.EXTRA_SELECTED_FRIEND_REQUEST_ID, "");
        }
    }

    /**

     Hiển thị một thông báo Toast với nội dung được chỉ định.
     @param text Nội dung của Toast.
     Tác giả: Văn Hoàng
     */
    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void setObservers() {
        /**

         Quan sát danh sách tin nhắn từ ViewModel và cập nhật giao diện người dùng tương ứng.
         Nếu danh sách tin nhắn rỗng, thông báo cho Adapter cần cập nhật toàn bộ danh sách.
         Ngược lại, cập nhật danh sách tin nhắn mới và cuộn xuống vị trí tin nhắn cuối cùng.
         Hiển thị RecyclerView và ẩn ProgressBar sau khi đã cập nhật giao diện.
         @param messages Danh sách tin nhắn mới nhận được từ ViewModel.
         Tác giả: Văn Hoàng
         */
        viewModel.getMessages().observe(this, messages -> {
            int count = messages.size();
            if (count == 0) {
                chatAdapter.notifyDataSetChanged();
            } else {
                chatAdapter.setMessages(messages);
                chatAdapter.notifyItemRangeInserted(count, count);
                binding.chatRecyclerView.smoothScrollToPosition(count - 1);
            }
            binding.chatRecyclerView.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.GONE);
        });

        /**
         Quan sát nội dung MessageInput từ ChatViewModel và cập nhật giao diện người dùng tương ứng.
         Hiển thị hoặc ẩn các phần tử giao diện tùy thuộc vào nội dung của tin nhắn.
         Nếu tin nhắn trống, các phần tử giao diện "chooseImage", "recognizeEye" sẽ được hiển thị,
         còn "iconSendMessage" sẽ bị ẩn. Ngược lại, nếu tin nhắn không trống,
         "chooseImage" và "recognizeEye" sẽ bị ẩn và "iconSendMessage" sẽ được hiển thị.
         @param message Nội dung tin nhắn đầu vào từ ViewModel.
         Tác giả: Văn Hoàng
         */
        viewModel.getMessageInput().observe(this, message -> {
            binding.chooseImage.setVisibility(message.isEmpty() ? View.VISIBLE : View.GONE);
            binding.recognizeEye.setVisibility(message.isEmpty() ? View.VISIBLE : View.GONE);
            binding.iconSendMessage.setVisibility(message.isEmpty() ? View.GONE : View.VISIBLE);
        });

        /**

         Quan sát sự kiện IsOpenImageDialog trong ChatViewModel.
         Nếu IsOpenImageDialog thay đổi giá trị, một yêu cầu được gửi đến Activity Result để chọn hình ảnh hoặc video.
         @param isOpenImageDialog Trạng thái mở hộp thoại chọn hình ảnh.
         Tác giả: Văn Hoàng
         */
        viewModel.getIsOpenImageDialog().observe(this, isOpenImageDialog -> {
            if(isOpenImageDialog) {
                pickMultipleMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageAndVideo.INSTANCE)
                        .build());
            }
        });

        /**

         Quan sát sự kiện điều hướng quay lại HomeActivity (NavigateBack) trong ChatViewModel.
         Nếu sự kiện được kích hoạt, một Intent được tạo để chuyển đến HomeActivity.
         @param isNavigateBack Trạng thái điều hướng quay lại.
         Tác giả: Văn Hoàng
         */
        viewModel.getNavigateBack().observe(this, isNavigateBack ->
                startActivity(new Intent(getApplicationContext(), HomeActivity.class)));

        /**

         Quan sát sự kiện khi hình ảnh được nhấp để xem chi tiết.
         Nếu một URL hình ảnh được trả về, một Intent được tạo để mở ImageActivity với URL hình ảnh được chuyển đi.
         @param imageClickedUrl URL của hình ảnh được nhấp.
         Tác giả: Văn Hoàng
         */
        viewModel.getIsImageClicked().observe(this, imageClickedUrl -> {
            if(!imageClickedUrl.isEmpty()) {
                Intent intent = new Intent(getApplicationContext(), ImageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra(Utils.KEY_IMAGE_CLICKED_URL, imageClickedUrl);
                startActivity(intent);
            }
        });


        /**
         Quan sát sự kiện khi nút recognizeEye được nhấp.
         Nếu được mở, một Intent được khởi động để chọn một hình ảnh duy nhất từ thư viện ảnh trong máy, chỉ hỗ trợ hình ảnh.
         @param isOpenImageDialog Cờ chỉ ra xem hộp thoại chọn hình ảnh có được mở hay không.
         Tác giả: Văn Hoàng
         */
        viewModel.getIsRecognizeEyeClicked().observe(this, isOpenImageDialog -> {
            if(isOpenImageDialog) {
                pickSingleMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
            }
        });

        // Nhận thông báo gửi tin nhắn
        viewModel.getNotification().observe(this, message -> {
            Log.i(TAG, "FCMSend");
            Log.i(TAG, message.toString());
            // Gửi thông báo đến thiết bị người nhận bằng cách sử dụng phương thức pushNotification trong lớp FCMSend
            FCMSend.pushNotification(
                    this,
                    message.get(Utils.KEY_FCM_TOKEN),
                    message.get(Utils.KEY_SENDER_NAME),
                    message.get(Utils.KEY_MESSAGE));
        });
    }

    private void setRegisterForActivityResult() {
        try {
            /**
             * Đăng ký một ActivityResult để xử lý việc chọn nhiều ảnh để gửi tin nhắn.
             * Số lượng phương tiện tối đa có thể được chọn là 5.
             *
             * @param uris Danh sách các URI của các phương tiện được chọn.
             * Nếu không có phương tiện nào được chọn, ghi nhật ký "No media selected".
             * Tác giả: Văn Hoàng
             */
            pickMultipleMedia = registerForActivityResult(new ActivityResultContracts.PickMultipleVisualMedia(5), uris -> {
                if (!uris.isEmpty()) {
                    for(Uri uri : uris) {
                        handleSelectedMedia(uri);
                    }
                    Log.d(TAG, "Number of items selected: " + uris.size());
                } else {
                    Log.d(TAG, "No media selected");
                }
            });

            /**
             * Đăng ký một ActivityResult để xử lý việc chọn một ảnh.
             * Sau khi chọn ảnh, tiến hành nhận diện văn bản trong ảnh và cập nhật văn bản vừa nhận diện vào MessageInput
             * Nếu không có phương tiện nào được chọn, ghi nhật ký "No image selected".
             *
             * @param uri URI của phương tiện được chọn.
             * Tác giả: Văn Hoàng
             */
            pickSingleMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    viewModel.recognizeTextFromImage(this, uri);
                } else {
                    Log.d(TAG, "No image selected");
                }
            });
        } catch (Exception e){
            Log.e(TAG, Objects.requireNonNull(e.getMessage()));
        }
    }

    /**
     * Xử lý Media được chọn bằng cách kiểm tra xem có phải là hình ảnh không.
     * Nếu phương tiện là hình ảnh, gọi phương thức để gửi hình ảnh đi.
     * Nếu không phải là hình ảnh, ghi Log "Media types are not supported".
     *
     * @param uri URI của phương tiện được chọn.
     * Tác giả: Văn Hoàng
     */
    private void handleSelectedMedia(Uri uri) {
        if (isImage(uri)) {
            viewModel.sendImage(uri);
        } else {
            showToast("Media types are not supported");
            Log.d(TAG, "Media types are not supported");
        }
    }

    /**
     * Kiểm tra xem URI có phải là hình ảnh không bằng cách kiểm tra kiểu MIME.
     *
     * @param uri URI của phương tiện.
     * @return true nếu là hình ảnh, ngược lại trả về false.
     * Tác giả: Văn Hoàng
     */
    private boolean isImage(Uri uri) {
        String mimeType = getContentResolver().getType(uri);
        return mimeType != null && mimeType.startsWith("image/");
    }

}