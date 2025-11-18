package com.example.user.repository;

import com.example.user.User;

import java.util.concurrent.CompletableFuture;

/**
 * UserRepos là một giao diện đại diện cho kho lưu trữ người dùng.
 * Các phương thức trong giao diện này sử dụng CompletableFuture để thực hiện các thao tác bất đồng bộ.
 *
 * Tác giả: Trần Văn An
 */
public interface UserRepos {

    /**
     * Thêm người dùng mới vào kho lưu trữ.
     *
     * @param user đối tượng User cần thêm.
     * @return CompletableFuture<Void> đại diện cho kết quả của thao tác bất đồng bộ.
     */
    CompletableFuture<Void> addUser(User user);

    /**
     * Cập nhật số điện thoại của người dùng xác định bằng UID.
     *
     * @param uid mã định danh duy nhất của người dùng.
     * @param phoneNumber số điện thoại mới cần cập nhật.
     * @return CompletableFuture<Void> đại diện cho kết quả của thao tác bất đồng bộ.
     *
     * Tác giả: Trần Văn An
     */
    CompletableFuture<Void> updatePhoneNumber(String uid, String phoneNumber);

    /**
     * Kiểm tra sự tồn tại của người dùng thông qua email.
     *
     * @param email địa chỉ email của người dùng.
     * @return CompletableFuture<Boolean> đại diện cho kết quả của thao tác bất đồng bộ,
     * true nếu người dùng tồn tại, false nếu không.
     *
     * Tác giả: Trần Văn An
     */
    CompletableFuture<Boolean> checkUserExistsByEmail(String email);

    /**
     * Cập nhật thông tin cơ bản của người dùng xác định bằng UID.
     *
     * @param uid mã định danh duy nhất của người dùng.
     * @param user đối tượng User chứa thông tin mới cần cập nhật.
     * @return CompletableFuture<Void> đại diện cho kết quả của thao tác bất đồng bộ.
     *
     * Tác giả: Trần Văn An
     */
    CompletableFuture<Void> updateBasicUser(String uid, User user);

    /**
     * Kiểm tra sự tồn tại của người dùng thông qua số điện thoại.
     *
     * @param phoneNumber số điện thoại của người dùng.
     * @return CompletableFuture<Boolean> đại diện cho kết quả của thao tác bất đồng bộ,
     * true nếu người dùng tồn tại, false nếu không.
     *
     * Tác giả: Trần Văn An
     */
    CompletableFuture<Boolean> existsByPhoneNumber(String phoneNumber);

    /**
     * Lấy thông tin người dùng thông qua UID.
     *
     * @param uid mã định danh duy nhất của người dùng.
     * @return CompletableFuture<User> đại diện cho kết quả của thao tác bất đồng bộ,
     * trả về đối tượng User nếu tìm thấy.
     *
     * Tác giả: Trần Văn An
     */
    CompletableFuture<User> getUserByUid(String uid);
}
