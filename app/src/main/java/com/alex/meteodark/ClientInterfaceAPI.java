package com.alex.meteodark;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ClientInterfaceAPI {
    @GET
    public Call<List<SensorData>> getSensorData(@Url String url);
}
