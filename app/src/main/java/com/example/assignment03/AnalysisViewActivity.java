package com.example.assignment03;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

public class AnalysisViewActivity extends AppCompatActivity {

    private ArrayList<String> al_run2;
    private ArrayList<String> al_run1;
    private AnalysisView av;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis_view);
//Reference the custom view for viewing both runs
        av = (AnalysisView) findViewById(R.id.cv_linechart_analysis);
        //Retrieve the arraylists representing the two runs to compare from the activity that started this activity
        Intent i1 = getIntent();
        al_run1 = i1.getStringArrayListExtra("run1");
        // Intent i2 = getIntent();
        al_run2 = i1.getStringArrayListExtra("run2");

        //Update the analysis custom view with two runs
        av.updateLineChart(al_run1, al_run2);
    }
}
