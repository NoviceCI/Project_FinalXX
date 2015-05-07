package com.xj.ij.projectxfinal;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.xj.ij.projectxfinal.model.Restautant_Model;

/**
 * Created by Taiy on 6/5/2558.
 */
public class ListClick implements View.OnClickListener {

    Context context ;

    public ListClick(Context context){
        this.context = context;
    }

    @Override
    public void onClick(View v) {

        Restautant_Model restautant_model = (Restautant_Model) v.getTag();

        Toast.makeText(context,restautant_model.getRestaurant_name(),Toast.LENGTH_LONG).show();

        Intent intent = new Intent(context,MapsActivity.class);

        intent.putExtra("lat",String.valueOf(restautant_model.getRestaurant_lat()));

        intent.putExtra("lang",String.valueOf(restautant_model.getRestaurant_lang()));

        intent.putExtra("id",String.valueOf(restautant_model.getRestaurant_id()));

        intent.putExtra("name",String.valueOf(restautant_model.getRestaurant_name()));

        context.startActivity(intent);

    }

}
