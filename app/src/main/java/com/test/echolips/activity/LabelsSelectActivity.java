package com.test.echolips.activity;
import java.util.ArrayList;
import java.util.List;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.test.echolips.R;
import com.test.echolips.dao.LabelDao;
import com.test.echolips.xTools.LogUtil;

public class LabelsSelectActivity extends BaseActivity {
	private EditText editText;
	private PopupWindow popWindow;
	private TableLayout tableLayout;
	private TableRow tableRow;
	private ArrayAdapter<String> adapter;
	private Button addLabelsFinishBtn;
	//得到的标签
	private ArrayList<String> labelList;
	//初始化标签
	private List<String> ldata;
	private void init(){
		labelList = new ArrayList<String>();
		ldata = new LabelDao(LabelsSelectActivity.this).queryLabelsFuzzy("");
		tableLayout = (TableLayout) findViewById(R.id.table_layout_id);
		addLabelsFinishBtn = (Button) findViewById(R.id.add_labels_finish_btn);
		addLabelsFinishBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("labels", labelList);
				LogUtil.v("LabelsSelectActivutyLabels", labelList.toString());
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		editText = (EditText) findViewById(R.id.labels_query);
		editText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				ldata.clear();
				ldata.addAll(new LabelDao(LabelsSelectActivity.this).queryLabelsFuzzy(editText.getText().toString()));
				if(ldata.size() > 0){
					getPopWindow();
					popWindow.showAsDropDown(editText);
				}else{
					Toast.makeText(LabelsSelectActivity.this, "没有这种标签", Toast.LENGTH_SHORT).show();
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				editText.setFocusable(true);
			}
		});
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.labels_select_layout);
		init();
		initPopWindow();
	}
	private void initPopWindow()
	{
		View pop_view = getLayoutInflater().inflate(R.layout.label_listview_layout, null, false);
		ListView listView = (ListView) pop_view.findViewById(R.id.label_list_view);
		adapter = new ArrayAdapter<String>(LabelsSelectActivity.this,
				android.R.layout.simple_list_item_1, ldata);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos, long id) {
				String label = ldata.get(pos);
				if(labelList.contains(label)){
					return;
				}
				labelList.add(label);
				updateTableLayout();
			}
		});
		popWindow = new PopupWindow(pop_view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
		pop_view.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(popWindow != null && popWindow.isShowing()){
					popWindow.dismiss();
					popWindow = null;
				}
				return false;
			}
		});
	}
	private void updateTableLayout(){
		tableLayout.removeAllViews();
		int w_screen = tableLayout.getWidth();
		for(int i = 0; i < labelList.size(); i++){
			TextView textView = new TextView(LabelsSelectActivity.this);
			textView.setWidth(w_screen / 3);
			textView.setHeight(100);
			textView.setGravity(Gravity.CENTER);
			//textView.setBackgroundColor(0xffa6937c);
			String str = labelList.get(i);
//			SpannableStringBuilder style = new SpannableStringBuilder(str);
//			style.setSpan(new BackgroundColorSpan(0xffa6937c),
//					0 , str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//			textView.setText(style);
			textView.setText(str);
			textView.setBackgroundColor(0xffa6937c);
			textView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					labelList.remove(((TextView)v).getText().toString());
					updateTableLayout();
				}
			});
			if(i % 3 == 0){
				tableRow = new TableRow(LabelsSelectActivity.this);
				tableRow.setWeightSum(1);
				tableLayout.addView(tableRow);
			}
			tableRow.addView(textView);
		}
	}
	private void getPopWindow(){
		if(null != popWindow){
			popWindow.dismiss();
			return;
		}else{
			//initPopWindow();
			adapter.notifyDataSetChanged();
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.labels_select, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
