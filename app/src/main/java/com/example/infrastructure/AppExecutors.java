package com.example.infrastructure;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Class AppExecutors quản lý một ScheduledExecutorService
 * để xử lý các tác vụ nền liên quan đến I/O mạng như gửi yêu cầu HTTP,
 * và giao tiếp với các dịch vụ web.
 *
 * Tác giả: Trần Văn An
 */
public class AppExecutors {
    // Biến instance singleton của AppExecutors
    private static AppExecutors ins;

    // ScheduledExecutorService để thực hiện các tác vụ I/O mạng
    private final ScheduledExecutorService networkIO;

    /**
     * Trả về instance singleton của AppExecutors.
     * Nếu instance là null, nó sẽ được khởi tạo.
     *
     * @return instance singleton của AppExecutors
     *
     * Tác giả: Trần Văn An
     */
    public static AppExecutors getIns() {
        if (ins == null) {
            // Khối synchronized để đảm bảo khởi tạo an toàn trong môi trường đa luồng
            synchronized (AppExecutors.class) {
                if (ins == null) {
                    ins = new AppExecutors();
                }
            }
        }
        return ins;
    }

    /**
     * Private constructor để ngăn chặn việc khởi tạo từ các class khác.
     * Khởi tạo ScheduledExecutorService với số lượng luồng bằng số lượng bộ xử lý có sẵn.
     *
     * Tác giả: Trần Văn An
     */
    private AppExecutors() {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        networkIO = Executors.newScheduledThreadPool(availableProcessors);
    }

    /**
     * Trả về instance của ScheduledExecutorService cho các tác vụ I/O mạng.
     *
     * @return ScheduledExecutorService cho các tác vụ I/O mạng
     *
     * Tác giả: Trần Văn An
     */
    public ScheduledExecutorService networkIO() {
        return networkIO;
    }
}
