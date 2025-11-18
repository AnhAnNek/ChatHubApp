package com.example.chat;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.R;
import com.example.chat.listener.ImageListener;
import com.example.chat.message.Message;
import com.example.chat.message.baseviewholder.BaseImageViewHolder;
import com.example.chat.message.baseviewholder.BaseMessageViewHolder;
import com.example.databinding.ItemContainerReceivedImageBinding;
import com.example.databinding.ItemContainerReceivedMessageBinding;
import com.example.databinding.ItemContainerSentImageBinding;
import com.example.databinding.ItemContainerSentMessageBinding;

import java.util.List;

/**
 * Adapter cho RecyclerView hiển thị các tin nhắn trong cuộc trò chuyện.
 *
 * @param messages Danh sách các tin nhắn.
 * @param senderId ID của người gửi tin nhắn.
 * @param recipientImage URI của hình ảnh của người nhận tin.
 * @param listener Lăng nghe sự kiện cho việc xem hình ảnh.

 * Tác giả: Văn Hoàng
 */
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private String TAG = ChatAdapter.class.getSimpleName();

    private List<Message> messages;
    private final String senderId;
    private final Uri recipientImage;
    private final ImageListener listener;

    /**
     * Các hằng số xác định các loại view khác nhau cho các tin nhắn trong adapter RecyclerView.
     * Mỗi loại view tương ứng với một bố cục khác nhau để hiển thị tin nhắn.
     * - VIEW_MESSAGE_SENT: Chỉ ra một tin nhắn được gửi bởi người dùng hiện tại.
     * - VIEW_IMAGE_SENT: Chỉ ra một hình ảnh được gửi bởi người dùng hiện tại.
     * - VIEW_MESSAGE_RECEIVED: Chỉ ra một tin nhắn nhận được từ người dùng khác.
     * - VIEW_IMAGE_RECEIVED: Chỉ ra một hình ảnh nhận được từ người dùng khác.

     * Tác giả: Văn Hoàng
     */
    public static final int VIEW_MESSAGE_SENT = 1;
    public static final int VIEW_IMAGE_SENT = 2;
    public static final int VIEW_MESSAGE_RECEIVED = 3;
    public static final int VIEW_IMAGE_RECEIVED = 4;

    public ChatAdapter(List<Message> messages, String senderId, Uri recipientImage, ImageListener listener) {
        this.messages = messages;
        this.senderId = senderId;
        this.recipientImage = recipientImage;
        this.listener = listener;
    }

    /**
     * Cập nhật danh sách tin nhắn mới và thông báo cho RecyclerView biết rằng dữ liệu đã thay đổi.
     *
     * @param messages Danh sách tin nhắn mới cần cập nhật.

     * Tác giả: Văn Hoàng
     */
    public void setMessages(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    /**
     * Tạo và trả về một ViewHolder dựa trên loại view được chỉ định.
     *
     * @param parent   ViewGroup cha của ViewHolder được tạo ra.
     * @param viewType Loại view của ViewHolder được tạo ra.
     * @return ViewHolder mới được tạo ra.

     * Tác giả: Văn Hoàng
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case VIEW_MESSAGE_RECEIVED: {
                ItemContainerReceivedMessageBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_container_received_message, parent, false);
                return new ReceivedMessageViewHolder(binding);
            }
            case VIEW_IMAGE_RECEIVED: {
                ItemContainerReceivedImageBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_container_received_image, parent, false);
                return new ReceivedImageViewHolder(binding);
            }
            case VIEW_MESSAGE_SENT: {
                ItemContainerSentMessageBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_container_sent_message, parent, false);
                return new SendMessageViewHolder(binding);
            }
            case VIEW_IMAGE_SENT: {
                ItemContainerSentImageBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_container_sent_image, parent, false);
                return new SendImageViewHolder(binding);
            }
        }
        return null;
    }

    /**
     * Liên kết dữ liệu của một message tương ứng với vị trí position của message trong messages với ViewHolder.
     *
     * @param holder   ViewHolder để cập nhật dữ liệu.
     * @param position Vị trí của mục trong danh sách dữ liệu.

     * Tác giả: Văn Hoàng
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);

        switch (viewType) {
            case VIEW_MESSAGE_RECEIVED: {
                ((ReceivedMessageViewHolder) holder).setData(messages.get(position));
                break;
            }
            case VIEW_IMAGE_RECEIVED: {
                ((ReceivedImageViewHolder) holder).setData(messages.get(position), position);
                break;
            }
            case VIEW_MESSAGE_SENT: {
                ((SendMessageViewHolder) holder).setData(messages.get(position));
                break;
            }
            case VIEW_IMAGE_SENT: {
                ((SendImageViewHolder) holder).setData(messages.get(position), position);
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    /**
     * Xác định loại view của một mục trong danh sách dựa trên loại tin nhắn và người gửi của tin nhắn đó.
     * Nếu tin nhắn được gửi bởi người dùng hiện tại, trả về loại view dựa trên loại tin nhắn (văn bản hoặc hình ảnh) của tin nhắn.
     * Nếu tin nhắn được nhận từ người khác, cũng trả về loại view dựa trên loại tin nhắn của tin nhắn đó.
     *
     * @param position Vị trí của mục trong danh sách dữ liệu.
     * @return Mã loại view: VIEW_MESSAGE_SENT nếu là tin nhắn được gửi đi, VIEW_IMAGE_SENT nếu là hình ảnh được gửi đi,
     * VIEW_MESSAGE_RECEIVED nếu là tin nhắn được nhận, VIEW_IMAGE_RECEIVED nếu là hình ảnh được nhận.

     * Tác giả: Văn Hoàng
     */
    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);

        if(message.getSenderId().equals(senderId)) {
            return message.getType().equals(Message.EType.TEXT) ? VIEW_MESSAGE_SENT : VIEW_IMAGE_SENT;
        } else {
            return message.getType().equals(Message.EType.TEXT) ? VIEW_MESSAGE_RECEIVED : VIEW_IMAGE_RECEIVED;
        }
    }

    /**
     * ViewHolder cho các tin nhắn được gửi, được kế thừa class BaseMessageViewHolder để xử lí dữ liệu.
     * Cung cấp cách liên kết dữ liệu cụ thể cho các tin nhắn được gửi và thiết lập các view cho dữ liệu đó.

     * Tác giả: Văn Hoàng
     */
    public class SendMessageViewHolder extends BaseMessageViewHolder {
        private final ItemContainerSentMessageBinding binding;

        SendMessageViewHolder(ItemContainerSentMessageBinding binding) {
            super(binding);
            this.binding = binding;
        }

        @Override
        protected void bindSpecificData(Message message) {
            // No specific data binding needed for sent messages
        }

        @Override
        protected TextView getTextMessage() {
            return binding.textMessage;
        }

        @Override
        protected TextView getTextDateTime() {
            return binding.textDateTime;
        }
    }

    /**
     * ViewHolder cho các tin nhắn nhận được, được kế thừa class BaseMessageViewHolder để xử lí dữ liệu.
     * Cung cấp cách liên kết dữ liệu cụ thể cho các tin nhắn nhận được và thiết lập các view cho dữ liệu đó.

     * Tác giả: Văn Hoàng
     */
    public class ReceivedMessageViewHolder extends BaseMessageViewHolder {
        private final ItemContainerReceivedMessageBinding binding;

        ReceivedMessageViewHolder(ItemContainerReceivedMessageBinding binding) {
            super(binding);
            this.binding = binding;
        }

        /**
         * Hiển thị ảnh của người gửi thông qua recipientImage.

         * Tác giả: Văn Hoàng
         */
        @Override
        protected void bindSpecificData(Message message) {
            loadProfileImage(recipientImage);
        }

        /**
         * Sử dung Glide để hiển thị ảnh cá nhân của người gửi.

         * Tác giả: Văn Hoàng
         */
        private void loadProfileImage(Uri recipientImage) {
            Glide.with(binding.getRoot())
                    .load(recipientImage)
                    .placeholder(R.drawable.ic_image)
                    .error(R.drawable.ic_image)
                    .into(binding.imageProfile);
        }

        @Override
        protected TextView getTextMessage() {
            return binding.textMessage;
        }

        @Override
        protected TextView getTextDateTime() {
            return binding.textDateTime;
        }
    }

    /**
     * ViewHolder cho các hình ảnh nhận được trong tin nhắn. Được kế thừa class BaseImageViewHolder để xử lí ảnh.
     * Cung cấp cách liên kết dữ liệu cụ thể cho các hình ảnh nhận được và thiết lập các view cho dữ liệu đó.

     * Tác giả: Văn Hoàng
     */
    public class ReceivedImageViewHolder extends BaseImageViewHolder {
        private final ItemContainerReceivedImageBinding binding;

        ReceivedImageViewHolder(ItemContainerReceivedImageBinding binding) {
            super(binding, listener);
            this.binding = binding;
        }

        /**
         * Hiển thị ảnh của người gửi thông qua recipientImage.

         * Tác giả: Văn Hoàng
         */
        @Override
        protected void bindSpecificData() {
            Glide.with(binding.getRoot())
                    .load(recipientImage)
                    .placeholder(R.drawable.ic_image)
                    .error(R.drawable.ic_image)
                    .into(binding.imageProfile);
        }

        @Override
        protected ImageView getImageView() {
            return binding.image;
        }
    }

    /**
     * ViewHolder cho các tin nhắn được gửi theo dạng hình ảnh. Được kế thừa class BaseImageViewHolder để xử lí ảnh.
     * Cung cấp cách liên kết dữ liệu cụ thể cho các tin nhắn được gửi theo dạng hình ảnh và thiết lập các view cho dữ liệu đó.

     * Tác giả: Văn Hoàng
     */
    public class SendImageViewHolder extends BaseImageViewHolder {
        private final ItemContainerSentImageBinding binding;

        SendImageViewHolder(ItemContainerSentImageBinding binding) {
            super(binding, listener);
            this.binding = binding;
        }

        @Override
        protected void bindSpecificData() {
            // No specific data binding needed for sent messages
        }

        @Override
        protected ImageView getImageView() {
            return binding.image;
        }
    }
}
