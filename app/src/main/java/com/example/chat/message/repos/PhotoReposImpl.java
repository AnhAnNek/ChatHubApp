package com.example.chat.message.repos;

import android.net.Uri;
import android.util.Log;

import com.example.chat.Utils;
import com.example.chat.message.callback.UploadImageCallBack;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class PhotoReposImpl implements PhotoRepos {
    private final String TAG = PhotoReposImpl.class.getSimpleName();

    private final StorageReference storageReference;

    public PhotoReposImpl() {
        storageReference = FirebaseStorage.getInstance().getReference();
    }


    /**
     * Tải một hình ảnh lên Firebase Storage.
     *
     * @param uriImage   URI của hình ảnh cần tải lên.
     * @param callBack   Callback để xử lý kết quả sau khi tải lên hình ảnh.
     * Tác giả: Văn Hoàng
     */
    @Override
    public void uploadImage(Uri uriImage, UploadImageCallBack callBack) {
        // Tạo một tham chiếu đến vị trí lưu trữ trên Firebase Storage
        StorageReference imageRef = storageReference.child(Utils.KEY_FOLDER_IMAGE).child(UUID.randomUUID().toString());
        // Bắt đầu quá trình tải lên hình ảnh
        UploadTask task = imageRef.putFile(uriImage);

        // Xử lý sự kiện khi quá trình tải lên hoàn thành hoặc thất bại
        task
        .addOnSuccessListener(taskSnapshot -> {
            // Nếu tải lên thành công, lấy URL tải xuống của hình ảnh và gửi lại qua callback
            imageRef.getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        callBack.onSuccess(uri.toString());
                    })
                    .addOnFailureListener(e -> Log.e(TAG, "Failed to get download URL", e));
        })
        .addOnFailureListener(e -> {
            // Nếu quá trình tải lên gặp lỗi, gửi URL trống qua callback và ghi log lỗi
            callBack.onSuccess("");
            Log.e(TAG, e.getMessage());
        });
    }
}
