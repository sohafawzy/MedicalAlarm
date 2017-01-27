package com.example.engsoha.medialarm.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.engsoha.medialarm.Data.DatabaseHelper;
import com.example.engsoha.medialarm.Model.Medicine;
import com.example.engsoha.medialarm.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView medicine_list;
    private ArrayAdapter medicineAdpter;
    private DatabaseHelper databaseHelper;
    private Medicine medicine;
    private ArrayList<String> from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddMedicineActivity.class);
                startActivity(intent);
            }
        });

        medicine_list = (ListView) findViewById(R.id.medicines_list);
        databaseHelper = new DatabaseHelper(this);
        from = new ArrayList<>();
        List<Medicine> medicines = databaseHelper.getAllMedicine();
        for (int i = 0; i < medicines.size(); i++) {
            medicine = medicines.get(i);
            from.add(medicine.getMedicine_name());
        }
        medicineAdpter = new ArrayAdapter<>(this, R.layout.medicines_list_row, R.id.text1, from);
        medicine_list.setAdapter(medicineAdpter);
        //click List item
        medicine_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String medicineName = (String) medicineAdpter.getItem(i);
                Intent intent = new Intent(getApplicationContext(), AddMedicineActivity.class).putExtra(Intent.EXTRA_TEXT, medicineName);
                startActivity(intent);
            }
        });


    }

    //prevent back button
    @Override
    public void onBackPressed() {

        moveTaskToBack(true);
    }


}
