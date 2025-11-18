package com.example.chat.notify;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.chat.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * Gửi thông báo đén Firebase Cloud Messaging từ device -> device
 *
 * Tác giả: Nguyễn Hà Quỳnh Giao
 */
public class FCMSend {
    private static final String TAG = FCMSend.class.getSimpleName();

    /**
     * Gửi thông báo đến FCM
     *
     * @param context       :   Context của ứng dụng.
     * @param token         :   Token của thiết bị nhận thông báo.
     * @param title         :   Tiêu đề thông báo
     * @param message       :   Nội dung thông báo
     *
     * Tác giả: Nguyễn Hà Quỳnh Giao
     */
    public static void pushNotification(Context context, String token, String title, String message){
        // Cho phép tất cả các hoạt động mạng trên luồng chính
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Tạo hàng đợi RequestQueue cho Volley
        RequestQueue queue = Volley.newRequestQueue(context);

        try{
            // Tạo đối tượng JSON để tạo thông báo
            JSONObject json = new JSONObject();
            json.put("to", token);
            JSONObject notification = new JSONObject();
            notification.put("title", title);
            notification.put("body", message);
            json.put("notification", notification);

            // Tạo đối tượng JSON để chứa dữ liệu kèm theo
            JSONObject data = new JSONObject();
            data.put(Utils.KEY_TOPIC, Utils.KEY_NOTIFY);
            json.put("data", data);

            // Tạo một request JSON object bằng cách sử dụng POST method
            JsonObjectRequest JsonObjectRequest =
                    new JsonObjectRequest(
                            Request.Method.POST,
                            Utils.FCM_BASE_URL, json, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    // Log response nếu thành công
                    Log.i(TAG, String.valueOf(response));
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Log error nếu xảy ra lỗi
                    Log.e(TAG, Objects.requireNonNull(error.getMessage()));
                }
            }) {
                // Ghi đè phương thức getHeaders để thêm tiêu đề yêu cầu
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Context-Type", "application/json");
                    params.put("Authorization", Utils.FCM_SERVER_KEY);
                    return params;
                }
            };

            // Thêm request vào hàng đợi
            queue.add(JsonObjectRequest);
        } catch (JSONException e) {
            // Ném một RuntimeException nếu có lỗi khi tạo JSON object
            throw new RuntimeException(e);
        }
    }
}
