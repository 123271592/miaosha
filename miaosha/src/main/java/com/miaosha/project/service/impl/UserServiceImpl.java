package com.miaosha.project.service.impl;

import com.miaosha.project.dao.UserDOMapper;
import com.miaosha.project.dao.UserPasswordDOMapper;
import com.miaosha.project.dataobject.UserDO;
import com.miaosha.project.dataobject.UserPasswordDO;
import com.miaosha.project.error.BusinessException;
import com.miaosha.project.error.EnumBusinessError;
import com.miaosha.project.service.UserService;
import com.miaosha.project.service.model.UserModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDOMapper userDOMapper;

    @Autowired
    UserPasswordDOMapper userPasswordDOMapper;

    @Override
    public UserModel getUserById(Integer id) {
        UserDO userDO = userDOMapper.selectByPrimaryKey(id);

        if(userDO == null){
            return null;
        }
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(id);

        return convertFromDataObject(userDO,userPasswordDO);
    }

    @Override
    @Transactional
    public void register(UserModel userModel) throws BusinessException{
        if(userModel == null){
            throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        if(StringUtils.isEmpty(userModel.getName())
                || userModel.getAge() == null
                || userModel.getGender() == null
                || userModel.getTelephone() == null){
            throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        UserDO userDO = convertFromModel(userModel);
        userDOMapper.insertSelective(userDO);

        userModel.setId(userDO.getId());

        UserPasswordDO userPwdDO = convertPwdDOFromModel(userModel);
        userPasswordDOMapper.insertSelective(userPwdDO);


    }

    private UserDO convertFromModel(UserModel userModel){
        if(userModel == null){
            return null;
        }
        UserDO userDO = new UserDO();

        BeanUtils.copyProperties(userModel,userDO);

        return userDO;
    }

    private UserPasswordDO convertPwdDOFromModel(UserModel userModel){
        if(userModel == null){
            return null;
        }
        UserPasswordDO userPwdDO = new UserPasswordDO();

        userPwdDO.setEncrptPassword(userModel.getEncrptPassword());
        userPwdDO.setUserId(userModel.getId());
        return userPwdDO;
    }


    private UserModel convertFromDataObject(UserDO userDO, UserPasswordDO userPasswordDO){
        UserModel userModel = new UserModel();

        BeanUtils.copyProperties(userDO,userModel);

        userModel.setEncrptPassword(userPasswordDO.getEncrptPassword());

        return userModel;
    }
}
