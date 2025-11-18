package com.example.friend.friendrequest.adapter;

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
import com.example.databinding.ItemFriendRequestBinding;
import com.example.friend.FriendRequestView;

import java.util.List;

/**
 * Adapter cho RecyclerView hiển thị danh sách yêu cầu kết bạn.
 *
 * Tác giả: Trần Văn An
 */
public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.FriendRequestViewHolder> {

    private Context context;
    private List<FriendRequestView> friendRequests;
    private FriendRequestListener listener;
    private MediaRepos mediaRepos;

    /**
     * Constructor cho FriendRequestAdapter.
     *
     * @param context       Ngữ cảnh của ứng dụng.
     * @param friendRequests Danh sách các yêu cầu kết bạn.
     * @param listener      Listener để xử lý các sự kiện khi người dùng tương tác.
     * @param mediaRepos    Repository để tải ảnh đại diện.
     */
    public FriendRequestAdapter(Context context,
                                List<FriendRequestView> friendRequests,
                                FriendRequestListener listener,
                                MediaRepos mediaRepos) {

        this.context = context;
        this.friendRequests = friendRequests;
        this.listener = listener;
        this.mediaRepos = mediaRepos;
    }

    /**
     * Cập nhật danh sách yêu cầu kết bạn và thông báo cho RecyclerView.
     *
     * @param friendRequests Danh sách mới của các yêu cầu kết bạn.
     */
    public void setFriendRequests(List<FriendRequestView> friendRequests) {
        this.friendRequests = friendRequests;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FriendRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Tạo một đối tượng LayoutInflater từ ngữ cảnh của ViewGroup cha
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        // Sử dụng LayoutInflater để tạo đối tượng ItemFriendRequestBinding từ file XML item_friend_request
        ItemFriendRequestBinding binding = ItemFriendRequestBinding
                .inflate(layoutInflater, parent, false);

        // Trả về một instance của FriendRequestViewHolder, sử dụng binding đã tạo
        return new FriendRequestViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendRequestViewHolder holder, int position) {
        // Lấy đối tượng FriendRequestView từ danh sách dựa trên vị trí (position)
        FriendRequestView request = friendRequests.get(position);

        // Gọi phương thức bind của ViewHolder để gắn dữ liệu vào View
        holder.bind(position, request);
    }

    @Override
    public int getItemCount() {
        // Trả về số lượng yêu cầu kết bạn trong danh sách
        return friendRequests.size();
    }

    /**
     * ViewHolder cho từng item trong danh sách yêu cầu kết bạn.
     */
    public class FriendRequestViewHolder extends RecyclerView.ViewHolder {
        private final ItemFriendRequestBinding binding;

        /**
         * Constructor cho FriendRequestViewHolder.
         *
         * @param binding Đối tượng binding cho item yêu cầu kết bạn.
         */
        public FriendRequestViewHolder(@NonNull ItemFriendRequestBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        /**
         * Gắn dữ liệu vào view của ViewHolder.
         *
         * @param position Vị trí của item trong danh sách.
         * @param request  Đối tượng yêu cầu kết bạn để hiển thị.
         */
        public void bind(int position, FriendRequestView request) {
            mediaRepos.downloadAvatar(request.getDisplayedUserId())
                    .thenAccept(uri -> {
                        Glide.with(context)
                                .load(uri)
                                .addListener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        binding.txvImgDesc.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(binding.rivAvatar);
                    });

            binding.setRequestView(request);
            binding.setListener(listener);
            binding.setPosition(position);
            binding.executePendingBindings();
        }
    }
}
