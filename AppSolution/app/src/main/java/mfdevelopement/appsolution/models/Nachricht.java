package mfdevelopement.appsolution.models;

public class Nachricht {

    private String title;
    private String mainMessage;
    private String date;

    public Nachricht() {
        this.title = null;
        this.mainMessage = null;
        this.date = null;
    }

    public Nachricht(String title, String mainMessage, String date) {
        this.title = title;
        this.mainMessage = mainMessage;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMainMessage() {
        return mainMessage;
    }

    public void setMainMessage(String mainMessage) {
        this.mainMessage = mainMessage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}