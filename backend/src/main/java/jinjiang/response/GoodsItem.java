package jinjiang.response;

public class GoodsItem {
    private String id;//编号

    private String name; //商品名称

    private String imageUrl; //商品封面图

    private String standard;//规格

    private double price;//规格

    public GoodsItem() {
    }

    public GoodsItem(String id, String name, String imageUrl, String standard, double price) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.standard = standard;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
