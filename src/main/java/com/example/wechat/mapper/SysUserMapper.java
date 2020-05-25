package com.example.wechat.mapper;

import com.example.wechat.domain.ContactUser;
import com.example.wechat.domain.SysUser;
import org.apache.ibatis.annotations.Param;
import tk.mybaits.mapper.MyMapper;

import java.util.List;

public interface SysUserMapper extends MyMapper<SysUser> {

    List<ContactUser> getAllContactUsers(@Param("userId") Integer userId);
}