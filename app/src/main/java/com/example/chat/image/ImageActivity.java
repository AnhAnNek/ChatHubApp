package com.example.chat.image;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.example.R;
import com.example.databinding.ActivityImageBinding;

public class ImageActivity extends AppCompatActivity {
    private ImageViewModel viewModel;
    private ActivityImageBinding binding;

    /**
     * Thiết lập onCreate cho ImageActivity.
     * Tác giả: Văn Hoàng
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Thiết lập binding với layout activity_image bằng DataBindingUtil.
        binding = DataBindingUtil.setContentView(
                this, R.layout.activity_image);

        // Lấy URL của hình ảnh được truyền qua Intent.
        String imageUrl = getIntent().getStringExtra("imageClickedUrl");

        // Khởi tạo ImageViewModel với URL của hình ảnh được truyền.
        viewModel = new ImageViewModel(imageUrl);

        // Gán ViewModel cho binding để liên kết dữ liệu với giao diện.
        binding.setViewModel(viewModel);

        // Đặt vòng đời của binding theo vòng đời của Activity này.
        binding.setLifecycleOwner(this);

        // Thiết lập các observer để lắng nghe và cập nhật giao diện khi dữ liệu thay đổi.
        setObservers();
    }

    /**
     * Thiết lập lắng nghe thay đổi cho thuộc tính imagePickedUrl trong viewModel.
     * Khi thuộc tính imagePickedUrl thay đổi, ảnh mới sẽ được tải và hiển thị trong imageView bằng thư viện Glide.
     * - Nếu ảnh tải thành công, nó sẽ được hiển thị trong imageView.
     * - Nếu ảnh tải không thành công, một hình ảnh mặc định (ic_image) sẽ được hiển thị trong imageView.
     *
     * Tác giả: Văn Hoàng
     */
    private void setObservers() {
        viewModel.getImagePickedUrl().observe(this, imageUrl -> {
            Glide.with(binding.getRoot())
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_image)
                    .error(R.drawable.ic_image)
                    .into(binding.imageView);
        });
    }
}