package com.example.wechat.services.Impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.example.wechat.mapper.SysUserGroudMapper;
import com.example.wechat.services.SysUserGroudService;

@Service
public class SysUserGroudServiceImpl implements SysUserGroudService {

    @Resource
    private SysUserGroudMapper sysUserGroudMapper;

}


