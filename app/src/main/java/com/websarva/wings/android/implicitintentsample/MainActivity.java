package com.websarva.wings.android.implicitintentsample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onMapSearchButtonClick(View view){
        EditText etSearchWord = findViewById(R.id.etSearchWord);
        String searchWord = etSearchWord.getText().toString();

        try {
            searchWord = URLEncoder.encode(searchWord,"UTF-8");

            String uriStr = "geo:0,0?q=" + searchWord;

            Uri uri = Uri.parse(uriStr);

            Intent intent = new Intent(Intent.ACTION_VIEW,uri);

            startActivity(intent);
        }
        catch (UnsupportedEncodingException ex){
            Log.e("MainActivity", "検索キーワード変換失敗", ex);
        }

    }
}