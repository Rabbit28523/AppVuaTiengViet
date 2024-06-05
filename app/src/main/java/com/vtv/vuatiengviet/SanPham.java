package com.vtv.vuatiengviet;

public class SanPham {
    private int id;
    private String hinhAnh;
    private int price;
    private int tinhtrang;

    public SanPham() {}

    public SanPham(int id, String hinhAnh, int price, int tinhtrang) {
        this.id = id;
        this.hinhAnh = hinhAnh;
        this.price = price;
        this.tinhtrang = tinhtrang;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getTinhtrang() {
        return tinhtrang;
    }

    public void setTinhtrang(int tinhtrang) {
        this.tinhtrang = tinhtrang;
    }
}
