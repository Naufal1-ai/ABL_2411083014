package com.naufal.order.query;

public class GetOrderByIdQuery {

    private Long id;

    public GetOrderByIdQuery() {
    }

    public GetOrderByIdQuery(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
