package com.example.myapplication_uni_bielefeld;


public class PagerItemModel {
    private int image;
    private String title;
    private String desc;
    private String button;

    public PagerItemModel(int image, String title, String desc, String button) {
        this.image = image;
        this.title = title;
        this.desc = desc;
        this.button = button;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getButton() {
        return button;
    }

    public  void setButton(String button) {
        this.button = button;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}



