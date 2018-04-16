package at.tugraz.recipro.data;

public class Recipe {

    private String title;
    private int time;
    private double rating;

    public Recipe(String title, int time, double rating) {
        this.title = title;
        this.time = time;
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

}
