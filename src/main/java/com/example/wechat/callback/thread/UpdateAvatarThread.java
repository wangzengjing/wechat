package com.example.wechat.callback.thread;

import com.example.wechat.exception.AvatarException;
import com.example.wechat.utils.UpdateGroupLogoUtils;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UpdateAvatarThread implements Runnable{


    private String imagePath;

    private String username;

    public UpdateAvatarThread(String imagePath, String username) {
        this.imagePath = imagePath;
        this.username = username;
    }

    @Override
    public void run() {
        List<String> picUrls = new ArrayList<>();
        picUrls.add(imagePath+"templates.jpg");
        try {
            UpdateGroupLogoUtils.generate(picUrls,imagePath+username+".jpg");
        } catch (Exception e) {
            throw new AvatarException(e.getMessage());

        }
    }
}
