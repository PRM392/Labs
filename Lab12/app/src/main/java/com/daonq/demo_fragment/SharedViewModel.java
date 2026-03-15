package com.daonq.demo_fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<Integer> clickCount = new MutableLiveData<>(0);

    public void incrementCount() {
        if (clickCount.getValue() != null) {
            clickCount.setValue(clickCount.getValue() + 1);
        }
    }

    public LiveData<Integer> getClickCount() {
        return clickCount;
    }
}
