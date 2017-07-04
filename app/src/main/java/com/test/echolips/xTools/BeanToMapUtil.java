package com.test.echolips.xTools;

import com.test.echolips.bean.EcCookingredient;
import com.test.echolips.bean.EcCookitem;
import com.test.echolips.bean.EcCookmenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Xuwenchao
 *
 */
public class BeanToMapUtil {
	public static Map<String, Object> CookMenuToMap(EcCookmenu cookMenu){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cookname", cookMenu.getMenuName());
		map.put("introduction", cookMenu.getIntroduction());
		map.put("imgsrc", cookMenu.getImgNetUrl());
		map.put("mealsHard", cookMenu.getMealsHard());
		map.put("mealsnumber", cookMenu.getMealsNum());
		map.put("mealsTime", cookMenu.getMealsTime());
		map.put("labels", cookMenu.getLabels());
		map.put("cookingpoint", cookMenu.getCookPoint());
		return map;
	}
	public static Map<String, Object> CookItemToMap(List<EcCookitem> list){
		int size = (int) (list.size()/.75) + 2;
		Map<String, Object> map = new HashMap<String, Object>(size);
		List<String> itemImgNetUrl = new ArrayList<String>();
		List<String> itemContent = new ArrayList<String>();
		for(EcCookitem cookItem : list){
			itemImgNetUrl.add(cookItem.getItemImgNetUrl());
			itemContent.add(cookItem.getItemContent());
		}
		map.put("pictureurl", itemImgNetUrl);
		map.put("itemContent", itemContent);
		return map;
	}
	public static Map<String, Object> CookIngredientToMap(List<EcCookingredient> list){
		int size = (int) (list.size()/.75) + 2;
		Map<String, Object> map = new HashMap<String, Object>(size);
		List<String> ingredientName = new ArrayList<String>();
		List<String> companyName = new ArrayList<String>();
		List<String> num = new ArrayList<String>();
		for(EcCookingredient cookIngredient : list){
			ingredientName.add(cookIngredient.getIngredientname());
			companyName.add(cookIngredient.getCompanyname());
			num.add(Float.toString(cookIngredient.getNum()));
		}
		map.put("ingredientname", ingredientName);
		map.put("companyname", companyName);
		map.put("num", num);
		return map;
	}
}
