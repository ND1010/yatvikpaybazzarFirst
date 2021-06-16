package com.payment.yatvik.mygooglepay.Model;

public class CatListGetSet {
    private String id;
    private String user_id;
    private String module_slug;
    private String module_sta;

    public CatListGetSet(){

    }

    public CatListGetSet(String id, String user_id, String module_slug, String module_sta) {
        this.id = id;
        this.user_id = user_id;
        this.module_slug = module_slug;
        this.module_sta = module_sta;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getModule_slug() {
        return module_slug;
    }

    public void setModule_slug(String module_slug) {
        this.module_slug = module_slug;
    }

    public String getModule_sta() {
        return module_sta;
    }

    public void setModule_sta(String module_sta) {
        this.module_sta = module_sta;
    }
}
