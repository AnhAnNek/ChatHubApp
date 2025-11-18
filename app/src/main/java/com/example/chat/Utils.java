package com.example.chat;

import android.util.Log;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class Utils {
    public static final String KEY_NAME = "name";
    public static final String KEY_TYPE = "type";
    public static final String KEY_IS_DELETED = "isDeleted";
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_COLLECTION_CHAT = "chat";
    public static final String KEY_SENDER_ID = "senderId";
    public static final String KEY_SENDER_NAME = "senderName";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_NOTIFY = "notify";
    public static final String KEY_IS_VISIBILITY = "visibility";
    public static final String KEY_SENDING_TIME = "sendingTime";
    public static final String KEY_IMAGE_CLICKED_URL = "imageClickedUrl";
    public static final String KEY_RECIPIENT_ID = "recipientId";
    public static final String KEY_CONVERSATION_ID = "conversationId";
    public static final String KEY_COLLECTION_CONVERSATION = "conversations";
    public static final String KEY_CONVERSATION = "conversation";
    public static final String KEY_LAST_MESSAGE = "lastMessage";
    public static final String REMOTE_MSG_AUTHORIZATION = "Authorization";
    public static final String REMOTE_MSG_CONTENT_TYPE = "Content-Type";
    public static final String REMOTE_MSG_DATA = "data";
    public static final String REMOTE_MSG_REGISTRATION_IDS = "registration_ids";
    public static final String KEY_FCM_TOKEN = "fcmToken";
    public static final String KEY_COLLECTION_TOKEN = "tokens";
    public static final String KEY_TOPIC = "topic";
    public static final String KEY_FOLDER_IMAGE = "chatImages";
    public static final String KEY_RECIPIENT_IMAGE = "recipientImage";
    public static final String KEY_INPUT_IMG = "[Ảnh]";
    public static final String KEY_FAILED_SEND_MESSAGE = "Undeliverable messages";
    public static final String FCM_CHANEL_ID = "MESSAGE";
    public static final String FCM_BASE_URL = "https://fcm.googleapis.com/fcm/send";
    public static final String FCM_SERVER_KEY = "key=AAAACmc24aE:APA91bGTXVKt5sFiYFKbFB572n99T1jKIQm4qv8uiL9NnsulnpdnSlQTt7YXXU-OzqH_ALH2J2AdLjp3MpMPtqNxkd0HAhWmrHCkKLwAASpyKc-vZjyGu6MQH_k-DayNEURSnlUvgz0W";

    public static HashMap<String, String> remoteMsgHeaders = null;

    /**
     * Lấy các Header yêu cầu cần thiết để gửi tin nhắn từ xa.
     *
     * @return HashMap chứa các Header yêu cầu cần thiết.
     * Tác giả: Văn Hoàng
     */
    public static HashMap<String, String> getRemoteMsgHeaders() {
        if (remoteMsgHeaders == null) {
            remoteMsgHeaders = new HashMap<>();
            // Thêm Header Authorization với giá trị khóa API
            remoteMsgHeaders.put(
                    REMOTE_MSG_AUTHORIZATION,
                    "key=AAAACmc24aE:APA91bGTXVKt5sFiYFKbFB572n99T1jKIQm4qv8uiL9NnsulnpdnSlQTt7YXXU-OzqH_ALH2J2AdLjp3MpMPtqNxkd0HAhWmrHCkKLwAASpyKc-vZjyGu6MQH_k-DayNEURSnlUvgz0W"
            );
            // Thêm Header Content-Type với giá trị application/json
            remoteMsgHeaders.put(
                    REMOTE_MSG_CONTENT_TYPE,
                    "application/json"
            );
        }
        return remoteMsgHeaders;
    }

    /**
     * Chuyển đổi chuỗi thời gian thành đối tượng LocalDateTime.
     *
     * @param dateTimeString Chuỗi thời gian cần chuyển đổi.
     * @return Đối tượng LocalDateTime được chuyển đổi từ chuỗi thời gian, hoặc null nếu có lỗi xảy ra.
     * Tác giả: Văn Hoàng
     */
    public static LocalDateTime getLocalDateTime(String dateTimeString) {
        try {
            return LocalDateTime.parse(dateTimeString);
        } catch (Exception e) {
            Log.e("DateTimeConverter", "Error parsing \"" + dateTimeString + "\" : " + e.getMessage(), e);
            return null;
        }
    }
}
