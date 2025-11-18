package com.example.chat.message.repos;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.chat.Utils;
import com.example.chat.message.Message;
import com.example.chat.message.MessageDTO;
import com.example.chat.message.api.FirebaseApiClient;
import com.example.chat.message.api.FirebaseApiClientFactory;
import com.example.chat.message.api.MessageApiClient;
import com.example.chat.message.callback.GetRemoteMessageCallBack;
import com.example.chat.message.callback.MessageCallback;
import com.example.chat.message.callback.TokenCallback;
import com.example.infrastructure.api.RestApiClient;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageReposImpl implements MessageRepos{
    private static final String TAG = MessageReposImpl.class.getSimpleName();

    private final FirebaseFirestore firebaseFirestore;


    public MessageReposImpl() {
        this.firebaseFirestore = FirebaseFirestore.getInstance();
    }

    /**
     * Lấy token FCM của người dùng từ Firestore.
     *
     * @param uid      ID của người dùng.
     * @param callback Callback để xử lý kết quả lấy token.
     * Tác giả: Văn Hoàng
     */
    @Override
    public void getToken(String uid, TokenCallback callback) {
        firebaseFirestore.collection(Utils.KEY_COLLECTION_TOKEN).document(uid)
                .get()
                /**
                 * Xử lý kết quả truy vấn Firestore khi hoàn thành.
                 *
                 * @param task Nhiệm vụ truy vấn Firestore.
                 * Tác giả: Văn Hoàng
                 */
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot.exists()) {
                            Object tokenO = documentSnapshot.get(Utils.KEY_FCM_TOKEN);
                            if (tokenO != null) {
                                String token = (String) tokenO;
                                callback.onTokenReceived(token);
                            } else {
                                Log.e(TAG, "Token is null for uid: " + uid);
                            }
                        } else {
                            Log.e(TAG, "Document does not exist for uid: " + uid);
                        }
                    } else {
                        Log.e(TAG, "Error getting document: ", task.getException());
                    }
                })
                /**
                 * Xử lý khi truy vấn Firestore thất bại.
                 *
                 * @param e Ngoại lệ xảy ra khi truy vấn Firestore thất bại.
                 * Tác giả: Văn Hoàng
                 */
                .addOnFailureListener(e -> Log.e(TAG, e.getMessage()));
    }

    /**
     * Cập nhật token FCM mới cho người dùng.
     *
     * @param uid ID của người dùng.
     * Tác giả: Văn Hoàng
     */
    @Override
    public void updateToken(String uid) {
        FirebaseMessaging.getInstance().getToken()
                .addOnSuccessListener(token -> {
                    updateFCMToken(uid,token);
                })
                .addOnFailureListener( e -> {
                    Log.e(TAG,e.getMessage());
                });
    }

    /**
     * Cập nhật token FCM trong Firestore.
     *
     * @param uid   ID của người dùng.
     * @param token Token FCM mới.
     * Tác giả: Văn Hoàng
     */
    private void updateFCMToken(String uid, String token) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        // Tham chiếu đến document của người dùng trong collection token
        DocumentReference documentReference =
                database.collection(Utils.KEY_COLLECTION_TOKEN).document(uid);

        // Tạo một map chứa dữ liệu cần cập nhật
        Map<String, Object> data = new HashMap<>();
        data.put(Utils.KEY_USER_ID, uid);
        data.put(Utils.KEY_FCM_TOKEN, token);

        documentReference.set(data)
                .addOnSuccessListener(s -> Log.d(TAG,"Unable to update token"))
                .addOnFailureListener(e -> Log.d(TAG,e.getMessage()));
    }

    /**
     * Gửi tin nhắn bằng cách gọi REST API sử dùn Retrofit.
     *
     * @param message  Đối tượng MessageDTO chứa thông tin tin nhắn.
     * @param callback Callback để xử lý kết quả gửi tin nhắn.
     * Tác giả: Văn Hoàng
     */
    @Override
    public void sendMessage(MessageDTO message, MessageCallback callback) {
        RestApiClient.getIns().create(MessageApiClient.class)
                .sendMessage(message)
                /**
                 * Xử lý kết quả trả về từ Backend khi gửi tin nhắn thành công hoặc thất bại.
                 */
                .enqueue(new Callback<MessageDTO>() {
                    /**
                     * Xử lý khi nhận được phản hồi từ Backend khi gửi tin nhắn.
                     *
                     * @param call     Một đối tượng Call đại diện cho yêu cầu HTTP để gửi tin nhắn.
                     * @param response Phản hồi từ Backend.
                     * Tác giả: Văn Hoàng
                     */
                    @Override
                    public void onResponse(@NonNull Call<MessageDTO> call, @NonNull Response<MessageDTO> response) {
                        // Nếu thành công
                        if (response.isSuccessful()) {
                            MessageDTO messageDTO = response.body();
                            callback.onResponse(messageDTO);
                        } else {
                            try {
                                /**
                                 * Xử lý lỗi nếu tin nhắn không được gửi thành công.
                                 *
                                 * @throws IOException Ngoại lệ xảy ra khi xử lý lỗi.
                                 * Tác giả: Văn Hoàng
                                 */
                                Log.e(TAG, "onResponse: " + response.errorBody().string());
                                callback.onResponse(null);
                            } catch (IOException e) {
                                callback.onResponse(null);
                            }
                        }
                    }
                    /**
                     * Xử lý khi gặp lỗi trong quá trình gửi tin nhắn.
                     *
                     * @param call Một đối tượng Call đại diện cho yêu cầu HTTP để gửi tin nhắn.
                     * @param t    Ngoại lệ xảy ra.
                     * Tác giả: Văn Hoàng
                     */
                    @Override
                    public void onFailure(@NonNull Call<MessageDTO> call, @NonNull Throwable t) {
                        Log.e(TAG, "onResponse: " + t.getMessage());
                    }
                });
    }

    /**
     * Gửi một tin nhắn thông qua Firebase Cloud Messaging (FCM).
     *
     * @param RemoteMsgHeaders Các header cần thiết cho yêu cầu FCM.
     * @param message          Đối tượng Message chứa thông tin tin nhắn.
     * @param receivedToken    Token FCM của người nhận tin nhắn.
     * Tác giả: Văn Hoàng
     */
    @Override
    public void sendFCMMessage(HashMap<String, String> RemoteMsgHeaders, Message message, String receivedToken) {

        getToken()
                /**
                 * Lấy token FCM của thiết bị.
                 *
                 * Tác giả: Văn Hoàng
                 */
                .addOnSuccessListener(token -> {
                    // Khởi tạo một JSONArray để chứa token của thiết bị nhận tin nhắn
                    JSONArray tokens = new JSONArray();
                    tokens.put(receivedToken);

                    try {
                        // Tạo một đối tượng JSONObject chứa dữ liệu tin nhắn
                        JSONObject data = new JSONObject();

                        // Thêm các thông tin của tin nhắn vào đối tượng data
                        data.put(Utils.KEY_TOPIC, Utils.KEY_COLLECTION_CHAT);
                        data.put(Utils.KEY_USER_ID, message.getSenderId());
                        data.put(Utils.KEY_RECIPIENT_ID, message.getRecipientId());
                        data.put(Utils.KEY_MESSAGE, message.getMessage());
                        data.put(Utils.KEY_IS_VISIBILITY, message.getVisibility().toString());
                        data.put(Utils.KEY_TYPE, message.getType().toString());
                        data.put(Utils.KEY_SENDING_TIME, message.getSendingTime());
                        data.put(Utils.KEY_FCM_TOKEN, token);

                        // Tạo một đối tượng JSONObject chứa dữ liệu tin nhắn và danh sách token nhận tin nhắn
                        JSONObject body = new JSONObject();
                        body.put(Utils.REMOTE_MSG_DATA, data);
                        body.put(Utils.REMOTE_MSG_REGISTRATION_IDS, tokens);

                        // Gửi thông điệp FCM tới server
                        send(body.toString());
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, e.getMessage());
                });
    }

    /**
     * Lấy danh sách tin nhắn từ Backend.
     *
     * @param senderUid ID của người gửi tin nhắn.
     * @param callBack  Callback để xử lý kết quả lấy tin nhắn từ Backend.
     * Tác giả: Văn Hoàng
     */
    @Override
    public void getRemoteMessages(String senderUid, GetRemoteMessageCallBack callBack) {
        RestApiClient.getIns().create(MessageApiClient.class)
                /**
                 * Gửi yêu cầu lấy danh sách tin nhắn từ Backend dựa trên ID của người gửi tin nhắn.
                 *
                 * @param senderUid ID của người gửi tin nhắn.
                 * @param callBack  Callback để xử lý kết quả sau khi lấy danh sách tin nhắn.
                 * Tác giả: Văn Hoàng
                 */
                .getMessages(senderUid)
                /**
                 * Xử lý kết quả trả về từ Backend khi lấy danh sách tin nhắn thành công hoặc thất bại.
                 */
                .enqueue(new Callback<List<MessageDTO>>() {
                    /**
                     * Xử lý khi nhận được phản hồi từ Backend khi lấy danh sách tin nhắn.
                     *
                     * @param call     Một đối tượng Call đại diện cho yêu cầu HTTP để lấy danh sách tin nhắn.
                     * @param response Phản hồi từ Backend.
                     * Tác giả: Văn Hoàng
                     */
                    @Override
                    public void onResponse(@NonNull Call<List<MessageDTO>> call, @NonNull Response<List<MessageDTO>> response) {
                        if (response.isSuccessful()) {
                            List<MessageDTO> messageDTOs = response.body();
                            callBack.onResponse(messageDTOs);
                        } else {
                            try {
                                /**
                                 * Xử lý lỗi nếu không lấy được danh sách tin nhắn từ Backend.
                                 *
                                 * @throws IOException Ngoại lệ xảy ra khi xử lý lỗi.
                                 * Tác giả: Văn Hoàng
                                 */
                                Log.e(TAG, "onResponse: " + response.errorBody().string());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    /**
                     * Xử lý khi gặp lỗi trong quá trình lấy danh sách tin nhắn.
                     *
                     * @param call Một đối tượng Call đại diện cho yêu cầu HTTP để lấy danh sách tin nhắn.
                     * @param t    Ngoại lệ xảy ra.
                     * Tác giả: Văn Hoàng
                     */
                    @Override
                    public void onFailure(@NonNull Call<List<MessageDTO>>  call, @NonNull Throwable t) {
                        Log.e(TAG, "onResponse: " + t.getMessage());
                    }
                });
    }

    /**
     * Gửi thông điệp FCM tới server.
     *
     * @param messageBody Nội dung tin nhắn FCM.
     * Tác giả: Văn Hoàng
     */
    private void send(String messageBody) {
        FirebaseApiClientFactory.getIns().create(FirebaseApiClient.class)
                /**
                 * Gửi tin nhắn FCM tới các thiết bị đích thông qua Firebase Cloud Messaging.
                 *
                 * @param RemoteMsgHeaders Thông tin header cho yêu cầu gửi tin nhắn FCM.
                 * @param messageBody      Nội dung tin nhắn FCM được gửi.
                 * Tác giả: Văn Hoàng
                 */
                .sendFCMMessage(Utils.getRemoteMsgHeaders(), messageBody)
                /**
                 * Xử lý kết quả trả về từ Firebase Cloud Messaging khi gửi tin nhắn FCM thành công hoặc thất bại.
                 */
                .enqueue(new Callback<String>() {
                    /**
                     * Xử lý khi nhận được phản hồi từ Firebase Cloud Messaging khi gửi tin nhắn FCM.
                     *
                     * @param call     Một đối tượng Call đại diện cho yêu cầu HTTP để gửi tin nhắn FCM.
                     * @param response Phản hồi từ Firebase Cloud Messaging.
                     * Tác giả: Văn Hoàng
                     */
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        if (response.isSuccessful()) {
                            try {
                                if (response.body() != null) {
                                    // Xử lý phản hồi từ Firebase Cloud Messaging khi gửi tin nhắn FCM thành công
                                    JSONObject responseJson = new JSONObject(response.body());
                                    JSONArray results = responseJson.getJSONArray("results");
                                    //Nếu phản hồi là thất bại
                                    if (responseJson.getInt("failure") == 1) {
                                        JSONObject error = (JSONObject) results.get(0);
                                        Log.e(TAG, error.getString("error"));
                                        return;
                                    }
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            Log.d(TAG, "Notification sent successfully");
                        } else {
                            // Xử lý khi gặp lỗi khi gửi tin nhắn FCM
                            Log.e(TAG, "Error: " + response.code());
                        }
                    }

                    /**
                     * Xử lý khi gặp lỗi trong quá trình gửi tin nhắn FCM.
                     *
                     * @param call Một đối tượng Call đại diện cho yêu cầu HTTP để gửi tin nhắn FCM.
                     * @param t    Ngoại lệ xảy ra.
                     * Tác giả: Văn Hoàng
                     */
                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        Log.e(TAG, Objects.requireNonNull(t.getMessage()));
                    }
        });
    }

    /**
     * Lấy token FCM của thiết bị hiện tại.
     *
     * @return Task chứa token FCM.
     * Tác giả: Văn Hoàng
     */
    private Task<String> getToken() {
        return FirebaseMessaging.getInstance().getToken();
    }
}
