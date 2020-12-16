package ir.allahverdi.digiapplication.entity;

public class Product {

    private int id;
    private String name;
    private int price;
    private String imgId;
    private String model;
    private float score;

    public Product(int id, String name, int price, String imgId, String model, float score) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imgId = imgId;
        this.model = model;
        this.score = score;
    }

    public Product(String name, int price, String imgId, String model, float score) {
        this.name = name;
        this.price = price;
        this.imgId = imgId;
        this.model = model;
        this.score = score;
    }

    public Product(int id, float score) {
        this.id = id;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "\nProduct{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", imgId='" + imgId + '\'' +
                ", model='" + model + '\'' +
                ", score=" + score +
                '}';
    }
}
