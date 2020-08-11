package jinjiang.entity.account;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "address")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Address {//地址
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;//id

    @Column(name = "province")
    private String province; //省

    @Column(name = "city")
    private String city; //市

    @Column(name = "district")
    private String district; //区

    @Column(name = "detail")
    private String detail; //详细地址

    @Column(name = "mobilePhone")
    private String mobilePhone; //联系电话

    @Column(name = "person")
    private String person; //收货人

    @Column(name = "userId")
    private String userId; //对应会员id

    @Column(name = "isDefault")
    private boolean isDefault; //默认

    public Address() {
    }

    public Address(String province, String city, String district, String detail, String mobilePhone, String person, String userId, boolean isDefault) {
        this.province = province;
        this.city = city;
        this.district = district;
        this.detail = detail;
        this.mobilePhone = mobilePhone;
        this.person = person;
        this.userId = userId;
        this.isDefault = isDefault;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }
}
