package com.vtv.vuatiengviet;

public class ThongTinNguoiChoi {
    private String name;
    private int ruby;
    private int level;
    private int avt_id;
    private int khung_id;
    private  String damua_avt;
    private String damua_khung;

    public ThongTinNguoiChoi() {
    }

    public ThongTinNguoiChoi(String name, int ruby, int level, int avt_id, int khung_id) {
        this.name = name;
        this.ruby = ruby;
        this.level = level;
        this.avt_id = avt_id;
        this.khung_id = khung_id;
    }

    public ThongTinNguoiChoi(String name, int ruby, int level, int avt_id, int khung_id, String damua_avt, String damua_khung) {
        this.name = name;
        this.ruby = ruby;
        this.level = level;
        this.avt_id = avt_id;
        this.khung_id = khung_id;
        this.damua_avt = damua_avt;
        this.damua_khung = damua_khung;
    }

    public String getDamua_avt() {
        return damua_avt;
    }

    public void setDamua_avt(String damua_avt) {
        this.damua_avt = damua_avt;
    }

    public String getDamua_khung() {
        return damua_khung;
    }

    public void setDamua_khung(String damua_khung) {
        this.damua_khung = damua_khung;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRuby() {
        return ruby;
    }

    public void setRuby(int ruby) {
        this.ruby = ruby;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getAvt_id() {
        return avt_id;
    }

    public void setAvt_id(int avt_id) {
        this.avt_id = avt_id;
    }

    public int getKhung_id() {
        return khung_id;
    }

    public void setKhung_id(int khung_id) {
        this.khung_id = khung_id;
    }
}
