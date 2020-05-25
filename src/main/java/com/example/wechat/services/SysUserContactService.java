package com.example.wechat.services;

import com.example.wechat.domain.ContactUser;


import java.util.List;

public interface SysUserContactService {


    void addContact(String username, String contactname);


    List<ContactUser> getContactUsersByUsername(String username);


}

