package com.example.myapplication2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ScoringActivity extends AppCompatActivity {

    public static final String TAG = "ScoringActivity";

    private TextView scoring1;

    private TextView scoring2;

    private int ints = 0;
    private int teams = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_scoring);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        scoring1 = findViewById(R.id.s1);
        scoring2 = findViewById(R.id.s2);

        if (scoring1 != null) scoring1.setText("0");
        if (scoring2 != null) scoring2.setText("0");

        findViewById(R.id.btn_reset).setOnClickListener(v -> resetScores());
    }

    public void click(View btn) {
        Log.i(TAG, "click:111111111111111");
        //String s=(String)scoring1.getText();
        //int ints=Integer.parseInt(s);

        if (btn.getId() == R.id.btn3) {
            ints += 3;
        } else if (btn.getId() == R.id.btn2) {
            ints += 2;
        } else {
            ints += 1;
        }
        scoring1.setText(String.valueOf(ints));
    }

    public void onclick(View btns) {
        Log.i(TAG, "click:111111111111111");
        //String t=(String)scoring1.getText();
        //int teams=Integer.parseInt(t);

        if (btns.getId() == R.id.btn23) {
            teams += 3;
        } else if (btns.getId() == R.id.btn22) {
            teams += 2;
        } else {
            teams += 1;
        }
        scoring2.setText(String.valueOf(teams));
    }
    private void resetScores() {
        ints = 0;
        teams = 0;
        if (scoring1 != null) scoring1.setText("0");
        if (scoring2 != null) scoring2.setText("0");
    }
}