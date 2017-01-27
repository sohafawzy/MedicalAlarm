package com.example.engsoha.medialarm.Activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.engsoha.medialarm.Data.DatabaseHelper;
import com.example.engsoha.medialarm.Model.Medicine;
import com.example.engsoha.medialarm.R;

public class AlarmActivity extends AppCompatActivity {

    private Long mRowId;
    private TextView Medicine_Name,Dose,Note;
    private ImageView image;
    private byte[] imageByteArray;
    private String Dose_txt;
    private DatabaseHelper databaseHelper;
    private Medicine medicine;
    private Ringtone r;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();
        mRowId = savedInstanceState != null ? savedInstanceState.getLong(DatabaseHelper.KEY_ID) : null;
        databaseHelper=new DatabaseHelper(this);
        //controls
        Medicine_Name=(TextView)findViewById(R.id.medicine_name);
        Dose=(TextView)findViewById(R.id.dose);
        Note=(TextView)findViewById(R.id.notes);
        image=(ImageView)findViewById(R.id.image);

        setRowIdFromIntent();
        populateFields();



    }

    private void setRowIdFromIntent() {
        if (mRowId == null) {
            Bundle extras = getIntent().getExtras();
            mRowId = extras != null ? extras.getLong(DatabaseHelper.KEY_ID)
                    : null;

        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(DatabaseHelper.KEY_ID,mRowId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        databaseHelper.closeDB();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setRowIdFromIntent();
    }

    private void populateFields()  {



        // Only populate the text boxes and change the calendar date
        // if the row is not null from the database.
        if (mRowId != null) {
            medicine=databaseHelper.getMedicineDatabyId(mRowId);
            Medicine_Name.setText(medicine.getMedicine_name());
            Dose_txt=medicine.getDose_value()+" "+medicine.getDose_unit();
            Dose.setText(Dose_txt);
            Note.setText(medicine.getNote());
            imageByteArray=medicine.getImage();
            if (imageByteArray!=null){

                Bitmap bitmap= BitmapFactory.decodeByteArray(medicine.getImage(),0, medicine.getImage().length);
                image.setImageBitmap(bitmap); }


        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        r.stop();
        moveTaskToBack(true);

    }
}

