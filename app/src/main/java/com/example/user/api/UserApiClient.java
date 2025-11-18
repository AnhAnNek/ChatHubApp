package com.example.user.api;

import com.example.user.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Interface UserApiClient chứa các phương thức để tương tác với API liên quan đến người dùng.
 *
 * Tác giả: Trần Văn An
 */
public interface UserApiClient {

    /**
     * Thêm người dùng mới.
     *
     * @param user thông tin người dùng mới.
     * @return Call<String> đại diện cho kết quả của thao tác bất đồng bộ.
     *
     * Tác giả: Trần Văn An
     */
    @POST("/api/v1/users/add-user")
    Call<String> addUser(@Body User user);

    /**
     * Cập nhật thông tin người dùng.
     *
     * @param user thông tin người dùng cần cập nhật.
     * @return Call<String> đại diện cho kết quả của thao tác bất đồng bộ.
     *
     * Tác giả: Trần Văn An
     */
    @PUT("/api/v1/users/update-user")
    Call<String> updateUser(@Body User user);

    /**
     * Cập nhật số điện thoại của người dùng.
     *
     * @param uid ID của người dùng cần cập nhật.
     * @param phoneNumber số điện thoại mới.
     * @return Call<String> đại diện cho kết quả của thao tác bất đồng bộ.
     *
     * Tác giả: Trần Văn An
     */
    @PUT("/api/v1/users/update-phone-number")
    Call<String> updatePhoneNumber(
            @Query("uid") String uid,
            @Query("phoneNumber") String phoneNumber
    );

    /**
     * Kiểm tra sự tồn tại của một người dùng bằng địa chỉ email.
     *
     * @param email địa chỉ email cần kiểm tra.
     * @return Call<Boolean> đại diện cho kết quả của thao tác bất đồng bộ.
     *
     * Tác giả: Trần Văn An
     */
    @GET("/api/v1/users/exists-by-email")
    Call<Boolean> existsByEmail(@Query("email") String email);

    /**
     * Kiểm tra sự tồn tại của một người dùng bằng số điện thoại.
     *
     * @param phoneNumber số điện thoại cần kiểm tra.
     * @return Call<Boolean> đại diện cho kết quả của thao tác bất đồng bộ.
     *
     * Tác giả: Trần Văn An
     */
    @GET("/api/v1/users/exists-by-phone-number")
    Call<Boolean> existsByPhoneNumber(@Query("phoneNumber") String phoneNumber);

    /**
     * Lấy thông tin người dùng dựa trên UID.
     *
     * @param uid ID của người dùng cần lấy thông tin.
     * @return Call<User> đại diện cho kết quả của thao tác bất đồng bộ.
     *
     * Tác giả: Trần Văn An
     */
    @GET("/api/v1/users/get-user-by-uid/{uid}")
    Call<User> getUserByUid(@Path("uid") String uid);
}
