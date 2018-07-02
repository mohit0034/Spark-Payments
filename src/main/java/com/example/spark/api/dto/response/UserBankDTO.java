package com.example.spark.api.dto.response;

import com.example.spark.dataobject.UserBankDO;

public class UserBankDTO {
    private int id;
    private int account_id;
    private float balance;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }
    public UserBankDTO(){}

    public UserBankDTO(UserBankDO userBankDO)
    {
        this.id = userBankDO.getId();
        this.account_id=userBankDO.getAccount_id();
        this.balance=userBankDO.getBalance();
    }
}
