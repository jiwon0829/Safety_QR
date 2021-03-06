package com.example.safety_qr.activity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.safety_qr.R;
import com.example.safety_qr.infrastructure.ScanQR;
import com.example.safety_qr.domain.History;
import com.example.safety_qr.infrastructure.SQLiteHelper;

import java.util.List;


public class ScanResult_Activity extends AppCompatActivity {

    SQLiteHelper dbHelper;
    List<History> history;

    int malicious, total;
    String VTUrl, url;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView checktextView = findViewById(R.id.check_textView);
        TextView urltextView = findViewById(R.id.url_textView);
        LinearLayout backGround = (LinearLayout) findViewById(R.id.qr_result);
        ImageView imageView = findViewById(R.id.markImgView);
        Button ok_button = findViewById(R.id.ok_button);
        Button cancle_button = (Button)findViewById(R.id.cancle_button);

        Intent urlIntent = getIntent();
        // **** 악성 : malicious // 백신 수 : total
        malicious = urlIntent.getIntExtra("malicious", 1);
        total = urlIntent.getIntExtra("total", 1);
        VTUrl = urlIntent.getStringExtra("VTUrl");
        url = urlIntent.getStringExtra("url");


        //final String url = "http://198.23.207.82/mad/men.exe";
        Log.w("ScanResult", "ScanResult " + malicious +" " + total);
        urltextView.setText(url);

        // <--- DB --->
        dbHelper = new SQLiteHelper(ScanResult_Activity.this);
        // 악성 유무값 toString
        String result = Integer.toString(malicious);
        History history = new History(url, result);
        dbHelper.insertHistory(history);
        // <-- DB END -- >

        if(malicious == -1){  //한번도 검색되지 않은 사이트라면
            //배경 회색
            urltextView.setBackgroundColor(0xe0e0e0);
            //check_textView '안전한 url 입니다.'
            checktextView.setTextSize(25);
            checktextView.setText("한번도 등록되지 않은 URL 입니다.");

            //mark == green
            imageView.setImageResource(R.drawable.gray_mark);


            //접속 버튼 눌렀을때 해당 액션 작동
            ok_button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    if(!url.contains("http")){
                        String http = "http://";
                        http = http.concat(url);
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(http));
                        startActivity(intent);
                    }
                    else{
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                    } finish();

                }
            });
        }

        else if(malicious != 0){  //악성이라면
            //배경 레드
            //backGround.setBackgroundColor(0xffff0000);
            urltextView.setBackgroundColor(0xffe84c3d);
            //check_textView '악성 url 입니다.'
            checktextView.setText("악성 URL 입니다.");

            ok_button.setText("Pro version");
            imageView.setImageResource(R.drawable.red_mark);



        }
        else{   //안전한 url이라면
            //배경 그린
            urltextView.setBackgroundColor(0xff27ae61);
            //check_textView '안전한 url 입니다.'
            checktextView.setText("안전한 URL 입니다.");

            //mark == green
            imageView.setImageResource(R.drawable.green_mark);


            //접속 버튼 눌렀을때 해당 액션 작동
            ok_button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    if(!url.contains("http")){
                        String http = "http://";
                        http = http.concat(url);
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(http));
                        startActivity(intent);
                    }
                    else{
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                    } finish();

                }
            });
        }

    }

    public void Cancle(View view) {
        finish();

    }

    public void Detail(View view){
        Intent intent = new Intent(this, Detail_Activity.class);
        intent.putExtra("VTUrl", VTUrl);
        intent.putExtra("malicious", malicious);
        intent.putExtra("total", total);
        intent.putExtra("url",url);
        startActivity(intent);
    }

}