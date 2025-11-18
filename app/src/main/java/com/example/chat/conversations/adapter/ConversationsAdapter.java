package com.example.chat.conversations.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat.conversations.Conversation;
import com.example.databinding.ItemConversationBinding;

import java.util.List;

/**
 * Adapter để hiển thị danh sách các cuộc hội thoại truyền lên RecyclerView.
 * Sử dụng để binding dữ liệu lên view.
 *
 * Tác giả: Nguyễn Hà Quỳnh Giao.
 */

public class ConversationsAdapter extends RecyclerView.Adapter<ConversationsAdapter.ConversationsHolderView>{

    //Danh sách cuộc trò chuyện
    private List<Conversation> conversations;
    //Sự kiện khi click vào một cuộc trò chuyện trong danh sách
    private ConversationListener listener;

    /**
     * Constructor
     *
     * @param conversations : Danh sách các cuộc hội thoại khởi tạo ban đầu.
     * @param listener      : Sự kiện click vào một cuộc trò chuyện trong danh sách
     *
     * Tác giả: Nguyễn Hà Quỳnh Giao.
     */
    public ConversationsAdapter(List<Conversation> conversations, ConversationListener listener){
        this.conversations = conversations;
        this.listener = listener;
    }

    /**
     * Thiết lập dữ liệu mới cho recycler và thông báo dữ liệu thay đổi.
     *
     * @param conversations : danh sách cuộc trò chuyện mới.
     *
     * Tác giả: Nguyễn Hà Quỳnh Giao.
     */
    @SuppressLint("NotifyDataSetChanged")
    public void setConversations(List<Conversation> conversations) {
        this.conversations = conversations;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ConversationsHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemConversationBinding binding = ItemConversationBinding
                .inflate(layoutInflater, parent, false);
        return new ConversationsHolderView(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationsHolderView holder, int position) {
        Conversation conversation = conversations.get(position);
        holder.bind(position, conversation);

    }

    @Override
    public int getItemCount() {
        if (conversations != null) {
            return conversations.size();
        }
        return 0;
    }

    /**
     * ViewHolder để binding dữ liệu cho các phần tử trong danh sách.
     *
     * Tác giả: Nguyễn Hà Quỳnh Giao
     */
    public class ConversationsHolderView extends RecyclerView.ViewHolder{
        // Binding với view
        ItemConversationBinding binding;
        public ConversationsHolderView(ItemConversationBinding item) {
            super(item.getRoot());
            binding = item;
        }

        /**
         * Binding dữ liệu lên phần tử cuộc trò chuyển
         *
         * @param position      : Vị trí của phần tử đó trong danh sách
         * @param conversation  : Dữ liệu của phần tử đó
         *
         * Tác giả: Nguyễn Hà Quỳnh Giao
         */
        void bind(int position, Conversation conversation){
            binding.setRequest(conversation);
            binding.setListener(listener);
            binding.setPosition(position);
            binding.executePendingBindings();
        }
    }
}
