package com.xj.ij.projectxfinal;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class ListRes extends ActionBarActivity {

    private ListView listView;
    private ArrayList<String> list;
    private ArrayAdapter<String> arrayAdapter;
    private ConnectServer connectServer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_res);

            listView = (ListView) findViewById(R.id.listView);

            connectServer = new ConnectServer(this,"http://qazx.servehttp.com:99/service/api/restaurant/res/format/json");


            connectServer.execute();
    }

     public void setList(ArrayList<String> list){
         this.list = list;
         arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,this.list);
         listView.setAdapter(arrayAdapter);
     }


    public void cannotConnectToServer() {
        Toast.makeText(this, "ไม่สามารถเชื่อมต่อกับ Server", Toast.LENGTH_LONG).show();
    }

    public void errorConnectToServer() {
        Toast.makeText(this, "เกิดขอผิดพลาดในการดึงข้อมูล", Toast.LENGTH_LONG).show();
    }
}
