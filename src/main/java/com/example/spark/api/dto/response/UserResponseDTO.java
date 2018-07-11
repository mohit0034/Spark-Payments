package com.example.spark.api.dto.response;

import com.example.spark.api.dto.request.UserRequestDTO;
import com.example.spark.dataobject.UserDO;

import java.io.Serializable;

public class UserResponseDTO implements Serializable {

    private String first_name;
    private String last_name;
    private String username;
    private String phone;
    private float wallet;

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public float getWallet() {
        return wallet;
    }

    public void setWallet(float wallet) {
        this.wallet = wallet;
    }

    public UserResponseDTO(UserRequestDTO userdto)
    {
        this.first_name=userdto.getFirst_name();
        this.last_name=userdto.getLast_name();
        this.phone=userdto.getPhone();
        this.username=userdto.getUsername();
        this.wallet=userdto.getWallet();
    }
    public UserResponseDTO(UserDO userdo)
    {
        this.first_name=userdo.getFirst_name();
        this.last_name=userdo.getLast_name();
        this.phone=userdo.getPhone();
        this.username=userdo.getUsername();
        this.wallet=userdo.getWallet();
    }
    public UserResponseDTO(){}
}