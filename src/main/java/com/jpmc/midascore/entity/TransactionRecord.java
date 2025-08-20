package com.jpmc.midascore.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import org.apache.catalina.User;
import org.springframework.context.annotation.Primary;

import java.time.LocalDateTime;

@Entity
public class TransactionRecord {

    @Id
    @GeneratedValue
    private Long Id;

    @ManyToOne
    private UserRecord sender;

    @ManyToOne
    private UserRecord receiver;

    private float amount;

    private LocalDateTime timeStamp;

    private float incentive;

    public float getIncentive() {
        return incentive;
    }

    public void setIncentive(float incentive) {
        this.incentive = incentive;
    }

    protected TransactionRecord(){} // JPA default constructor

    public TransactionRecord(UserRecord sender, UserRecord receiver, float amount){
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public UserRecord getSender() {
        return sender;
    }

    public void setSender(UserRecord sender) {
        this.sender = sender;
    }

    public UserRecord getReceiver() {
        return receiver;
    }

    public void setReceiver(UserRecord receiver) {
        this.receiver = receiver;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }
}
