package jinjiang.entity.account;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "balance")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Balance {//人员交易
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;//id

    @Column(name = "userId")
    private String userId; //用户id

    @Column(name = "username")
    private String username; //用户名

    @Column(name = "type")
    private String type; //收入、支出、赊账、充值、积分

    @Column(name = "price")
    private double price; //价格

    @Column(name = "detail")
    private String detail; //详细内容

    @Column(name = "time")
    private String time; //时间

    @Column
    @ElementCollection(targetClass = String.class)
    private List<String> goodsList; //所购买商品名称列表

    public Balance() {
    }

    public Balance(String userId, String username, String type, double price, String detail, String time, List<String> goodsList) {
        this.userId = userId;
        this.username = username;
        this.type = type;
        this.price = price;
        this.detail = detail;
        this.time = time;
        this.goodsList = goodsList;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<String> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<String> goodsList) {
        this.goodsList = goodsList;
    }
}
