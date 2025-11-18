package com.example.common.repository;

import android.net.Uri;

import java.util.concurrent.CompletableFuture;

/**
 * Interface MediaRepos định nghĩa các phương thức để quản lý việc tải lên và tải xuống các tệp phương tiện.
 *
 * Tác giả: Trần Văn An
 */
public interface MediaRepos {

    /**
     * Tải xuống một tệp từ kho lưu trữ dựa trên đường dẫn tệp.
     *
     * @param filePath Đường dẫn của tệp cần tải xuống.
     * @return Một CompletableFuture chứa Uri của tệp đã tải xuống.
     *
     * Tác giả: Trần Văn An
     */
    CompletableFuture<Uri> downloadFile(String filePath);

    /**
     * Tải lên một tệp lên kho lưu trữ dựa trên đường dẫn tệp và Uri của tệp.
     *
     * @param filePath Đường dẫn của tệp cần tải lên.
     * @param uri Uri của tệp cần tải lên.
     * @return Một CompletableFuture biểu thị hoàn thành công việc.
     *
     * Tác giả: Trần Văn An
     */
    CompletableFuture<Void> uploadFile(String filePath, Uri uri);

    /**
     * Tải xuống avatar của người dùng dựa trên ID người dùng.
     *
     * @param userId ID của người dùng.
     * @return Một CompletableFuture chứa Uri của avatar đã tải xuống.
     *
     * Tác giả: Trần Văn An
     */
    CompletableFuture<Uri> downloadAvatar(String userId);

    /**
     * Tải lên avatar của người dùng dựa trên ID người dùng và Uri của avatar.
     *
     * @param userId ID của người dùng.
     * @param uri Uri của avatar cần tải lên.
     * @return Một CompletableFuture biểu thị hoàn thành công việc.
     *
     * Tác giả: Trần Văn An
     */
    CompletableFuture<Void> uploadAvatar(String userId, Uri uri);
}
