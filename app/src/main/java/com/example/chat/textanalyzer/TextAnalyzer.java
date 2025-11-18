package com.example.chat.textanalyzer;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognizer;

public class TextAnalyzer {
    private final String TAG = TextAnalyzer.class.getSimpleName();
    private final TextRecognizer recognizer;

    public TextAnalyzer(TextRecognizer recognizer) {
        this.recognizer = recognizer;
    }

    /**
     * Phân tích hình ảnh bitmap được cung cấp bằng cách sử dụng bộ nhận diện văn bản
     * và gọi lại callback với kết quả phân tích.
     *
     * @param bitmap   Hình ảnh bitmap cần phân tích.
     * @param callBack Callback để gọi lại với kết quả phân tích.
     * Tác giả: Văn Hoàng
     */
    public void analyze(Bitmap bitmap, AnalyzeCallBack callBack) {
        InputImage image = InputImage.fromBitmap(bitmap, 0);

        recognizer.process(image)
                .addOnSuccessListener(visionText -> {
                    String resultText = visionText.getText();
                    callBack.onAnalyzeSuccess(resultText);
                })
                .addOnFailureListener( e -> Log.d(TAG, "Analyze failed") );
    }
}
