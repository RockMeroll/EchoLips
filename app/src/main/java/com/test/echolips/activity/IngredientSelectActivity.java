package com.test.echolips.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.test.echolips.R;
import com.test.echolips.bean.EcCookingredient;
import com.test.echolips.dao.IngredientDao;


public class IngredientSelectActivity extends BaseActivity {
	private EditText editText;
	private PopupWindow popWindow;
	private Button addIngredientFinishBtn;
	private LinearLayout linearLayout;
	private ArrayAdapter<String> adapter;
	private List<LinearLayout> layoutList;
	//得到的食材
	private ArrayList<EcCookingredient> ingredientList;
	//初始化食材
	private List<String> ldata;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ingredient_select_layout);
		init();
		initPopWindow();
	}

	private void init() {
		ingredientList = new ArrayList<>();
		layoutList = new ArrayList<>();
		ldata = new IngredientDao(IngredientSelectActivity.this).queryLabelsFuzzy("");
		addIngredientFinishBtn = (Button) findViewById(R.id.add_ingredient_finish_btn);
		linearLayout = (LinearLayout) findViewById(R.id.linear_layout_id);
		addIngredientFinishBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				for(LinearLayout ll : layoutList){
					ingredientList.add(new EcCookingredient(((TextView)ll.getChildAt(0)).getText().toString(),
							((Spinner)ll.getChildAt(2)).getSelectedItem().toString(),
							Float.parseFloat(((Spinner)ll.getChildAt(1)).getSelectedItem().toString())));
				}
				Intent intent = new Intent();
				intent.putExtra("ingredients", ingredientList);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		editText = (EditText) findViewById(R.id.ingredient_query);
		editText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				ldata.clear();
				ldata.addAll(new IngredientDao(IngredientSelectActivity.this).queryLabelsFuzzy(editText.getText().toString()));
				if(ldata.size() > 0){
					getPopWindow();
					popWindow.showAsDropDown(editText);
				}else{
					Toast.makeText(IngredientSelectActivity.this, "没有这种食材", Toast.LENGTH_SHORT).show();
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
	private void initPopWindow()
	{
		View pop_view = getLayoutInflater().inflate(R.layout.ingredient_listview_layout, null, false);
		ListView listView = (ListView) pop_view.findViewById(R.id.ingredient_list_view);
		adapter = new ArrayAdapter<String>(IngredientSelectActivity.this,
				android.R.layout.simple_list_item_1, ldata);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos, long id) {
				String ingredientName = ldata.get(pos);
				addlayout(ingredientName);
				updateLinerLayout();
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
	private void addlayout(String ingredientName){
		LinearLayout ly = new LinearLayout(IngredientSelectActivity.this);
		ly.setOrientation(LinearLayout.HORIZONTAL);
		TextView textView = new TextView(IngredientSelectActivity.this);
		textView.setWidth(linearLayout.getWidth() / 3);
		textView.setHeight(100);
		textView.setGravity(Gravity.CENTER);
		textView.setBackgroundColor(0xffa6937c);
		textView.setText(ingredientName);
		textView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				layoutList.remove(v.getParent());
				updateLinerLayout();
			}
		});

		String[] arrs = new String[100];
		arrs[0] = "0.3";
		arrs[1] = "0.5";
		int index = 2;
		for(int j = 1; j <= 98; j++){
			arrs[index++] = Integer.toString(j);
		}
		//获取下拉框的宽度
		LinearLayout.LayoutParams sp_params =
				new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		sp_params.width = linearLayout.getWidth() / 3;
		//数字下拉框
		Spinner spinnerNum = new Spinner(IngredientSelectActivity.this);
		spinnerNum.setMinimumHeight(100);
		spinnerNum.setLayoutParams(sp_params);
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(IngredientSelectActivity.this,
				android.R.layout.simple_spinner_item, arrs);
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerNum.setAdapter(adapter1);
		//单位下拉框
		Spinner spinnerCompany = new Spinner(IngredientSelectActivity.this);
		spinnerCompany.setMinimumHeight(100);
		spinnerCompany.setLayoutParams(sp_params);
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(IngredientSelectActivity.this,
				android.R.layout.simple_spinner_item, new String[] {"g","ml","根","个","盒","条",
				"块","只","颗","粒","段","勺","滴","片","杯","匙","张","斤","两"});
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerCompany.setAdapter(adapter2);
		//添加View
		ly.addView(textView);
		ly.addView(spinnerNum);
		ly.addView(spinnerCompany);

		layoutList.add(ly);
	}

	private void updateLinerLayout(){
		linearLayout.removeAllViews();
		for(LinearLayout i : layoutList){
			linearLayout.addView(i);
		}
	}
	private void getPopWindow(){
		if(null != popWindow){
			popWindow.dismiss();
			return;
		}else{
			adapter.notifyDataSetChanged();
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.ingredient_select, menu);
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
