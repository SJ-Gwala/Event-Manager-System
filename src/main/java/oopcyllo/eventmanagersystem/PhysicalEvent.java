
package oopcyllo.eventmanagersystem;

 import oopcyllo.sjrmtgnr.Event;

public class PhysicalEvent extends Event {
    private String location;

    public PhysicalEvent(String title, String date, String category, String location) {
        super(title, date, category);
        this.location = location;
    }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    @Override
    public String toFileString() {
        return super.toFileString() + ",Physical," + location;
    }

    @Override
    public String toString() {
        return super.toString() + "\nType: Physical\nLocation: " + location;
    }
}



    

