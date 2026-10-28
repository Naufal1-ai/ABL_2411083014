package com.naufal.order.event;

/**
 * OrderEvent = data yang dikirim ke RabbitMQ saat ada order baru.
 *
 * Class ini adalah "surat" yang ditaruh di dalam queue.
 * Email Service akan membacanya dan mengirim notifikasi ke pelanggan.
 *
 * Perhatikan: field-nya sengaja dibuat sederhana (hanya data penting)
 * agar Email Service tidak perlu tahu seluruh isi database Order.
 */
public class OrderEvent {

    private Long orderId;
    private Long produkId;
    private Long pelangganId;
    private Integer jumlah;
    private Double total;

    public OrderEvent() {
    }

    public OrderEvent(Long orderId, Long produkId, Long pelangganId, Integer jumlah, Double total) {
        this.orderId = orderId;
        this.produkId = produkId;
        this.pelangganId = pelangganId;
        this.jumlah = jumlah;
        this.total = total;
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

    @Override
    public String toString() {
        return "OrderEvent{orderId=" + orderId + ", produkId=" + produkId
                + ", pelangganId=" + pelangganId + ", jumlah=" + jumlah + ", total=" + total + "}";
    }
}
