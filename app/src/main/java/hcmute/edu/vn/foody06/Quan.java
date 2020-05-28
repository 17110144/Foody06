package hcmute.edu.vn.foody06;


public class Quan {

    private String Title;
    private String Category;
    private String Desription;
    private int Thumbnail;

    public Quan() {
    }

    public Quan(String title, String category, String desription, int thumbnail) {
        Title = title;
        Category = category;
        Desription = desription;
        Thumbnail = thumbnail;
    }

    public String getTitle() {
        return Title;
    }

    public String getCategory() {
        return Category;
    }

    public String getDesription() {
        return Desription;
    }

    public int getThumbnail() {
        return Thumbnail;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public void setDesription(String desription) {
        Desription = desription;
    }

    public void setThumbnail(int thumbnail) {
        Thumbnail = thumbnail;
    }
}

