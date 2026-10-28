package com.naufal.email.event;

/**
 * OrderEvent = struktur data event order yang dikirim service order.
 *
 * Field HARUS sama dengan OrderEvent di service order agar JSON-nya
 * bisa dibaca dengan benar.
 */
public class OrderEvent {

    private Long orderId;
    private Long produkId;
    private Long pelangganId;
    private Integer jumlah;
    private Double total;

    public OrderEvent() {
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getProdukId() {
        return produkId;
    }

    public void setProdukId(Long produkId) {
        this.produkId = produkId;
    }

    public Long getPelangganId() {
        return pelangganId;
    }

    public void setPelangganId(Long pelangganId) {
        this.pelangganId = pelangganId;
    }

    public Integer getJumlah() {
        return jumlah;
    }

    public void setJumlah(Integer jumlah) {
        this.jumlah = jumlah;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
