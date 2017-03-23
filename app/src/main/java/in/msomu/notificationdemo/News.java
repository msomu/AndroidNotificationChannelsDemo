package in.msomu.notificationdemo;

/**
 * Created by msomu on 23/03/17.
 */

public class News {

    private String category;
    private String title;
    private String categoryId;

    News(String title, String category) {
        this.category = category;
        this.title = title;
        this.categoryId = category.toLowerCase();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
