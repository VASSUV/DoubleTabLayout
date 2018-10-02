package ru.mediasoft.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ru.mediasoft.doubletablayout.DoubleTabLayout;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final DoubleTabLayout doubleTabLayout = findViewById(R.id.double_tab_layout);
        final TimeTabAdapter adapter = new TimeTabAdapter(doubleTabLayout.getLayoutManager());
        doubleTabLayout.setAdapter(adapter);
    }
}
