package com.miaosha.project.controller;


import com.miaosha.project.controller.viewobject.UserVO;
import com.miaosha.project.dataobject.UserDO;
import com.miaosha.project.error.BusinessException;
import com.miaosha.project.error.EnumBusinessError;
import com.miaosha.project.response.CommonReturnType;
import com.miaosha.project.service.UserService;
import com.miaosha.project.service.model.UserModel;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController("user")
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class UserController extends BaseController{


    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @RequestMapping("/get")
    public CommonReturnType getUserById(@RequestParam(name="id") Integer id) throws BusinessException{
        UserModel userModel = userService.getUserById(id);

        if(userModel == null){
            //userModel.setEncrptPassword("");
            throw new BusinessException(EnumBusinessError.USER_NOT_EXIST);
        }
        UserVO userVO = new UserVO() ;
        BeanUtils.copyProperties(userModel,userVO);
        return CommonReturnType.create(userVO);
    }

    @RequestMapping("/getOpt")
    @ResponseBody
    public CommonReturnType getOpt(@RequestParam(name="telphone") String telephone){

        Random random = new Random();
        int randomNum = random.nextInt(99999);
        String optCode = String.valueOf(randomNum + 10000);

        httpServletRequest.getSession().setAttribute(telephone,optCode);

        System.out.print(optCode);

        return CommonReturnType.create(optCode);
    }

    @RequestMapping(value = "/register",method = RequestMethod.POST)
    @ResponseBody
    public CommonReturnType register(@RequestParam String telphone,
                                     @RequestParam String optCode,
                                     @RequestParam String name,
                                     @RequestParam String gender,
                                     @RequestParam Short age,
                                     @RequestParam String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        String isSessionOptCode = (String)httpServletRequest.getSession().getAttribute(telphone);

        if(!com.alibaba.druid.util.StringUtils.equals(optCode,isSessionOptCode)){
            throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR,"短信验证码不符合！");
        }

        UserModel userModel = new UserModel();
        userModel.setName(name);
        userModel.setAge(age);
        userModel.setGender(gender);
        userModel.setTelephone(telphone);

        userModel.setRegisterMode("by phone");
        userModel.setEncrptPassword(this.EncodeByMD5(password));

        userService.register(userModel);

        return CommonReturnType.create(null);
    }

    public String EncodeByMD5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();

        String newStr = base64Encoder.encode(md5.digest(str.getBytes("utf-8")));

        return newStr;
    }
}
