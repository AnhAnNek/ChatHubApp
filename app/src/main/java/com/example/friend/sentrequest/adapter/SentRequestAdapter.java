package com.example.friend.sentrequest.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.common.repository.MediaRepos;
import com.example.databinding.ItemSentRequestBinding;
import com.example.friend.FriendRequestView;

import java.util.List;

/**
 * Adapter này quản lý dữ liệu và hiển thị danh sách các yêu cầu kết bạn đã gửi.
 *
 * Tác giả: Trần Văn An
 */
public class SentRequestAdapter extends RecyclerView.Adapter<SentRequestAdapter.SentRequestViewHolder> {

    private Context context;
    private List<FriendRequestView> items;
    private SentRequestListener listener;
    private MediaRepos mediaRepos;

    /**
     * Constructor để khởi tạo SentRequestAdapter.
     *
     * @param context   Context của activity hoặc fragment sử dụng adapter.
     * @param items     Danh sách các yêu cầu kết bạn đã gửi.
     * @param listener  Đối tượng lắng nghe sự kiện của adapter.
     * @param mediaRepos Repository để tải về hình đại diện của người dùng.
     *
     * Tác giả: Trần Văn An
     */
    public SentRequestAdapter(
            Context context,
            List<FriendRequestView> items,
            SentRequestListener listener,
            MediaRepos mediaRepos
    ) {
        this.context = context;
        this.items = items;
        this.listener = listener;
        this.mediaRepos = mediaRepos;
    }

    /**
     * Phương thức này cập nhật dữ liệu của adapter và thông báo cho RecyclerView biết để cập nhật giao diện.
     *
     * @param items Danh sách mới của yêu cầu kết bạn đã gửi.
     *
     * Tác giả: Trần Văn An
     */
    public void setItems(List<FriendRequestView> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    /**
     * Phương thức được gọi khi RecyclerView cần một ViewHolder mới của adapter để hiển thị một item.
     *
     * @param parent   ViewGroup mà ViewHolder mới sẽ được gắn vào sau khi tạo.
     * @param viewType Loại view của item.
     * @return ViewHolder mới được tạo.
     *
     * Tác giả: Trần Văn An
     */
    @NonNull
    @Override
    public SentRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemSentRequestBinding binding = ItemSentRequestBinding
                .inflate(layoutInflater, parent, false);
        return new SentRequestViewHolder(binding);
    }

    /**
     * Phương thức này được gọi bởi RecyclerView để hiển thị dữ liệu tại vị trí đã cho.
     *
     * @param holder   ViewHolder cần được cập nhật.
     * @param position Vị trí của item trong RecyclerView.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    public void onBindViewHolder(@NonNull SentRequestViewHolder holder, int position) {
        FriendRequestView friendRequestView = items.get(position);
        holder.bind(position, friendRequestView);
    }

    /**
     * Phương thức này trả về số lượng item trong danh sách.
     *
     * @return Số lượng item trong danh sách.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    /**
     * ViewHolder này đại diện cho mỗi item trong RecyclerView.
     *
     * Tác giả: Trần Văn An
     */
    public class SentRequestViewHolder extends RecyclerView.ViewHolder {
        private final ItemSentRequestBinding binding;

        /**
         * Constructor để khởi tạo SentRequestViewHolder.
         *
         * @param binding Databinding cho item view.
         *
         * Tác giả: Trần Văn An
         */
        public SentRequestViewHolder(@NonNull ItemSentRequestBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        /**
         * Phương thức này gắn dữ liệu vào ViewHolder.
         *
         * @param position         Vị trí của item trong RecyclerView.
         * @param friendRequestView Thông tin về yêu cầu kết bạn.
         *
         * Tác giả: Trần Văn An
         */
        public void bind(int position, FriendRequestView friendRequestView) {
            // Tải hình đại diện từ mediaRepos dựa trên ID của người dùng và hiển thị nó trong itemView
            mediaRepos.downloadAvatar(friendRequestView.getDisplayedUserId())
                    .thenAccept(uri -> {
                        // Sử dụng Glide để tải hình ảnh từ URI và hiển thị nó trong ImageView
                        Glide.with(context)
                                .load(uri)
                                .addListener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        // Xử lý sự kiện khi tải ảnh thành công (nếu cần)
                                        binding.txvImgDesc.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(binding.rivAvatar);
                    });

            // Gắn dữ liệu và lắng nghe sự kiện vào itemView thông qua data binding
            binding.setRequest(friendRequestView);
            binding.setListener(listener);
            binding.setPosition(position);
            binding.executePendingBindings();
        }
    }
}
