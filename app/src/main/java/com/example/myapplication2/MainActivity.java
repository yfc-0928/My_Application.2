package com.example.myapplication2;

import android.content.ClipData;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText inp1, inp2;
    private TextView result, suggetion;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        result = findViewById(R.id.result);
        suggetion = findViewById(R.id.suggestion);
        inp1 = findViewById(R.id.inp1);
        inp2 = findViewById(R.id.inp2);
        btn = findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jisuanBMI();
            }
        });
    }

    private void jisuanBMI() {
        String heightStr = inp1.getText().toString();
        String weightStr = inp2.getText().toString();

        if (!heightStr.isEmpty() && !weightStr.isEmpty()) {
            try {
                float h1 = Float.parseFloat(heightStr);
                float w1 = Float.parseFloat(weightStr);

                float bmi = w1 / (h1 * h1);

                String bmiResult = String.format("%.2f", bmi);
                result.setText("BMI: " + bmiResult);

                if (bmi < 18.5) {
                    suggetion.setText("体重过轻，建议增加营养摄入,适当增加体重。");
                } else if (bmi >= 18.5 && bmi < 23.9) {
                    suggetion.setText("体重正常");
                } else if (bmi >= 24 && bmi < 28) {
                    suggetion.setText("体重过重，建议适当减肥。");
                } else {
                    suggetion.setText("肥胖，建议严格控制饮食并增加运动。");
                }
            } catch (NumberFormatException e) {
                result.setText("输入无效，请输入有效的数字！");
                suggetion.setText("");
            }
        } else {
            result.setText("请输入身高和体重。");
            suggetion.setText("");
        }
    }
}
