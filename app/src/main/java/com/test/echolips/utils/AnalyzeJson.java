package com.test.echolips.utils;

import com.alibaba.fastjson.JSON;
import com.test.echolips.bean.EcCookmenu;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AnalyzeJson{
    //menu的list
    List<EcCookmenu> EcCookmenus = new ArrayList<EcCookmenu>();
    //构造函数
    public List<EcCookmenu> JsonToList(String response){
        //将json字符串转化为list
        //List<Student> students = JSON.parseArray(json_array, Student.class);
            EcCookmenus = JSON.parseArray(response,EcCookmenu.class);
            return EcCookmenus;
        }
    }
