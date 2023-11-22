package com.example.bank.Model;



import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.example.bank.Model.Enumeration.CustomerProfile;


@Entity
public class MobileBankingUser {
	
	public MobileBankingUser() {
	}
	
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int MobileBankingUser_id;
    
    @Enumerated(EnumType.STRING)
    private CustomerProfile customerProfile;
    
    private Long cif;
    private String userName;
    private String pin;
    private String password;
    private String phoneNumber;
    private int version;
    private String language;
    
    
	public int getMobileBankingUser_id() {
		return MobileBankingUser_id;
	}
	public void setMobileBankingUser_id(int mobileBankingUser_id) {
		MobileBankingUser_id = mobileBankingUser_id;
	}
	public CustomerProfile getCustomerProfile() {
		return customerProfile;
	}
	public void setCustomerProfile(CustomerProfile customerProfile) {
		this.customerProfile = customerProfile;
	}
	public Long getCif() {
		return cif;
	}
	public void setCif(Long cif) {
		this.cif = cif;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	@Override
	public String toString() {
		return "MobileBankingUser [MobileBankingUser_id=" + MobileBankingUser_id + ", customerProfile="
				+ customerProfile + ", cif=" + cif + ", userName=" + userName + ", pin=" + pin + ", password="
				+ password + ", phoneNumber=" + phoneNumber + ", version=" + version + ", language=" + language + "]";
	}

    

	

    
}

