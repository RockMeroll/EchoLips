package com.test.echolips.bean;

/**
 * Created by 14439 on 2017-1-9.
 */
/*
"clList":[{"id":5,"cid":4,"uid":1,"labelname":"减肥"},
{"id":6,"cid":4,"uid":1,"labelname":"丰胸"},
{"id":7,"cid":4,"uid":1,"labelname":"降血压"},
{"id":8,"cid":4,"uid":1,"labelname":"清热解毒"}],
 */
public class Difficulty {

    int id;
    int cid;
    int uid;
    String teg;
    String labelname;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getTeg() {
        return teg;
    }

    public void setTeg(String teg) {
        this.teg = teg;
    }

    public String getLabelname() {
        return labelname;
    }

    public void setLabelname(String labelname) {
        this.labelname = labelname;
    }

    public Difficulty() {
    }

    public Difficulty(int id, int cid, int uid, String teg, String labelname) {
        this.id = id;
        this.cid = cid;
        this.uid = uid;
        this.teg = teg;
        this.labelname = labelname;
    }
}
