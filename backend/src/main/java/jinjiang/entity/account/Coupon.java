package jinjiang.entity.account;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "coupon")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Coupon {
	@Id
	@GeneratedValue(generator = "jpa-uuid")
	private String id;//编号

	@Column(name = "user")
	private String user;//用户id

	@Column(name = "discount")
	private String discount; //优惠券id

	@Column(name = "status")
	private String status; //状态：未使用、已使用、已过期

	@Column(name = "startTime")
	private String startTime; //开始时间

	@Column(name = "endTime")
	private String endTime; //结束时间

	public Coupon() {
	}

	public Coupon(String user, String discount, String status, String startTime, String endTime) {
		this.user = user;
		this.discount = discount;
		this.status = status;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
}
