package com.example.wechat.services.Impl;

import com.alibaba.fastjson.JSONObject;
import com.example.wechat.domain.SysUser;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.example.wechat.mapper.SysUserMapper;
import com.example.wechat.services.SysUserService;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;


    @Override
    public SysUser getUserByUsername(String username) {

        Example example = new Example(SysUser.class);

        example.createCriteria().andEqualTo("username",username);

        return sysUserMapper.selectOneByExample(example);
    }


}


