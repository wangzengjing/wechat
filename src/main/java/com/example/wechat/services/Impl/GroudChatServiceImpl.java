package com.example.wechat.services.Impl;


import com.example.wechat.domain.GroudChat;
import com.example.wechat.domain.SysUser;
import com.example.wechat.domain.SysUserGroud;
import com.example.wechat.mapper.SysUserGroudMapper;
import com.example.wechat.services.SysUserService;
import com.example.wechat.utils.IdGenrateUtils;
import com.example.wechat.utils.UpdateGroupLogoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;


import com.example.wechat.mapper.GroudChatMapper;
import com.example.wechat.services.GroudChatService;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class GroudChatServiceImpl implements GroudChatService {

    @Resource
    private GroudChatMapper groudChatMapper;

    @Resource
    private SysUserGroudMapper sysUserGroudMapper;

    @Autowired
    private SysUserService sysUserService;

    @Value("${image.path}")
    private String imagePath;

    /**
     * 发起群聊
     * @param usernames
     * @return
     */
    @Transactional(readOnly = false)
    @Override
    public String addGroudChat(List<String> usernames) {
        SysUser user = sysUserService.getUserByUsername(usernames.get(0));

        //创建群聊
        GroudChat groudChat = new GroudChat();
        String id = IdGenrateUtils.generateId();
        groudChat.setId(id);
        groudChat.setOwnerId(user.getId());
        groudChat.setCreateTime(new Date());

        //生成群聊头像图片
        String groudLogoPath = imagePath+id+".jpg";
        groudAvatar(usernames,groudLogoPath);

        groudChat.setAvatar(id+".jpg");
        groudChatMapper.insert(groudChat);


        //群聊关联用户
        List<SysUserGroud> sysUserGrouds = new ArrayList<>();

        usernames.forEach(name -> {

            SysUserGroud sysUserGroud = new SysUserGroud();
            sysUserGroud.setUsername(name);
            sysUserGroud.setGroudId(id);

            sysUserGrouds.add(sysUserGroud);

        });


        sysUserGroudMapper.insertList(sysUserGrouds);



        return id;
    }

    /**
     * 生成群聊头像图片
     * @param usernames
     * @param groudLogoPath
     */
    public void groudAvatar(List<String> usernames,String groudLogoPath){
        List<String> userAvatars = new ArrayList<>();
        //如果群聊中人数小于9人
        if(usernames.size()<=9) {
            usernames.forEach(username -> {

                SysUser user = sysUserService.getUserByUsername(username);
                userAvatars.add(imagePath+user.getAvatar());
            });
        }
        //如果群聊中人数多于9人，则只取前9个人的头像
        else{
            for (int i = 0; i < 9 ; i++) {
                SysUser user = sysUserService.getUserByUsername(usernames.get(i));
                userAvatars.add(imagePath+user.getAvatar());
            }
        }

        try {
            UpdateGroupLogoUtils.generate(userAvatars,groudLogoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}


