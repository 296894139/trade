package jinjiang.entity.shop;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "shopBalance")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class ShopBalance {//地址
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;//id

    @Column(name = "shopId")
    private String shopId; //酒庄id

    @Column(name = "name")
    private String name; //酒庄名称

    @Column(name = "type")
    private String type; //收入、报销、支出

    @Column(name = "expenseType")
    private String expenseType; //购买应用物资、房租、水电、员工工资、其它

    @Column(name = "price")
    private double price; //价格

    @Column(name = "detail")
    private String detail; //详细内容

    @Column(name = "time")
    private String time; //时间

    @Column
    @ElementCollection(targetClass = String.class)
    private List<String> goodsList; //所购买商品名称列表

    public ShopBalance() {
    }

    public ShopBalance(String shopId, String name, String type, String expenseType, double price, String detail, String time, List<String> goodsList) {
        this.shopId = shopId;
        this.name = name;
        this.type = type;
        this.expenseType = expenseType;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(String expenseType) {
        this.expenseType = expenseType;
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
