package com.example.engsoha.medialarm.Fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.engsoha.medialarm.Data.DatabaseHelper;
import com.example.engsoha.medialarm.Activities.MainActivity;
import com.example.engsoha.medialarm.Model.Medicine;
import com.example.engsoha.medialarm.R;
import com.example.engsoha.medialarm.ReminderManager;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by engsoha on 1/27/17.
 */

public class AddMedicineFragment extends android.support.v4.app.Fragment {
    private TextView Start_Date, Time;
    private EditText Medicine_Name, Dose_Value, Interval, Notes;
    private Spinner Dose_Unit;
    private ImageView Medicine_Image;
    private Button Done, Update, Delete;
    private DatabaseHelper databaseHelper;
    private Medicine medicine;
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd kk:mm:ss";
    private String medicine_name, dosage_value, dosage_unit, start_date, time, interval, note, date_time_txt, format1 = "";
    private Calendar c, Now;
    private int year, month, day, hour, min;
    private byte[] image;
    private Long mRowId;
    private String IntentMendicneName;

    private static final int CAMERA_REQUEST = 1888;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_medicine, container, false);

        //database
        databaseHelper = new DatabaseHelper(getActivity());
        //Medicine Class
        medicine = new Medicine();
        //UI Controls
        Medicine_Name = (EditText)rootView. findViewById(R.id.medname);
        Dose_Value = (EditText)rootView. findViewById(R.id.dosage_num);
        Dose_Unit = (Spinner) rootView. findViewById(R.id.dosage_value);
        Start_Date = (TextView) rootView. findViewById(R.id.startdate);
        Time = (TextView) rootView. findViewById(R.id.time1);
        Interval = (EditText) rootView. findViewById(R.id.interval);
        Notes = (EditText) rootView. findViewById(R.id.note);
        Medicine_Image = (ImageView) rootView. findViewById(R.id.image);
        Done = (Button) rootView. findViewById(R.id.Add);
        Update = (Button) rootView. findViewById(R.id.update);
        Delete = (Button) rootView. findViewById(R.id.del);

        //calender
        c = Calendar.getInstance();
        //time variables
        hour = c.get(Calendar.HOUR_OF_DAY);
        min = c.get(Calendar.MINUTE);
        //date variables
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);


        //fill Dosage spinner
        ArrayAdapter doseUnitAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.dos, R.layout.support_simple_spinner_dropdown_item);
        Dose_Unit.setAdapter(doseUnitAdapter);
        //Start date picker
        Start_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog dd = new DatePickerDialog(getActivity(), R.style.datepicker,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                try {
                                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                    String dateInString = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                    Date date = formatter.parse(dateInString);
                                    c.setTime(date);
                                    Start_Date.setText("Start Date: " + formatter.format(date).toString() + "");

                                    //formatter = new SimpleDateFormat("dd/MMM/yyyy");

                                    //  StartDate.setText(StartDate.getText().toString()+"\n"+formatter.format(date).toString());

                                } catch (Exception ex) {

                                }


                            }
                        }, year, month, day);
                dd.show();


            }
        });

        //Time  picker
        Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog td = new TimePickerDialog(getActivity(), R.style.timepicker,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                try {
                                    if (hourOfDay == 0) {
                                        hourOfDay += 12;
                                        format1 = "AM";
                                        c.set(Calendar.AM_PM, Calendar.AM);
                                    } else if (hourOfDay == 12) {
                                        format1 = "PM";
                                        c.set(Calendar.AM_PM, Calendar.PM);

                                    } else if (hourOfDay > 12) {
                                        hourOfDay -= 12;
                                        format1 = "PM";
                                        c.set(Calendar.AM_PM, Calendar.PM);

                                    } else {
                                        format1 = "AM";
                                        c.set(Calendar.AM_PM, Calendar.AM);
                                    }
                                    c.set(Calendar.HOUR, hourOfDay);
                                    c.set(Calendar.MINUTE, minute);

                                    Time.setText(new StringBuilder().append(hourOfDay).append(" : ").append(minute)
                                            .append(" ").append(format1));

                                } catch (Exception ex) {
                                    Time.setText(ex.getMessage().toString());
                                }
                            }
                        },
                        hour, min,
                        DateFormat.is24HourFormat(getActivity())
                );
                td.show();
            }
        });

        //Take photo
        Medicine_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });
        mRowId = savedInstanceState != null ? savedInstanceState.getLong(DatabaseHelper.KEY_ID) : null;

        Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Now = Calendar.getInstance();
                long now = Now.getTimeInMillis();
                long settime = c.getTimeInMillis();
                medicine_name = Medicine_Name.getText().toString();
                dosage_value = Dose_Value.getText().toString();
                dosage_unit = Dose_Unit.getSelectedItem().toString();
                SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
                date_time_txt = dateTimeFormat.format(c.getTime());
                note = Notes.getText().toString();
                time = Time.getText().toString();
                start_date = Start_Date.getText().toString();
                interval = Interval.getText().toString();

                medicine.setMedicine_name(medicine_name);
                medicine.setDose_value(dosage_value);
                medicine.setDose_unit(dosage_unit);
                medicine.setDate_time(date_time_txt);
                medicine.setInterval(interval);
                medicine.setNote(note);
                medicine.setImage(image);

                if (medicine_name.equals("")) {
                    Medicine_Name.setError("Enter Medicine Name");
                }
                else  if (dosage_value.equals("")){
                    Dose_Value.setError("Enter Dose");
                }
                else if (start_date.equals("Tap to set start date")) {
                    Start_Date.setError("set start date");
                }
                else if (time.equals("Tap to set Time")) {
                    Time.setError("Enter Time");
                } else if (interval.equals("")) {
                    Interval.setError("Enter Interval hours");
                }  else if (now > settime) {
                    Toast.makeText(getActivity(), "Time Passed", Toast.LENGTH_LONG).show();
                } else {


                    String exist = databaseHelper.checkMedicine(medicine_name);
                    if (exist.equals("Not EXIST")) {
                        long id = databaseHelper.createMedicine(medicine);
                        if (id > 0) {
                            mRowId = id;
                            new ReminderManager(getContext()).setReminder(mRowId, c);
                            Intent i = new Intent(getContext(), MainActivity.class);
                            startActivity(i);
                        }


                    } else if (exist.equals("Existed before")) {
                        Medicine_Name.setError("This Medicine was entered before");
                    }

                }

            }

        });

        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Now = Calendar.getInstance();
                long now = Now.getTimeInMillis();
                long settime = c.getTimeInMillis();
                medicine_name = Medicine_Name.getText().toString();
                dosage_value = Dose_Value.getText().toString();
                dosage_unit = Dose_Unit.getSelectedItem().toString();
                SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
                date_time_txt = dateTimeFormat.format(c.getTime());
                note = Notes.getText().toString();
                time = Time.getText().toString();
                start_date = Start_Date.getText().toString();
                interval = Interval.getText().toString();

                medicine.setMedicine_name(medicine_name);
                medicine.setDose_value(dosage_value);
                medicine.setDose_unit(dosage_unit);
                medicine.setDate_time(date_time_txt);
                medicine.setInterval(interval);
                medicine.setNote(note);
                medicine.setImage(image);

                if (medicine_name.equals("")) {
                    Medicine_Name.setError("Enter Medicine Name");
                }
                else  if (dosage_value.equals("")){
                    Dose_Value.setError("Enter Dose");
                }
                else if (start_date.equals("Tap to set start date")) {
                    Start_Date.setError("set start date");
                }
                else if (time.equals("Tap to set Time")) {
                    Time.setError("Enter Time");
                } else if (interval.equals("")) {
                    Interval.setError("Enter Interval hours");
                }  else if (now > settime) {
                    Toast.makeText(getActivity(), "Time Passed", Toast.LENGTH_LONG).show();
                } else {



                    long id = databaseHelper.updateMedicine(medicine, IntentMendicneName);
                    if (id > 0) {
                        mRowId = id;
                        new ReminderManager(getContext()).setReminder(mRowId, c);
                        Toast.makeText(getContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getContext(), MainActivity.class);
                        startActivity(i);
                    }


                }

            }


        });
        //Delete Button
        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                medicine = databaseHelper.getMedicineData(IntentMendicneName);
                long id = medicine.getId();
                databaseHelper.deleteMedicine(id);
                Toast.makeText(getContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getContext(), MainActivity.class);
                startActivity(i);
            }
        });

        setRowIdFromIntent();
        PopulateField();

        return rootView;
    }
    //get medecine name from Intent
    private void setRowIdFromIntent() {
        Intent i = getActivity().getIntent();
        if (i != null && i.hasExtra(Intent.EXTRA_TEXT)) {
            IntentMendicneName = i.getStringExtra(Intent.EXTRA_TEXT);

        }


    }

    //fill activity with medicine data and visable update and delete buttons
    public void PopulateField() {
        if (IntentMendicneName != null) {
            medicine = databaseHelper.getMedicineData(IntentMendicneName);
            Medicine_Name.setText(medicine.getMedicine_name());
            Dose_Value.setText(medicine.getDose_value());
            ArrayAdapter<String> array_spinner = (ArrayAdapter<String>) Dose_Unit.getAdapter();
            Dose_Unit.setSelection(array_spinner.getPosition(medicine.getDose_unit()));
            Interval.setText(medicine.getInterval());
            Notes.setText(medicine.getNote());
            image = medicine.getImage();
            if (image != null) {

                Bitmap bitmap = BitmapFactory.decodeByteArray(medicine.getImage(), 0, medicine.getImage().length);
                Medicine_Image.setImageBitmap(bitmap);
            }
            Done.setVisibility(View.GONE);
            Update.setVisibility(View.VISIBLE);
            Delete.setVisibility(View.VISIBLE);


        }

    }

    //set image view by captured photo and convert it to byte array
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Medicine_Image.setImageBitmap(photo);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 70, stream);
            image = stream.toByteArray();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setRowIdFromIntent();
        PopulateField();

    }

}