package com.example.myapplication2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private static final String TAG = "CustomListActivity";
    private ListView mylist;
    Handler handler;
    private ArrayList<HashMap<String,String>> listItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_custom_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mylist=findViewById(R.id.mylist2);

        ArrayList<HashMap<String,String>>listItems=new ArrayList<HashMap<String,String>>();
        for(int i=0;i<10;i++){
            HashMap<String,String> map=new HashMap<String, String>();
            map.put("ItemTitle","Rate:"+i);
            map.put("ItemDetail","detail"+i);
            listItems.add(map);
        }

        SimpleAdapter listItemAdapter=new SimpleAdapter(this,
                listItems,
                R.layout.list_item,
                new String[] {"ItemTitle","ItemDetail"},
                new int[] {R.id.itemTitle,R.id.itemDetail}
                );
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == 6) {
                    Log.i(TAG, "handleMessage: 获取网络数据");
                    ArrayList<HashMap<String, String>> list2 = (ArrayList<HashMap<String, String>>) msg.obj;
                    MyAdapter adapter2 = new MyAdapter(CustomListActivity.this, R.layout.list_item, list2);
                    if (mylist != null) {
                        mylist.setAdapter(adapter2);
                    } else {
                        Log.e(TAG, "mylist is null");
                    }
                    mylist.setAdapter(adapter2);
                }
                super.handleMessage(msg);
            }
        };

        MyAdapter myAdapter=new MyAdapter(this,R.layout.list_item,listItems);
        mylist.setAdapter(listItemAdapter);
        mylist.setOnItemClickListener(this);

        new Thread(() -> {
            try {
                Document doc = Jsoup.connect("https://www.huilvbiao.com/bank/spdb").userAgent("Mozilla/5.0").timeout(10000).get();
                ArrayList<HashMap<String, String>> retlist = new ArrayList<HashMap<String, String>>();
                Element tables = doc.select("table").first();
                if (tables != null) {
                    Elements trs = tables.getElementsByTag("tr");
                    for (Element tr : trs) {
                        Elements tds = tr.children();
                        if (tds.size() >= 5) {
                            String currencyname = tds.get(0).text().trim();
                            String rateStr = tds.get(1).text().trim();
                            String sellRateStr = tds.get(2).text().trim();

                            if (!rateStr.isEmpty() && rateStr.matches("[0-9.]+")
                                    && !sellRateStr.isEmpty() && sellRateStr.matches("[0-9.]+")) {
                                Log.i("当前数据", currencyname + "==>" + rateStr);
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put("ItemTitle", currencyname);
                                map.put("ItemDetail", rateStr);
                                retlist.add(map);
                            } else {
                                Log.w("当前数据", "Invalid data for " + currencyname);
                            }
                        }
                    }
                }
                Message msg = handler.obtainMessage(6, retlist);
                handler.sendMessage(msg);
                Log.i(TAG, "onCreate: handler.sendMessage(msg)");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG, "onItemClick: ");
        Object itemAtPosition = mylist.getItemAtPosition(position);
        HashMap<String, String> map = (HashMap<String, String>) itemAtPosition;

        String currencyname = map.get("ItemTitle");
        String rateStr = map.get("ItemDetail");
        Intent intent = new Intent(CustomListActivity.this, CalculateActivity.class);
        intent.putExtra("CURRENCY_NAME", currencyname);
        intent.putExtra("RATE", rateStr);

        startActivity(intent);
    }
    //@Override
    //public boolean onItemLongClick(AdapterView<?> adapterView,View view,int position,long id){
    //    Log.i(TAG,"onItemLonngClick");

    //    return true;
    //}
}