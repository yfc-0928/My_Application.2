/*package com.example.myapplication2;

import android.app.Activity;
import android.app.ListActivity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

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
import java.util.List;

public class MainListActivity extends ListActivity {

    private static final String TAG="MainListActivity";
    private List<String> rateList = new ArrayList<>();
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ListView mylist=findViewById(android.R.id.list);
        Button btn=findViewById(R.id.button6);
    //    List<String> list1=new ArrayList<String>();
    //    for(int i=1;i<100;i++){
    //        list1.add("item"+i);
    //    }
        ListAdapter adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,rateList);
        mylist.setAdapter(adapter);

        handler=new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg){
                //接收返回的数据项
                if(msg.what==9){
                    List<String> list2=(List<String>) msg.obj;
                    ListAdapter adapter2=new ArrayAdapter<String>(MainListActivity.this,android.R.layout.simple_list_item_1,list2);
                    setListAdapter(adapter2);
                }
                super.handleMessage(msg);
          }
        };
    }
    public void dtclick(View btn) {
        Thread t = new Thread(() -> {
            List<String> rateList = new ArrayList<>();
            //获取数据，带回到主线程
            try {
                Thread.sleep(2000);
        //        Document doc = Jsoup.connect("https://www.huilvbiao.com/bank/spdb").get();
        //        Elements rows = doc.select("table.table tbody tr");
//
         //       for (Element row : rows) {
         //           Elements cols = row.select("td");
         //           if (cols.size() >= 4) {
         //               String currency = cols.get(0).select("span").text();
         //               String rate = cols.get(1).text(); // 获取现汇买入价
          //              rateList.add(currency + ": " + rate);
           //         }
          //      }
                Document doc = Jsoup.connect("https://www.huilvbiao.com/bank/spdb").get();
                Elements rows = doc.select("table.table tbody tr");

                // 打印表头
                System.out.println("币种\t\t现汇买入\t现汇卖出\t现钞买入");
                System.out.println("----------------------------------------");

                for (Element row : rows) {
                    Elements cols = row.select("td");
                    if (cols.size() >= 4) {
                        String currency = cols.get(0).select("span").text();
                        // 使用String.format保证对齐
                        System.out.println(String.format("%-8s\t%-8s\t%-8s\t%-8s",
                                currency,
                                cols.get(1).text(),
                                cols.get(2).text(),
                                cols.get(3).text()));
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "获取汇率数据失败",e);
                rateList.add("获取汇率数据失败，请检查网络连接");
            }

            Log.i(TAG, "onCreate:返回数据");
            Message msg = handler.obtainMessage(9, rateList);
            handler.sendMessage(msg);
        });
        t.start();
    }

}*/
package com.example.myapplication2;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.annotation.NonNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class MainListActivity extends ListActivity {

    private static final String TAG = "MainListActivity";
    private List<String> rateList = new ArrayList<>();
    private Handler handler;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);

        Button btn = findViewById(R.id.button6);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, rateList);
        setListAdapter(adapter);

        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == 9) {
                    @SuppressWarnings("unchecked")
                    List<String> list2 = (List<String>) msg.obj;
                    rateList.clear();
                    rateList.addAll(list2);
                    adapter.notifyDataSetChanged();
                }
            }
        };
    }

    public void dtclick(View btn) {
        rateList.clear();
        rateList.add("正在获取汇率数据...");
        adapter.notifyDataSetChanged();

        new Thread(() -> {
            List<String> resultList = new ArrayList<>();
            try {
                Document doc = Jsoup.connect("https://www.huilvbiao.com/bank/spdb").timeout(10000).get();
                Elements rows = doc.select("table.table tbody tr");
                Log.i(TAG, "成功获取网页数据，开始解析...");
                resultList.add("币种\t现汇买入\t现汇卖出\t现钞买入");
                resultList.add("----------------------------------");
                for (Element row : rows) {
                    Elements cols = row.select("td");
                    if (cols.size() >= 3) {
                        String currency = cols.get(0).select("span").text();
                        String cashBuy = cols.get(1).text();
                        String cashSell = cols.get(2).text();
                        String spotBuy = cols.get(3).text();

                        String line = String.format("%-6s\t%-8s\t%-8s\t%-8s",
                                currency, cashBuy, cashSell, spotBuy);
                        resultList.add(line);
                        Log.d(TAG, "解析到汇率数据: " + line);
                    }
                }
                Log.i(TAG, "汇率数据解析完成，共解析到 " + (resultList.size()-2) + " 条记录");
            } catch (Exception e) {
                Log.e(TAG, "获取汇率数据失败", e);
                resultList.clear();
                resultList.add("获取汇率数据失败，请检查网络连接");
            }

            Message msg = handler.obtainMessage(9, resultList);
            handler.sendMessage(msg);
        }).start();
    }
}