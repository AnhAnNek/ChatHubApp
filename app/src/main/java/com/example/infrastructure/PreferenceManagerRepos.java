package com.example.infrastructure;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Class PreferenceManagerRepos quản lý lưu trữ và truy xuất các giá trị trong SharedPreferences.
 *
 * Tác giả: Trần Văn An
 */
public class PreferenceManagerRepos {
    private final SharedPreferences sharedPreferences;

    /**
     * Constructor khởi tạo PreferenceManagerRepos.
     *
     * @param context Context của ứng dụng
     *
     * Tác giả: Trần Văn An
     */
    public PreferenceManagerRepos(Context context) {
        sharedPreferences = context.getSharedPreferences(Utils.KEY_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Phương thức putBoolean() lưu trữ một giá trị boolean vào SharedPreferences.
     *
     * @param key   Key dùng để lưu trữ giá trị
     * @param value Giá trị boolean cần lưu trữ
     *
     * Tác giả: Trần Văn An
     */
    public void putBoolean(String key, Boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    /**
     * Phương thức getBoolean() truy xuất một giá trị boolean từ SharedPreferences.
     *
     * @param key Key dùng để truy xuất giá trị
     * @return Giá trị boolean được truy xuất, mặc định là false nếu không tìm thấy
     *
     * Tác giả: Trần Văn An
     */
    public Boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    /**
     * Phương thức putString() lưu trữ một chuỗi vào SharedPreferences.
     *
     * @param key   Key dùng để lưu trữ chuỗi
     * @param value Chuỗi cần lưu trữ
     *
     * Tác giả: Trần Văn An
     */
    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * Phương thức getString() truy xuất một chuỗi từ SharedPreferences.
     *
     * @param key Key dùng để truy xuất chuỗi
     * @return Chuỗi được truy xuất, null nếu không tìm thấy
     *
     * Tác giả: Trần Văn An
     */
    public String getString(String key) {
        return sharedPreferences.getString(key, null);
    }
}
