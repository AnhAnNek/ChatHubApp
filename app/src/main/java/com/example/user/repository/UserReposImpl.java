package com.example.user.repository;

import android.net.Uri;
import android.util.Log;

import com.example.common.repository.MediaRepos;
import com.example.common.util.FileUtils;
import com.example.infrastructure.BaseRepos;
import com.example.user.User;
import com.example.user.api.UserApiClient;
import com.example.user.api.UserApiClientFactory;

import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * UserReposImpl là một lớp triển khai của giao diện UserRepos.
 * Lớp này sử dụng Retrofit để giao tiếp với API và CompletableFuture để thực hiện các thao tác bất đồng bộ.
 *
 * Tác giả: Trần Văn An
 */
public class UserReposImpl extends BaseRepos implements UserRepos {

    private UserApiClient userApiClient;
    private MediaRepos mediaRepos;

    /**
     * Khởi tạo UserReposImpl với một MediaRepos.
     *
     * @param mediaRepos đối tượng MediaRepos để xử lý các thao tác liên quan đến media.
     */
    public UserReposImpl(MediaRepos mediaRepos) {
        this.userApiClient = UserApiClientFactory.create();
        this.mediaRepos = mediaRepos;
    }

    @Override
    protected String getTag() {
        return UserReposImpl.class.getSimpleName();
    }

    /**
     * Thêm người dùng mới vào kho lưu trữ.
     *
     * @param user đối tượng User cần thêm.
     * @return CompletableFuture<Void> đại diện cho kết quả của thao tác bất đồng bộ.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    public CompletableFuture<Void> addUser(User user) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        userApiClient
                .addUser(user)
                .enqueue(handleResponseCallbackWithoutData(future));
        return future;
    }

    /**
     * Cập nhật số điện thoại của người dùng xác định bằng UID.
     *
     * @param uid mã định danh duy nhất của người dùng.
     * @param phoneNumber số điện thoại mới cần cập nhật.
     * @return CompletableFuture<Void> đại diện cho kết quả của thao tác bất đồng bộ.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    public CompletableFuture<Void> updatePhoneNumber(String uid, String phoneNumber) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        userApiClient
                .updatePhoneNumber(uid, phoneNumber)
                .enqueue(handleResponseCallbackWithoutData(future));
        return future;
    }

    /**
     * Kiểm tra sự tồn tại của người dùng thông qua email.
     *
     * @param email địa chỉ email của người dùng.
     * @return CompletableFuture<Boolean> đại diện cho kết quả của thao tác bất đồng bộ,
     * true nếu người dùng tồn tại, false nếu không.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    public CompletableFuture<Boolean> checkUserExistsByEmail(String email) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        userApiClient
                .existsByEmail(email)
                .enqueue(handleResponseCallback(future));
        return future;
    }

    /**
     * Cập nhật thông tin cơ bản của người dùng xác định bằng UID.
     *
     * @param uid mã định danh duy nhất của người dùng.
     * @param user đối tượng User chứa thông tin mới cần cập nhật.
     * @return CompletableFuture<Void> đại diện cho kết quả của thao tác bất đồng bộ.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    public CompletableFuture<Void> updateBasicUser(String uid, User user) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        user.setId(uid);
        userApiClient
                .updateUser(user)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (!response.isSuccessful()) {
                            handleErrorResponse(response, future);
                            return;
                        }

                        Uri avatarUri = user.getUri();
                        if (FileUtils.isHttpUri(avatarUri)) {
                            future.complete(null);
                            return;
                        }

                        mediaRepos
                                .uploadAvatar(user.getId(), avatarUri)
                                .thenAccept(unused -> {
                                    future.complete(null);
                                })
                                .exceptionally(e -> {
                                    future.completeExceptionally(e);
                                    return null;
                                });
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        future.completeExceptionally(t);
                        Log.e(getTag(), "Failed to execute API call", t);
                    }
                });
        return future;
    }

    /**
     * Kiểm tra sự tồn tại của người dùng thông qua số điện thoại.
     *
     * @param phoneNumber số điện thoại của người dùng.
     * @return CompletableFuture<Boolean> đại diện cho kết quả của thao tác bất đồng bộ,
     * true nếu người dùng tồn tại, false nếu không.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    public CompletableFuture<Boolean> existsByPhoneNumber(String phoneNumber) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        userApiClient
                .existsByPhoneNumber(phoneNumber)
                .enqueue(handleResponseCallback(future));
        return future;
    }

    /**
     * Lấy thông tin người dùng thông qua UID.
     *
     * @param uid mã định danh duy nhất của người dùng.
     * @return CompletableFuture<User> đại diện cho kết quả của thao tác bất đồng bộ,
     * trả về đối tượng User nếu tìm thấy.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    public CompletableFuture<User> getUserByUid(String uid) {
        CompletableFuture<User> future = new CompletableFuture<>();
        userApiClient
                .getUserByUid(uid)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (!response.isSuccessful()) {
                            handleErrorResponse(response, future);
                            return;
                        }

                        User user = response.body();
                        if (user == null) {
                            return;
                        }

                        mediaRepos.downloadAvatar(uid)
                                .thenAccept(uri -> {
                                    user.setUri(uri);
                                    future.complete(user);
                                })
                                .exceptionally(e -> {
                                    future.completeExceptionally(e);
                                    return null;
                                });
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        future.completeExceptionally(t);
                        Log.e(getTag(), "Failed to execute API call", t);
                    }
                });
        return future;
    }
}
