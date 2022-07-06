package com.pyramid.witmat.net;

import com.google.gson.JsonObject;
import com.pyramid.witmat.bean.AddressBean;
import com.pyramid.witmat.bean.BomBean;
import com.pyramid.witmat.bean.ComListBody;

import java.util.List;

import com.pyramid.witmat.bean.UserData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @Author : xinlong.lv
 */
public interface NetApi {


    @POST("user/get_token/")
    Call<UserData> login(@Body JsonObject jsonObject);

    @GET("locations/")
    Call<ComListBody<AddressBean>> getLocationsList(@Query("page") int page, @Query("page_size") int pageSize, @Query("type") String type);


    @GET("locations/{id}/assets/")
    Call<List<BomBean>> getAssetsList(@Path ("id")int id);

    @POST("locations/{id}/stock_check/")
    Call<Object> stock_check(@Path ("id")int id,@Body JsonObject jsonObject);

}
