package com.xj.ij.projectxfinal;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private ListView listView;
    private ArrayList<String> list;
    private ArrayAdapter<String> arrayAdapter;
    private ConnectServer connectServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //เชื่อม listView กับ View
        listView = (ListView)findViewById(R.id.listView);

        //สร้างตัวเชื่อมต่อกับ Server ไปที่ URL ที่กำหนด
        connectServer = new ConnectServer(this, "http://qazx.servehttp.com:99/service/api/restaurant/res/format/json");



        //เชื่อมต่อกับ Server
        connectServer.execute();
    }

    //ถ้าดึงข้อมูลจาก Server เสร็จแล้ว จะมาทำงานที่ Function นี้
    public void setList(ArrayList<String> list){
        this.list = list;
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, this.list);
        listView.setAdapter(arrayAdapter);
    }

    //ถ้าไม่สามารถเชื่อมต่อกับ Server ได้จะมาทำงานที่ Function นี้
    public void cannotConnectToServer() {
        Toast.makeText(this, "ไม่สามารถเชื่อมต่อกับ Server", Toast.LENGTH_LONG).show();
    }

    //ถ้าดึงข้อมูลจาก Server มีปัญหา จะมาทำงานที่ Function นี้
    public void errorConnectToServer() {
        Toast.makeText(this, "เกิดขอผิดพลาดในการดึงข้อมูล", Toast.LENGTH_LONG).show();
    }
}