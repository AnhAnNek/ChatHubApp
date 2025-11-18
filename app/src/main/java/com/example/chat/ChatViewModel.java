package com.example.chat;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.chat.conversations.Conversation;
import com.example.chat.conversations.repos.ConversationRepos;
import com.example.chat.listener.ImageListener;
import com.example.chat.message.Message;
import com.example.chat.message.MessageDTO;
import com.example.chat.message.repos.MessageRepos;
import com.example.chat.message.repos.PhotoRepos;
import com.example.chat.textanalyzer.RecognizeUtils;
import com.example.chat.textanalyzer.TextAnalyzer;
import com.example.common.customcontrol.customalertdialog.AlertDialogModel;
import com.example.infrastructure.BaseViewModel;
import com.example.user.User;
import com.example.user.repository.AuthRepos;
import com.example.user.repository.UserRepos;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class ChatViewModel extends BaseViewModel implements ImageListener {

    private static final String TAG = ChatViewModel.class.getSimpleName();

    private final MutableLiveData<String> curSenderUid = new MutableLiveData<>("");
    private final MutableLiveData<String> curUsername = new MutableLiveData<>("");
    private final MutableLiveData<String> curRecipientId = new MutableLiveData<>(null);
    private final MutableLiveData<String> curRecipientToken = new MutableLiveData<>(null);
    private final MutableLiveData<String> curRecipientName = new MutableLiveData<>("");
    private final MutableLiveData<Uri> curRecipientImage = new MutableLiveData<>();
    private final MutableLiveData<String> conversationId = new MutableLiveData<>(null);
    private static final MutableLiveData<List<Message>> messages = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<String> messageInput = new MutableLiveData<>("");
    private final MutableLiveData<Boolean> isReceiverAvailable = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> isOpenImageDialog = new MutableLiveData<>(false);
    private final MutableLiveData<String> isImageClicked = new MutableLiveData<>("");
    private final MutableLiveData<Boolean> isRecognizeEyeClicked = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> navigateBack = new MutableLiveData<>();
    private final MutableLiveData<HashMap<String, String>> sendNotification = new MutableLiveData<>();
    private final MutableLiveData<AlertDialogModel> openCustomAlertDialog = new MutableLiveData<>();

    private final UserRepos userRepos;
    private final MessageRepos messageRepos;
    private final AuthRepos authRepos;
    private final ConversationRepos conversationRepos;
    private final PhotoRepos photoRepos;

    public LiveData<AlertDialogModel> getOpenCustomAlertDialog() {
        return openCustomAlertDialog;
    }

    public LiveData<Boolean> getNavigateBack() {
        return navigateBack;
    }

    public LiveData<Boolean> getIsOpenImageDialog() {
        return isOpenImageDialog;
    }

    public LiveData<Boolean> getIsRecognizeEyeClicked() {
        return isRecognizeEyeClicked;
    }

    public LiveData<String> getCurUsername() {
        return curUsername;
    }

    public LiveData<String> getCurSenderUid() {
        return curSenderUid;
    }

    public LiveData<String> getCurRecipientToken() {
        return curRecipientToken;
    }

    public LiveData<String> getCurRecipientId() {
        return curRecipientId;
    }

    public LiveData<String> getCurRecipientName() {
        return curRecipientName;
    }

    public LiveData<Uri> getCurRecipientImage() {
        return curRecipientImage;
    }

    public LiveData<String> getConversationId() {
        return conversationId;
    }

    public MutableLiveData<String> getMessageInput() {
        return messageInput;
    }

    public LiveData<List<Message>> getMessages() {
        return messages;
    }

    public LiveData<String> getIsImageClicked() {
        return isImageClicked;
    }

    public LiveData<Boolean> getIsReceiverAvailable() {
        return isReceiverAvailable;
    }

    public LiveData<HashMap<String, String>> getNotification() {
        return sendNotification;
    }

    public ChatViewModel(UserRepos userRepos, AuthRepos authRepos, ConversationRepos conversationRepos, MessageRepos messageRepos, Conversation conversation, PhotoRepos photoRepos) {
        this.userRepos = userRepos;
        this.authRepos = authRepos;
        this.messageRepos = messageRepos;
        this.conversationRepos = conversationRepos;
        this.photoRepos = photoRepos;

        conversationRepos.getConversationBySenderAndRecipient(conversation.getSenderId(), conversation.getRecipientId()).thenAccept(oldConversation -> conversationId.setValue(oldConversation.getId())).exceptionally(e -> {
            conversationId.setValue(null);
            return null;
        });

        userRepos.getUserByUid(conversation.getRecipientId()).thenAccept(recipient -> {
            curRecipientName.setValue(recipient.getFullName());
            curRecipientImage.setValue(recipient.getUri());
        });

        curRecipientId.setValue(conversation.getRecipientId());
        curSenderUid.setValue(conversation.getSenderId());
        getRecipientToken(curRecipientId.getValue());
        updateToken(curSenderUid.getValue());

        getInformation();
    }

    @Override
    public void onStart() {
        try {
            getRemoteMessages();
        } catch (Exception e) {
            Log.e(TAG, Objects.requireNonNull(e.getMessage()));
        }
    }

    /**
     * Thêm một tin nhắn mới vào danh sách các tin nhắn hiện tại và cập nhật LiveData để cập nhật RecycleView.
     *
     * @param message Tin nhắn mới cần thêm vào danh sách.
     *
     * Tác giả: Văn Hoàng
     */
    public static void addMessage(Message message) {
        List<Message> curMessages = messages.getValue();
        curMessages.add(message);
        messages.postValue(curMessages);
    }

    /**
     * Lấy token của người nhận dựa trên UID và cập nhật giá trị vào biến curRecipientToken.
     *
     * @param uid UID của người nhận để lấy token.
     * Tác giả: Văn Hoàng
     */
    private void getRecipientToken(String uid) {
        messageRepos.getToken(uid, curRecipientToken::setValue);
    }

    /**
     * Cập nhật token của người dùng dựa trên UID.
     *
     * @param uid UID của người dùng để cập nhật token.
     * Tác giả: Văn Hoàng
     */
    private void updateToken(String uid) {
        messageRepos.updateToken(uid);
    }

    /**
     * Lấy thông tin người dùng hiện tại và cập nhật các biến LiveData liên quan(curUsername, curSenderUid) và cập nhật token.
     * Tác giả: Văn Hoàng
     */
    public void getInformation() {
        this.authRepos.getCurrentUser().thenAccept(user -> {
            if (user != null) {
                curUsername.setValue(user.getFullName());
                curSenderUid.setValue(user.getId());
                updateToken(user.getId());
            } else {
                Log.d(TAG, "Unauthorized: ");
            }
        }).exceptionally(e -> {
            Log.e(TAG, "Error: " + e);
            return null;
        });
    }

    /**
     * Lấy danh sách tin nhắn MessageDTO từ bên Backend, chuyển lại thành Message và cập nhật vào biến LiveData `messages`.
     * Tác giả: Văn Hoàng
     */
    public void getRemoteMessages() {
        messageRepos.getRemoteMessages(curSenderUid.getValue(), messageDTOs -> {
            List<Message> receivedMessages = new ArrayList<>();
            Message ms;
            for (MessageDTO messageDTO : messageDTOs) {
                ms = new Message();
                ms.convertDTOToEntity(messageDTO);
                receivedMessages.add(ms);
            }

            messages.postValue(receivedMessages);
        });

    }

    private void openConversationNotFoundDialog() {
        AlertDialogModel model = new AlertDialogModel.Builder().setTitle("Conversation Not Found").setMessage("The conversation you are trying to access was not found! Click OK to quit!").setPositiveButton("Ok", aVoid -> navigateBack()).build();
        openCustomAlertDialog.postValue(model);
    }

    /**
     * Phương thức điều hướng quay lại màn hình chính(Home Activity).
     *
     * Đặt giá trị của biến LiveData navigateBack thành true để kích hoạt sự kiện điều hướng quay lại.
     * Tác giả: Văn Hoàng
     */
    public void navigateBack() {
        this.navigateBack.postValue(true);
    }

    public void onSendBtnClick() {
        /**
         * Khi nút gửi tin nhắn được nhấn, sẽ gửi request để thêm tin nhắn tới server và nhận response từ server để cập nhật tin nhắn lên View.
         *
         * Lấy văn bản tin nhắn từ `messageInput` gán cho messageText, gọi hàm sendMedia truyền vào `messageText` với loại văn bản là TEXT,
         * đặt nội dung tin nhắn, đặt lại giá trị `messageInput` thành chuỗi rỗng.
         *
         * Tác giả: Văn Hoàng
         */
        String messageText = messageInput.getValue();
        Message newMessage = sendMedia(messageText, Message.EType.TEXT);
        newMessage.setMessage(messageText);

        messageInput.postValue("");

        //
        updateConversation(newMessage);
    }

    /**
     * Phương thức tạo và gửi tin nhắn phương tiện (media message) với nội dung và loại tin nhắn được chỉ định gồm IMAGE và TEXT.
     *
     * @param textMedia Nội dung của tin nhắn.
     * @param type Loại của tin nhắn (TEXT hoặc IMAGE).
     * @return Đối tượng Message đại diện cho tin nhắn đã gửi.
     *
     * Tác giả: Văn Hoàng
     */
    public Message sendMedia(String textMedia, Message.EType type) {
        // Tạo một đối tượng MessageDTO với nội dung tin nhắn và loại tin nhắn được chỉ định
        MessageDTO message = createMessageWithMessageAndType(textMedia, type);
        Message ms = new Message();

        // Gửi tin nhắn.
        messageRepos.sendMessage(message, messageDTO -> {
            if(messageDTO != null) {
                // Chuyển đổi đối tượng MessageDTO thành đối tượng Message
                ms.convertDTOToEntity(messageDTO);
                // Thêm tin nhắn vào danh sách tin nhắn hiện tại
                addMessage(ms);
                // Gửi tin nhắn thông qua FCM (Firebase Cloud Messaging) đến người nhận
                messageRepos.sendFCMMessage(Utils.getRemoteMsgHeaders(), ms, curRecipientToken.getValue());
            } else {
                errorToastMessage.postValue(Utils.KEY_FAILED_SEND_MESSAGE);
            }
        });

        return ms;
    }

    /**
     * Phương thức gửi hình ảnh bằng cách tải lên hình ảnh và tạo một tin nhắn phương tiện với URL của hình ảnh đã tải lên.
     *
     * @param uriImage Địa chỉ URI của hình ảnh cần gửi.
     *
     * Tác giả: Văn Hoàng
     */
    public void sendImage(Uri uriImage) {
        /**
         * Gọi phương thức uploadImage từ photoRepos để tải hình ảnh lên máy chủ.
         * Nếu tải lên thành công, URL của hình ảnh sẽ được trả về trong callback.
         * Nếu tải lên không thành công thì sẽ trả về chuỗi rỗng trong callback.

         * Tác giả: Văn Hoàng
         */
        photoRepos.uploadImage(uriImage, url -> {
            if (!url.isEmpty()) {
                Message newMessage = sendMedia(url, Message.EType.IMAGE);
                newMessage.setMessage(Utils.KEY_INPUT_IMG);
                updateConversation(newMessage);
            } else {
                errorToastMessage.postValue("Unable to send image");
            }
        });
    }

    /**
     * Cập nhật cuộc hội thoại
     *
     * @param newMessage    :   Tin nhắn mới nhận
     *
     * Tác giả: Nguyễn Hà Quỳnh Giao
     */
    private void updateConversation(Message newMessage) {
        try {
            newMessage.setSenderId(this.curSenderUid.getValue());
            newMessage.setRecipientId(this.curRecipientId.getValue());

            Log.i(TAG, "Send button clicked");
            // Kiểm tra conversation có tồn tại không
            if (conversationId.getValue() != null) {
                // Cập nhật conversation
                conversationRepos.update(newMessage);
            } else {
                // Thêm mới và set giá trị cho conversationId
                conversationRepos.add(newMessage).thenAccept(
                        conversation -> conversationId.setValue(conversation.getId()));
            }
            // Gửi thông báo
            pushNotification(newMessage);
        } catch (Exception e) {
            errorToastMessage.postValue("Undeliverable messages");
        }
    }

    /**
     * Gửi thông báo tin nhắn đến người nhận
     *      *
     * @param message   :   Tin nhắn vừa gửi.
     *
     * Tác giả: Nguyễn Hà Quỳnh Giao
     */
    private void pushNotification(Message message) {
        // Lấy thông tin người dùng hiện tại
        CompletableFuture<User> currentUser = authRepos.getCurrentUser();
        // Khi thông tin người dùng hiện tại đã lấy thành công
        currentUser.thenAccept(sender -> {
            // Tạo một HashMap để chứa thông tin của tin nhắn
            HashMap<String, String> completedMessage = new HashMap<>();
            completedMessage.put(Utils.KEY_FCM_TOKEN, curRecipientToken.getValue());
            completedMessage.put(Utils.KEY_SENDER_NAME, sender.getFullName());
            completedMessage.put(Utils.KEY_MESSAGE, message.getMessage());
            // Thông báo gửi tin nhắn
            sendNotification.postValue(completedMessage);
        });
    }

    /**
     * Tạo một đối tượng Message với nội dung và loại tin nhắn được truyền vào và các dữ liệu khác có sẵn trong ChatViewModel.
     *
     * @param content Nội dung của tin nhắn.
     * @param eType   Loại tin nhắn.
     * @return Đối tượng Message được tạo.

     * Tác giả: Văn Hoàng
     */
    private MessageDTO createMessageWithMessageAndType(String content, Message.EType eType) {
        MessageDTO message = new MessageDTO();

        message.setMessage(content);
        message.setType(eType);
        message.setSenderId(curSenderUid.getValue());
        message.setRecipientId(curRecipientId.getValue());
        message.setSendingTime(LocalDateTime.now().toString());
        message.setVisibility(Message.EVisible.ACTIVE);

        return message;
    }

    /**
     * Chọn hình ảnh từ thư viện ảnh của thiết bị.
     * Gửi một tín hiệu thay đổi của isOpenImageDialog để mở hộp thoại chọn hình ảnh bên ChatActivity.

     * Tác giả: Văn Hoàng
     */
    public void pickImage() {
        isOpenImageDialog.postValue(true);
    }

    /**
     * Chọn hình ảnh để nhận diện văn bản.
     * Gửi một tín hiệu thay đổi của isRecognizeEyeClicked để mở hộp thoại chọn hình ảnh để nhận diện văn bản bên ChatActivity.

     * Tác giả: Văn Hoàng
     */
    public void pickRecognizeImage() {
        isRecognizeEyeClicked.postValue(true);
    }

    /**
     * Xử lý sự kiện khi người dùng nhấn vào một hình ảnh trong danh sách tin nhắn trên màn hình.
     *
     * @param position Vị trí của hình ảnh trong danh sách tin nhắn.
     *                Từ vị trí này, lấy URL của hình ảnh và gửi đi thông báo qua biến "isImageClicked" để xử lí bên ChatActivity.

     * Tác giả: Văn Hoàng
     */
    @Override
    public void onImageClick(int position) {
        String urlImage = Objects.requireNonNull(messages.getValue()).get(position).getMessage();
        isImageClicked.postValue(urlImage);
    }

    void checkForConversationRemotely() {
        if (Objects.requireNonNull(messages.getValue()).size() != 0) {
            conversationRepos.getConversationBySenderAndRecipient(this.curSenderUid.getValue(), this.curRecipientId.getValue()).thenAccept(conversation -> this.conversationId.setValue(conversation.getId()));
        }
    }

    /**
     * Nhận dạng văn bản từ hình ảnh được cung cấp.
     *
     * @param context Context của ứng dụng.
     * @param uri     Uri của hình ảnh cần nhận dạng văn bản.

     * Tác giả: Văn Hoàng
     */
    public void recognizeTextFromImage(Context context, Uri uri) {
        // Khởi tạo một đối tượng nhận diện văn bản
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        TextAnalyzer analyzer = new TextAnalyzer(recognizer);

        Bitmap bitmap = RecognizeUtils.uriToBitmap(context, uri);

        // Phân tích văn bản từ hình ảnh
        analyzer.analyze(bitmap, textResult -> {
            if (!textResult.isEmpty()) {
                // Nếu văn bản được nhận dạng thành công, gán giá trị vào messageInput để hiển thị
                messageInput.postValue(textResult);
                Log.d(TAG, "Text recognized : " + textResult);
            } else {
                // Nếu không thể nhận dạng được văn bản trên hình ảnh, gửi một thông báo lỗi
                errorToastMessage.postValue("Unable to recognize text on image");
            }
        });
    }
}
