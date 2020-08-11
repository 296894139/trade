package jinjiang.entity.admin;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "deduct")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Deduct {
	@Id
	@GeneratedValue(generator = "jpa-uuid")
	private String id;//编号

	@Column(name = "personal")
	private double personal;//股东个人帐户比例

	@Column(name = "takeBalance")
	private double takeBalance; //股东提成账户比例

	@Column(name = "staffRatio")
	private double staffRatio; //店员提成比例

	@Column(name = "other")
	private double other; //会员至其他门店消费，店员让出比例

	@Column(name = "shopId")
	private String shopId; //所属酒庄

	public Deduct() {
	}

	public Deduct(double personal, double takeBalance, double staffRatio, double other, String shopId) {
		this.personal = personal;
		this.takeBalance = takeBalance;
		this.staffRatio = staffRatio;
		this.other = other;
		this.shopId = shopId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getPersonal() {
		return personal;
	}

	public void setPersonal(double personal) {
		this.personal = personal;
	}

	public double getTakeBalance() {
		return takeBalance;
	}

	public void setTakeBalance(double takeBalance) {
		this.takeBalance = takeBalance;
	}

	public double getStaffRatio() {
		return staffRatio;
	}

	public void setStaffRatio(double staffRatio) {
		this.staffRatio = staffRatio;
	}

	public double getOther() {
		return other;
	}

	public void setOther(double other) {
		this.other = other;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
}
