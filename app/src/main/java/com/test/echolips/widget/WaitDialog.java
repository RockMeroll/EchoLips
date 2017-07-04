package com.test.echolips.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.Window;

/**
 * Created by RockMeRoLL on 2016/5/11.
 */
public class WaitDialog extends ProgressDialog {

    public WaitDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(false);
        setProgressStyle(STYLE_SPINNER);
        setMessage("正在请求，请稍候…");
    }

}
