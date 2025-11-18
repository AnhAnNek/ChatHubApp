package com.example.infrastructure;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.MutableLiveData;

import com.example.R;
import com.example.friend.FriendRequest;
import com.example.friend.profileviewer.EFriendshipStatus;

import org.jetbrains.annotations.Nullable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * Class Utils chứa các phương thức tiện ích được sử dụng trong ứng dụng.
 */
public class Utils {
    // Các khóa cho SharedPreferences
    public static final String KEY_PREFERENCE_NAME = "chatAppPreference";

    // Số chữ số của OTP
    public static final int PIN_DIGIT_COUNT = 6;


    // Thời gian time-out của OTP (đơn vị: giây)
    public static long OTP_TIME_OUT_SECONDS = 60L;

    // Extra keys dùng trong Intent
    public static final String EXTRA_SELECTED_USER_ID = "selectedUserId";
    public static final String EXTRA_SELECTED_FRIEND_REQUEST_ID = "selectedFriendRequestId";
    public static final String EXTRA_PHONE_NUMBER = "phoneNumber";

    // Tag cho Log
    private static final String TAG = Utils.class.getSimpleName();

    // Định dạng ngày tháng
    private static final String DATE_PATTERN = "dd/MM/yyyy";

    // Số ngày trong một tháng
    private static final int DAYS_IN_MONTH = 30;

    public static final String KEY_SENDER_ID = "senderId";
    public static final String KEY_RECIPIENT_ID = "recipientId";

    /**
     * Phương thức setStatusBarGradiant() để thiết lập màu gradient cho thanh trạng thái.
     * Chỉ hoạt động từ Android Lollipop trở lên.
     *
     * @param activity Activity hiện tại
     *
     * Tác giả: Trần Văn An
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarGradiant(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            Drawable bg = ResourcesCompat.getDrawable(activity.getResources(), R.drawable.gradient_color_primary, activity.getTheme());
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setNavigationBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(bg);
        }
    }

    /**
     * Phương thức dateToString() chuyển đổi đối tượng Date thành chuỗi ngày tháng.
     *
     * @param date Đối tượng Date cần chuyển đổi
     * @return Chuỗi ngày tháng
     *
     * Tác giả: Trần Văn An
     */
    public static String dateToString(Date date) {
        if (date == null) {
            return "";
        }
        DateFormat df = new SimpleDateFormat(DATE_PATTERN);
        return df.format(date);
    }

    /**
     * Phương thức stringToDate() chuyển đổi chuỗi ngày tháng thành đối tượng Date.
     *
     * @param dateStr Chuỗi ngày tháng cần chuyển đổi
     * @return Đối tượng Date
     *
     * Tác giả: Trần Văn An
     */
    public static Date stringToDate(String dateStr) {
        DateFormat df = new SimpleDateFormat(DATE_PATTERN);
        try {
            return df.parse(dateStr);
        } catch (ParseException e) {
            Log.e(TAG, "ERROR: " + e);
            return null;
        }
    }

    /**
     * Phương thức getFullPhoneNumber() kết hợp mã quốc gia và số điện thoại địa phương thành số điện thoại đầy đủ.
     *
     * @param countryCode Mã quốc gia
     * @param localNumber Số điện thoại địa phương
     * @return Số điện thoại đầy đủ
     *
     * Tác giả: Trần Văn An
     */
    public static String getFullPhoneNumber(@Nullable String countryCode,
                                            @Nullable String localNumber) {
        if (countryCode != null && localNumber != null) {
            return countryCode.concat(localNumber);
        }
        return null;
    }

    /**
     * Phương thức calculateTimeAgo() tính toán thời gian đã trôi qua từ một ngày trước đến hiện tại.
     *
     * @param pastDate Ngày trong quá khứ cần tính toán
     * @return Chuỗi biểu diễn thời gian trôi qua
     *
     * Ví dụ: Giả sử ngày hiện tại là ngày 12 tháng 5 năm 2024
     * Đầu vào: Date pastDate = new Date(2023, 4, 12); // Ngày 12 tháng 5 năm 2023
     * Kết quả: "1y".
     *
     * Tác giả: Trần Văn An
     */
    public static String calculateTimeAgo(Date pastDate) {
        Date currentDate = new Date();

        long timeDifference = currentDate.getTime() - pastDate.getTime();

        long seconds = timeDifference / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        long weeks = days / 7;
        long months = days / DAYS_IN_MONTH;
        long years = days / 365;

        if (years > 0) {
            return years + "y";
        } else if (weeks > 0) {
            return weeks + "w";
        }
        if (months > 0) {
            return months + "mo";
        }
        if (days > 0) {
            return days + "d";
        } else if (hours > 0) {
            return hours + "h";
        } else if (minutes > 0) {
            return minutes + "m";
        } else {
            return "Just now";
        }
    }

    /**
     * Phương thức isEmpty() kiểm tra xem một chuỗi có rỗng hay không.
     *
     * @param text Chuỗi cần kiểm tra
     * @return true nếu chuỗi rỗng, false nếu không rỗng
     *
     * @author Trần Văn An
     */
    public static boolean isEmpty(String text) {
        return text == null || text.isEmpty();
    }

    /**
     * Phương thức isValidOtp() kiểm tra tính hợp lệ của một OTP.
     *
     * @param text Chuỗi OTP cần kiểm tra
     * @return true nếu chuỗi OTP hợp lệ, false nếu không hợp lệ
     *
     * @author Trần Văn An
     */
    public static boolean isValidOtp(String text) {
        return text != null && text.length() == PIN_DIGIT_COUNT;
    }

    /**
     * Phương thức convertFriendRequestStatusToFriendshipStatus()
     * chuyển đổi trạng thái yêu cầu kết bạn thành trạng thái tương ứng của mối quan hệ trong ứng dụng.
     *
     * @param curUserId Id của người dùng hiện tại
     * @param senderId Id của người gửi yêu cầu kết bạn
     * @param status Trạng thái của yêu cầu kết bạn
     * @return Trạng thái tương ứng của mối quan hệ
     *
     * @author Trần Văn An
     */
    public static EFriendshipStatus convertFriendRequestStatusToFriendshipStatus(
            String curUserId,
            String senderId,
            FriendRequest.EStatus status) {

        switch (status) {
            case PENDING:
                if (Objects.equals(curUserId, senderId)) {
                    return EFriendshipStatus.SENT_REQUEST;
                } else {
                    return EFriendshipStatus.RECEIVED_REQUEST;
                }
            case ACCEPTED:
                return EFriendshipStatus.FRIEND;
            case REJECTED:
                return EFriendshipStatus.NOT_FRIEND;
            default:
                return EFriendshipStatus.NOT_FOUND;
        }
    }

    /**
     * Phương thức convertToString() chuyển đổi MutableLiveData thành chuỗi.
     *
     * @param stringMutableLiveData MutableLiveData chứa chuỗi cần chuyển đổi
     * @return Chuỗi kết quả sau khi chuyển đổi, hoặc chuỗi rỗng nếu MutableLiveData là null
     *
     * @author Trần Văn An
     */
    public static String convertToString(MutableLiveData<String> stringMutableLiveData) {
        return stringMutableLiveData != null ? stringMutableLiveData.getValue() : "";
    }
}
