package com.example.chat.message.baseviewholder;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.R;
import com.example.chat.listener.ImageListener;
import com.example.chat.message.Message;
import com.example.databinding.ItemContainerReceivedImageBinding;
import com.example.databinding.ItemContainerSentImageBinding;

/**
 * Lớp cơ sở cho các ImageViewHolder của RecyclerView xử lý hình ảnh.
 * Sử dụng DataBinding để liên kết view và Glide để tải hình ảnh.
 *
 * Tác giả: Văn Hoàng
 */
public abstract class BaseImageViewHolder extends RecyclerView.ViewHolder {
    // TAG để ghi log
    private final String TAG = BaseImageViewHolder.class.getSimpleName();

    // Đối tượng ViewDataBinding để liên kết view
    protected final ViewDataBinding binding;

    // ImageListener để xử lý sự kiện click trên hình ảnh
    private final ImageListener listener;

    /**
     * Constructor cho BaseImageViewHolder.
     *
     * @param binding  Đối tượng ViewDataBinding để liên kết view.
     * @param listener ImageListener để xử lý sự kiện click trên hình ảnh.
     *
     * Tác giả: Văn Hoàng
     */
    protected BaseImageViewHolder(ViewDataBinding binding, ImageListener listener) {
        super(binding.getRoot());
        this.binding = binding;
        this.listener = listener;
    }

    /**
     * Thiết lập dữ liệu cho ViewHolder.
     *
     * @param message  Đối tượng Message chứa dữ liệu.
     * @param position Vị trí của item trong adapter.
     *
     * Tác giả: Văn Hoàng
     */
    public void setData(Message message, int position) {
        try {
            String imageUrl = message.getMessage();
            loadImage(imageUrl);

            /**
             * Thiết lập dữ liệu cho ViewHolder tuỳ theo kiểu binding.
             *
             * Kiểm tra kiểu của binding và thiết lập dữ liệu phù hợp cho từng kiểu.
             * - Nếu binding là `ItemContainerReceivedImageBinding`:
             *    - Set text cho `textDateTime` bằng thời gian gửi của message.
             *    - Set listener cho binding.
             *    - Set vị trí (position) cho binding.
             *    - Gọi hàm `bindSpecificData` để thiết lập các dữ liệu đặc thù khác (nếu có).
             * - Nếu binding là `ItemContainerSentImageBinding`:
             *    - Set text cho `textDateTime` bằng thời gian gửi của message.
             *    - Set listener cho binding.
             *    - Set vị trí (position) cho binding.
             *
             * Tác giả: Văn Hoàng
             */
            if (binding instanceof ItemContainerReceivedImageBinding) {
                ((ItemContainerReceivedImageBinding) binding).textDateTime.setText(message.getSendingTime());
                ((ItemContainerReceivedImageBinding) binding).setListener(listener);
                ((ItemContainerReceivedImageBinding) binding).setPosition(position);
                bindSpecificData();
            } else if (binding instanceof ItemContainerSentImageBinding) {
                ((ItemContainerSentImageBinding) binding).textDateTime.setText(message.getSendingTime());
                ((ItemContainerSentImageBinding) binding).setListener(listener);
                ((ItemContainerSentImageBinding) binding).setPosition(position);
            }

            // Thực hiện bất kỳ liên kết đang chờ nào ngay lập tức
            binding.executePendingBindings();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * Hàm tải và hiển thị ảnh từ URL.
     *
     * Hàm này sử dụng thư viện Glide để tải ảnh từ URL được cung cấp.
     * - Sử dụng `Glide.with(binding.getRoot())` để lấy instance của Glide gắn với context của ViewHolder.
     * - Chuyển đổi ảnh thành dạng Bitmap bằng `.asBitmap()`.
     * - Sử dụng `.load(imageUrl)` để xác định URL của ảnh cần tải.
     * - Sử dụng `CustomTarget<Bitmap>` để nhận ảnh đã tải về.
     *   - Trong `onResourceReady`:
     *     - Lấy chiều rộng (`imageWidth`) và chiều cao (`imageHeight`) của ảnh gốc.
     *     - Tính toán kích thước mong muốn (`desiredWidth`, `desiredHeight`) bằng một nửa kích thước gốc.
     *     - Sử dụng Glide một lần nữa để tải lại ảnh từ cùng URL (`imageUrl`).
     *     - Set kích thước hiển thị của ảnh bằng `.override(desiredWidth, desiredHeight)`.
     *     - Set ảnh placeholder (`placeholder`) hiển thị trong khi chờ tải ảnh bằng `.placeholder(R.drawable.ic_image)`.
     *     - Set ảnh hiển thị khi tải ảnh thất bại (`error`) bằng `.error(R.drawable.ic_image)`.
     *     - Cuối cùng, sử dụng `.into(getImageView())` để hiển thị ảnh đã resize vào ImageView.
     *   - Trong `onLoadCleared`:
     *     - Kiểm tra nếu placeholder tồn tại (`placeholder != null`).
     *     - Nếu có, set placeholder cho ImageView bằng `.setImageDrawable(placeholder)`.
     *
     * @param imageUrl Đường dẫn URL của ảnh cần tải.

     * Tác giả: Văn Hoàng
     */
    private void loadImage(String imageUrl) {
        Glide.with(binding.getRoot())
                .asBitmap()
                .load(imageUrl)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        int imageWidth = resource.getWidth();
                        int imageHeight = resource.getHeight();

                        // Thu nhỏ hình ảnh xuống một nửa kích thước
                        int desiredWidth = imageWidth / 2;
                        int desiredHeight = imageHeight / 2;

                        Glide.with(binding.getRoot())
                                .load(imageUrl)
                                .override(desiredWidth, desiredHeight)
                                .placeholder(R.drawable.ic_image)
                                .error(R.drawable.ic_image)
                                .into(getImageView());
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        if (placeholder != null) {
                            getImageView().setImageDrawable(placeholder);
                        }
                    }
                });
    }

    /**
     * Phương thức trừu tượng để liên kết dữ liệu cụ thể với ViewHolder.
     * Sẽ được triển khai bởi các lớp con để xử lý liên kết dữ liệu bổ sung.
     * Tác giả: Văn Hoàng
     */
    protected abstract void bindSpecificData();

    /**
     * Phương thức trừu tượng để lấy ImageView của binding tương ứng.
     * Sẽ được triển khai bởi các lớp con để trả về đúng ImageView.
     *
     * @return ImageView để tải hình ảnh vào.
     * Tác giả: Văn Hoàng
     */
    protected abstract ImageView getImageView();
}

