package com.example.common.customcontrol.customimageview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * Lớp CustomImageView mở rộng từ AppCompatImageView để tạo một ImageView với các góc bo tròn.
 * Tác giả: Văn Hoàng
 */
public class CustomImageView extends AppCompatImageView {

    // Biến tĩnh để giữ giá trị bán kính cho các góc bo tròn.
    public static float radius = 18.0f;

    /**
     * Hàm constructor cho CustomImageView.
     *
     * @param context Ngữ cảnh mà view đang chạy, thông qua đó có thể truy cập đến theme hiện tại, tài nguyên, v.v.
     * Tác giả: Văn Hoàng
     */
    public CustomImageView(Context context) {
        super(context);
    }

    /**
     * Hàm constructor cho CustomImageView được gọi khi khởi tạo từ XML.
     *
     * @param context Ngữ cảnh mà view đang chạy, thông qua đó có thể truy cập đến theme hiện tại, tài nguyên, v.v.
     * @param attrs Các thuộc tính của thẻ XML đang khởi tạo view.
     * Tác giả: Văn Hoàng
     */
    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Hàm constructor cho CustomImageView cho phép định nghĩa một thuộc tính style ngoài ngữ cảnh và các thuộc tính.
     *
     * @param context Ngữ cảnh mà view đang chạy, thông qua đó có thể truy cập đến theme hiện tại, tài nguyên, v.v.
     * @param attrs Các thuộc tính của thẻ XML đang khởi tạo view.
     * @param defStyle Một thuộc tính trong theme hiện tại chứa tham chiếu đến tài nguyên style cung cấp các giá trị mặc định cho view. Có thể là 0 nếu không tìm giá trị mặc định.
     * Tác giả: Văn Hoàng
     */
    public CustomImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * Ghi đè phương thức onDraw để cắt canvas thành hình chữ nhật bo tròn trước khi vẽ nội dung của view.
     *
     * @param canvas Canvas trên đó nền sẽ được vẽ.
     * Tác giả: Văn Hoàng
     */
    @Override
    protected void onDraw(Canvas canvas) {
        // Tạo một đối tượng Path mới để định nghĩa đường cắt.
        Path clipPath = new Path();

        // Tạo một đối tượng RectF định nghĩa kích thước của view.
        RectF rect = new RectF(0, 0, this.getWidth(), this.getHeight());

        // Thêm một hình chữ nhật bo tròn vào đường dẫn với bán kính được chỉ định.
        clipPath.addRoundRect(rect, radius, radius, Path.Direction.CW);

        // Cắt canvas với đường dẫn đã định nghĩa.
        canvas.clipPath(clipPath);

        // Gọi phương thức onDraw của lớp cha để vẽ nội dung.
        super.onDraw(canvas);
    }
}