package mychef.mychef.backend;

import android.os.Parcel;
import android.os.Parcelable;

public class Chef implements Parcelable {
    private int icon;
    private String firstName;
    private String lastName;
    private String aboutChef;
    private double latitude;
    private double longitude;

    public Chef(int icon, String firstName, String lastName, String aboutChef, double latitude, double longitude) {
        this.icon = icon;
        this.firstName = firstName;
        this.lastName = lastName;
        this.aboutChef = aboutChef;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    private Chef(Parcel in) {
        this.icon = in.readInt();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.aboutChef = in.readString();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
    }

    public int getIcon() {
        return icon;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAboutChef() {
        return aboutChef;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(icon);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(aboutChef);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }

    public static final Parcelable.Creator<Chef> CREATOR
            = new Parcelable.Creator<Chef>() {

        @Override
        public Chef createFromParcel(Parcel in) {
            return new Chef(in);
        }

        @Override
        public Chef[] newArray(int size) {
            return new Chef[size];
        }
    };
}
