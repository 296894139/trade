package jinjiang.response;

import java.util.List;

public class OrderResponse {

    private String id;//编号

    private String userId; //对应人员id

    private String address; //送货地址

    private String mobilePone; //联系手机

    private String person; //收货人

    private String type; //配送方式: 送货上门、物流快递、上门取货

    private String remark; //备注

    private double freight; //运费

    private double price; //总价

    private double discountPrice; //优惠

    private List<GoodsItem> goodsList; //所购买商品id列表

    private String buyTime;//购买时间

    private String status; //状态，包含:待付款、待发货、待收货、已完成、已取消、积分待收货、积分待发货、积分已完成

    public OrderResponse() {
    }

    public OrderResponse(String id, String userId, String address, String mobilePone, String person, String type, String remark, double freight, double price, double discountPrice, List<GoodsItem> goodsList, String buyTime, String status) {
        this.id = id;
        this.userId = userId;
        this.address = address;
        this.mobilePone = mobilePone;
        this.person = person;
        this.type = type;
        this.remark = remark;
        this.freight = freight;
        this.price = price;
        this.discountPrice = discountPrice;
        this.goodsList = goodsList;
        this.buyTime = buyTime;
        this.status = status;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobilePone() {
        return mobilePone;
    }

    public void setMobilePone(String mobilePone) {
        this.mobilePone = mobilePone;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public double getFreight() {
        return freight;
    }

    public void setFreight(double freight) {
        this.freight = freight;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public List<GoodsItem> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<GoodsItem> goodsList) {
        this.goodsList = goodsList;
    }

    public String getBuyTime() {
        return buyTime;
    }

    public void setBuyTime(String buyTime) {
        this.buyTime = buyTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
