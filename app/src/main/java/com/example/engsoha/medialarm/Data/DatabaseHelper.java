package com.example.engsoha.medialarm.Data;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.engsoha.medialarm.Model.Medicine;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "MedicalAlertDB";

    // Table Names
    private static final String TABLE_MEDICINE = "Medicine";

    // Medicine Table - column names
    public static final String KEY_ID = "id";
    public static final String KEY_MEDICINE_NAME = "medicine_name";
    public static final String KEY_DOSE_VALUE = "dose_value";
    public static final String KEY_DOSE_UNIT = "dose_unit";
    public static final String KEY_DATE_TIME = "date_time";
    public static final String KEY_INTERVAL = "interval";
    public static final String KEY_NOTES = "note";
    public static final String KEY_IMAGE = "image";

    // Medicine  table create statement
    private static final String CREATE_TABLE_MEDICINE = "CREATE TABLE "
            + TABLE_MEDICINE + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_MEDICINE_NAME + " TEXT UNIQUE, "
            + KEY_DOSE_VALUE + " TEXT, "
            + KEY_DOSE_UNIT + " TEXT,"
            + KEY_DATE_TIME + " TEXT, "
            + KEY_INTERVAL + " TEXT,"
            + KEY_NOTES + " TEXT, "
            + KEY_IMAGE + " BLOB);";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_MEDICINE);

    }

    //Insert new Medicine
    public long createMedicine(Medicine medicine) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MEDICINE_NAME, medicine.getMedicine_name());
        values.put(KEY_DOSE_VALUE, medicine.getDose_value());
        values.put(KEY_DOSE_UNIT, medicine.getDose_unit());
        values.put(KEY_DATE_TIME, medicine.getDate_time());
        values.put(KEY_INTERVAL, medicine.getInterval());
        values.put(KEY_NOTES, medicine.getNote());
        values.put(KEY_IMAGE, medicine.getImage());

        // insert row
        long medicine_id = db.insert(TABLE_MEDICINE, null, values);

        return medicine_id;
    }

    //Check if medicine added before or not
    public String checkMedicine(String medicine_name) {

        SQLiteDatabase db = this.getReadableDatabase();
        String status;
        Cursor cursor = db.query(TABLE_MEDICINE, null, KEY_MEDICINE_NAME + "=?", new String[]{medicine_name}, null, null, null);
        if (cursor.getCount() < 1) {
            cursor.close();
            status = "Not EXIST";
        } else {
            cursor.moveToFirst();
            status = "Existed before";
            cursor.close();
        }
        return status;
    }

    //Check if medicine still in database
    public String checkMedicineById(long id) {
        String selectQuery = "SELECT * FROM " + TABLE_MEDICINE+" WHERE "+KEY_ID+" = "+id;
        String status="";
        Log.e(LOG, selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() < 1) {
            cursor.close();
            status = "Not EXIST";
        } else {
            cursor.moveToFirst();
            status = "Existed";
            cursor.close();
        }
        return status;
    }
    // get all medicines

    public List<Medicine> getAllMedicine() {

        List<Medicine> medicines = new ArrayList<Medicine>();
        String selectQuery = "SELECT  * FROM " + TABLE_MEDICINE;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Medicine medicine = new Medicine();
                medicine.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                medicine.setMedicine_name(c.getString(c.getColumnIndex(KEY_MEDICINE_NAME)));
                medicine.setDose_value(c.getString(c.getColumnIndex(KEY_DOSE_VALUE)));
                medicine.setDose_unit(c.getString(c.getColumnIndex(KEY_DOSE_UNIT)));
                medicine.setDate_time(c.getString(c.getColumnIndex(KEY_DATE_TIME)));
                medicine.setInterval(c.getString(c.getColumnIndex(KEY_INTERVAL)));
                medicine.setNote(c.getString(c.getColumnIndex(KEY_NOTES)));
                medicine.setImage(c.getBlob(c.getColumnIndex(KEY_IMAGE)));

                // adding to medicine list
                medicines.add(medicine);
            } while (c.moveToNext());
        }

        return medicines;
    }

    //get one Medicine
    public Medicine getMedicineData(String medicine_name) {

        Medicine medicine = new Medicine();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_MEDICINE, null, KEY_MEDICINE_NAME + "=?", new String[]{medicine_name}, null, null, null);
        int cc=c.getCount();
        // looping through all rows and adding to list
        if (!(c.getCount() < 1)) {

            c.moveToFirst();
            medicine.setId(c.getInt(c.getColumnIndex(KEY_ID)));
            medicine.setMedicine_name(c.getString(c.getColumnIndex(KEY_MEDICINE_NAME)));
            medicine.setDose_value(c.getString(c.getColumnIndex(KEY_DOSE_VALUE)));
            medicine.setDose_unit(c.getString(c.getColumnIndex(KEY_DOSE_UNIT)));
            medicine.setDate_time(c.getString(c.getColumnIndex(KEY_DATE_TIME)));
            medicine.setInterval(c.getString(c.getColumnIndex(KEY_INTERVAL)));
            medicine.setNote(c.getString(c.getColumnIndex(KEY_NOTES)));
            medicine.setImage(c.getBlob(c.getColumnIndex(KEY_IMAGE)));
            c.close();
        }


        return medicine;
    }
    //get Interval medicine by ID
    public String getMedicineDataById(long id) {
        String interval = "";
        String selectQuery = "SELECT " +KEY_INTERVAL+" FROM " + TABLE_MEDICINE+" WHERE "+KEY_ID+" = "+id;
        Log.e(LOG, selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (!(c.getCount() < 1)) {

            c.moveToFirst();
            interval = (c.getString(c.getColumnIndex(KEY_INTERVAL)));

            c.close();
        }


        return interval;
    }
    //get  medicine by ID
    public Medicine getMedicineDatabyId(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_MEDICINE, null, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);

        Medicine medicine=new Medicine();
        // looping through all rows and adding to list
        if (!(c.getCount() < 1)) {

            c.moveToFirst();
            medicine.setId(c.getInt(c.getColumnIndex(KEY_ID)));
            medicine.setMedicine_name(c.getString(c.getColumnIndex(KEY_MEDICINE_NAME)));
            medicine.setDose_value(c.getString(c.getColumnIndex(KEY_DOSE_VALUE)));
            medicine.setDose_unit(c.getString(c.getColumnIndex(KEY_DOSE_UNIT)));
            medicine.setDate_time(c.getString(c.getColumnIndex(KEY_DATE_TIME)));
            medicine.setInterval(c.getString(c.getColumnIndex(KEY_INTERVAL)));
            medicine.setNote(c.getString(c.getColumnIndex(KEY_NOTES)));
            medicine.setImage(c.getBlob(c.getColumnIndex(KEY_IMAGE)));
            c.close();
        }


        return medicine;
    }

    //update Medicine Table
    public long updateMedicine(Medicine medicine,String medicine_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        long id=0;

        ContentValues values = new ContentValues();
        values.put(KEY_MEDICINE_NAME, medicine.getMedicine_name());
        values.put(KEY_DOSE_VALUE, medicine.getDose_value());
        values.put(KEY_DOSE_UNIT, medicine.getDose_unit());
        values.put(KEY_DATE_TIME, medicine.getDate_time());
        values.put(KEY_INTERVAL, medicine.getInterval());
        values.put(KEY_NOTES, medicine.getNote());
        values.put(KEY_IMAGE, medicine.getImage());
        // updating row
        db.update(TABLE_MEDICINE, values, KEY_MEDICINE_NAME + " = ?",
                new String[]{medicine_name});

        db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_MEDICINE,new String[]{KEY_ID},KEY_MEDICINE_NAME+"='"+medicine_name+"'",null,null,null,null);
        //db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (!(c.getCount() < 1)) {

            c.moveToFirst();
            id= (c.getInt(c.getColumnIndex(KEY_ID)));

            c.close();
        }

        return  id;
    }
    //get cursor l medicine i
    public Cursor fetchAllReminders() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_MEDICINE, new String[]{KEY_ID, KEY_MEDICINE_NAME,
                KEY_DOSE_VALUE, KEY_DOSE_UNIT, KEY_DATE_TIME, KEY_INTERVAL, KEY_NOTES}, null, null, null, null, null);
    }


    //Delete Medicine
    public void deleteMedicine(long id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_MEDICINE, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDICINE);
        // create new tables
        onCreate(db);

    }
}
