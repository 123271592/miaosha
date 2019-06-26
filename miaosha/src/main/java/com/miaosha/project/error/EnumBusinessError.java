package com.miaosha.project.error;

public enum EnumBusinessError implements CommonError {

    PARAMETER_VALIDATION_ERROR(10001,"参数不合法"),
    USER_NOT_EXIST(10001,"用户不存在"),
    UNKNOWN_ERROR(20002,"未知错误")
    ;

    private EnumBusinessError(int errCode,String errMsg){
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    private int errCode;
    private String errMsg;

    @Override
    public int getErrorCode() {
        return this.errCode;
    }

    @Override
    public String getErrorMsg() {
        return this.errMsg;
    }

    @Override
    public CommonError setErrorMsg(String errMsg) {
        return this;
    }
}
