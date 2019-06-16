package mfdevelopement.appsolution.models;


public class Icon {

    private String authorReference;
    private Integer imageID;

    public Icon(String authorReference, Integer imageID) {
        this.authorReference = authorReference;
        this.imageID = imageID;
    }

    public String getAuthorReference() {
        return authorReference;
    }

    public void setAuthorReference(String authorReference) {
        this.authorReference = authorReference;
    }

    public Integer getImageID() {
        return imageID;
    }

    public void setImageID(Integer imageID) {
        this.imageID = imageID;
    }
}
