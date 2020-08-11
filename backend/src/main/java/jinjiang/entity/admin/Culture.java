package jinjiang.entity.admin;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "culture")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Culture {
	@Id
	@GeneratedValue(generator = "jpa-uuid")
	private String id;//编号

	@Column(name = "title")
	private String title;//标题

	@Column(name = "brief")
	private String brief; //摘要

	@Column(name = "time")
	private String time; //发布时间

	@Column(name = "type")
	private String type; //类型：酒文化、企业新闻、实时动态

	@Column(name = "detail",length = 20000)
	private String detail; //详情

	@Column(name = "image")
	private String image; //封面图

	public Culture() {
	}

	public Culture(String title, String brief, String time, String type, String detail, String image) {
		this.title = title;
		this.brief = brief;
		this.time = time;
		this.type = type;
		this.detail = detail;
		this.image = image;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
}
