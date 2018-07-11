package com.example.spark.api.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class AddMoneyDTO {
    @NotNull
    @Min(1)
    private float amount;

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
