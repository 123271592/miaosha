package com.miaosha.project.service;

import com.miaosha.project.error.BusinessException;
import com.miaosha.project.service.model.UserModel;

public interface UserService {
    UserModel getUserById(Integer id);
    void register(UserModel userModel) throws BusinessException;
}
