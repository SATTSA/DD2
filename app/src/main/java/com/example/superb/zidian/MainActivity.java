package com.example.superb.zidian;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    public static final String WORD_KEY = "f98139cfac1da1eed4810e33d0d51a11";//在聚合数据里面申请的产品秘钥，拥有秘钥才能去访问这个网址

    private EditText editword;

    private TextView result,result1,result2;

    private Handler handler;

    private Button button;



    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        initialize();

        initData();

    }



    private void initData() {

        button.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {

                switch (view.getId()){

                    case R.id.button:
                        Log.e("ssssss", "onsssssssssssssssssssssss");
                        final String edit_result=editword.getText().toString();

                        handler= new Handler() {

                            @Override

                            public void handleMessage(Message msg) {//handler加Thread组成异步线程访问网络

                                super.handleMessage(msg);

                                switch (msg.what) {

                                    case 1:

                                        if (edit_result!=null) {

                                            String url= "http://v.juhe.cn/chengyu/query?key="+ WORD_KEY+"&word="+edit_result;

                                            RxVolley.get(url, new HttpCallback() {

                                                @Override

                                                public void onSuccess(String t) {//字符串是一个json格式的字符串，RxVolley是访问网络的一个框架，是添加的依赖

                                                    super.onSuccess(t);

                                                    result.setText(parseJson(t));



                                                }

                                            });

                                            break;

                                        }else{

                                            Toast.makeText(MainActivity.this, "输入的成语为空"+edit_result, Toast.LENGTH_SHORT).show();

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



    private void initialize() {

        editword = (EditText) findViewById(R.id.edit_word);

        result = (TextView) findViewById(R.id.result);

        button= (Button) findViewById(R.id.button);



    }



    private String parseJson(String t) {//解析json字符串

        String s="";

        try {

            JSONObject jsonObject = new JSONObject(t);

            JSONObject result = jsonObject.getJSONObject("result");



            String pinyin=result.getString("pinyin");

            String chengyujs = result.getString("chengyujs");

            String from_=result.getString("from_");

            String yinzhengjs=result.getString("yinzhengjs");

            s=chengyujs+from_+yinzhengjs;

            String tongyi = result.getString("tongyi");

            // s=s+tongyi;

            s=chengyujs+from_+yinzhengjs+tongyi;

            String fanyi = result.getString("fanyi");

            s=pinyin+"/n"+tongyi+"/n"+fanyi+"/n"+chengyujs+"/n"+from_+"/n"+yinzhengjs+"/n";










            //s=s+fanyi;

            //s=chengyujs;

        } catch (JSONException e) {

            e.printStackTrace();

        }


        return s;

    }

}
