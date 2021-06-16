package com.payment.yatvik.mygooglepay.Helpers;

/**
 * Created by anupamchugh on 22/12/17.
 */

public class MenuModel {

    public int imgId;
    public String menuName;
    public String url;
    public boolean hasChildren, isGroup;

    public MenuModel(int imgId, String menuName, boolean isGroup, boolean hasChildren, String url) {
        this.imgId = imgId;
        this.menuName = menuName;
        this.url = url;
        this.isGroup = isGroup;
        this.hasChildren = hasChildren;
    }
}
