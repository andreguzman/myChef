package mychef.mychef.backend;

public class PictureText {
    private int mIcon;
    private String mText;

    public PictureText(int icon, String text) {
        this.mIcon = icon;
        this.mText = text;
    }

    public int getIcon() {
        return mIcon;
    }

    public String getText() {
        return mText;
    }
}
