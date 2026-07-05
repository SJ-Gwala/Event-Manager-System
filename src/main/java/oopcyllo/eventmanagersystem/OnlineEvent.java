
package oopcyllo.eventmanagersystem;
import oopcyllo.sjrmtgnr.Event;



// Online Event class
public class OnlineEvent extends Event {
    private String platform;

    public OnlineEvent(String title, String date, String category, String platform) {
        super(title, date, category);
        this.platform = platform;
    }

    public String getPlatform() { return platform; }
    public void setPlatform(String platform) { this.platform = platform; }

    @Override
    public String toFileString() {
        return super.toFileString() + ",Online," + platform;
    }

    @Override
    public String toString() {
        return super.toString() + "\nType: Online\nPlatform: " + platform;
    }
}