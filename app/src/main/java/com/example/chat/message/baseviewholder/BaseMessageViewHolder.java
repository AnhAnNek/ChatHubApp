package com.example.chat.message.baseviewholder;

import android.util.Log;
import android.widget.TextView;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat.message.Message;

/**
 * Là lớp cha BaseMessageViewHolder cho ViewHolder của tin nhắn.
 *
 * Lớp này định nghĩa các phương thức chung cho việc hiển thị tin nhắn.
 * Các lớp kế thừa từ `BaseMessageViewHolder` cần implement các phương thức trừu tượng
 * để tuỳ chỉnh hiển thị cho từng kiểu tin nhắn cụ thể.
 * Tác giả: Văn Hoàng
 */
public abstract class BaseMessageViewHolder extends RecyclerView.ViewHolder {
    private final String TAG = BaseMessageViewHolder.class.getSimpleName();
    protected final ViewDataBinding binding;

    /**
     * Khởi tạo ViewHolder.
     *
     * @param binding ViewDataBinding của item.
     * Tác giả: Văn Hoàng
     */
    protected BaseMessageViewHolder(ViewDataBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    /**
     * Thiết lập dữ liệu cho ViewHolder.
     *
     * Hàm này gọi các hàm khác để thiết lập dữ liệu chung và dữ liệu riêng cho từng kiểu tin nhắn.
     * Cuối cùng thực hiện binding dữ liệu lên giao diện.
     *
     * @param message Đối tượng Message chứa dữ liệu tin nhắn.
     * Tác giả: Văn Hoàng
     */
    public void setData(Message message) {
        try {
            bindCommonData(message);
            bindSpecificData(message);
            binding.executePendingBindings();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * Thiết lập dữ liệu chung cho tất cả các kiểu tin nhắn.
     *
     * Hàm này thiết lập nội dung tin nhắn (`message.getMessage()`) và thời gian gửi (`message.getSendingTime()`)
     * cho các TextView tương ứng.
     *
     * @param message Đối tượng Message chứa dữ liệu tin nhắn.
     * Tác giả: Văn Hoàng
     */
    private void bindCommonData(Message message) {
        getTextMessage().setText(message.getMessage());
        getTextDateTime().setText(message.getSendingTime());
    }

    /**
     * Phương thức trừu tượng để các lớp kế thừa implement logic thiết lập dữ liệu riêng cho từng kiểu tin nhắn.
     *
     * @param message Đối tượng Message chứa dữ liệu tin nhắn.
     * Tác giả: Văn Hoàng
     */
    protected abstract void bindSpecificData(Message message);

    /**
     * Phương thức trừu tượng để các lớp kế thừa trả về TextView hiển thị nội dung tin nhắn.
     *
     * @return TextView hiển thị nội dung tin nhắn.
     * Tác giả: Văn Hoàng
     */
    protected abstract TextView getTextMessage();

    /**
     * Phương thức trừu tượng để các lớp kế thừa trả về TextView hiển thị thời gian gửi tin nhắn.
     *
     * @return TextView hiển thị thời gian gửi tin nhắn.
     * Tác giả: Văn Hoàng
     */
    protected abstract TextView getTextDateTime();
}

