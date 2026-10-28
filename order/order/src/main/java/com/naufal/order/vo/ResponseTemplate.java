package com.naufal.order.vo;

import com.naufal.order.entity.Order;

public class ResponseTemplate {
    private Order order;
    private Produk produk;

    public ResponseTemplate() {
    }

    public ResponseTemplate(Order order, Produk produk) {
        this.order = order;
        this.produk = produk;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Produk getProduk() {
        return produk;
    }

    public void setProduk(Produk produk) {
        this.produk = produk;
    }
}
