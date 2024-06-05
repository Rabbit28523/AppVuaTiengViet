package com.vtv.vuatiengviet;

public class CaDao {
    private int id;
    private String HinhAnh;
    private String DapAn;

    public CaDao() {}

    public CaDao(int id, String hinhAnh, String dapAn) {
        this.id = id;
        HinhAnh = hinhAnh;
        DapAn = dapAn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHinhAnh() {
        return HinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        HinhAnh = hinhAnh;
    }

    public String getDapAn() {
        return DapAn;
    }

    public void setDapAn(String dapAn) {
        DapAn = dapAn;
    }
}
