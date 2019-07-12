package test_tracker;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Row {
    private final SimpleStringProperty description;
    private final SimpleStringProperty ip;
    private final SimpleStringProperty esxIp;
//    private final SimpleIntegerProperty testID;

    public Row(String description, String ip, String esxIp) {
        this.description = new SimpleStringProperty(description);
        this.ip = new SimpleStringProperty(ip);
        this.esxIp = new SimpleStringProperty(esxIp);
//        this.testID = new SimpleIntegerProperty(testID);
    }

    public String getDescription() {
        return this.description.get();
    }

    public void setDescription(String desc) {
        this.description.set(desc);
    }

    public String getIp() {
        return this.ip.get();
    }

    public void setIp(String ip) {
        this.description.set(ip);
    }

    public String getEsxIp() {
        return this.esxIp.get();
    }

    public void setEsxIp(String esxIp) {
        this.description.set(esxIp);
    }

//    public int getTestID() {
//        return testID.get();
//    }

    public String toString() {
        return this.description + ", " + this.ip + ", " + this.esxIp;
    }
}
