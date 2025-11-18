package com.example.common.navigation;

/**
 * Enum EAnimationType đại diện cho các loại hoạt ảnh có thể được sử dụng khi chuyển đổi giữa các activity.
 *
 * Tác giả: Trần Văn An
 */
public enum EAnimationType {
    /**
     * Chỉ ra rằng activity sẽ mờ dần khi nó xuất hiện.
     * Loại hoạt ảnh này thường được sử dụng cho các chuyển tiếp điều hướng tiến lên.
     */
    FADE_IN,

    /**
     * Chỉ ra rằng activity sẽ mờ dần khi nó biến mất.
     * Loại hoạt ảnh này thường được sử dụng cho các chuyển tiếp điều hướng quay lại.
     */
    FADE_OUT,

    /**
     * Chỉ ra rằng không có hoạt ảnh nào sẽ được áp dụng cho chuyển tiếp của activity.
     */
    NONE
}
