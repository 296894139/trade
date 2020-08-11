package jinjiang.entity.shop;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "shop")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Shop {//分类
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;//编号

    @Column(name = "name")
    private String name;//分类名称

    @Column(name = "mobilePhone")
    private String mobilePhone;//酒庄电话

    @Column(name = "workTime")
    private String workTime;//酒庄营业时间

    @Column(name = "province")
    private String province; //省

    @Column(name = "city")
    private String city; //市

    @Column(name = "district")
    private String district; //区

    @Column(name = "detail")
    private String detail; //详细地址

    @Column(name = "balance")
    private double balance; //酒庄余额

    @Column(name = "faceUrl")
    private String faceUrl; //酒庄头像

    @Column(name = "showUrl")
    private String showUrl; //酒庄展示图

    public Shop(String name, String mobilePhone, String workTime, String province, String city, String district, String detail, double balance, String faceUrl, String showUrl) {
        this.name = name;
        this.mobilePhone = mobilePhone;
        this.workTime = workTime;
        this.province = province;
        this.city = city;
        this.district = district;
        this.detail = detail;
        this.balance = balance;
        this.faceUrl = faceUrl;
        this.showUrl = showUrl;
    }

    public Shop() {
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

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getWorkTime() {
        return workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getFaceUrl() {
        return faceUrl;
    }

    public void setFaceUrl(String faceUrl) {
        this.faceUrl = faceUrl;
    }

    public String getShowUrl() {
        return showUrl;
    }

    public void setShowUrl(String showUrl) {
        this.showUrl = showUrl;
    }
}
