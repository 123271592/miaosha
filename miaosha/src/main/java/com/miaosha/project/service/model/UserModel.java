package com.miaosha.project.service.model;

import lombok.Data;

@Data
public class UserModel {
    private Integer id;
    private String name;
    private String gender;
    private Short age;
    private String telephone;
    private String registerMode;
    private Integer thirdPartId;

    private String encrptPassword;

    public String getEncrptPassword() {
        return encrptPassword;
    }

    public void setEncrptPassword(String encrptPassword) {
        this.encrptPassword = encrptPassword;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Short getAge() {
        return age;
    }

    public void setAge(Short age) {
        this.age = age;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getRegisterMode() {
        return registerMode;
    }

    public void setRegisterMode(String registerMode) {
        this.registerMode = registerMode;
    }

    public Integer getThirdPartId() {
        return thirdPartId;
    }

    public void setThirdPartId(Integer thirdPartId) {
        this.thirdPartId = thirdPartId;
    }
}
