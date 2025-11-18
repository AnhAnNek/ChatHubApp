package com.example.common.repository;

import android.net.Uri;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.concurrent.CompletableFuture;

/**
 * Lớp MediaReposImpl triển khai interface MediaRepos để quản lý việc tải lên và tải xuống các tệp phương tiện từ Firebase Storage.
 *
 * Tác giả: Trần Văn An
 */
public class MediaReposImpl implements MediaRepos {

    private final StorageReference storageRef;

    /**
     * Khởi tạo MediaReposImpl và thiết lập tham chiếu đến Firebase Storage.
     *
     * Tác giả: Trần Văn An
     */
    public MediaReposImpl() {
        storageRef = FirebaseStorage.getInstance().getReference();
    }

    /**
     * {@inheritDoc}
     *
     * Tác giả: Trần Văn An
     */
    @Override
    public CompletableFuture<Uri> downloadFile(String filePath) {
        StorageReference fileRef = storageRef.child(filePath);
        return downloadFile(fileRef);
    }

    /**
     * {@inheritDoc}
     *
     * Tác giả: Trần Văn An
     */
    @Override
    public CompletableFuture<Void> uploadFile(String filePath, Uri uri) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        StorageReference imageRef = storageRef.child(filePath);
        imageRef.putFile(uri)
                .addOnSuccessListener(snapshot -> {
                    future.complete(null);
                })
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }

    /**
     * {@inheritDoc}
     *
     * Tác giả: Trần Văn An
     */
    @Override
    public CompletableFuture<Uri> downloadAvatar(String userId) {
        CompletableFuture<Uri> future = new CompletableFuture<>();

        String avatarPath = getAvatarFilePath(userId);
        downloadFile(avatarPath)
                .thenAccept(future::complete)
                .exceptionally(e -> {
                    String defaultAvatarPath = getDefaultAvatarPath();
                    downloadFile(defaultAvatarPath)
                            .thenAccept(future::complete)
                            .exceptionally(defAvatarException -> {
                                future.completeExceptionally(defAvatarException);
                                return null;
                            });
                    return null;
                });

        return future;
    }

    /**
     * {@inheritDoc}
     *
     * Tác giả: Trần Văn An
     */
    @Override
    public CompletableFuture<Void> uploadAvatar(String userId, Uri uri) {
        String avatarPath = getAvatarFilePath(userId);
        return uploadFile(avatarPath, uri);
    }

    /**
     * Lấy đường dẫn của avatar dựa trên ID người dùng.
     *
     * @param userId ID của người dùng.
     * @return Đường dẫn của avatar.
     *
     * Tác giả: Trần Văn An
     */
    private String getAvatarFilePath(String userId) {
         return String.format("%s%s/%s", ECloudFolder.AVATARS.path, userId, userId);
    }

    /**
     * Lấy đường dẫn của avatar mặc định.
     *
     * @return Đường dẫn của avatar mặc định.
     *
     * Tác giả: Trần Văn An
     */
    private String getDefaultAvatarPath() {
        return String.format("%s%s", ECloudFolder.AVATARS.path, "default.png");
    }

    /**
     * Tải xuống một tệp từ kho lưu trữ dựa trên tham chiếu đến tệp.
     *
     * @param fileRef Tham chiếu đến tệp cần tải xuống.
     * @return Một CompletableFuture chứa Uri của tệp đã tải xuống.
     *
     * Tác giả: Trần Văn An
     */
    private CompletableFuture<Uri> downloadFile(StorageReference fileRef) {
        CompletableFuture<Uri> future = new CompletableFuture<>();

        fileRef.getDownloadUrl()
                .addOnSuccessListener(future::complete)
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }

    /**
     * Enum ECloudFolder đại diện cho các thư mục trong kho lưu trữ.
     *
     * Tác giả: Trần Văn An
     */
    public enum ECloudFolder {
        AVATARS("avatars/");

        private final String path;

        ECloudFolder(String path) {
            this.path = path;
        }
    }
}
