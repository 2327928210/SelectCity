package com.haoke.selectcity.bean;

/**
 * 介绍：美团最顶部Header
 * 作者：zhangxutong
 * 邮箱：mcxtzhang@163.com
 * CSDN：http://blog.csdn.net/zxt0601
 * 时间： 16/11/28.
 */

public class SelectCityTopHeaderBean {
    private String txt;

    public SelectCityTopHeaderBean(String txt) {
        this.txt = txt;
    }

    public String getTxt() {
        return txt;
    }

    public SelectCityTopHeaderBean setTxt(String txt) {
        this.txt = txt;
        return this;
    }

}
