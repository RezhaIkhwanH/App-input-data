package com.example.sertifikasi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class TampilData extends AppCompatActivity {

    public static final String col_Name = "name";
    public static final String col_Alamat = "alamat";
    public static final String col_NOhp = "noHp";
    public static final String col_Location = "location";
    public static final String col_Gambar = "gambar";
    public static final String col_Kelamin = "kelamin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_data);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("tampil data");

        dataBase sql = new dataBase(getApplicationContext());
        TableLayout table = findViewById(R.id.table);

        Cursor data = sql.getAllData();
        if (data.getCount() <= 0) {
            TableRow tableRow = (TableRow) getLayoutInflater().inflate(R.layout.row_style, null);
            table.addView(tableRow);
            return;
        }
        int no = 0;
        data.moveToFirst();
        do {
            no++;
            TableRow tableRow = (TableRow) getLayoutInflater().inflate(R.layout.row_style, null);
            ((TextView) tableRow.findViewById(R.id.no)).setText("" + no);
            ((TextView) tableRow.findViewById(R.id.nama)).setText(data.getString((int) data.getColumnIndex(col_Name)));
            ((TextView) tableRow.findViewById(R.id.alamat)).setText(data.getString((int) data.getColumnIndex(col_Alamat)));
            ((TextView) tableRow.findViewById(R.id.nohp)).setText(data.getString((int) data.getColumnIndex(col_NOhp)));
            ((TextView) tableRow.findViewById(R.id.location)).setText(data.getString((int) data.getColumnIndex(col_Location)));
            ((TextView) tableRow.findViewById(R.id.jenisKelamin)).setText(data.getString((int) data.getColumnIndex(col_Kelamin)));

            Bitmap image = BitmapFactory.decodeFile(data.getString((int) data.getColumnIndex(col_Gambar)));
            ((ImageView) tableRow.findViewById(R.id.image)).setImageBitmap(image);

            table.addView(tableRow);
        } while (data.moveToNext());

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return super.onOptionsItemSelected(item);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}