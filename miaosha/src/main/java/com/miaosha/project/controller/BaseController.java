package com.miaosha.project.controller;

import com.miaosha.project.error.BusinessException;
import com.miaosha.project.error.EnumBusinessError;
import com.miaosha.project.response.CommonReturnType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class BaseController {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handlerException(HttpServletRequest httpServletRequest, Exception ex){
        Map<String, Object> responseData = new HashMap<String,Object>();
        if(ex instanceof BusinessException){
            BusinessException exception = (BusinessException)ex;
            responseData.put("errCode",exception.getErrorCode());
            responseData.put("errMsg",exception.getErrorMsg());
        }else{
            responseData.put("errCode",EnumBusinessError.UNKNOWN_ERROR.getErrorCode());
            responseData.put("errMsg",EnumBusinessError.UNKNOWN_ERROR.getErrorMsg());

        }
        return CommonReturnType.create(responseData,"fail");
    }
}
