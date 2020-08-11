package jinjiang.response;

public class MemberResponse {
    private String faceUrl;//编号

    private String username; //商品名称

    private String mobilePhone; //商品封面图

    private String level;//规格

    private int recommend;//规格

    private String regtime;//规格
    public MemberResponse() {
    }

    public MemberResponse(String faceUrl, String username, String mobilePhone, String level, int recommend, String regtime) {
        this.faceUrl = faceUrl;
        this.username = username;
        this.mobilePhone = mobilePhone;
        this.level = level;
        this.recommend = recommend;
        this.regtime = regtime;
    }

    public String getFaceUrl() {
        return faceUrl;
    }

    public void setFaceUrl(String faceUrl) {
        this.faceUrl = faceUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getRecommend() {
        return recommend;
    }

    public void setRecommend(int recommend) {
        this.recommend = recommend;
    }

    public String getRegtime() {
        return regtime;
    }

    public void setRegtime(String regtime) {
        this.regtime = regtime;
    }
}
