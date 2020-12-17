package com.test.myfirstandroidapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class Second2Fragment extends Fragment {

    private String[] stats;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        if(getArguments()!=null){
        // Take all arguments and save it as a Matched User
        stats = getArguments().getStringArray("Stats");
    }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second2, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(stats!=null){
            ((TextView)view.findViewById(R.id.txt_stat_1)).setText("Temprature : "+stats[0]+" F");
            ((TextView)view.findViewById(R.id.txt_stat_2)).setText("Humidity : "+stats[1]);
            ((TextView)view.findViewById(R.id.txt_stat_3)).setText("Pressure : "+stats[2]);
            ((TextView)view.findViewById(R.id.txt_stat_4)).setText("Feels Like : "+stats[3]);
            ((TextView)view.findViewById(R.id.txt_stat_5)).setText("Min. Expected Temprature  : "+stats[4]+" F");
            ((TextView)view.findViewById(R.id.txt_stat_6)).setText("Max. Expected Temprature : "+stats[5]+" F");
            ((TextView)view.findViewById(R.id.txt_stat_7)).setText("Speed : "+stats[6]);
            ((TextView)view.findViewById(R.id.txt_stat_8)).setText("Sunrise : "+stats[7]);
            ((TextView)view.findViewById(R.id.txt_stat_9)).setText("Sunset : "+stats[8]);
        }
        else{
            Toast.makeText(Second2Fragment.super.getContext(), "Failed getting weather updates", Toast.LENGTH_LONG).show();
        }

        view.findViewById(R.id.btn_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(Second2Fragment.this)
                        .navigate(R.id.action_Second2Fragment_to_First2Fragment);
            }
        });

        view.findViewById(R.id.btn_dashboard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Second2Fragment.super.getContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }
}