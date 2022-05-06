package com.chernybro.loftmoneyjava.remote.models.money;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MoneyListResponse {
    @SerializedName("data")
    private List<MoneyItemResponse> itemList;
    private String status;

    public List<MoneyItemResponse> getItemList() {
        return itemList;
    }

    public String getStatus() {
        return status;
    }
}
