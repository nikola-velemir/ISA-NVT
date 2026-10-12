package rs.ac.uns.ftn.nvt;

import java.util.Date;

public class Measurement {
    private String name;
    private float value;
    private Date timestamp;

    public Measurement() {
    }

    public Measurement(String name, float value) {
        this.name = name;
        this.value = value;
        this.timestamp = new Date();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Measurement{" +
                "name='" + name + '\'' +
                ", value=" + value +
                ", timestamp=" + timestamp +
                '}';
    }
}
