package com.example.common.util;

import android.net.Uri;

/**
 * FileUtils cung cấp các phương thức tiện ích để làm việc với tệp tin.
 *
 * Tác giả: Trần Văn An
 */
public class FileUtils {

    private static final String SCHEME_HTTP = "http://";
    private static final String SCHEME_HTTPS = "https://";

    /**
     * Kiểm tra xem URI có sử dụng scheme HTTP hoặc HTTPS không.
     *
     * @param uri URI cần kiểm tra
     * @return true nếu URI có scheme là HTTP hoặc HTTPS, ngược lại trả về false
     */
    public static boolean isHttpUri(Uri uri) {
        String uriString = uri.toString();
        return uriString.startsWith(SCHEME_HTTP) || uriString.startsWith(SCHEME_HTTPS);
    }
}
