package com.test.echolips.activity;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;
import android.widget.Toast;

import com.test.echolips.R;
import com.test.echolips.adapter.RideAdapter;
import com.test.echolips.bean.EcCookitem;

import java.util.List;

/**
 * Created by 14439 on 2017-1-10.
 */

public class StartRide extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_ride);
        ListView listView = (ListView)findViewById(R.id.listView_ride);
        //接收
        Intent intent = getIntent();
        List<EcCookitem> list = (List<EcCookitem>) intent.getSerializableExtra("steps");
        RideAdapter rideAdapter = new RideAdapter(StartRide.this,R.layout.rideadapt_layout,list);
        listView.setAdapter(rideAdapter);
    }
}
