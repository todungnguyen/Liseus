package com.example.todungnguyen.liseuse;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ReadActivity extends AppCompatActivity {
    private pageView page_view;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        checkAndRequestPermissions();

        Intent intent = getIntent();
        path = intent.getStringExtra("path");

        if(path != null) {
            page_view = findViewById(R.id.pageView);
            readData();
        } else {
            Toast.makeText(this, "Path is null.", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkAndRequestPermissions() {
        String[] permissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(permission);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 1);
        }
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public void readData() {
        if (isExternalStorageReadable()) {
            StringBuffer buffer = new StringBuffer();
            try {
                File file = new File(path);
                FileInputStream fis = new FileInputStream(file);

                if (fis != null) {
                    BufferedReader input = new BufferedReader(new InputStreamReader(fis));
                    String line;
                    while ((line = input.readLine()) != null) {
                        buffer.append(line);
                        buffer.append("\n");
                    }
                    fis.close();
                    page_view.splitTextIntoPages(buffer);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Cannot read from external storage.", Toast.LENGTH_SHORT).show();
        }
    }
}
