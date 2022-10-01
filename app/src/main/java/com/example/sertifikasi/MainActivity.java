package com.example.sertifikasi;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    public static final String col_Name = "name";
    public static final String col_Alamat = "alamat";
    public static final String col_NOhp = "noHp";
    public static final String col_Location = "location";
    public static final String col_Gambar = "gambar";
    public static final String col_Kelamin = "kelamin";


    public EditText nama, alamat, nohp, inputlocation;
    String kelamin;
    Bitmap image;
    ImageView imageView;
    Button sumbit, getLocation, getImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("daftar");
        mintaAces();
        sumbit = findViewById(R.id.btnSumbit);
        sumbit.setOnClickListener(v -> simpan());

        getLocation = findViewById(R.id.btnLocation);
        getLocation.setOnClickListener(v -> getLocation());

        getImage = findViewById(R.id.btnImage);
        getImage.setOnClickListener(v -> getImage());

    }

    private void getImage() {
        if (ActivityCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            mintaAces();
            Toast.makeText(this, "berikan akses memory", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent ambilGambar = new Intent(Intent.ACTION_PICK);
        ambilGambar.setType("image/*");
        startActivityForResult(ambilGambar, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            Toast.makeText(this, "pilih 1 gambar", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            imageView = findViewById(R.id.imageView);
            Uri uriImage = data.getData();
            imageView.setImageURI(uriImage);
            InputStream imgStream = getContentResolver().openInputStream(uriImage);
            this.image = BitmapFactory.decodeStream(imgStream);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || (ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            mintaAces();
            Toast.makeText(this, "berikan akses lokasi", Toast.LENGTH_LONG).show();
            return;
        }
        FusedLocationProviderClient client;
        client = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                inputlocation = findViewById(R.id.inputCordinat);
                inputlocation.setText(location.getLatitude() + "," + location.getLongitude());

            }
        });


    }

    private void simpan() {
        nama = findViewById(R.id.inputNama);
        alamat = findViewById(R.id.inputAlamat);
        nohp = findViewById(R.id.inputNomer);
        inputlocation = findViewById(R.id.inputCordinat);


        if (nama.getText().toString().equals("") ||
                alamat.getText().toString().equals("")  ||
                nohp.getText().toString().equals("") ||
                inputlocation.getText().toString().equals("") ||
                kelamin == null ||
                image == null) {
            Toast.makeText(this, "isi lengkap data", Toast.LENGTH_SHORT).show();
            return;
        }
        ContentValues dataInput = new ContentValues();
        dataInput.put(col_Name, nama.getText().toString());
        dataInput.put(col_Alamat, alamat.getText().toString());
        dataInput.put(col_NOhp, "+64 " + nohp.getText().toString());
        dataInput.put(col_Location, inputlocation.getText().toString());
        dataInput.put(col_Kelamin, kelamin);
        dataInput.put(col_Gambar, simpanGambar(image));
        dataBase sql = new dataBase(getApplicationContext());
        sql.addData(dataInput);
        Toast.makeText(this, "data berhasil di tambah", Toast.LENGTH_SHORT).show();

        clear();
        Intent tampilData = new Intent(this, TampilData.class);
        startActivity(tampilData);

    }

    public String simpanGambar(Bitmap img) {
        File appRoot = getApplicationContext().getDir("data", MODE_PRIVATE);
        File imgFolder = new File(appRoot, "image");
        if (!imgFolder.exists()) {
            imgFolder.mkdir();
        }
        String imgName = Math.random() + "image.jpg";
        File imgFile = new File(imgFolder, imgName);
        try {
            FileOutputStream imgOut = new FileOutputStream(imgFile);
            img.compress(Bitmap.CompressFormat.JPEG, 100, imgOut);
            imgOut.flush();
            imgOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imgFile.getAbsoluteFile().toString();

    }

    public void mintaAces() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.lakiLaki:
            case R.id.perempuan:
                if (checked)
                    kelamin = ((RadioButton) view).getText().toString();
                break;

        }
    }

    public void clear() {
        ((TextView) findViewById(R.id.inputNama)).setText(null);
        ((TextView) findViewById(R.id.inputNama)).requestFocus();
        ((TextView) findViewById(R.id.inputAlamat)).setText(null);
        ((TextView) findViewById(R.id.inputNomer)).setText(null);
        ((TextView) findViewById(R.id.inputCordinat)).setText(null);
        kelamin = null;
        image = null;
        imageView.setImageBitmap(null);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        radioGroup.clearCheck();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}