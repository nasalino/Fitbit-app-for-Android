package com.example.assignment03;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class AnalysisListActivity extends AppCompatActivity {


    // private fields of the class
    private TextView tv_display;
    private ListView lv_mainlist;
    private EditText et_new_strings;
    private ArrayList<String> al_strings;
    private ArrayList<String> al_run1;
    private ArrayList<String> al_run2;
    private ArrayAdapter<String> aa_strings;
    private int count = 0;
    private String runs1 = "";
    private String runs2 = "";
    private File f1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis_list);

        count = 0;
// pull the list view and the edit text from the xml
        tv_display = (TextView) findViewById(R.id.tv_display);
        lv_mainlist = (ListView) findViewById(R.id.lv_mainlist);
        // generate an array list with some simple strings
        al_strings = new ArrayList<String>();

        // create an array adapter for al_strings and set it on the list view
        aa_strings = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al_strings);
        lv_mainlist.setAdapter(aa_strings);

        String dir = getFilesDir().toString();
        File fr = new File(dir);
        if (fr.exists()) {
            String[] filenames = fr.list();
            for (int i = 0; i < filenames.length; i++) {
                System.out.println("Hello " + filenames[i]);
                al_strings.add(filenames[i]);
            }
        }


        // add in a listener that listens for long clicks on our list items
        lv_mainlist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            // overridden method that we must implement to get access to long clicks
            public boolean onItemLongClick(AdapterView<?> adapterview, View view, int pos, long id) {
                //  tv_display.setText("item " + lv_mainlist.getItemAtPosition(pos) + " long clicked");
                al_run1 = new ArrayList<String>();
                al_run2 = new ArrayList<String>();

                //if ount is 0 no run has been selected, if it is 1 then one run has been selected
                //After which a second can be selected and this will trigger a new activity for comparing both runs
                if (count == 0) {
                    runs1 = lv_mainlist.getItemAtPosition(pos).toString();
                    f1 = new File(getFilesDir(), runs1);
                    count++;
                    Toast.makeText(AnalysisListActivity.this, " One file selected", Toast.LENGTH_LONG).show();

                } else if (count == 1) {
                    runs2 = lv_mainlist.getItemAtPosition(pos).toString();
                    count++;


                    Toast.makeText(AnalysisListActivity.this, " Two files selected", Toast.LENGTH_LONG).show();


                    File f2 = new File(getFilesDir(), runs2);

//Read two files selected and construct two array lists from it
                    try {
                        BufferedReader br = new BufferedReader(new FileReader(f1));
                        String temp = "";
                        while ((temp = br.readLine()) != null) {
                            al_run1.add(temp);
                            System.out.println("file Hello 1 " + temp);
                        }
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }// print the string on the text view


                    try {
                        BufferedReader   br = new BufferedReader(new FileReader(f2));
                        String temp2 = "";
                        while ((temp2 = br.readLine()) != null) {
                            al_run2.add(temp2);
                            System.out.println("file Hello 2 " + temp2);
                        }
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //to check what is contained in both arraylists
                    for(int i=0;i< al_run1.size();i++){
                        System.out.println("Yo 1 " + al_run1.get(i));
                    }
                    for(int i=0;i< al_run2.size();i++){
                        System.out.println("Yo 2 " + al_run2.get(i));
                    }

                    //Sends two array list to new activity representing the two runs to compare
                    Intent intent = new Intent(AnalysisListActivity.this, AnalysisViewActivity.class);
                    intent.putStringArrayListExtra("run1", al_run1);
                    intent.putStringArrayListExtra("run2", al_run2);
                    startActivity(intent);


                    Toast.makeText(AnalysisListActivity.this, " Analyse runs ", Toast.LENGTH_LONG).show();

                }
                return true;
            }

        });
    }

}



