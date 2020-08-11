package jinjiang.entity.shop;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "stock")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Stock {//进货
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;//id

    @Column(name = "shopId")
    private String shopId; //酒庄id

    @Column(name = "name")
    private String name; //酒庄名称

    @Column(name = "goodsId")
    private String goodsId; //

    @Column(name = "goodsName")
    private String goodsName;

    @Column(name = "imageUrl")
    private String imageUrl;

    @Column(name = "number")
    private int number; //数量

    @Column(name = "price")
    private double price; //价格

    @Column(name = "status")
    private String status; //待发货、待收货、待上架、已上架、退款中、已退款

    @Column(name = "time")
    private String time; //时间


    public Stock() {
    }

    public Stock(String shopId, String name, String goodsId, String goodsName, String imageUrl, int number, double price, String status, String time) {
        this.shopId = shopId;
        this.name = name;
        this.goodsId = goodsId;
        this.goodsName = goodsName;
        this.imageUrl = imageUrl;
        this.number = number;
        this.price = price;
        this.status = status;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
