package com.uof.uof_mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;


import androidx.appcompat.app.AppCompatActivity;

import com.uof.uof_mobile.R;
import com.uof.uof_mobile.listview.LobbyListViewItem;
import com.uof.uof_mobile.listview.LobbyListViewItemAdapter;

import org.json.JSONArray;

public class LobbyActivity extends AppCompatActivity {
    ImageButton btn_card, btn_orderlist, btn_setting;
    ImageView ivLobbyRecognizeQR;
    ListView lvLobbyNowOrderList;
    LobbyListViewItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        btn_card = findViewById(R.id.btn_lobby_card);
        btn_orderlist = findViewById(R.id.btn_lobby_orderlist);
        btn_setting = findViewById(R.id.btn_lobby_setting);
        ivLobbyRecognizeQR = findViewById(R.id.iv_lobby_recognizeqr);
        lvLobbyNowOrderList = findViewById(R.id.lv_lobby_noworderlist);

        btn_card.setOnClickListener(view -> {
            Intent intent = new Intent(LobbyActivity.this, PayActivity.class);
            startActivity(intent);
        });

        btn_orderlist.setOnClickListener(view -> {
            Intent intent = new Intent(LobbyActivity.this, OrderListActivity.class);
            startActivity(intent);
        });

        btn_setting.setOnClickListener(view -> {
            Intent intent = new Intent(LobbyActivity.this, SettingActivity.class);
            startActivity(intent);
        });

        ivLobbyRecognizeQR.setOnClickListener(view -> {
            Intent intent = new Intent(LobbyActivity.this, QRRecognitionActivity.class);
            startActivity(intent);
        });

        try{
            adapter = new LobbyListViewItemAdapter();
            JSONArray menulist = new JSONArray("[{name : \"홍익수제비\", count : 3},{name : \"쉑섹버거\", count : 3}]");
            adapter.addItem(new LobbyListViewItem(1002,menulist));
            adapter.addItem(new LobbyListViewItem(1003,menulist));
        }catch(Exception e){
            e.printStackTrace();
        }
        lvLobbyNowOrderList.setAdapter(adapter);

    }

}