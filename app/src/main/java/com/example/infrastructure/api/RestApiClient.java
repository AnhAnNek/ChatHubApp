package com.example.infrastructure.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Class RestApiClient quản lý việc khởi tạo và cung cấp instance của Retrofit
 * để thực hiện các cuộc gọi API và xử lý lỗi của response.
 *
 * Tác giả: Trần Văn An
 */
public class RestApiClient {
    // Định dạng ngày tháng sử dụng trong JSON
    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    // URL cơ sở cho API
    private static final String BASE_URL = "https://chat-hub-mobile-api-v1-d2ad8c55d883.herokuapp.com";

    // Instance singleton của Retrofit
    private static Retrofit ins;

    /**
     * Trả về instance singleton của Retrofit.
     * Nếu instance là null, nó sẽ được khởi tạo với các cấu hình cần thiết.
     *
     * @return instance singleton của Retrofit
     *
     * Tác giả: Trần Văn An
     */
    // synchronized đảm bảo rằng chỉ một luồng có thể truy cập phương thức này tại một thời điểm
    public synchronized static Retrofit getIns() {
        if (ins == null) {
            new RestApiClient();
        }
        return ins;
    }

    /**
     * Private constructor để ngăn chặn việc khởi tạo từ các class khác.
     * Khởi tạo Retrofit với các cấu hình cần thiết.
     *
     * Tác giả: Trần Văn An
     */
    private RestApiClient() {
        // Khởi tạo Gson với cấu hình tùy chỉnh
        Gson gson = new GsonBuilder()
                .setLenient() // Cho phép xử lý JSON không đúng chuẩn
                .setDateFormat(DATE_FORMAT) // Định dạng ngày tháng
                .registerTypeAdapter(Date.class, new DateDeserializer()) // Đăng ký bộ chuyển đổi Date
                .create();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.level(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(interceptor);

        // Khởi tạo Retrofit với base URL, OkHttpClient, và các converter cần thiết
        ins = new Retrofit.Builder()
                .baseUrl(BASE_URL) // Thiết lập base URL cho API
                .client(builder.build()) // Thiết lập OkHttpClient cho Retrofit
                .addConverterFactory(ScalarsConverterFactory.create()) // Thêm converter cho kiểu Scalars
                .addConverterFactory(GsonConverterFactory.create(gson)) // Thêm converter cho Gson
                .build();
    }

    /**
     * Phân tích lỗi từ response và chuyển đổi nó thành đối tượng ErrorResponse.
     *
     * @param response response từ Retrofit chứa lỗi
     * @return đối tượng ErrorResponse chứa thông tin lỗi
     *
     * Tác giả: Trần Văn An
     */
    public static ErrorResponse parseError(Response<?> response) {
        Converter<ResponseBody, Object> converter =
                getIns().responseBodyConverter(ErrorResponse.class, new Annotation[0]);

        ErrorResponse errorResponse;
        try {
            errorResponse = (ErrorResponse) converter.convert(response.errorBody());
        } catch (IOException e) {
            return new ErrorResponse();
        }

        return errorResponse;
    }
}
