package com.test.echolips.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.test.echolips.R;

/**
 * Created by RockMeRoLL on 2017/1/17.
 */
public class CompletedActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_completed);

        TextView tv = (TextView) findViewById(R.id.title_bar_btn_back);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
