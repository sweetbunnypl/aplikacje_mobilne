package com.example.lista3_bolanowski;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.UUID;

public class CrimeActivity extends AppCompatActivity {

    private static final int CAMERA_INTENT = 1;

    private Uri savePicturePath = null;

    private DBHandler dbHandler;
    private Intent intent;
    private int crimeId;
    private Crime crime;

    private Button datePicker;
    private EditText titleText;
    private CheckBox checkBox;
    private Calendar date;
    private ImageView imageView;
    private ImageButton imageButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime);

        dbHandler = new DBHandler(this);

        intent = getIntent();
        crimeId = intent.getIntExtra("id", 0);
        crime = dbHandler.getCrime(crimeId);

        datePicker = findViewById(R.id.datePicker);
        titleText = findViewById(R.id.titleTextEdit);
        checkBox = findViewById(R.id.checkBoxSolved);
        imageView = findViewById(R.id.showImage);
        imageButton = findViewById(R.id.getImage);

        checkBox.setChecked(crime.isSolved());
        titleText.setText(crime.getTitle());
        datePicker.setText(crime.getDate().toString());
        addImageListener();
        if (crime.getImage() != null) {
            imageView.setImageBitmap(BitmapFactory.decodeFile(crime.getImage()));
        }
        else {
            imageView.setImageResource(R.drawable.ic_baseline_panorama_100);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_crime, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteCrimeButton:
                deleteCrime();
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    public void setCheckBox(View view) {
        crime.setSolved(checkBox.isChecked());
        dbHandler.editCrime(crime);
    }

    public void pickDate(View view) {

        date = Calendar.getInstance();

        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(Calendar.HOUR_OF_DAY, crime.getDate().getHours());
                date.set(Calendar.MINUTE, crime.getDate().getMinutes());
                date.set(year, monthOfYear, dayOfMonth);
                datePicker.setText(date.getTime().toString());

                crime.setDate(date.getTime());
                dbHandler.editCrime(crime);

                new TimePickerDialog(peekAvailableContext(), new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        datePicker.setText(date.getTime().toString());

                        crime.setDate(date.getTime());
                        dbHandler.editCrime(crime);
                    }
                }, crime.getDate().getHours(), crime.getDate().getMinutes(), true).show();
            }
        }, crime.getDate().getYear()+1900, crime.getDate().getMonth(), crime.getDate().getDate()).show();

    }

    @Override
    protected void onPause() {
        super.onPause();
        crime.setTitle(titleText.getText().toString());
        dbHandler.editCrime(crime);
    }

    public void addImageListener() {
        imageButton.setOnClickListener(v -> {
            Dexter.withContext(this).withPermission(
                    Manifest.permission.CAMERA
            ).withListener(new PermissionListener() {
                @Override
                public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAMERA_INTENT);
                }

                @Override
                public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                }

                @Override
                public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                    showRationaleDialog();
                }
            }).onSameThread().check();
        });
    }

    private void showRationaleDialog(){
        new AlertDialog.Builder(this)
                .setMessage("Camera permissions needed")
                .setPositiveButton("Ask me", (dialog, which) -> {
                    try{
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    } catch (ActivityNotFoundException e){
                        e.printStackTrace();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == CAMERA_INTENT && data != null) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(thumbnail);
                savePicturePath = savePicture(thumbnail);
                crime.setImage(savePicturePath.toString());
                dbHandler.editCrime(crime);
            }
        }
    }

    private Uri savePicture(Bitmap bitmap){
        ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
        File file = wrapper.getDir("myGallery", Context.MODE_PRIVATE);
        file = new File(file, UUID.randomUUID().toString() + ".jpg");

        try {
            OutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.flush();
            stream.close();
        } catch (IOException e){
            e.printStackTrace();
        }

        return Uri.parse(file.getAbsolutePath());
    }

    public void deleteCrime(){
        dbHandler.deleteCrime(crime);
        finish();
    }
}
