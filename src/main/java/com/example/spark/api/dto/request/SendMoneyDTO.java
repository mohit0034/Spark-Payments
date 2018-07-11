package com.example.spark.api.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class SendMoneyDTO {
    @NotNull
    @Size(min =10, max=10)
    private String phone;

    @NotNull
    @Min(1)
    private float amount;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
