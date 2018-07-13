package com.example.xiangjianqiu.readr;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xys.libzxing.zxing.activity.CaptureActivity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.xiangjianqiu.readr.R.id.tv_result;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnQrCode; // 扫码
    TextView tvResult; // 结果

    private HttpURLConnection httpURLConnection;
    protected static final int SUCCESS=1;
    protected static final int ERROR=2;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SUCCESS:
                    String text=(String)msg.obj;
                    tvResult.setText(text);//更新主界面
                    break;
                case ERROR:
                    Toast.makeText(MainActivity.this,"请求失败",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        btnQrCode = (Button) findViewById(R.id.btn_scan);
        btnQrCode.setOnClickListener(this);

        tvResult = (TextView) findViewById(tv_result);

    }

    // 开始扫码
    private void startQrCode() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // 申请权限
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
            return;
        }
        // 二维码扫码
        Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onClick(View view) {
      switch (view.getId()) {
           case R.id.btn_scan:
               startQrCode();
               break;




        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch (resultCode) {
            case RESULT_OK:
                new Thread(){
                    public void run(){
                        try {
                            //1.利用URL 类的实力接受URL地址
                            URL url=new URL("https://api.douban.com/v2/book/isbn/9787535619969");
                            //2.通过路径得到一个连接http的打开方式
                            httpURLConnection=(HttpURLConnection)url.openConnection();
                            //设置请求方式，默认是GET
                            httpURLConnection.setRequestMethod("GET");
                            //3.判断服务器返回的状态
                            int code=httpURLConnection.getResponseCode();
                            if(code==200){
                                //4.连接成功的conn得到输入流
                                InputStream is=httpURLConnection.getInputStream();
                                String result=StreamTools.readStream(is);
                                //5.将获取的数据信息发送给UI主线程Handle，通过message作为桥梁
                                Message msg=Message.obtain();
                                msg.obj=result;
                                msg.what=SUCCESS;
                                handler.sendMessage(msg);//和handleMessage对应
                            }

                        }catch (Exception e){
                            Message msg=Message.obtain();
                            msg.what=ERROR;
                            handler.sendMessage(msg);
                            e.printStackTrace();
                        }
                    }

                }.start();
         //       tvResult.setText(data.getStringExtra("result"));  //or do sth

                break;
        }

        //扫描结果回调
//        if ( resultCode == 1) {
//            Bundle bundle = data.getExtras();
//            if (bundle == null) {
//                return;
//            }
//
//            String scanResult = bundle.getString("result");
//            //将扫描出的信息显示出来
//            tvResult.setText(scanResult);
//        }

    }
        @Override
        public void onRequestPermissionsResult( int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults){
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

            if (requestCode == 1) {

                // 摄像头权限申请
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获得授权
                    startQrCode();
                } else {
                    // 被禁止授权
                    Toast.makeText(MainActivity.this, "请至权限中心打开本应用的相机访问权限", Toast.LENGTH_LONG).show();
                }

            }
        }





    }
