package com.example.bank.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;


import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.example.bank.Model.Enumeration.ResponseCode;
import com.example.bank.Model.Enumeration.Side;
import com.example.bank.Model.Enumeration.TransactionCode;


@Entity
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int history_id;
    private Long rrn;
    
    @Enumerated(EnumType.STRING)
    private TransactionCode transactionCode;
    
    @Enumerated(EnumType.STRING)
    private Side side;
    
    private BigDecimal Amount;
    
    @Enumerated(EnumType.STRING)
    private ResponseCode responseCode;
    
    private LocalDateTime transactionDate;
    

	public History() {

	}

	
	public int getHistory_id() {
		return history_id;
	}


	public void setHistory_id(int history_id) {
		this.history_id = history_id;
	}


	public Long getRrn() {
		return rrn;
	}


	public void setRrn(Long rrn) {
		this.rrn = rrn;
	}


	public TransactionCode getTransactionCode() {
		return transactionCode;
	}


	public void setTransactionCode(TransactionCode transactionCode) {
		this.transactionCode = transactionCode;
	}


	public Side getSide() {
		return side;
	}


	public void setSide(Side side) {
		this.side = side;
	}


	public BigDecimal getAmount() {
		return Amount;
	}


	public void setAmount(BigDecimal amount) {
		Amount = amount;
	}


	public ResponseCode getResponseCode() {
		return responseCode;
	}


	public void setResponseCode(ResponseCode responseCode) {
		this.responseCode = responseCode;
	}


	public LocalDateTime getTransactionDate() {
		return transactionDate;
	}


	public void setTransactionDate(LocalDateTime transactionDate) {
		this.transactionDate = transactionDate;
	}




	@Override
	public String toString() {
		return "History [history_id=" + history_id + ", rrn=" + rrn + ", transactionCode=" + transactionCode + ", side="
				+ side + ", Amount=" + Amount + ", responseCode=" + responseCode + ", transactionDate="
				+ transactionDate + "]";
	}


	
}

