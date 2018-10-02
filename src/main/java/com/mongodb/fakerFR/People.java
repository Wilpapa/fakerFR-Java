package com.mongodb.fakerFR;
import java.util.List;

public class People {
    String name;
    String ssn;
    String job;
    List<Phone> phone;
    Address address;

    public People(String name, String ssn, String job, List<Phone> phone, Address address) {
        this.name = name;
        this.ssn = ssn;
        this.job = job;
        this.phone = phone;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public List<Phone> getPhone() {
        return phone;
    }

    public void setPhone(List<Phone> phone) {
        this.phone = phone;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}