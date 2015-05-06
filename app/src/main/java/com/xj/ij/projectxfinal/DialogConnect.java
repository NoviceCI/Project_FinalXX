package com.xj.ij.projectxfinal;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class DialogConnect extends ProgressDialog{

    private AsyncTask task;

    public DialogConnect(Context context,AsyncTask task) {
        //สร้าง Dialog จาก super class
        super(context);

        //เชื่อม AsyncTask (ConnectServer) เข้ากับ Dialog
        this.task = task;
    }

    //ถ้ามีการยกเลิกตอนที่ Dialog ขึ้นมาจะมาทำงาน Function นี้
    public void cancel() {
        //ยกเลิกเชื่อมต่อกับ Server
        task.cancel(true);
        super.cancel();
    }

}