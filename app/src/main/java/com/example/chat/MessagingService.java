package com.example.chat;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.R;
import com.example.chat.conversations.ConversationViewModel;
import com.example.chat.message.Message;
import com.example.home.HomeActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Service xử lý tin nhắn từ Firebase Cloud Messaging (FCM).
 * Tác vụ chính của service là xử lý các tin nhắn nhận được khi người khác gửi FCM đến thiết bị này.
 *
 * Tác giả: Văn Hoàng
 */
public class MessagingService extends FirebaseMessagingService {

    private static final String TAG = "FCM";

    /**
     * Phương thức này được gọi khi một token FCM mới được tạo hoặc cập nhật.
     * @param token Token FCM mới.

     * Tác giả: Văn Hoàng
     */
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);

        Log.d(TAG, "Token: " + token);
    }

    /**
     * Xử lý tin nhắn nhận được
     *
     * @param message Remote message that has been received.
     *
     * Cập nhật view tin nhắn
     * Tác giả: Nguyễn Văn Hoàng
     *
     * Gửi thông báo và cập nhật view conversation
     * Tác giả: Nguyễn Hà Quỳnh Giao
     */

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        Map<String, String> data = message.getData();
        if (data.size() > 0) {
            try {
                String topic = data.get(Utils.KEY_TOPIC);

            // Kiểm tra  topic của dữ liệu thuộc loại topic "chat" của ứng dụng hay không
                if (topic.equals(Utils.KEY_COLLECTION_CHAT)) {
                    Message ms = createMessageFromRemoteMessage(data);
                    ChatViewModel.addMessage(ms);
                    // Cập nhật giao diện conversations
                    ConversationViewModel.updateConversations(ms);
                }
                // Xử lý tin nhắn thông báo
                else if (topic.equals(Utils.KEY_NOTIFY)){

                    Log.i(TAG, "onMessageReceived: called");
                    Log.i(TAG, "onMessageReceived: Message receiver from: " + message.getFrom());
                    // Kiểm tra xem thông báo có tồn tại không
                    if (message.getNotification() != null) {
                        String title = message.getNotification().getTitle();
                        String body = message.getNotification().getBody();

                        // Tạo một kênh thông báo
                        NotificationChannel chanel = new NotificationChannel(
                                Utils.FCM_CHANEL_ID,
                                "Message Notifications",
                                NotificationManager.IMPORTANCE_HIGH);


                        Log.i(TAG, "Creating Chanel...");
                        getSystemService(NotificationManager.class).createNotificationChannel(chanel);
                        // Tạo thông báo
                        Notification.Builder notification = new Notification.Builder(this, Utils.FCM_CHANEL_ID)
                                .setContentTitle(title)
                                .setContentText(body)
                                .setSmallIcon(R.drawable.ic_launcher_foreground)
                                .setAutoCancel(true);
                        // Thiết lập một intent để mở ứng dụng khi thông báo được nhấp
                        Intent intent = new Intent(this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_MUTABLE);
                        notification.setContentIntent(pendingIntent);

                        // Thông báo cho người dùng
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        NotificationManagerCompat.from(this).notify(1, notification.build());
                    }

                    if (message.getData().size() > 0) {
                        Log.i(TAG, "onMessageReceived: Data " + message.getData().toString());
                    }
                }
                Log.d(TAG, "Message: " + message.getData());
            } catch (Exception e) {
                Log.e(TAG, "Error: " + e.getMessage());
            }
        }
    }

    /**
     * Tạo một đối tượng Message từ dữ liệu của tin nhắn FCM.
     * @param data Dữ liệu của tin nhắn FCM.
     * @return Đối tượng Message được tạo ra.

     * Tác giả: Văn Hoàng
     */
    private Message createMessageFromRemoteMessage(Map<String, String> data) {
        String senderId = data.get(Utils.KEY_USER_ID);
        String recipientId = data.get(Utils.KEY_RECIPIENT_ID);
        String messageText = data.get(Utils.KEY_MESSAGE);
        String isVisibility = data.get(Utils.KEY_IS_VISIBILITY);
        String type = data.get(Utils.KEY_TYPE);
        String sendingTime = data.get(Utils.KEY_SENDING_TIME);

        Message ms = new Message(
                senderId,
                recipientId,
                messageText,
                Message.EVisible.valueOf(isVisibility),
                Message.EType.valueOf(type),
                sendingTime
        );

        return  ms;
    }
}

