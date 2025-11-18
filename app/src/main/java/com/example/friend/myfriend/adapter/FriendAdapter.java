package com.example.friend.myfriend.adapter;

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
import com.example.databinding.ItemFriendBinding;
import com.example.friend.FriendRequestView;

import java.util.List;

/**
 * Adapter cho RecyclerView hiển thị danh sách bạn bè.
 *
 * Adapter này quản lý dữ liệu và tạo các view cho mỗi mục trong RecyclerView.
 *
 * Tác giả: Trần Văn An
 */
public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {

    private Context context;
    private List<FriendRequestView> items;
    private FriendListener listener;
    private MediaRepos mediaRepos;

    /**
     * Constructor để khởi tạo FriendAdapter.
     *
     * @param context  Context của ứng dụng.
     * @param items    Danh sách các mục bạn bè cần hiển thị.
     * @param listener Đối tượng nghe sự kiện của các mục trong RecyclerView.
     * @param mediaRepos Repository cho các phương thức liên quan đến phương tiện.
     *
     * Tác giả: Trần Văn An
     */
    public FriendAdapter(
            Context context,
            List<FriendRequestView> items,
            FriendListener listener,
            MediaRepos mediaRepos
    ) {
        this.context = context;
        this.items = items;
        this.listener = listener;
        this.mediaRepos = mediaRepos;
    }

    /**
     * Cập nhật danh sách các mục bạn bè và thông báo cho Adapter biết về sự thay đổi dữ liệu.
     *
     * @param items Danh sách các mục bạn bè mới cần hiển thị.
     *
     * Tác giả: Trần Văn An
     */
    public void setItems(List<FriendRequestView> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    /**
     * Tạo và trả về một ViewHolder mới khi RecyclerView cần.
     *
     * @param parent   ViewGroup cha chứa ViewHolder mới.
     * @param viewType Loại view trong RecyclerView.
     * @return ViewHolder mới được tạo.
     *
     * Tác giả: Trần Văn An
     */
    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemFriendBinding binding = ItemFriendBinding
                .inflate(layoutInflater, parent, false);
        return new FriendViewHolder(binding);
    }

    /**
     * Liên kết dữ liệu của một mục trong RecyclerView với ViewHolder.
     *
     * @param holder   ViewHolder cần được liên kết dữ liệu.
     * @param position Vị trí của mục trong RecyclerView.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        FriendRequestView requestView = items.get(position);
        holder.bind(position, requestView);
    }

    /**
     * Trả về số lượng mục trong RecyclerView.
     *
     * @return Số lượng mục trong RecyclerView.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        }
        return 0;
    }

    /**
     * ViewHolder cho mỗi mục bạn bè trong RecyclerView.
     *
     * Tác giả: Trần Văn An
     */
    public class FriendViewHolder extends RecyclerView.ViewHolder {
        private final ItemFriendBinding binding;

        public FriendViewHolder(@NonNull ItemFriendBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        /**
         * Bind dữ liệu vào ViewHolder.
         *
         * @param position           Vị trí của mục trong danh sách.
         * @param friendRequestView  Đối tượng chứa thông tin về một mục bạn bè.
         *
         * Tác giả: Trần Văn An
         */
        public void bind(int position, FriendRequestView friendRequestView) {
            // Tải avatar của bạn bè từ MediaRepos
            mediaRepos.downloadAvatar(friendRequestView.getDisplayedUserId())
                    .thenAccept(uri -> {
                        // Sử dụng Glide để tải ảnh từ uri và hiển thị trong ImageView
                        Glide.with(context)
                                .load(uri)
                                .addListener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        // Nếu việc tải ảnh thất bại, không làm gì cả
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        // Khi ảnh đã tải thành công, ẩn TextView hiển thị mô tả
                                        binding.txvImgDesc.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(binding.rivAvatar);
                    });

            binding.setRequest(friendRequestView);
            binding.setListener(listener);
            binding.setPosition(position);
            binding.executePendingBindings();
        }
    }
}
