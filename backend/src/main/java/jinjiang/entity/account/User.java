package jinjiang.entity.account;

import org.hibernate.annotations.GenericGenerator;
import jinjiang.entity.account.Level;

import javax.persistence.*;

@Entity
@Table(name = "user")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class User {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;//编号

    @Column(name = "username")
    private String username;//用户名

    @Column(name = "mobilePhone")
    private String mobilePhone; //手机号

    @Column(name = "openid")
    private String openid; //密码

    @Column(name = "name")
    private String name; //姓名

    @Column(name = "email")
    private String email;//用户邮箱

    @Column(name = "faceUrl")
    private String faceUrl; //头像地址

    @Column(name = "identity")
    private String identity; //用户身份，可分为 worker（专家）、boss(企业)


    @Column(name= "balance")
    private double balance;//余额

    @Column(name= "takeBalance")
    private double takeBalance;//评价

    @Column(name = "level")
    private String level;//类别

    @Column(name = "regtime")
    private String regtime; //注册时间



    @Column(name = "defaultAddress")
    private String defaultAddress; //默认地址的id

    @Column(name = "birthday")
    private String birthday;//用户生日

    @Column(name = "remark")
    private String remark;//备注






    @Column(name= "integral")
    private int integral;//评价



    @Column(name = "shopId")
    private String shopId; //所属酒庄的id

    @Column(name = "shareholderId")
    private String shareholderId; //股东

    @Column(name= "invest")
    private double invest;//投资

    public User() {
    }

    public User(String username, String openid, String name, String mobilePhone, String defaultAddress, String faceUrl, String identity, String birthday, String email, String remark, String level, double balance, double takeBalance, int integral, String regtime, String shopId, String shareholderId, double invest) {
        this.username = username;
        this.openid = openid;
        this.name = name;
        this.mobilePhone = mobilePhone;
        this.defaultAddress = defaultAddress;
        this.faceUrl = faceUrl;
        this.identity = identity;
        this.birthday = birthday;
        this.email = email;
        this.remark = remark;
        this.level = level;
        this.balance = balance;
        this.takeBalance = takeBalance;
        this.integral = integral;
        this.regtime = regtime;
        this.shopId = shopId;
        this.shareholderId = shareholderId;
        this.invest = invest;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
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

    public String getDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(String defaultAddress) {
        this.defaultAddress = defaultAddress;
    }

    public String getFaceUrl() {
        return faceUrl;
    }

    public void setFaceUrl(String faceUrl) {
        this.faceUrl = faceUrl;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getTakeBalance() {
        return takeBalance;
    }

    public void setTakeBalance(double takeBalance) {
        this.takeBalance = takeBalance;
    }

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public String getRegtime() {
        return regtime;
    }

    public void setRegtime(String regtime) {
        this.regtime = regtime;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShareholderId() {
        return shareholderId;
    }

    public void setShareholderId(String shareholderId) {
        this.shareholderId = shareholderId;
    }

    public double getInvest() {
        return invest;
    }

    public void setInvest(double invest) {
        this.invest = invest;
    }
}
