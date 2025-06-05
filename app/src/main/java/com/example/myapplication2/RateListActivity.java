package com.example.myapplication2;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class RateListActivity extends ListActivity {

    private static final String TAG="RateListActivity";
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        String[] list_data={"one","two","three","four"};
        ListAdapter adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,list_data);
        setListAdapter(adapter);

        handler=new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg){
                //接收返回的数据项
                if(msg.what==9){
                    List<String> list2=(List<String>) msg.obj;
                    ListAdapter adapter2=new ArrayAdapter<String>(RateListActivity.this,android.R.layout.simple_list_item_1,list_data);
                    setListAdapter(adapter2);
                }
                super.handleMessage(msg);
            }
        };

        Thread t = new Thread(()->{
            List<String> rateList = new ArrayList<>();
            //获取数据，带回到主线程
            try {
                Thread.sleep(2000);
                // 从网页获取数据
                Document doc = Jsoup.connect("https://www.huilvbiao.com/bank/spdb").get();
                Elements rows = doc.select("table.table tbody tr");

                for (Element row : rows) {
                    Elements cols = row.select("td");
                    if (cols.size() >= 3) {
                        String currency = cols.get(0).text();
                        String rate = cols.get(2).text();
                        rateList.add(currency + ": " + rate);
                    }
                }
            } catch (Exception e) {
                Log.i(TAG, "获取汇率数据失败");
                rateList.add("获取汇率数据失败，请检查网络连接");
            }
        //    List<String> list1=new ArrayList<String>();
        //    for(int i=1;i<100;i++){
        //        list1.add("item"+i);
        //    }

            Log.i(TAG,"onCreate:返回数据");
            Message msg=handler.obtainMessage(9,rateList);
            handler.sendMessage(msg);
        });
        t.start();
    }
}