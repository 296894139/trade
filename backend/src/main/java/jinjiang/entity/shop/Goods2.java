package jinjiang.entity.shop;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "goods2")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Goods2 {//商品
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;//编号

    @Column(name = "name")
    private String name; //商品名称

    @Column(name = "brief")
    private String brief; //商品简介

    @Column(name = "price")
    private double price; //零售价

    @Column(name = "memberPrice")
    private double memberPrice; //会员价格

    @Column(name = "stockPrice")
    private double stockPrice; //进货价格

    @Column(name = "freight")
    private double freight; //运费

    @Column(name = "standard")
    private String standard; //商品规格

    @Column(name = "number")
    private int number;//剩余数量

    @Column(name = "sales")
    private int sales;//销售量

    @Column(name = "imageUrl")
    private String imageUrl; //商品封面图

    @Column
    @ElementCollection(targetClass = String.class)
    private List<String> swiperImgs; //商品轮播图图片

    @Column(name = "detail", length = 10000)
    private String detail; //商品详情

    @Column(name = "discountId")
    private String discountId; //可领取的优惠券id

    @Column(name = "shopId")
    private String shopId; //对应门店id

    public Goods2() {
    }

    public Goods2(String name, String brief, double price, double memberPrice, double stockPrice, double freight, String standard, int number, int sales, String imageUrl, List<String> swiperImgs, String detail, String discountId, String shopId) {
        this.name = name;
        this.brief = brief;
        this.price = price;
        this.memberPrice = memberPrice;
        this.stockPrice = stockPrice;
        this.freight = freight;
        this.standard = standard;
        this.number = number;
        this.sales = sales;
        this.imageUrl = imageUrl;
        this.swiperImgs = swiperImgs;
        this.detail = detail;
        this.discountId = discountId;
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

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getMemberPrice() {
        return memberPrice;
    }

    public void setMemberPrice(double memberPrice) {
        this.memberPrice = memberPrice;
    }

    public double getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(double stockPrice) {
        this.stockPrice = stockPrice;
    }

    public double getFreight() {
        return freight;
    }

    public void setFreight(double freight) {
        this.freight = freight;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<String> getSwiperImgs() {
        return swiperImgs;
    }

    public void setSwiperImgs(List<String> swiperImgs) {
        this.swiperImgs = swiperImgs;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDiscountId() {
        return discountId;
    }

    public void setDiscountId(String discountId) {
        this.discountId = discountId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }
}