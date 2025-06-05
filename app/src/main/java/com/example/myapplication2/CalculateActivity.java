package com.example.myapplication2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CalculateActivity extends AppCompatActivity {
    private TextView tvName;
    private EditText rmbInput;
    private TextView tvResult;
    private Button calculateBtn;
    private double rate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calculate);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tvName=findViewById(R.id.tv_Name);
        rmbInput=findViewById(R.id.rmb_input);
        tvResult=findViewById(R.id.tv_result);
        calculateBtn=findViewById(R.id.calculate_Btn);

        Intent intent = getIntent();
        String currencyName = intent.getStringExtra("CURRENCY_NAME");
        String rateStr = intent.getStringExtra("RATE");
        if(rateStr == null || rateStr.trim().isEmpty()) {
            Toast.makeText(this, "无效的汇率数据", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        try {
            rate = Double.parseDouble(rateStr.trim());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "汇率格式错误", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        tvName.setText(currencyName);
        try {
            rate = Double.parseDouble(rateStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "汇率错误", Toast.LENGTH_SHORT).show();
            finish();
        }
        calculateBtn.setOnClickListener(v -> calculate());
    }
    private void calculate() {
        String amountStr = rmbInput.getText().toString();
        if (amountStr.isEmpty()) {
            Toast.makeText(this, "输入金额", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            double amount = Double.parseDouble(amountStr);
            double res = amount * rate;
            tvResult.setText(String.format("结果: %.2f", res));
        } catch (NumberFormatException e) {
            Toast.makeText(this, "输入有效数字", Toast.LENGTH_SHORT).show();
        }

    }
}

