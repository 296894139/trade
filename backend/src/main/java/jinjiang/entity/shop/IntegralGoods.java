package jinjiang.entity.shop;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "integralGoods")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class IntegralGoods {//积分商品
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;//编号

    @Column(name = "name")
    private String name; //积分商品名称

    @Column(name = "brief")
    private String brief; //商品简介

    @Column(name = "integral")
    private int integral; //所需积分

    @Column(name = "standard")
    private String standard; //商品规格

    @Column(name = "number")
    private int number;//剩余数量

    @Column(name = "imageUrl")
    private String imageUrl; //商品封面图

    @Column
    @ElementCollection(targetClass = String.class)
    private List<String> swiperImgs; //商品轮播图

    @Column(length = 20000)
    private String detail; //商品详情

    public IntegralGoods() {
    }

    public IntegralGoods(String name, String brief, int integral, String standard, int number, String imageUrl, List<String> swiperImgs, String detail) {
        this.name = name;
        this.brief = brief;
        this.integral = integral;
        this.standard = standard;
        this.number = number;
        this.imageUrl = imageUrl;
        this.swiperImgs = swiperImgs;
        this.detail = detail;
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

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
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
}