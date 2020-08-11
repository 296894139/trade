package jinjiang.entity.account;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "level")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Level {//等级
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;//等级号

    @Column(name="name")
    private String name;//等级名称

    @Column(name="discount")
    private double discount;//所需消费金额

    @Column(name="url")
    private String url;//等级图标

    @Column(name="discountId")
    private String discountId;//该等级会员所能获得的优惠卷对应的id

    @Column(name="shopId")
    private String shopId;//酒庄id

    public Level() {
    }

    public Level(String name, double discount, String url, String discountId, String shopId) {
        this.name = name;
        this.discount = discount;
        this.url = url;
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

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
