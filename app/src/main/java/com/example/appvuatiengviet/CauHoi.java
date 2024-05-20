package com.example.appvuatiengviet;

public class CauHoi {
    private int id;
    private String Tu;
    private String DapAn;
    private int TinhTrang;

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

    public String getDapAn() {
        return DapAn;
    }

    public void setDapAn(String dapAn) {
        DapAn = dapAn;
    }

    public int getTinhTrang() {
        return TinhTrang;
    }

    public void setTinhTrang(int tinhTrang) {
        TinhTrang = tinhTrang;
    }

    public CauHoi(int id, String Tu, String dapAn, int tinhTrang) {
        this.id = id;
        this.Tu = Tu;
        DapAn = dapAn;
        TinhTrang = tinhTrang;
    }

    public CauHoi() {
    }

}
