package com.example.myapplication2;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication2.SettingActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class exchangeActivity extends AppCompatActivity implements Runnable{

    private static final String TAG = "exchangeActicity";

    private EditText inputRmb;
    private TextView tvResult;
    float dollarRate = 34.5f;
    float euroRate = 666.66f;
    float wonRate = 345f;

    Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_exchange);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        inputRmb = findViewById(R.id.input_rmb);
        tvResult = findViewById(R.id.result);
        //读取数据
        SharedPreferences sp=getSharedPreferences("myrate", exchangeActivity.MODE_PRIVATE);
        dollarRate=sp.getFloat("sp_dollar_key",1.23f);
        euroRate=sp.getFloat("sp_euro_key",2.12f);
        wonRate=sp.getFloat("sp_won_key",3.88f);
        Log.i(TAG,"onCreate:from sp dollarRate="+dollarRate);
        Log.i(TAG,"onCreate:from sp euroRate="+euroRate);
        Log.i(TAG,"onCreate:from sp wonRate="+wonRate);

        handler=new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@Nullable Message msg){
                Log.i(TAG,"handleMessage:接收消息");
                if(msg.what==7){
                    String str=(String)msg.obj;
                    Log.i(TAG,"handleMessage:str="+str);
                    tvResult.setText(str);
                }
                //处理返回
                super.handleMessage(msg);
            }
        };

    }
    public void htclick(View btn){
        //thread
        Log.i(TAG,"onCreate:启动线程");
        Thread t=new Thread(this);
        t.start();//this.run()
    }

    public void myclick(View btn) {
        Log.i(TAG, "myclick:2222222222222");
        //获取输入
        String strInput = inputRmb.getText().toString();

        try {
            //计算，输入x汇率=result
            float inputf = Float.parseFloat(strInput);
            float result = 0;
            if (btn.getId() == R.id.btn_dollar) {
                result = inputf * dollarRate;
            } else if (btn.getId() == R.id.btn_euro) {
                result = inputf * euroRate;
            } else {
                result = inputf * wonRate;
            }

            //显示结果
            tvResult.setText(String.valueOf(result));
        } catch (NumberFormatException e) {
            //tvResult.setText("请输入正确的数据")
            Toast.makeText(this, "请输入正确的数据", Toast.LENGTH_SHORT).show();
        }

    }

    ActivityResultLauncher luncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                //处理返回
                if (result.getResultCode()==3){
                    Intent retIntent=result.getData();
                    Bundle retBundle=retIntent.getExtras();
                    dollarRate=retBundle.getFloat("ret_dollar");
                    euroRate=retBundle.getFloat("ret_euro");
                    wonRate=retBundle.getFloat("ret_won");
                    Log.i(TAG,"onActivityResult:dollarRate="+dollarRate);
                    Log.i(TAG,"onActivityResult:euroRate="+euroRate);
                    Log.i(TAG,"onActivityResult:wonRate="+wonRate);

                    SharedPreferences sp=getSharedPreferences("myrate", exchangeActivity.MODE_PRIVATE);
                    SharedPreferences.Editor editor=sp.edit();
                    editor.putFloat("sp_dollar_key",dollarRate);
                    editor.putFloat("sp_euro_key",euroRate);
                    editor.putFloat("sp_won_key",wonRate);
                    editor.apply();
                    Log.i(TAG,"save to sp");

                }
            });


    public void openConfig(View btn) {
        //打开新窗口
        Intent intent = new Intent(this, SettingActivity.class);
        intent.putExtra("ret_dollar", dollarRate);
        intent.putExtra("ret_euro", euroRate);
        intent.putExtra("ret_won", wonRate);

        Log.i(TAG, "openConfig:dollarRate=" + dollarRate);
        Log.i(TAG, "openConfig:euroRate=" + euroRate);
        Log.i(TAG, "openConfig:wonRate=" + wonRate);

        //startActivityForResult(intent,6);
        luncher.launch(intent);
    }

    @Override
    public void run(){
        Log.i(TAG,"run:run()");

        //获取网络数据
        URL url=null;
        String html= "";
        try {
              url=new URL("https://www.boc.cn/sourcedb/whpj/");
              HttpURLConnection http=(HttpURLConnection) url.openConnection();
              InputStream in=http.getInputStream();

              html=inputStream2String(in);
              Log.i(TAG,"run:html="+html);
            Document doc = Jsoup.connect("https://www.boc.cn/sourcedb/whpj/").get();
            Elements tables=doc.getElementsByTag("table");
            Element table=tables.first();
            Log.i(TAG,"run:table2="+table);
            Elements rows=table.getElementsByTag("tr");
            rows.remove(0);
            for(Element row:rows){
                //Log.i(TAG,"run:row="+row);
                Elements tds = row.getElementsByTag("td");
                Element td1 = tds.first();
                Element td2 = tds.get(7);
                Log.i(TAG,"run:td1=" + td1.text()+"->"+ td2.text());
                //Log.i(TAG,"run: tdl="+ td1.html()+"->"+ td2.html());
                html += (td1.text()+"=>"+ td2.text()+ "\n");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //发送消息
        Message msg=handler.obtainMessage(7,html);
        handler.sendMessage(msg);
        Log.i(TAG,"run:消息发送完毕");
    }

    private String inputStream2String(InputStream inputStream) throws IOException{
        final int bufferSize=1024;
        final char[] buffer=new char[bufferSize];
        final StringBuilder out=new StringBuilder();
        Reader in=new InputStreamReader(inputStream,"utf-8");
        while(true){
            int rsz=in.read(buffer,0,buffer.length);
            if(rsz<0)
                break;
            out.append(buffer,0,rsz);
        }
        return out.toString();
    }

    //@Override
    //protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    //    super.onActivityResult(requestCode, resultCode, data);

    //从data获取带回的数据
    //   if(requestCode==6&&resultCode==3){

    //        Bundle ret=data.getExtras();
    //        dollarRate=ret.getFloat("key_dollar");
    //        euroRate=ret.getFloat("key_euro");
    //        wonRate=ret.getFloat("key_won");
    //        Log.i(TAG,"onActivityResult:dollarRate="+dollarRate);
    //        Log.i(TAG,"onActivityResult:euroRate="+euroRate);
    //        Log.i(TAG,"onActivityResult:wonRate="+wonRate);
    //    }
    //}
}