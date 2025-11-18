package com.example.chat.textanalyzer;

/**

 * Interface CallBack được sử dụng để trả về kết quả của việc phân tích văn bản.
 * @param result Kết quả của quá trình phân tích.
 * Tác giả: Văn Hoàng
 */
public interface AnalyzeCallBack {
    void onAnalyzeSuccess(String result);
}
