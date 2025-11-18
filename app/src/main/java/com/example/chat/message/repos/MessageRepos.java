package com.example.chat.message.repos;

import com.example.chat.message.Message;
import com.example.chat.message.MessageDTO;
import com.example.chat.message.callback.GetRemoteMessageCallBack;
import com.example.chat.message.callback.MessageCallback;
import com.example.chat.message.callback.TokenCallback;

import java.util.HashMap;

/**
 * Interface định nghĩa các phương thức để thao tác với dữ liệu tin nhắn.
 * Tác giả: Văn Hoàng
 */
public interface MessageRepos {

    /**
     * Lấy token của người dùng.
     *
     * @param uid      ID của người dùng.
     * @param callback Callback để xử lý kết quả lấy token.
     * Tác giả: Văn Hoàng
     */
    void getToken(String uid, TokenCallback callback);

    /**
     * Cập nhật token của người dùng.
     *
     * @param uid ID của người dùng.
     * Tác giả: Văn Hoàng
     */
    void updateToken(String uid);

    /**
     * Gửi một tin nhắn.
     *
     * @param message  Đối tượng MessageDTO chứa thông tin tin nhắn.
     * @param callback Callback để xử lý kết quả gửi tin nhắn.
     * Tác giả: Văn Hoàng
     */
    void sendMessage(MessageDTO message, MessageCallback callback);

    /**
     * Gửi một tin nhắn thông qua Firebase Cloud Messaging (FCM).
     *
     * @param RemoteMsgHeaders Các header cần thiết cho yêu cầu FCM.
     * @param message          Đối tượng Message chứa thông tin tin nhắn.
     * @param receivedToken    Token FCM của người nhận tin nhắn.
     * Tác giả: Văn Hoàng
     */
    void sendFCMMessage(HashMap<String, String> RemoteMsgHeaders, Message message, String receivedToken);

    /**
     * Lấy danh sách tin nhắn từ Backend.
     *
     * @param senderUid ID của người gửi tin nhắn.
     * @param callBack  Callback để xử lý kết quả lấy tin nhắn từ Backend.
     * Tác giả: Văn Hoàng
     */
    void getRemoteMessages(String senderUid, GetRemoteMessageCallBack callBack);

    // Ngrok command để mở đường hầm HTTP cho localhost, dùng cho việc thử nghiệm và phát triển.
    // ngrok http http://localhost:8000
}

