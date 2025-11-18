package com.example.user;

import com.google.gson.annotations.SerializedName;

/**
 * Enum đại diện cho giới tính của người dùng.
 *
 * Tác giả: Trần Văn An
 */
public enum EGender {
    @SerializedName("MALE")
    MALE("Male"),
    @SerializedName("FEMALE")
    FEMALE("Female"),
    @SerializedName("OTHER")
    OTHER("Other");

    String display;

    EGender(String display) {
        this.display = display;
    }

    /**
     * Lấy giá trị hiển thị của giới tính.
     *
     * @return Giá trị hiển thị
     *
     * Tác giả: Trần Văn An
     */
    public String getDisplay() {
        return display;
    }

    /**
     * Lấy tất cả giá trị hiển thị của giới tính.
     *
     * @return Mảng chứa tất cả giá trị hiển thị
     *
     * Tác giả: Trần Văn An
     */
    public static String[] getAllDisplays() {
        EGender[] values = values();
        String[] displays = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            displays[i] = values[i].display;
        }
        return displays;
    }

    /**
     * Lấy chỉ số của giới tính hiện tại trong enum.
     *
     * @param currentGender Giới tính hiện tại
     * @return Chỉ số của giới tính trong enum
     *
     * Tác giả: Trần Văn An
     */
    public static int getCurrentIndex(EGender currentGender) {
        for (int i = 0; i < values().length; i++) {
            if (values()[i] == currentGender) {
                return i;
            }
        }
        return -1;
    }
}
