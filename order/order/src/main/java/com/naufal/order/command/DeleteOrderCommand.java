package com.naufal.order.command;

public class DeleteOrderCommand {

    private Long id;

    public DeleteOrderCommand() {
    }

    public DeleteOrderCommand(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
