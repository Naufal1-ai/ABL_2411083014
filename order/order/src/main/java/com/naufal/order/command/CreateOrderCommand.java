package com.naufal.order.command;

public class CreateOrderCommand {

    private Long produkId;
    private Long pelangganId;
    private String tglTransaksi;
    private Integer jumlah;
    private Double total;

    public CreateOrderCommand() {
    }

    public CreateOrderCommand(Long produkId, Long pelangganId,
            String tglTransaksi, Integer jumlah, Double total) {
        this.produkId = produkId;
        this.pelangganId = pelangganId;
        this.tglTransaksi = tglTransaksi;
        this.jumlah = jumlah;
        this.total = total;
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

    public String getTglTransaksi() {
        return tglTransaksi;
    }

    public void setTglTransaksi(String tglTransaksi) {
        this.tglTransaksi = tglTransaksi;
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
