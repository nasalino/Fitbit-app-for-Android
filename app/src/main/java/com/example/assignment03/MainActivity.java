package com.example.assignment03;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    // private fields of the class
    private TextView tv_lat, tv_long, tv_result;
    private LocationManager lm;
    private Button btn_start;
    private LocationListener ls;
    private Button btn_stop;
    private Button btn_analyse;
    ArrayList<Location> al_loc;
    ArrayList<Float> al_speed;

    Float distBtw = 0.0f;

    Location old_location = null;
    Float total_dist = 0.0f;
    Float distance = 0.0f;
    long time = 0;
    int kmID = 0;
    Calendar oldTime = null;
    Calendar newTime = null;
    long seconds = 0;
    private CustomView cv;

    private static ArrayList<Integer> xTics = new ArrayList<Integer>();

    private static ArrayList<Float> yTics = new ArrayList<Float>();
    private static ArrayList<String> runDetails = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // pull the textviews from the xml and get access to the location manager
        tv_lat = (TextView) findViewById(R.id.tv_lat);
        tv_long = (TextView) findViewById(R.id.tv_long);

        tv_result = (TextView) findViewById(R.id.tv_result);

        btn_start = (Button) findViewById(R.id.btn_start);
        btn_stop = (Button) findViewById(R.id.btn_stop);
        btn_analyse = (Button) findViewById(R.id.btn_analyse);
        cv = (CustomView) findViewById(R.id.cv_linechart);
        al_speed = new ArrayList<Float>();
        al_loc =new ArrayList<Location>();

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // add in the location listener
        btn_start.setOnClickListener(new View.OnClickListener() {
            // overridden method to handle a button click
            public void onClick(View v) {


                addLocationListener();

                Toast.makeText(MainActivity.this, " Tracking started", Toast.LENGTH_LONG).show();

            }
        });

        //Trigger the analysis activity
        btn_analyse.setOnClickListener(new View.OnClickListener() {
            // overridden method to handle a button click
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, AnalysisListActivity.class);
                startActivity(intent);


                Toast.makeText(MainActivity.this, " Analyse runs ", Toast.LENGTH_LONG).show();

            }
        });


        btn_stop.setOnClickListener(new View.OnClickListener() {
            // overridden method to handle a button click
            public void onClick(View v) {

                //Disable location listener when user stops tracking
                lm.removeUpdates(ls);



                if(al_loc.size() != 0){


                //Write details of each run into a file with date and time that the user stopped tracking

                    Toast.makeText(MainActivity.this, "saved to " + "run.txt " + Calendar.getInstance().getTime(), Toast.LENGTH_LONG).show();

                    File f = new File(getFilesDir(), "run.txt " + Calendar.getInstance().getTime());
                try {
                    BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));
                    for (int i = 0; i < runDetails.size(); i++) {
                       bw.write(runDetails.get(i) + "\n");
                    }
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


//Variable total will hold the sum total of speeds and will be divided tby the number of recorded speeds to get the average over the whole journey
                float total = 0.0f;

                if (al_speed.size() != 0) {
                    for (int i = 0; i < al_speed.size(); i++) {
                        total += al_speed.get(i);
                        System.out.println("Speedy Gonzalez:" + al_speed.get(i));

                    }

                    /*Display this in a text view*/
                    System.out.println("Hello The average speed: " + (total / al_speed.size()) + "km/h for a total distance of :" + total_dist);
                    // System.out.println("Hello just checking remainder " + distance + "and  :" + time);
                    System.out.println("Speedy Gonzalez: speed" + (total) + "count :" + al_speed.size());

tv_result.setText("The average speed is: " + total / al_speed.size());
                }

                /*
                 *Calculate the last distance that is not a km
                 *
                 *
                 * */


                    tv_result.setText("The average speed is: " + (total / al_speed.size())+" km/h over the whole run" +"\nLast non Km is " +distance+
                                    " metres and speed " + calcLastSpeedPerKm( distance, time) + "km/h");

                //Reset the value for total distance whenever a run is finished
                total_dist = 0.0f;

                    for (int i = 0; i < runDetails.size(); i++) {
                        System.out.println("Here " + runDetails.get(i));
                    }

                //  cv.updateLineChart(xTics,yTics);
                xTics.clear();
                yTics.clear();
            }


                else{
                    Toast.makeText(MainActivity.this, " No location recorded ", Toast.LENGTH_LONG).show();

                }
            }

        });


    }


    // private method that will add a location listener to the location manager
    private void addLocationListener() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Permission is granted

//initialize new listener
            ls = new LocationListener() {

                public void onLocationChanged(Location location) {
                    // the location of the device has changed so update the // textviews to reflect this
                    tv_lat.setText("Latitude: " + location.getLatitude());
                    tv_long.setText("Longitude: " + location.getLongitude());


                    //get the current time and store it in a calender variable
                    newTime = Calendar.getInstance();

                    //if user has moved from one point ot another
                    if (old_location != null) {
                        //Save the long and lat into location variables every time on Location changed is called

                        //    calculate the distance between the new location and the old location
                        distBtw = old_location.distanceTo(location);


                        //calculate the total distance of the whole run in meters
                        total_dist += distBtw;

                        //    System.out.println(" Hello The distance between is now :" + distBtw + "\n " + "Hello The total distance is now :" + total_dist + "\n\n");


                    }


                    //if a new time has been recorded since the user started moving
                    if (oldTime != null) {

                        //calculate difference in time from one point to another
                        long diff = newTime.getTimeInMillis() - oldTime.getTimeInMillis();

                        //convert the time from Milliseconds to seconds
                        seconds = ((diff / 1000) % 60);
                        // System.out.println("Hello + " + oldTime.getTimeInMillis() + " and " + newTime.getTimeInMillis() + " and Hours" + seconds);
                    }


                    //   System.out.println("Hello speed: " + (distBtw / seconds) *3.6f);
                    //calculate the speed across the whole  journey in m/s

                    old_location = location;
                    oldTime = newTime;

                    //calculate the speed between distances recorded and store the speed in an array list


                    //add each speed across the whole journey into an array list in km/h
                    if(seconds>0) {
                        al_speed.add((distBtw / seconds) * 3.6f);
                    }
                    al_loc.add(location);


                    distance += distBtw;
                    time += seconds;
                    if (distance >= 1000) {
                        kmID++;


                        System.out.println("Hello speed in m/s: d: " + distance + "  t: " + time);

//Calculate the speed for each Km recorded
                        calcSpeedPerKm(kmID, distance, time);
                        distance = 0.0f;
                        time = 0;
                    }


                }

                public void onProviderDisabled(String provider) {
                    // if GPS has been disabled then update the text views to reflect //
                    // this
                    if (provider == LocationManager.GPS_PROVIDER) {

                        tv_lat.setText(R.string.tv_lat_text);
                        tv_long.setText(R.string.tv_long_text);
                    }
                }


                public void onProviderEnabled(String provider) {
                    // if there is a last known location then set it on the //textviews
                    if (provider == LocationManager.GPS_PROVIDER) {
                        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            Location l = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (l != null) {
                                //Set text view to show the lat and long being recorded
                                tv_lat.setText("Latitude: " + l.getLatitude());
                                tv_long.setText("Longitude: " + l.getLongitude());
                            }
                        }
                    }
                }


                public void onStatusChanged(String provider, int status, Bundle extras) {
                }
            };

            //Request for location updates every 3 seconds
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, ls);
        }


    }
//Calculate speed per km by dividing distance over time
    private float calcSpeedPerKm(int id, Float d, long t) {
        System.out.println("For Km " + id + " The average speed is :" + (d / t) * 3.6f + " km/h");
        xTics.add(id);
        yTics.add((d / t) * 3.6f);
        runDetails.add(id+":"+((d / t) * 3.6f));

        cv.updateLineChartLive(id, (d / t) * 3.6f);
        return (d / t) * 3.6f;
    }
    private float calcLastSpeedPerKm( Float d, long t) {
        System.out.println("For Last non Km The average speed is :" + (d / t) * 3.6f + " km/h");
        return (d / t) * 3.6f;
    }
}
















