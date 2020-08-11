package jinjiang.entity.order;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "o")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Order {//订单
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;//编号

    @Column(name = "userId")
    private String userId; //对应人员id

    @Column(name = "address")
    private String address; //送货地址

    @Column(name = "mobilePone")
    private String mobilePone; //联系手机

    @Column(name = "person")
    private String person; //收货人

    @Column(name = "type")
    private String type; //配送方式: 送货上门、物流快递、上门取货

    @Column(name = "remark")
    private String remark; //备注

    @Column(name = "freight")
    private double freight; //运费

    @Column(name = "price")
    private double price; //总价

    @Column(name = "discountPrice")
    private double discountPrice; //优惠

    @Column
    @ElementCollection(targetClass = String.class)
    private List<String> goodsList; //所购买商品id列表

    @Column(name = "buyTime")
    private String buyTime;//购买时间

    @Column(name = "status")
    private String status; //状态，包含:待付款、待发货、待收货、待评价、已完成、已取消、退款中、已退款、积分待收货、积分待发货、积分待审核、积分已完成

    public Order() {
    }

    public Order(String userId, String address, String mobilePone, String person, String type, String remark, double freight, double price, double discountPrice, List<String> goodsList, String buyTime, String status) {
        this.userId = userId;
        this.address = address;
        this.mobilePone = mobilePone;
        this.person = person;
        this.type = type;
        this.remark = remark;
        this.freight = freight;
        this.price = price;
        this.discountPrice = discountPrice;
        this.goodsList = goodsList;
        this.buyTime = buyTime;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobilePone() {
        return mobilePone;
    }

    public void setMobilePone(String mobilePone) {
        this.mobilePone = mobilePone;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public double getFreight() {
        return freight;
    }

    public void setFreight(double freight) {
        this.freight = freight;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public List<String> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<String> goodsList) {
        this.goodsList = goodsList;
    }

    public String getBuyTime() {
        return buyTime;
    }

    public void setBuyTime(String buyTime) {
        this.buyTime = buyTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}