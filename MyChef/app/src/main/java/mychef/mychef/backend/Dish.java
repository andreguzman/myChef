package mychef.mychef.backend;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

public class Dish implements Parcelable{
    private int mIcon;
    private String mName;
    private String description;
    private double mPrice;
    private double mDistance;
    private double uberPrice;
    private Chef maker;
    private int priority;
    private String category;

    public Dish(int foodImage, String foodName, String description, double foodPrice, double distance, Chef maker, String category ) {
        this.mIcon = foodImage;
        this.mName = foodName;
        this.description = description;
        this.mPrice = foodPrice;
        this.mDistance = distance;
        this.maker = maker;
        this.category = category;
        priority = 0;
    }

    public Dish(int foodImage, String foodName, String description, double foodPrice, Chef maker, String category) {
        this.mIcon = foodImage;
        this.mName = foodName;
        this.description = description;
        this.mPrice = foodPrice;
        this.maker = maker;
        this.mDistance = -1;
        priority = 0;
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    private Dish(Parcel in) {
        mIcon = in.readInt();
        mName = in.readString();
        description = in.readString();
        mPrice = in.readDouble();
        mDistance = in.readDouble();
        uberPrice = in.readDouble();

        maker = in.readParcelable(Chef.class.getClassLoader());
        priority = in.readInt();
        category = in.readString();
    }

    public int getIcon() {
        return mIcon;
    }

    public String getName() {
        return mName;
    }

    public double getPrice() {
        return mPrice;
    }

    public double getDistance() {
        return mDistance;
    }

    public Chef getChef() { return maker; }

    public String getDescription() { return description; }

    public double getUberPrice() { return uberPrice; }

    public void setDistance(double distance) {
        this.mDistance = distance;
    }

    public void setUberPrice(double uberPrice) { this.uberPrice = uberPrice; }

    public int getPriority() { return priority; }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mIcon);
        dest.writeString(mName);
        dest.writeString(description);
        dest.writeDouble(mPrice);
        dest.writeDouble(mDistance);
        dest.writeDouble(uberPrice);
        dest.writeParcelable(maker, flags);
        dest.writeInt(priority);
        dest.writeString(category);
    }

    public static final Parcelable.Creator<Dish> CREATOR
            = new Parcelable.Creator<Dish>() {

        @Override
        public Dish createFromParcel(Parcel in) {
            return new Dish(in);
        }

        @Override
        public Dish[] newArray(int size) {
            return new Dish[size];
        }
    };
}
