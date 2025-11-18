package com.example.infrastructure;

import android.util.Log;

import com.example.infrastructure.api.ErrorResponse;
import com.example.infrastructure.api.RestApiClient;

import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 1. Class BaseRepos là một abstract class cung cấp các phương thức cơ bản
 * để xử lý các cuộc gọi API thông qua Retrofit.
 * 2. Các class repository khác trong ứng dụng có thể kế thừa từ BaseRepos này
 * để sử dụng các phương thức đã được định nghĩa sẵn.
 *
 * Tác giả: Trần Văn An
 */
public abstract class BaseRepos {
    /**
     * Phương thức abstract getTag() cần được cài đặt trong các class con để cung cấp tag cho việc log.
     *
     * @return Tag được sử dụng để log
     *
     * Tác giả: Trần Văn An
     */
    protected abstract String getTag();

    /**
     * Phương thức handleResponseCallback() trả về một Callback để xử lý response từ Retrofit và
     * CompletableFuture cho việc xử lý kết quả.
     *
     * @param future CompletableFuture để xử lý kết quả
     * @return Callback để xử lý response
     *
     * Tác giả: Trần Văn An
     */
    protected <T> Callback<T> handleResponseCallback(CompletableFuture<T> future) {
        return new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (!response.isSuccessful()) {
                    handleErrorResponse(response, future);
                    return;
                }

                T responseData = response.body();
                future.complete(responseData);
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                future.completeExceptionally(t);
                Log.e(getTag(), "Failed to execute API call", t);
            }
        };
    }

    /**
     * Phương thức handleResponseCallbackWithoutData() trả về một Callback để xử lý response
     * từ Retrofit mà không cần dữ liệu trả về, và CompletableFuture cho việc xử lý kết quả.
     *
     * @param future CompletableFuture để xử lý kết quả
     * @return Callback để xử lý response
     *
     * Tác giả: Trần Văn An
     */
    protected <T> Callback<T> handleResponseCallbackWithoutData(CompletableFuture<Void> future) {
        return new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (!response.isSuccessful()) {
                    handleErrorResponse(response, future);
                    return;
                }

                future.complete(null);
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                future.completeExceptionally(t);
                Log.e(getTag(), "Failed to execute API call", t);
            }
        };
    }

    /**
     * Phương thức handleErrorResponse() xử lý response khi gặp lỗi.
     * Nó chuyển đổi response lỗi từ Retrofit thành một đối tượng Exception
     * và hoàn tất CompletableFuture.
     *
     * @param response Response từ Retrofit chứa lỗi
     * @param future CompletableFuture để xử lý kết quả
     *
     * Tác giả: Trần Văn An
     */
    protected void handleErrorResponse(Response<?> response, CompletableFuture<?> future) {
        ErrorResponse errorResponse = RestApiClient.parseError(response);
        String errorMsg = String.format("HttpStatus: %s \nTime: %s \nMessage: %s",
                        errorResponse.getStatus(),
                        errorResponse.getTimestamp(),
                        errorResponse.getMessage());
        future.completeExceptionally(new RuntimeException(errorMsg));
    }
}
