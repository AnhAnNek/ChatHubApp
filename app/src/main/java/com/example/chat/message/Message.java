package com.example.chat.message;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.chat.Utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * Lớp Message đại diện cho một tin nhắn trong ứng dụng.
 *
 * Các thuộc tính bao gồm thông tin về người gửi, người nhận, nội dung, thời gian gửi và loại tin nhắn.
 * Cung cấp phương thức để chuyển đổi giữa đối tượng Message và MessageDTO.
 * Tác giả: Văn Hoàng
 */
public class Message implements Parcelable {
    // Khai báo các thuộc tính của tin nhắn
    private String senderId, senderName, message;
    private String senderImage;
    private EType type;
    private String sendingTime;
    private EVisible visibility;
    private LocalDateTime dateObject;
    private String recipientId;
    private String conversationId;

    public Message() {
        this.senderId = "";
        this.senderImage = "";
        this.message = "";
        this.type = EType.TEXT;
        this.sendingTime = LocalDateTime.now().toString();
        this.recipientId = "";
        this.visibility = EVisible.HIDDEN;
        this.dateObject = LocalDateTime.now();
    }

    public Message(String senderId, String recipientId, String message, EVisible visibility, EType type, String sendingTime) {
        this.senderId = senderId;
        this.message = message;
        this.type = type;
        try {
            this.sendingTime = formatTime(sendingTime);
        }catch (Exception e) {
            this.sendingTime = sendingTime;
        }
        this.visibility = visibility;
        this.recipientId = recipientId;
    }

    public Message(String senderId,
                   String senderName,
                   String senderImage,
                   String message,
                   EType type,
                   String sendingTime,
                   EVisible visibility,
                   String recipientId) {
        this.senderId = senderId;
        this.senderName = senderName;
        this.senderImage = senderImage;
        this.message = message;
        this.type = type;
        try {
            this.sendingTime = formatTime(sendingTime);
        }catch (Exception e) {
            this.sendingTime = sendingTime;
        }
        this.visibility = visibility;
        this.recipientId = recipientId;
    }


    /**
     * Phương thức constructor được sử dụng để tạo một đối tượng Message từ đối tượng Parcel.
     *
     * @param in Đối tượng Parcel chứa dữ liệu để tạo Message.
     * Tác giả: Văn Hoàng
     */
    protected Message(Parcel in) {
        senderId = in.readString();
        recipientId = in.readString();
        message = in.readString();
        visibility = EVisible.valueOf(in.readString());
        type = EType.valueOf(in.readString());
        sendingTime = in.readString();
    }

    /**
     * Creator được sử dụng để tạo một đối tượng Message từ đối tượng Parcel và cũng để tạo một mảng Message.
     * Khi implement Parcelable, chúng ta cần cung cấp một Creator để hỗ trợ quá trình đọc và ghi dữ liệu của đối tượng từ Parcel.
     * Khi một đối tượng Message được ghi vào Parcel (ví dụ: khi chúng ta truyền đối tượng qua Intent),
     * hệ thống sẽ sử dụng Creator để tạo lại đối tượng Message ban đầu từ Parcel.
     *
     * @param in Đối tượng Parcel chứa dữ liệu để tạo Message.
     * @return Một đối tượng Message được tạo từ Parcel.
     * Tác giả: Văn Hoàng
     */
    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    public LocalDateTime getDateObject() {
        return dateObject;
    }

    public void setDateObject(LocalDateTime dateObject) {
        this.dateObject = dateObject;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderImage() {
        return senderImage;
    }

    public void setSenderImage(String senderImage) {
        this.senderImage = senderImage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSendingTime() {
        return sendingTime;
    }

    public void setSendingTime(String sendingTime) {
        this.sendingTime = sendingTime;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public EType getType() {
        return type;
    }

    public void setType(EType type) {
        this.type = type;
    }

    public EVisible getVisibility() {
        return visibility;
    }

    public void setVisibility(EVisible visibility) {
        this.visibility = visibility;
    }

    /**
     * Phương thức chuyển đổi đối tượng MessageDTO thành đối tượng Message.
     *
     * @param dto Đối tượng MessageDTO cần chuyển đổi.
     * Tác giả: Văn Hoàng
     */
    public void convertDTOToEntity(MessageDTO dto) {
        senderId = dto.getSenderId();
        recipientId = dto.getRecipientId();
        message = dto.getMessage();
        visibility= dto.getVisibility();
        type = dto.getType();
        sendingTime =  formatTime(dto.getSendingTime());
        dateObject = Utils.getLocalDateTime(sendingTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**

     Ghi dữ liệu của đối tượng Message vào Parcel.
     @param parcel Đối tượng Parcel để ghi dữ liệu.
     @param i Một cờ chỉ định.
     Tác giả: Văn Hoàng
     */
    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(senderId);
        parcel.writeString(recipientId);
        parcel.writeString(message);
        parcel.writeString(visibility.toString());
        parcel.writeString(type.toString());
        parcel.writeString(sendingTime);
    }

    /**
     * Phương thức để định dạng thời gian gửi tin nhắn thành chuỗi có định dạng "dd-MM HH:mm".
     *
     * @param sendingTime Thời gian gửi tin nhắn dưới dạng chuỗi.
     * @return Chuỗi thời gian đã được định dạng theo "dd-MM HH:mm".
     * Tác giả: Văn Hoàng
     */
    private String formatTime(String sendingTime) {
        LocalDateTime originalDateTime = LocalDateTime.parse(sendingTime);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM HH:mm");

        String formattedDateTime = originalDateTime.format(formatter);
        return formattedDateTime;
    }

    public enum EType {
        TEXT, IMAGE, VIDEO;
    }

    public enum EVisible {
        ACTIVE, DELETE, HIDDEN;
    }
}
