package recognizerdemo.speech.baidu.com.xft2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eclipsesource.json.Json;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements InitListener{


    @Override
    public void onInit(int code) {
        if (code != ErrorCode.SUCCESS) {
            Toast.makeText(MainActivity.this,"初始化失败，错误码：" + code,Toast.LENGTH_LONG).show();
        }
    }

    public String result(String resultString){
        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(resultString);
        com.alibaba.fastjson.JSONArray jsonArray = jsonObject.getJSONArray("ws");
        StringBuffer stringBuffer = new StringBuffer();
        ImageView imagev1=(ImageView)findViewById(R.id.image1);
        for (int i = 0; i < jsonArray.size(); i++) {
            com.alibaba.fastjson.JSONObject jsonObject1 = ( jsonArray).getJSONObject(i);
            JSONArray jsonArray1 = jsonObject1.getJSONArray("cw");
            JSONObject jsonObject2 = jsonArray1.getJSONObject(0);
            String w = jsonObject2.getString("w");
            stringBuffer.append(w);
        }
        String result = stringBuffer.toString();
        /*Log.i("test_xunfei", "识别结果为：" + result);*/
        Toast.makeText(this, "识别结果："+result, Toast.LENGTH_SHORT).show();
        imagev1.setImageResource(R.drawable.bird);

        return result;
    }





    public void startVoice(){
        //1.创建RecognizerDialog对象
        RecognizerDialog recognizerDialog = new RecognizerDialog(MainActivity.this,this);
        //2.设置accent、language等参数
        recognizerDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");//语种，这里可以有zh_cn和en_us
        recognizerDialog.setParameter(SpeechConstant.ACCENT, "mandarin");//设置口音，这里设置的是汉语普通话 具体支持口音请查看讯飞文档，
        recognizerDialog.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");//设置编码类型

        //其他设置请参考文档http://www.xfyun.cn/doccenter/awd
        //3.设置讯飞识别语音后的回调监听
        recognizerDialog.setListener(new RecognizerDialogListener() {
            @Override
            public void onResult(RecognizerResult recognizerResult, boolean b) {//返回结果
                if (!b) {
                    Log.i("test_xunfei", recognizerResult.getResultString());
                    result(recognizerResult.getResultString());
                }
                /*result(recognizerResult.getResultString());*/
            }




            @Override
            public void onError(SpeechError speechError) {//返回错误
                Log.e("test_xunfei", speechError.getErrorCode() + "");
            }

        });
        //显示讯飞语音识别视图
        recognizerDialog.show();
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn1=(Button)findViewById(R.id.btn);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            //没有授权
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.INTERNET}, 1);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            //没有授权
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            //没有授权
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 1);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
            //没有授权
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_WIFI_STATE}, 1);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CHANGE_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            //没有授权
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CHANGE_NETWORK_STATE}, 1);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            //没有授权
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            //没有授权
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
        };




        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=5c553b89");

        btn1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startVoice();
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,@NonNull String[] permissions,int[] grantResults)
    {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    //用户拒绝了授权
                    Toast.makeText(this, "权限被拒绝", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }


}
