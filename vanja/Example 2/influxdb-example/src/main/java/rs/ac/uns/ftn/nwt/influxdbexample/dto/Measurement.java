package rs.ac.uns.ftn.nwt.influxdbexample.dto;

import java.util.Date;

public class Measurement {
    private String name;
    private double value;
    private Date timestamp;

    public Measurement() {
    }

    public Measurement(String name, double value, Date timestamp) {
        this.name = name;
        this.value = value;
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
