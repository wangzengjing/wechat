package com.example.wechat.services.Impl;

import com.example.wechat.domain.ContactUser;
import com.example.wechat.domain.SysUser;
import com.example.wechat.domain.SysUserContact;
import com.example.wechat.mapper.SysUserMapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.example.wechat.mapper.SysUserContactMapper;
import com.example.wechat.services.SysUserContactService;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class SysUserContactServiceImpl implements SysUserContactService {

    @Resource
    private SysUserContactMapper sysUserContactMapper;

    @Resource
    private SysUserMapper sysUserMapper;

    /**
     * 添加用户关联
     * @param username
     * @param contactname
     */
    @Override
    @Transactional(readOnly = false)
    public void addContact(String username, String contactname) {
        //根据用户账号查找申请id

        SysUser sysUser = new SysUser();
        sysUser.setUsername(username);

        SysUser sysUser1 = sysUserMapper.selectOne(sysUser);

        int userid = sysUser1.getId();
        String viewname1 = sysUser1.getViewname();


        //根据用户账号查找好友id

        sysUser.setUsername(contactname);

        SysUser sysUser2 = sysUserMapper.selectOne(sysUser);

        int contactId = sysUser2.getId();
        String viewname2 = sysUser2.getViewname();


                // 账号关联
        SysUserContact sysUserContactOne = new SysUserContact();

        sysUserContactOne.setUserId(userid);
        sysUserContactOne.setContactId(contactId);
        sysUserContactOne.setNickname(viewname2);

        //反转关联
        SysUserContact sysUserContactTwo = new SysUserContact();

        sysUserContactTwo.setUserId(contactId);
        sysUserContactTwo.setContactId(userid);
        sysUserContactTwo.setNickname(viewname1);

        //插入关联数据
        sysUserContactMapper.insert(sysUserContactOne);
        sysUserContactMapper.insert(sysUserContactTwo);


    }

    /**
     * 获取所有好友
     * @param username
     * @return
     */
    @Override
    public List<ContactUser> getContactUsersByUsername(String username) {
        //添加条件
        SysUser sysUser = new SysUser();
        sysUser.setUsername(username);

        SysUser example = sysUserMapper.selectOne(sysUser);

        List<ContactUser> users = sysUserMapper.getAllContactUsers(example.getId());

//        List<JSONObject> users = new ArrayList<>();
//        SysUsers.forEach(user ->{
//
//        });
        return users;
    }
}

