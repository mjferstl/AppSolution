package mfdevelopement.appsolution.models;


public class IconCredit {

    private Author author;
    private License license;
    private TitleCredits titleCredits;
    private int imageID;

    public IconCredit() {}

    public IconCredit(TitleCredits title, Author author, License license, Integer imageID) {
        this.titleCredits = title;
        this.author = author;
        this.license = license;
        this.imageID = imageID;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public License getLicense() {
        return license;
    }

    public void setLicense(License license) {
        this.license = license;
    }

    public TitleCredits getTitleCredits() {
        return titleCredits;
    }

    public void setTitleCredits(TitleCredits titleCredits) {
        this.titleCredits = titleCredits;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }
}
