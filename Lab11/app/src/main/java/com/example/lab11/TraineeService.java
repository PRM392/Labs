package com.example.lab11;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface TraineeService {
    String TRAINEES = "Nhanvien";

    @GET(TRAINEES)
    Call<List<Trainee>> getAllTrainees();

    @POST(TRAINEES)
    Call<Trainee> createTrainees(@Body Trainee trainee);

    @PUT(TRAINEES + "/{id}")
    Call<Trainee> updateTrainees(@Path("id") String id, @Body Trainee trainee);

    @DELETE(TRAINEES + "/{id}")
    Call<Trainee> deleteTrainees(@Path("id") String id);
}
