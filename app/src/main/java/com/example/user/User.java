package com.example.user;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * Lớp đại diện cho người dùng.
 *
 * Tác giả: Trần Văn An
 */
public class User implements Serializable {
    private String id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private EGender gender;
    private Date birthday;

    @SerializedName("online")
    private boolean isOnline;

    private transient Uri uri;

    /**
     * Khởi tạo người dùng với các giá trị mặc định.
     *
     * Tác giả: Trần Văn An
     */
    public User() {
        this.fullName = "Default name";
        this.email = "";
        phoneNumber = "";
        gender = EGender.MALE;
        birthday = new Date(90, 1, 1);
        isOnline = false;
    }

    /**
     * Khởi tạo người dùng với ID và email.
     *
     * @param id ID của người dùng
     * @param email Email của người dùng
     *
     * Tác giả: Trần Văn An
     */
    public User(String id, String email) {
        this();
        this.id = id;
        this.email = email;
    }

    /**
     * Khởi tạo người dùng với ID, tên đầy đủ và email.
     *
     * @param id ID của người dùng
     * @param fullName Tên đầy đủ của người dùng
     * @param email Email của người dùng
     *
     * Tác giả: Trần Văn An
     */
    public User(String id, String fullName, String email) {
        this();
        this.id = id;
        this.fullName = fullName;
        this.email = email;
    }

    /**
     * Khởi tạo người dùng với tất cả thông tin.
     *
     * @param id ID của người dùng
     * @param fullName Tên đầy đủ của người dùng
     * @param email Email của người dùng
     * @param phoneNumber Số điện thoại của người dùng
     * @param gender Giới tính của người dùng
     * @param birthday Ngày sinh của người dùng
     * @param isOnline Trạng thái trực tuyến của người dùng
     *
     * Tác giả: Trần Văn An
     */
    public User(String id, String fullName, String email, String phoneNumber, EGender gender,
                Date birthday, boolean isOnline) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.birthday = birthday;
        this.isOnline = isOnline;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public EGender getGender() {
        return gender;
    }

    public void setGender(EGender gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}