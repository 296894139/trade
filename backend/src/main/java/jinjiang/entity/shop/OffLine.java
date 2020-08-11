package jinjiang.entity.shop;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "offLine")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class OffLine {//线下
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;//id

    @Column(name = "staffId")
    private String staffId; //员工id

    @Column(name = "name")
    private String name; //员工名称

    @Column(name = "userId")
    private String userId; //

    @Column(name = "username")
    private String username; //

    @Column(name = "shopId")
    private String shopId; //

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
    private String status; //待审批、已审批

    @Column(name = "time")
    private String time; //时间


    public OffLine() {
    }

    public OffLine(String staffId, String name, String userId, String username, String shopId, String goodsId, String goodsName, String imageUrl, int number, double price, String status, String time) {
        this.staffId = staffId;
        this.name = name;
        this.userId = userId;
        this.username = username;
        this.shopId = shopId;
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

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
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
