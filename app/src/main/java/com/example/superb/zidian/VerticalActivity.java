package com.example.superb.zidian;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
//import android.support.v7.app.LinearLayoutManager;
//import android.support.v7.app.RecyclerView;

//import com.haocent.android.recyclerview.R;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 纵向布局 的 Activity
 *
 * Created by Tnno Wu on 2018/03/05.
 */

public class VerticalActivity extends AppCompatActivity {
    public static final String WORD_KEY = "f98139cfac1da1eed4810e33d0d51a11";//在聚合数据里面申请的产品秘钥，拥有秘钥才能去访问这个网址

    private EditText editword;

    private TextView result,result1,result2;

    private Handler handler;

    private Button button;
    //private static final String TAG = VerticalActivity.class.getSimpleName();

    private List<String> mList = new ArrayList<>();





    String pinyin,tongyi,fanyi,chengyujs,from_,yinzhengjs;


















    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vertical_activity);
        initialize();
        initData();
        //initView();
       initView();
    }


    private void initialize() {

        editword = (EditText) findViewById(R.id.edit_word);

        result = (TextView) findViewById(R.id.result);

        button= (Button) findViewById(R.id.button);



    }


    private void initData() {

        button.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {

                switch (view.getId()){

                    case R.id.button:
                        Log.e("ssssss", "onsssssssssssssssssssssss");
                        final String edit_result=editword.getText().toString();
                        mList = new ArrayList<>();
                        handler= new Handler() {

                            @Override

                            public void handleMessage(final Message msg) {//handler加Thread组成异步线程访问网络

                                super.handleMessage(msg);

                                switch (msg.what) {

                                    case 1:

                                        if (edit_result!=null) {

                                            String url= "http://v.juhe.cn/chengyu/query?key="+ WORD_KEY+"&word="+edit_result;

                                            RxVolley.get(url, new HttpCallback() {

                                                @Override

                                                public void onSuccess(String t) {//字符串是一个json格式的字符串，RxVolley是访问网络的一个框架，是添加的依赖

                                                    super.onSuccess(t);

                                                   mList.add(parseJson(t));
//+'\n'+tongyi+'\n'+fanyi+'\n'+chengyujs+'\n'+from_+'\n'+yinzhengjs+'\n'
                                                    mList.add("同义词： "+tongyi+'\n'+"反义词： "+fanyi);
                                                    mList.add("含义： "+chengyujs);
                                                    mList.add("应征解释： "+yinzhengjs);
                                                    mList.add("出处： "+from_);
                                                    result.setText(from_);
                                                   initView();

                                                }

                                            });

                                            break;

                                        }else{

                                            Toast.makeText(VerticalActivity.this, "输入的成语为空"+edit_result, Toast.LENGTH_SHORT).show();

                                        }

                                }



                            }

                        };

                        new Thread(){

                            @Override

                            public void run() {

                                super.run();

                                Message message=new Message();

                                message.what=1;

                                handler.sendMessage(message);

                            }

                        }.start();



                        break;

                }

            }

        });



    }









    private String parseJson(String t) {//解析json字符串

        String s="";

        try {

            JSONObject jsonObject = new JSONObject(t);

            JSONObject result = jsonObject.getJSONObject("result");



             pinyin=result.getString("pinyin");

           chengyujs = result.getString("chengyujs");

           from_=result.getString("from_");

             yinzhengjs=result.getString("yinzhengjs");

            s=chengyujs+from_+yinzhengjs;

             tongyi = result.getString("tongyi");

            // s=s+tongyi;

            s=chengyujs+from_+yinzhengjs+tongyi;

           fanyi = result.getString("fanyi");

            s=pinyin;










            //s=s+fanyi;

            //s=chengyujs;

        } catch (JSONException e) {

            e.printStackTrace();

        }


return s;

    }









    private void initView() {
        VerticalAdapter adapter = new VerticalAdapter(this);

        RecyclerView rcvVertical = findViewById(R.id.rcv_vertical);

        LinearLayoutManager managerVertical = new LinearLayoutManager(this);
        managerVertical.setOrientation(LinearLayoutManager.VERTICAL);

        // 也可以直接写成：
//        rcvVertical.setLayoutManager(new LinearLayoutManager(this));

        rcvVertical.setLayoutManager(managerVertical);
        rcvVertical.setHasFixedSize(true);
        rcvVertical.setAdapter(adapter);

        adapter.setVerticalDataList(mList);
    }




}
