package com.test.echolips.activity;
import com.test.echolips.R;

import android.R.string;
import android.R.xml;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Instrumentation;
import android.app.AlertDialog.Builder;
import android.content.ClipData.Item;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;

import com.baidu.speech.VoiceRecognitionService;
import com.baidu.speechsynthesizer.SpeechSynthesizer;
import com.baidu.speechsynthesizer.SpeechSynthesizerListener;
import com.baidu.speechsynthesizer.publicutility.SpeechError;
import com.test.echolips.bean.EcCookitem;
import com.test.echolips.bean.SaveDate;
import com.test.echolips.utils.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.PublicKey;
import java.util.*;

public class SpeechActivity extends Activity implements RecognitionListener,
        OnClickListener, SpeechSynthesizerListener {
    // 语音合成
    protected static final int UI_LOG_TO_VIEW = 0;
    public static SpeechSynthesizer speechSynthesizer;
    private String voicePeople = "4";
    // 语音识别
    private static final String TAG = "Sdk2Api";
    private static final int REQUEST_UI = 1;
    public TextView previousTxtLog;
    public TextView currentTxtLog;
    public TextView nextTextLog;
    private Button btn;

    public static final int STATUS_None = 0;
    public static final int STATUS_WaitingReady = 2;
    public static final int STATUS_Ready = 3;
    public static final int STATUS_Speaking = 4;
    public static final int STATUS_Recognition = 5;
    public static SpeechRecognizer speechRecognizer;
    private int status = STATUS_None;
    // private TextView txtResult;
    private long speechEndTime = -1;
    private static final int EVENT_ERROR = 11;

    public List<SaveDate> dataList;

    private ScrollView mScrollView;
    public ImageView imageView;// 菜谱照片
    public int listNum = 0;// 第几个imagelist
    private boolean firstnum = true;// 判断第一个语音合成是否放完
    Intent intent = new Intent();

    // 悬浮按钮
    public ImageButton mFloatViewPrevious;
    public ImageButton mFloatViewCurrent;
    public ImageButton mFloatViewNext;
    public ImageButton mFloatViewHelp;
    public ImageButton mFloatViewVoice;

    // 指令提示框
    // final String[] mList = { "上翻：<a font color=\"#ff00cc\">上一步、前一步</a>",
    // "下翻：下一步、后一步", "重复播报：重复、重播",
    // "语音指令界面：Help、帮助、指令", "配音选项页面：语音、声音、配音", "配音选项：童声、女声、男声",
    // "关闭语音指令/配音选项界面：关闭、返回", "结束导航：结束导航、停止导航", };
    final String mList = "<p><a font color=\"#aabb00\">上翻：</a><font color=\"#ff0000\">上一步、前一步</p>"
            + "<p><a font color=\"#aabb00\">下翻：</a><font color=\"#ff0000\">下一步、后一步</p>"
            + "<p><a font color=\"#aabb00\">重复播报：</a><font color=\"#ff0000\">重复、重播</p>"
            + "<p><a font color=\"#aabb00\">语音指令界面：</a><font color=\"#ff0000\">Help、帮助、指令</p>"
            + "<p><a font color=\"#aabb00\">配音选项页面：</a><font color=\"#ff0000\">语音、声音、配音</p>"
            + "<p><a font color=\"#aabb00\">配音选项：</a><font color=\"#ff0000\">童声、女声、男声</p>"
            + "<p><a font color=\"#aabb00\">关闭语音指令/配音选项界面：</a><font color=\"#ff0000\">关闭、返回</p>"
            + "<p><a font color=\"#aabb00\">结束导航：</a><font color=\"#ff0000\">结束导航、停止导航</p>";

    // 声音选择框
    final String[] voiceList = { "童声", "女声", "男声" };
    AlertDialog.Builder listDia, voiceDia;
    AlertDialog dlg, voiceDlg;
    int width1;
    int height1;
    // handler

    boolean judgeBack = false;//是否返回
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            speechRecognizer.cancel();
            bindParams(intent);
            SharedPreferences sp = PreferenceManager
                    .getDefaultSharedPreferences(SpeechActivity.this);
            String args = sp.getString("args", "");
            if (null != args) {
                intent.putExtra("args", args);
            }
            speechEndTime = -1;
            speechRecognizer.startListening(intent);

        }
    };

    //合并时
    List<String> addPicList;
    String cook_name;
    private List<Bitmap> imgList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
        setContentView(R.layout.sdk2_api);

        // 初始化
        init();

        WindowManager wm1 = this.getWindowManager();
        width1 = wm1.getDefaultDisplay().getWidth();
        height1 = wm1.getDefaultDisplay().getHeight();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (firstnum == true && getLocalIpAddress() != null) {
                    firstnum = false;
                    setParams();
                    speakText();
                } else if (getLocalIpAddress() == null) {
                    Toast.makeText(SpeechActivity.this, "世界上最遥远的距离就是没网",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 浮动窗口按钮上一步
        mFloatViewPrevious = (ImageButton) findViewById(R.id.alert_window_imagebtn_previous);
        mFloatViewPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listNum == 0) {
                    Toast.makeText(SpeechActivity.this, "已经是第一步",
                            Toast.LENGTH_SHORT).show();
                } else {
                    listNum--;
                    judgeUpOrDown();
                }
            }
        });

        // 浮动窗口按钮重复
        mFloatViewCurrent = (ImageButton) findViewById(R.id.alert_window_imagebtn_current);
        mFloatViewCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakText();
            }
        });

        // 浮动窗口按钮下一步
        mFloatViewNext = (ImageButton) findViewById(R.id.alert_window_imagebtn_next);
        mFloatViewNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listNum == (dataList.size() - 1)) {
                    Toast.makeText(SpeechActivity.this, "已经是最后一步",
                            Toast.LENGTH_SHORT).show();
                } else {
                    listNum++;
                    judgeUpOrDown();
                }
            }
        });

        // 帮助按钮
        mFloatViewHelp = (ImageButton) findViewById(R.id.alert_window_imagebtn_help);
        mFloatViewHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog();
            }
        });

        // 选择声音输出
        mFloatViewVoice = (ImageButton) findViewById(R.id.alert_window_imagebtn_voice);
        mFloatViewVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogVoice();
            }
        });
        // 上面返回键
        TextView tv = (TextView) findViewById(R.id.title_bar_btn_back);
        tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        speechSynthesizer
                .setParam(SpeechSynthesizer.PARAM_SPEAKER, voicePeople);// 说话的人
    }

    private void init() {
        // 指令提示框
        listDia = new Builder(this);
        voiceDia = new AlertDialog.Builder(this);

        // 语音合成
        speechSynthesizer = new SpeechSynthesizer(getApplicationContext(),
                "holder", this);
        speechSynthesizer.setApiKey("psY73cYCMjfO25Pdkk6ro3sY",
                "154073dd9a6b78b3d98c3d31d9b9d8d8");
        speechSynthesizer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        // 语音识别

        // txtResult = (TextView) findViewById(R.id.txtResult);

        previousTxtLog = (TextView) findViewById(R.id.txtLog);
        currentTxtLog = (TextView) findViewById(R.id.txtLog2);
        nextTextLog = (TextView) findViewById(R.id.txtLog3);

        btn = (Button) findViewById(R.id.btn);

        // mScrollView = (ScrollView) findViewById(R.id.scroll);
        imageView = (ImageView) findViewById(R.id.imgLog);// 图片

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this,
                new ComponentName(this, VoiceRecognitionService.class));
        speechRecognizer.setRecognitionListener(this);

        List<String> addStepList = new ArrayList<String>();

        Intent intent = getIntent();
        List<EcCookitem> stepList = (List<EcCookitem>) intent.getSerializableExtra("steps");

        cook_name = intent.getStringExtra("cook_name");

        for (EcCookitem ec : stepList) {
            addStepList.add(ec.getItemcontent());
        }
        addPicList = (List<String>) intent.getSerializableExtra("pic");


        imgList = new ArrayList<>();
        for (String path : addPicList) {
            BitmapFactory.Options option = new BitmapFactory.Options();
            option.inSampleSize = 5;
            imgList.add(BitmapFactory.decodeFile(path, option));
        }

        dataList = new ArrayList<SaveDate>();
        for (int i = 0; i < addStepList.size(); i++) {
            SaveDate saveDate = new SaveDate(i == 0 ? "开始" : addStepList.get(i - 1),
                    addPicList.get(i),
                    addStepList.get(i),
                    i == addStepList.size() - 1 ? "结束" : addStepList.get(i + 1));
            dataList.add(saveDate);
        }
        previousTxtLog.setText(dataList.get(0).getPreviousText());
        imageView.setImageBitmap(imgList.get(0));
        currentTxtLog.setText("<" + String.valueOf((listNum + 1)) + "/"
                + dataList.size() + ">" + dataList.get(0).getCurrentText());
        nextTextLog.setText("<" + String.valueOf((listNum + 2)) + "/"
                + dataList.size() + ">" + dataList.get(0).getNextText());
        imageView.setImageBitmap(imgList.get(0));
    }

    public void alertDialog() {
        listDia.setTitle("语音命令");
        listDia.setMessage(Html.fromHtml(mList));
        // listDia.setItems(mList, null);
        listDia.setCancelable(false);
        listDia.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dlg = null;
            }
        });
        dlg = listDia.create();
        dlg.show();
        WindowManager.LayoutParams params = dlg.getWindow().getAttributes();
        params.width = width1 - 35;
        params.height = height1 - 50;
        dlg.getWindow().setAttributes(params);
    }

    public void alertDialogVoice() {
        voiceDia.setTitle("语音播放选项");
        voiceDia.setItems(voiceList, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        voicePeople = "4";
                        voiceDlg = null;
                        break;
                    case 1:
                        voicePeople = "0";
                        voiceDlg = null;
                        break;
                    case 2:
                        voicePeople = "3";
                        voiceDlg = null;
                        break;
                    default:
                        break;
                }
            };
        });
        voiceDia.setCancelable(false);
        voiceDia.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                voiceDlg = null;
            }
        });
        voiceDlg = voiceDia.create();
        voiceDlg.show();
        WindowManager.LayoutParams params = voiceDlg.getWindow()
                .getAttributes();
        params.width = width1 - 50;
        params.height = height1 - 500;
        voiceDlg.getWindow().setAttributes(params);
    }

    public boolean isChinese(char c) {
        boolean result = false;
        if (c >= 19968 && c <= 171941) {// 汉字范围 \u4e00-\u9fa5 (中文)
            result = true;
        }
        return result;
    }

    // 判断是否联网
    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            // Log.e(LOG_TAG, ex.toString());
        }
        return null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            back();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        // speechRecognizer.destroy();
        // speechRecognizer.cancel();
        // firstnum = true;
        // listNum = 0;
        // back();
        speechSynthesizer
                .setParam(SpeechSynthesizer.PARAM_SPEAKER, voicePeople);// 说话的人
        if (judgeBack)
            speechSynthesizer.speak("结束导航");
        else
            speechSynthesizer.speak("悦时，继续为您导航");
        super.onPause();
    }

    // @Override
    // protected void onResume() {
    // // if (firstnum == false)//说明不是第一次进入
    // start();
    // super.onResume();
    // }

    // @Override
    // protected void onStart() {
    // start();
    // speechRecognizer.startListening(intent);
    // super.onStart();
    // }
    public void back() {
        speechRecognizer.destroy();
        speechRecognizer.cancel();
        firstnum = true;
        listNum = 0;
        judgeBack = true;
        imgList.clear();
        addPicList.clear();
        dataList.clear();
        speechSynthesizer
                .setParam(SpeechSynthesizer.PARAM_SPEAKER, voicePeople);// 说话的人
        speechSynthesizer.speak("结束导航");
        this.finish();
    }

    @Override
    protected void onDestroy() {

        speechRecognizer.destroy();
        // this.finish();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int key = 1;
        // if (resultCode == RESULT_OK) {
        // onResults(data.getExtras());
        // }else
        // if (FloatViewService.judgeNextOnClick == true) {p
        // onResults(data.getExtras());
        // }

    }

    public void start() {
        Message msg = new Message();
        handler.sendMessage(msg);
    }

    @Override
    public void onError(int error) {
        start();
    }

    @Override
    public void onResults(Bundle results) {
        long end2finish = System.currentTimeMillis() - speechEndTime;
        status = STATUS_None;
        ArrayList<String> nbest = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

        final String result = Arrays.toString(nbest.toArray(new String[nbest
                .size()]));
        // Log.i("efffffffff", "dffeefefeffffffffffffffffffffff");
        currentTxtLog.post(new Runnable() {
            @Override
            public void run() {
                excuteOrder(result);
            }
        });
        Toast.makeText(SpeechActivity.this, result, Toast.LENGTH_SHORT).show();
        String json_res = results.getString("origin_result");
        btn.setBackgroundResource(R.drawable.m_canspeak);
        btn.setText("");
        // btn.setText("语音监听中...");
        String strEnd2Finish = "";
        if (end2finish < 60 * 1000) {
            strEnd2Finish = "(waited " + end2finish + "ms)";
        }
        // txtResult.setText(nbest.get(0) + strEnd2Finish);
        btn.performClick();

    }

    private void speakText() {// 播放
        if (getLocalIpAddress() != null) {
            setParams();
            String[] speak = currentTxtLog.getText().toString().split(">");
            speak[1] = intToZH(listNum + 1) + "," + speak[1];
            int ret = speechSynthesizer.speak(speak[1]);
            speechRecognizer.cancel();
            if (firstnum == false) {

                btn.setBackgroundResource(R.drawable.m_cantspeak);
                btn.setText("");
            }
            // btn.setText("语音播放中...");
            // btn.setBackgroundColor(Color.parseColor("#fff003"));
            if (ret != 0) {
                logError("开始合成器失败：" + errorCodeAndDescription(ret));
            }
        } else {
            Toast.makeText(SpeechActivity.this, "世界上最遥远的距离就是没网",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void judgeUpOrDown() {// 判断是上一步还是下一步,传入下一list的值
        previousTxtLog.setText((listNum == 0 ? "" : "<"
                + String.valueOf(listNum) + "/" + dataList.size() + ">")
                + dataList.get(listNum).getPreviousText());
        imageView.setImageBitmap(imgList.get(listNum));
        currentTxtLog.setText("<" + String.valueOf((listNum + 1)) + "/"
                + dataList.size() + ">"
                + dataList.get(listNum).getCurrentText());
        nextTextLog.setText((listNum == dataList.size() - 1 ? "" : "<"
                + String.valueOf(listNum + 2) + "/" + dataList.size() + ">")
                + dataList.get(listNum).getNextText());

        speakText();
    }

    private void excuteOrder(String result) {
        // 执行完了一句指令之后下一步菜谱里的指令不会执行，重复说也不会再执行，第一句指令要么是下一步或是重复，重复之后的下一句不能执行，下一步也是。第一句菜谱说完后，下一句一定是人的指令，否则程序不会继续进行
        if (result.contains("下一步") || result.contains("后一步")) {
            if (listNum == (dataList.size() - 1)) {
                Toast.makeText(SpeechActivity.this, "已经是最后一步",
                        Toast.LENGTH_SHORT).show();
                speechRecognizer.startListening(intent);// 没有执行speech命令就再次开启
            } else {
                listNum++;
                // Log.v("ffff",
                // "efefffffffffffffffffffffffffffffffffffffffff");
                judgeUpOrDown();
            }

        } else if (result.contains("上一步") || result.contains("前一步")) {

            if (listNum == 0) {
                Toast.makeText(SpeechActivity.this, "已经是第一步了",
                        Toast.LENGTH_SHORT).show();
                speechRecognizer.startListening(intent);// 没有执行speech命令就再次开启
            } else {
                listNum--;
                judgeUpOrDown();
            }
        } else if (result.contains("重复") || result.contains("重播")) {
            speakText();
        } else if (result.contains("结束导航") || result.contains("停止导航")) {
            back();
        }
        // else if (result.contains("关闭帮助") && result.contains("语音选项") == false)
        // {
        // if (dlg != null) {
        // // 判断是否有dlg，否则关闭的时候会闪退，因为没有new呗
        // dlg.dismiss();
        // dlg = null;
        // }
        // speechRecognizer.startListening(intent);
        // } else if (result.contains("关闭语音选项") && result.contains("帮助") ==
        // false) {
        // if (voiceDlg != null) {
        // voiceDlg.dismiss();
        // voiceDlg = null;
        // }
        //
        // speechRecognizer.startListening(intent);
        // }
        else if (result.contains("关闭") || result.contains("返回")) {
            if (dlg != null) {
                // 判断是否有dlg，否则关闭的时候会闪退，因为没有new呗
                dlg.dismiss();
                dlg = null;
            }
            if (voiceDlg != null) {
                voiceDlg.dismiss();
                voiceDlg = null;
            }
            speechRecognizer.startListening(intent);
        } else if ((result.contains("help") || result.contains("Help")
                || result.contains("指令") || result.contains("帮助"))
                && result.contains("关") == false && dlg == null) {
            alertDialog();
            speechRecognizer.startListening(intent);
        } else if ((result.contains("声音") || result.contains("语音") || result
                .contains("配音")) && voiceDlg == null) {
            alertDialogVoice();
            speechRecognizer.startListening(intent);
        } else if (result.contains("女声") || result.contains("女生")) {
            voicePeople = "0";
            if (voiceDlg != null) {
                voiceDlg.dismiss();
                voiceDlg = null;
            }
            speechRecognizer.startListening(intent);
        } else if (result.contains("男声") || result.contains("男生")) {
            voicePeople = "3";
            if (voiceDlg != null) {
                voiceDlg.dismiss();
                voiceDlg = null;
            }
            speechRecognizer.startListening(intent);
        } else if (result.contains("童声") || result.contains("童生")) {
            voicePeople = "4";
            if (voiceDlg != null) {
                voiceDlg.dismiss();
                voiceDlg = null;
            }
            speechRecognizer.startListening(intent);
        } else {
            speechRecognizer.startListening(intent);// 没有执行speech命令就再次开启
        }
    }

    /**
     * 将数字转换成中文数字
     *
     * @author Prosper
     *
     */
    public static String intToZH(int i) {
        String[] zh = { "零", "一", "二", "三", "四", "五", "六", "七", "八", "九" };
        String[] unit = { "", "十", "百", "千", "万", "十", "百", "千", "亿", "十" };

        String str = "";
        StringBuffer sb = new StringBuffer(String.valueOf(i));
        sb = sb.reverse();
        int r = 0;
        int l = 0;
        for (int j = 0; j < sb.length(); j++) {
            /**
             * 当前数字
             */
            r = Integer.valueOf(sb.substring(j, j + 1));

            if (j != 0)
            /**
             * 上一个数字
             */
                l = Integer.valueOf(sb.substring(j - 1, j));

            if (j == 0) {
                if (r != 0 || sb.length() == 1)
                    str = zh[r];
                continue;
            }

            if (j == 1 || j == 2 || j == 3 || j == 5 || j == 6 || j == 7
                    || j == 9) {
                if (r != 0)
                    str = zh[r] + unit[j] + str;
                else if (l != 0)
                    str = zh[r] + str;
                continue;
            }

            if (j == 4 || j == 8) {
                str = unit[j] + str;
                if ((l != 0 && r == 0) || r != 0)
                    str = zh[r] + str;
                continue;
            }
        }
        return str;
    }

    private void setSimulateClick(View view, float x, float y) {
        long downTime = SystemClock.uptimeMillis();
        final MotionEvent downEvent = MotionEvent.obtain(downTime, downTime,
                MotionEvent.ACTION_DOWN, x, y, 0);
        downTime += 1000;
        final MotionEvent upEvent = MotionEvent.obtain(downTime, downTime,
                MotionEvent.ACTION_UP, x, y, 0);
        view.onTouchEvent(downEvent);
        view.onTouchEvent(upEvent);
        downEvent.recycle();
        upEvent.recycle();
    }

    private void cancel() {
        speechRecognizer.cancel();
        status = STATUS_None;
        // print("点击了“取消”");
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        status = STATUS_Ready;
        // print("准备就绪，可以开始说话");
    }

    @Override
    public void onBeginningOfSpeech() {
        status = STATUS_Speaking;
        btn.setBackgroundResource(R.drawable.m_null);
        btn.setText("说完了");
        // print("检测到用户的已经开始说话");
    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    //
    @Override
    public void onEndOfSpeech() {
        speechEndTime = System.currentTimeMillis();
        status = STATUS_Recognition;
        // print("检测到用户的已经停止说话");
        // btn.setText("识别中");
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        ArrayList<String> nbest = partialResults
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if (nbest.size() > 0) {
            // print("~临时识别结果：" + Arrays.toString(nbest.toArray(new
            // String[0])));
            // txtResult.setText(nbest.get(0));
            btn.setBackgroundResource(R.drawable.m_null);
            btn.setText(nbest.get(0));
        }
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        switch (eventType) {
            case EVENT_ERROR:
                String reason = params.get("reason") + "";
                // print("EVENT_ERROR, " + reason);
                break;
            case VoiceRecognitionService.EVENT_ENGINE_SWITCH:
                int type = params.getInt("engine_type");
                // print("*引擎切换至" + (type == 0 ? "在线" : "离线"));
                break;
        }
    }

    private void print2(String msg) {
        // txtLog.append(msg + "\n");
        // ScrollView sv = (ScrollView) txtLog.getParent();
        // sv.smoothScrollTo(0, 1000000);
        // System.out.println(msg);
        Log.d(TAG, "----" + msg);
    }

    private String buildTestSlotData() {
        JSONObject slotData = new JSONObject();
        JSONArray name = new JSONArray().put("李涌泉").put("郭下纶");
        JSONArray song = new JSONArray().put("七里香").put("发如雪");
        JSONArray artist = new JSONArray().put("周杰伦").put("李世龙");
        JSONArray app = new JSONArray().put("手机百度").put("百度地图");
        JSONArray usercommand = new JSONArray().put("关灯").put("开门");
        try {
            slotData.put(Constant.EXTRA_OFFLINE_SLOT_NAME, name);
            slotData.put(Constant.EXTRA_OFFLINE_SLOT_SONG, song);
            slotData.put(Constant.EXTRA_OFFLINE_SLOT_ARTIST, artist);
            slotData.put(Constant.EXTRA_OFFLINE_SLOT_APP, app);
            slotData.put(Constant.EXTRA_OFFLINE_SLOT_USERCOMMAND, usercommand);
        } catch (JSONException e) {

        }
        return slotData.toString();
    }

    public void bindParams(Intent intent) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(this);
        if (sp.getBoolean("tips_sound", true)) {
            intent.putExtra(Constant.EXTRA_SOUND_START,
                    R.raw.bdspeech_recognition_start);
            intent.putExtra(Constant.EXTRA_SOUND_END, R.raw.bdspeech_speech_end);
            intent.putExtra(Constant.EXTRA_SOUND_SUCCESS,
                    R.raw.bdspeech_recognition_success);
            intent.putExtra(Constant.EXTRA_SOUND_ERROR,
                    R.raw.bdspeech_recognition_error);
            intent.putExtra(Constant.EXTRA_SOUND_CANCEL,
                    R.raw.bdspeech_recognition_cancel);
        }
        if (sp.contains(Constant.EXTRA_INFILE)) {
            String tmp = sp.getString(Constant.EXTRA_INFILE, "")
                    .replaceAll(",.*", "").trim();
            intent.putExtra(Constant.EXTRA_INFILE, tmp);
        }
        if (sp.getBoolean(Constant.EXTRA_OUTFILE, false)) {
            intent.putExtra(Constant.EXTRA_OUTFILE, "sdcard/outfile.pcm");
        }
        if (sp.contains(Constant.EXTRA_SAMPLE)) {
            String tmp = sp.getString(Constant.EXTRA_SAMPLE, "")
                    .replaceAll(",.*", "").trim();
            if (null != tmp && !"".equals(tmp)) {
                intent.putExtra(Constant.EXTRA_SAMPLE, Integer.parseInt(tmp));
            }
        }
        if (sp.contains(Constant.EXTRA_LANGUAGE)) {
            String tmp = sp.getString(Constant.EXTRA_LANGUAGE, "")
                    .replaceAll(",.*", "").trim();
            if (null != tmp && !"".equals(tmp)) {
                intent.putExtra(Constant.EXTRA_LANGUAGE, tmp);
            }
        }
        if (sp.contains(Constant.EXTRA_NLU)) {
            String tmp = sp.getString(Constant.EXTRA_NLU, "")
                    .replaceAll(",.*", "").trim();
            if (null != tmp && !"".equals(tmp)) {
                intent.putExtra(Constant.EXTRA_NLU, tmp);
            }
        }

        if (sp.contains(Constant.EXTRA_VAD)) {
            String tmp = sp.getString(Constant.EXTRA_VAD, "")
                    .replaceAll(",.*", "").trim();
            if (null != tmp && !"".equals(tmp)) {
                intent.putExtra(Constant.EXTRA_VAD, tmp);
            }
        }
        String prop = null;
        if (sp.contains(Constant.EXTRA_PROP)) {
            String tmp = sp.getString(Constant.EXTRA_PROP, "")
                    .replaceAll(",.*", "").trim();
            if (null != tmp && !"".equals(tmp)) {
                intent.putExtra(Constant.EXTRA_PROP, Integer.parseInt(tmp));
                prop = tmp;
            }
        }

        // offline asr
        {
            intent.putExtra(Constant.EXTRA_OFFLINE_ASR_BASE_FILE_PATH,
                    "/sdcard/easr/s_1");
            intent.putExtra(Constant.EXTRA_LICENSE_FILE_PATH,
                    "/sdcard/easr/license-tmp-20150530.txt");
            if (null != prop) {
                int propInt = Integer.parseInt(prop);
                if (propInt == 10060) {
                    intent.putExtra(Constant.EXTRA_OFFLINE_LM_RES_FILE_PATH,
                            "/sdcard/easr/s_2_Navi");
                } else if (propInt == 20000) {
                    intent.putExtra(Constant.EXTRA_OFFLINE_LM_RES_FILE_PATH,
                            "/sdcard/easr/s_2_InputMethod");
                }
            }
            intent.putExtra(Constant.EXTRA_OFFLINE_SLOT_DATA,
                    buildTestSlotData());
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

    // 语音合成
    public void setParams() {
        speechSynthesizer
                .setParam(SpeechSynthesizer.PARAM_SPEAKER, voicePeople);// 说话的人
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, "5");// 中级音量
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED, "5");// 速度
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_PITCH, "5");// 中调
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_AUDIO_ENCODE,//
                SpeechSynthesizer.AUDIO_ENCODE_AMR);
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_AUDIO_RATE,//
                SpeechSynthesizer.AUDIO_BITRATE_AMR_15K85);
        // speechSynthesizer.setParam(SpeechSynthesizer.PARAM_LANGUAGE,
        // SpeechSynthesizer.LANGUAGE_ZH);
        // speechSynthesizer.setParam(SpeechSynthesizer.PARAM_NUM_PRON, "0");
        // speechSynthesizer.setParam(SpeechSynthesizer.PARAM_ENG_PRON, "0");
        // speechSynthesizer.setParam(SpeechSynthesizer.PARAM_PUNC, "0");
        // speechSynthesizer.setParam(SpeechSynthesizer.PARAM_BACKGROUND, "0");
        // speechSynthesizer.setParam(SpeechSynthesizer.PARAM_STYLE, "0");
        // speechSynthesizer.setParam(SpeechSynthesizer.PARAM_TERRITORY, "0");
    }

    @Override
    public void onStartWorking(SpeechSynthesizer synthesizer) {
        logDebug("开始工作，请等待数据...");
    }

    @Override
    public void onSpeechStart(SpeechSynthesizer synthesizer) {
        logDebug("朗读开始");
    }

    @Override
    public void onSpeechResume(SpeechSynthesizer synthesizer) {
        logDebug("朗读继续");
    }

    @Override
    public void onSpeechProgressChanged(SpeechSynthesizer synthesizer,
                                        int progress) {
    }

    @Override
    public void onSpeechPause(SpeechSynthesizer synthesizer) {
        logDebug("朗读已暂停");
    }

    // 语音合成结束
    @Override
    public void onSynthesizeFinish(SpeechSynthesizer arg0) {

    }

    @Override
    public void onSpeechFinish(SpeechSynthesizer synthesizer) {// 每次运行两遍啊，刚开始和结束。
        Log.i("停止了哈哈哈哈哈或或或好好", "edeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
        // judgeSpeechEnd = true;
        // if (firstnum)
        // btn.setText("开启监听");
        if (getLocalIpAddress() != null && firstnum == false) {
            // btn.setBackgroundColor(Color.parseColor("#ffffff"));
            start();
            btn.setBackgroundResource(R.drawable.m_canspeak);
            btn.setText("");
            // btn.setText("语音监听中...");
        }

    }

    @Override
    public void onNewDataArrive(SpeechSynthesizer synthesizer,
                                byte[] audioData, boolean isLastData) {
        logDebug("新的音频数据：" + audioData.length + (isLastData ? "(end)" : ""));
    }

    @Override
    public void onError(SpeechSynthesizer synthesizer, SpeechError error) {
        logError("发生错误：" + error.errorDescription + "(" + error.errorCode + ")");
    }

    @Override
    public void onCancel(SpeechSynthesizer synthesizer) {
        logDebug("已取消");
    }

    @Override
    public void onBufferProgressChanged(SpeechSynthesizer synthesizer,
                                        int progress) {
        // TODO Auto-generated method stub

    }

    private void logDebug(String logMessage) {
        logMessage(logMessage, Color.BLUE);
    }

    private void logError(String logMessage) {
        logMessage(logMessage, Color.RED);
    }

    private void logMessage(String logMessage, int color) {
        Spannable colorfulLog = new SpannableString(logMessage + "\n");
        colorfulLog.setSpan(new ForegroundColorSpan(color), 0,
                logMessage.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // uiHandler.sendMessage(uiHandler.obtainMessage(UI_LOG_TO_VIEW,
        // colorfulLog));
    }

    private void scrollLogViewToBottom2() {
    }

    private String errorCodeAndDescription(int errorCode) {
        String errorDescription = SpeechError.errorDescription(errorCode);
        return errorDescription + "(" + errorCode + ")";
    }

}
