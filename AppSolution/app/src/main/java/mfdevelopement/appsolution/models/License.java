package mfdevelopement.appsolution.models;

public class License {

    private String licenseName;
    private String licenseLink;

    public License() {}

    public License(String licenseName, String licenseLink) {
        this.licenseName = licenseName;
        this.licenseLink = licenseLink;
    }

    public String getLicenseName() {
        return licenseName;
    }

    public void setLicenseName(String licenseName) {
        this.licenseName = licenseName;
    }

    public String getLicenseLink() {
        return licenseLink;
    }

    public void setLicenseLink(String licenseLink) {
        this.licenseLink = licenseLink;
    }
}
