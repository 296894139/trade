package jinjiang.entity.account;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "discount")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Discount {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;//id

    @Column(name="name")
    private String name;//优惠券名称

    @Column(name="d")
    private String desc;//介绍

    @Column(name="tag")
    private String tag;//标签

    @Column(name="min")
    private double min;//最低消费

    @Column(name="discount")
    private double discount;//满减金额

    @Column(name="l")
    private int limit;//每人限领

    @Column(name="goodsType")
    private String goodsType;//商品使用范围

    @Column(name="type")
    private String type;//优惠券类型

    @Column(name="total")
    private int total;//优惠券数量

    @Column(name="timeType")
    private int timeType;//时间类型

    @Column(name="days")
    private int days;//天数

    @Column(name="startTime")
    private String startTime;//开始日期

    @Column(name="endTime")
    private String endTime;//结束日期

    @Column(name="shopId")
    private String shopId;//酒庄id

    public Discount() {
    }

    public Discount(String name, String desc, String tag, double min, double discount, int limit, String goodsType, String type, int total, int timeType, int days, String startTime, String endTime, String shopId) {
        this.name = name;
        this.desc = desc;
        this.tag = tag;
        this.min = min;
        this.discount = discount;
        this.limit = limit;
        this.goodsType = goodsType;
        this.type = type;
        this.total = total;
        this.timeType = timeType;
        this.days = days;
        this.startTime = startTime;
        this.endTime = endTime;
        this.shopId = shopId;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTimeType() {
        return timeType;
    }

    public void setTimeType(int timeType) {
        this.timeType = timeType;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }
}
