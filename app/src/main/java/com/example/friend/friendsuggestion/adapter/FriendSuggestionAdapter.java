package com.example.friend.friendsuggestion.adapter;

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
import com.example.databinding.ItemFriendSuggestionBinding;
import com.example.friend.FriendRequestView;

import java.util.List;

/**
 * Lớp Adapter để hiển thị gợi ý bạn bè trong một RecyclerView.
 * Adapter này gán dữ liệu FriendRequestView tới các view tương ứng trong layout của mỗi item.
 * Nó cũng xử lý các sự kiện click trên các item.
 *
 * Tác giả: Trần Văn An
 */
public class FriendSuggestionAdapter extends RecyclerView.Adapter<FriendSuggestionAdapter.FriendSuggestionViewHolder> {

    private Context context;
    private List<FriendRequestView> items;
    private FriendSuggestionListener listener;
    private MediaRepos mediaRepos;

    /**
     * Constructor cho FriendSuggestionAdapter.
     *
     * @param context  Context của activity hoặc fragment gọi.
     * @param items    Danh sách các gợi ý bạn bè cần hiển thị.
     * @param listener Interface lắng nghe để xử lý các sự kiện click.
     * @param mediaRepos Một instance của lớp MediaRepos để tải ảnh đại diện.
     *
     * Tác giả: Trần Văn An
     */
    public FriendSuggestionAdapter(
            Context context,
            List<FriendRequestView> items,
            FriendSuggestionListener listener,
            MediaRepos mediaRepos
    ) {
        this.context = context;
        this.items = items;
        this.listener = listener;
        this.mediaRepos = mediaRepos;
    }

    /**
     * Cập nhật danh sách các gợi ý bạn bè và thông báo cho adapter về sự thay đổi.
     *
     * @param items Danh sách mới của các gợi ý bạn bè.
     *
     * Tác giả: Trần Văn An
     */
    public void setItems(List<FriendRequestView> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    /**
     * Tạo một ViewHolder mới khi RecyclerView cần hiển thị một item.
     *
     * @param parent   ViewGroup cha của item hiện tại.
     * @param viewType Loại view được tạo ra.
     * @return ViewHolder mới được tạo.
     *
     * Tác giả: Trần Văn An
     */
    @NonNull
    @Override
    public FriendSuggestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Sử dụng LayoutInflater để inflate layout cho một item trong RecyclerView.
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        // Sử dụng data binding để inflate layout và tạo ra một instance của ItemFriendSuggestionBinding.
        ItemFriendSuggestionBinding binding = ItemFriendSuggestionBinding
                .inflate(layoutInflater, parent, false);
        // Trả về một ViewHolder mới với binding này.
        return new FriendSuggestionViewHolder(binding);
    }

    /**
     * Gắn dữ liệu của một item vào ViewHolder đã tạo.
     *
     * @param holder   ViewHolder để gắn dữ liệu vào.
     * @param position Vị trí của item trong danh sách.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    public void onBindViewHolder(@NonNull FriendSuggestionViewHolder holder, int position) {
        // Lấy ra đối tượng FriendRequestView tại vị trí position trong danh sách items.
        FriendRequestView request = items.get(position);
        // Gọi phương thức bind của ViewHolder để gắn dữ liệu vào các view.
        holder.bind(position, request);
    }

    /**
     * Trả về số lượng item trong danh sách.
     *
     * @return Số lượng item trong danh sách hoặc 0 nếu danh sách rỗng.
     *
     * Tác giả: Trần Văn An
     */
    @Override
    public int getItemCount() {
        // Kiểm tra nếu danh sách items không null, trả về số lượng item, ngược lại trả về 0.
        if (items != null) {
            return items.size();
        }
        return 0;
    }

    /**
     * Lớp ViewHolder để giữ các view của item.
     *
     * Tác giả: Trần Văn An
     */
    public class FriendSuggestionViewHolder extends RecyclerView.ViewHolder {
        private final ItemFriendSuggestionBinding binding;

        public FriendSuggestionViewHolder(@NonNull ItemFriendSuggestionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        /**
         * Gán dữ liệu của một gợi ý bạn bè vào các view tương ứng.
         *
         * @param position         Vị trí của item trong RecyclerView.
         * @param friendRequestView Đối tượng FriendRequestView chứa dữ liệu cần hiển thị.
         *
         * Tác giả: Trần Văn An
         */
        public void bind(int position, FriendRequestView friendRequestView) {
            // Tải và hiển thị ảnh đại diện bằng Glide.
            mediaRepos.downloadAvatar(friendRequestView.getDisplayedUserId())
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
                                        // Ẩn TextView mô tả hình ảnh khi ảnh được tải thành công.
                                        binding.txvImgDesc.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(binding.rivAvatar);
                    });

            // Gán dữ liệu FriendRequestView vào các view bằng data binding
            binding.setRequestView(friendRequestView);
            binding.setPosition(position);
            binding.setListener(listener);
        }
    }
}
