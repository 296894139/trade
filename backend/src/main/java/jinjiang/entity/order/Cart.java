package jinjiang.entity.order;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "cart")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Cart {//购物车
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;//编号

    @Column(name = "goodsId")
    private String goodsId; //商品id

    @Column(name = "picUrl")
    private String picUrl; //商品图片

    @Column(name = "goodsName")
    private String goodsName; //商品名称

    @Column(name = "number")
    private int number; //数量

    @Column(name = "price")
    private double price; //价格

    @Column(name = "userOpenid")
    private String userOpenid; //用户openid

    @Column(name = "checked")
    private boolean checked; //是否选中

    public Cart() {
    }

    public Cart(String goodsId, String picUrl, String goodsName, int number, double price, String userOpenid, boolean checked) {
        this.goodsId = goodsId;
        this.picUrl = picUrl;
        this.goodsName = goodsName;
        this.number = number;
        this.price = price;
        this.userOpenid = userOpenid;
        this.checked = checked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
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

    public String getUserOpenid() {
        return userOpenid;
    }

    public void setUserOpenid(String userOpenid) {
        this.userOpenid = userOpenid;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}