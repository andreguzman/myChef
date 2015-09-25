package mychef.mychef.backend;

public class User {

    private int icon;
    private String firstName;
    private String lastName;

    private double latitude;
    private double longitude;

    private String creditCardUsername;
    private String creditCardPassword;

    public User(int icon, String firstName, String lastName, double latitude, double longitude) {
        this.icon = icon;
        this.firstName = firstName;
        this.lastName = lastName;
        this.latitude = latitude;
        this.longitude = longitude;

        creditCardUsername = null;
        creditCardPassword = null;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public double getLatitude() { return latitude; }

    public double getLongitude() { return longitude; }

    public void setLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setCreditCardCredentials(String username, String password) {
        creditCardUsername = username;
        creditCardPassword = password;
    }

    public String getCreditCardUsername() {
        return creditCardUsername;
    }

    public String getCreditCardPassword() {
        return creditCardPassword;
    }

    public int getIcon() { return icon; }
}
