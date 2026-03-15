package com.daonq.demo_fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class FragmentTwo extends Fragment {

    private SharedViewModel sharedViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_two, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Retrieve the SharedViewModel scoped to the Activity explicitly
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        TextView tvCount = view.findViewById(R.id.tv_count);

        sharedViewModel.getClickCount().observe(getViewLifecycleOwner(), count -> {
            tvCount.setText("Count: " + count);
        });
    }
}
