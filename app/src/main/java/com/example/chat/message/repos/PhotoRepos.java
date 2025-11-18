package com.example.chat.message.repos;

import android.net.Uri;

import com.example.chat.message.callback.UploadImageCallBack;

public interface PhotoRepos {
    void uploadImage(Uri uriImage, UploadImageCallBack callBack);
}
