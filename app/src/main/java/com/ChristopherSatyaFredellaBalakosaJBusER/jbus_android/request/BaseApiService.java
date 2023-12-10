package com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android.request;

import com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android.model.Account;
import com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android.model.BaseResponse;
import com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android.model.Bus;
import com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android.model.BusType;
import com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android.model.Facility;
import com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android.model.Payment;
import com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android.model.Renter;
import com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android.model.Station;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
public interface BaseApiService {
    @GET("account/{id}")
    Call<Account> getAccountbyId (@Path("id") int id);

    @POST("account/register")
    Call<BaseResponse<Account>> register (
            @Query("name") String name,
            @Query("email") String email,
            @Query("password") String password);

    @POST("account/login")
    Call<BaseResponse<Account>> login (
            @Query("email")String email,
            @Query("password")String password);

    @POST("account/{id}/topUp")
    Call<BaseResponse<Double>> topUp(
            @Path("id") int id,
            @Query("amount") double amount
    );

    @POST("account/{id}/registerRenter")
    Call<BaseResponse<Renter>> registerRenter(
            @Path("id") int id,
            @Query("companyName") String companyName,
            @Query("address") String address,
            @Query("phoneNumber") String phoneNumber
    );
    @GET("bus/getMyBus")
    Call<List<Bus>> getMyBus(
            @Query("accountId") int accountId
    );
    @GET("station/getAll")
    Call<List<Station>> getAllStation();
    @GET("station/getAllBus")
    Call<List<Bus>> getAllBus();

    @POST("bus/addSchedule")
    Call<BaseResponse<Bus>> addSchedule(
            @Query("busId") int busId,
            @Query("time") String time
    );
    @GET("bus/{id}")
    Call<Bus> getBusbyId(
            @Path("id") int busId
    );
    @POST("bus/create")
    Call<BaseResponse<Bus>> addBus(
            @Query("accountId") int accountId,
            @Query("name") String name,
            @Query("capacity") int capacity,
            @Query("facilities") List<Facility> facilities,
            @Query("busType") BusType busType,
            @Query("price") int price,
            @Query("stationDepartureId") int stationDepartureId,
            @Query("stationArrivalId") int stationArrivalId
    );
    @POST("makeBooking")
    Call<BaseResponse<Payment>> makeBooking(
            @Query("buyerId") int buyerId,
            @Query("renterId") int renterId,
            @Query("busId") int busId,
            @Query("busSeats") List<String> busSeats,
            @Query("departureDate") String departureDate
    );
    @GET("bus/total")
    Call<Integer> numberOfBuses();


}

