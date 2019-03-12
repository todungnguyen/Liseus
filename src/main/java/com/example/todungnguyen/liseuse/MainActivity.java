package com.example.todungnguyen.liseuse;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private String path = Environment.getExternalStorageDirectory().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openFile();

        Button bt_back = findViewById(R.id.bt_back);
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!path.equals(Environment.getExternalStorageDirectory().toString())) {
                    path = getPreviousPath();
                    openFile();
                }
            }
        });
    }

    public void openFile() {
        File file = new File(path);

        if (file.isDirectory()) {
            File[] files = file.listFiles();

            TextView tv_title = findViewById(R.id.tv_title);
            tv_title.setText("Path: " + path + ". Size: " + files.length + ".");

            final ArrayList<File> listFiles = new ArrayList<>();
            for (int i = 0; i < files.length; i++) {
                listFiles.add(files[i]);
            }

            final ListView lv_elements = (ListView) findViewById(R.id.lv_elements);
            lv_elements.setAdapter(new ArrayAdapter<File>(this, android.R.layout.simple_list_item_1, listFiles));
            lv_elements.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    path = listFiles.get(position).getPath();
                    openFile();
                }
            });

        } else if (file.isFile() && file.canRead()) {
            Intent intent = new Intent(MainActivity.this, ReadActivity.class);
            intent.putExtra("path", path);
            startActivity(intent);

        } else {
            Toast.makeText(getApplicationContext(), "Cannot open.", Toast.LENGTH_SHORT).show();
        }
    }

    public String getPreviousPath() {
        int fin = 0;
        for (int i = 0; i < path.length(); i++) {
            if (path.charAt(i) == '/') fin = i;
        }
        return path.substring(0, fin);
    }
}
