package com.chernybro.loftmoneyjava.remote;

import com.chernybro.loftmoneyjava.remote.models.money.MoneyListResponse;

import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MoneyApi {

    @GET("./items")
    Single<MoneyListResponse> getItems(@Query("type") String type);

    @POST("./items/add")
    @FormUrlEncoded
    Single<MoneyListResponse> addItem(@Field("price") int price,
                                      @Field("name") String name,
                                      @Field("type") String type);

}
