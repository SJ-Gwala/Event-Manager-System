
package oopcyllo.sjrmtgnr;


public class Event {
    protected String title;
    protected String date;
    protected String category;

    public Event(String title, String date, String category) {
        this.title = title;
        this.date = date;
        this.category = category;
    }

    public String getTitle() { return title; }
    public String getDate() { return date; }
    public String getCategory() { return category; }

    public void setTitle(String title) { this.title = title; }
    public void setDate(String date) { this.date = date; }
    public void setCategory(String category) { this.category = category; }

    public String toFileString() {
        return title + "," + date + "," + category;
    }

    @Override
    public String toString() {
        return "Title: " + title + "\nDate: " + date + "\nCategory: " + category;
    }
}
