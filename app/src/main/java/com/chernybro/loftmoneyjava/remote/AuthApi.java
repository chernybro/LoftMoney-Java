package com.chernybro.loftmoneyjava.remote;

import com.chernybro.loftmoneyjava.remote.models.auth.AuthResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AuthApi {

    @GET("./auth")
    Single<AuthResponse> makeLogin(@Query("social_user_id") String socialUserId);
}
