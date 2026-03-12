package com.example.lab11;

public class TraineeRepository {
    public static TraineeService getTraineeService() {
        return APIClient.getClient().create(TraineeService.class);
    }
}
