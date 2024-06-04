package com.vtv.appvuatiengviet;

public class LoiChinhTa {
    private int id;
    private String Tu;
    private String DapAnDung;
    private String DapAnSai;

    public LoiChinhTa() {
    }

    public LoiChinhTa(int id, String tu, String dapAnDung, String dapAnSai) {
        this.id = id;
        Tu = tu;
        DapAnDung = dapAnDung;
        DapAnSai = dapAnSai;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTu() {
        return Tu;
    }

    public void setTu(String tu) {
        Tu = tu;
    }

    public String getDapAnDung() {
        return DapAnDung;
    }

    public void setDapAnDung(String dapAnDung) {
        DapAnDung = dapAnDung;
    }

    public String getDapAnSai() {
        return DapAnSai;
    }

    public void setDapAnSai(String dapAnSai) {
        DapAnSai = dapAnSai;
    }
}
