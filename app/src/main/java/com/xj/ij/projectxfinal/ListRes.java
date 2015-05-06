package com.xj.ij.projectxfinal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xj.ij.projectxfinal.model.Restautant_Model;

import java.util.List;


public class ListRes extends ActionBarActivity {

    private ListView listView;
    private List<Restautant_Model> list;
    private ArrayAdapter<String> arrayAdapter;
    private ConnectServer connectServer;
    private Context  context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_res);

            context = this ;

            listView = (ListView) findViewById(R.id.listView);

            connectServer = new ConnectServer(this,"http://qazx.servehttp.com:99/service/api/restaurant/res/format/json");


            connectServer.execute();
    }

    public void setList(List<Restautant_Model> list1){


         this.list = list1;

         listView.setAdapter(new ListAdapter());

     }


    private class ListAdapter extends BaseAdapter {




        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = LayoutInflater.from(context).inflate(R.layout.listitem_layout,null);

            Restautant_Model restautant_model = list.get(position);

            if (restautant_model!=null){

                TextView resName = (TextView) convertView.findViewById(R.id.resNameMap);
                TextView resDesc = (TextView) convertView.findViewById(R.id.resDescMap);
                ImageView resImg  = (ImageView) convertView.findViewById(R.id.resImg);


                byte[] bytes = Base64.decode(restautant_model.getImage(),Base64.DEFAULT);
                Bitmap  bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);

                resImg.setImageBitmap(bitmap);
                resName.setText(restautant_model.getRestaurant_name());
                resDesc.setText(restautant_model.getRestaurant_des());

                convertView.setTag(restautant_model);

                convertView.setOnClickListener(new ListClick(context));
            }



            return convertView;
        }
    }


    public void cannotConnectToServer() {
        Toast.makeText(this, "ไม่สามารถเชื่อมต่อกับ Server", Toast.LENGTH_LONG).show();
    }

    public void errorConnectToServer() {
        Toast.makeText(this, "เกิดขอผิดพลาดในการดึงข้อมูล", Toast.LENGTH_LONG).show();
    }
}
