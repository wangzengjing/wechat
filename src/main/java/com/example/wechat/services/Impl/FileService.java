package com.example.wechat.services.Impl;

import com.example.wechat.security.output.ApiResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.*;

@Service
public class FileService {

    @Value("${image.path}")
    private String imagePath;

    /**
     * 保存头像
     * @param request
     * @param username
     * @throws IOException
     */
    public ApiResult savePicByIo(HttpServletRequest request, String username) {

        // 图片存储路径
        String path = imagePath;
        // 判断是否有路径
        if (!new File(path).exists()) {
            new File(path).mkdirs();
        }
        ServletInputStream inputStream = null;
        try {
            inputStream = request.getInputStream();

            String fileName = username + ".jpg";
            File tempFile = new File(path,fileName);
            if (!tempFile.exists()) {
                OutputStream os = new FileOutputStream(tempFile);
                BufferedOutputStream bos = new BufferedOutputStream(os);
                byte[] buf = new byte[1024];
                int length;
                length = inputStream.read(buf,0,buf.length);
                while (length != -1) {
                    bos.write(buf, 0 , length);
                    length = inputStream.read(buf);
                }
                bos.close();
                os.close();
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ApiResult.fail(1,"保存头像图片失败，请重试");
        }

        return ApiResult.ok(0,"保存头像图片成功");
    }
}
