package com.example.myapplication2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingActivity extends AppCompatActivity {
    private static final String TAG = "SettingActivity";
    private EditText inpDollar, inpEuro, inpWon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        inpDollar = findViewById(R.id.dollar_rate);
        inpEuro = findViewById(R.id.euro_rate);
        inpWon = findViewById(R.id.won_rate);

        // 获取输入的数据
        Intent intent = getIntent();
        float dollar = intent.getFloatExtra("ret_dollar", 0.1f);
        float euro = intent.getFloatExtra("ret_euro", 0.1f);
        float won = intent.getFloatExtra("ret_won", 0.1f);

        Log.i(TAG, "onCreate: dollar=" + dollar);
        Log.i(TAG, "onCreate: euro=" + euro);
        Log.i(TAG, "onCreate: won=" + won);

        // 在控件中显示传入的数据
        inpDollar.setText(String.valueOf(dollar));
        inpEuro.setText(String.valueOf(euro));
        inpWon.setText(String.valueOf(won));
    }

    public void save(View btn) {
        // 获取新的汇率
        String dollarStr = inpDollar.getText().toString();
        String euroStr = inpEuro.getText().toString();
        String wonStr = inpWon.getText().toString();

        if (dollarStr.isEmpty() || euroStr.isEmpty() || wonStr.isEmpty()) {
            Toast.makeText(this, "请输入所有汇率", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.i(TAG, "save: dollarStr=" + dollarStr);
        Log.i(TAG, "save: euroStr=" + euroStr);
        Log.i(TAG, "save: wonStr=" + wonStr);

        try {
            float dollar = Float.parseFloat(dollarStr);
            float euro = Float.parseFloat(euroStr);
            float won = Float.parseFloat(wonStr);

            Intent retIntent = new Intent();
            retIntent.putExtra("ret_dollar", dollar);
            retIntent.putExtra("ret_euro", euro);
            retIntent.putExtra("ret_won", won);

            setResult(3, retIntent);
            finish();
        } catch (NumberFormatException e) {
            Log.e(TAG, "save: 输入格式错误", e);
            Toast.makeText(this, "请输入有效的汇率数值", Toast.LENGTH_SHORT).show();
        }
    }
}