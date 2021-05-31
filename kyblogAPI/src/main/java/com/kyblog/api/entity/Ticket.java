package com.kyblog.api.entity;

public class Ticket {
    private String ticket;
    private Long userId;

    @Override
    public String toString() {
        return "Ticket{" +
                "ticket='" + ticket + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
