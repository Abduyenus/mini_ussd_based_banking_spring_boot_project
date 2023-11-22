package com.example.bank.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
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
public class Transaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int transaction_id;
	private Long rrn;
	
	@Enumerated(EnumType.STRING)
	private TransactionCode transactionCode;
	
	private int accountNumber;
	
	@Enumerated(EnumType.STRING)
	private Side side;
	
	private BigDecimal Amount;
	
	@Enumerated(EnumType.STRING)
	private ResponseCode responseCode;
	
	private LocalDateTime transactionDate;
	@Column(name = "OTP", nullable = true, length = 6)
	private String otp;

	public Transaction() {
		super();
	}

	public int getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(int transaction_id) {
		this.transaction_id = transaction_id;
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

	public int getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
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

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	@Override
	public String toString() {
		return "Transaction [transaction_id=" + transaction_id + ", rrn=" + rrn + ", transactionCode=" + transactionCode
				+ ", accountNumber=" + accountNumber + ", side=" + side + ", Amount=" + Amount + ", responseCode="
				+ responseCode + ", transactionDate=" + transactionDate + ", otp=" + otp + "]";
	}

}
