package jinjiang.entity.admin;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "ad")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Ad {
	@Id
	@GeneratedValue(generator = "jpa-uuid")
	private String id;

	@Column(name = "image")
	private String image; //图片

	@Column(name = "link")
	private String link; //广告导向的链接

	@Column(name = "checked")
	private boolean checked; //是否被选中在首页展示

	@Column(name = "showPlace")
	private String showPlace; //展示的位置：首页1，首页2，首页3,首页4

	public Ad() {
	}

	public Ad(String image, String link, boolean checked, String showPlace) {
		this.image = image;
		this.link = link;
		this.checked = checked;
		this.showPlace = showPlace;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}


	public String getShowPlace() {
		return showPlace;
	}

	public void setShowPlace(String showPlace) {
		this.showPlace = showPlace;
	}
}
