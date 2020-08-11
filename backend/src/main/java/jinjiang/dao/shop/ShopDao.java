package jinjiang.dao.shop;

import jinjiang.entity.shop.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopDao extends JpaRepository<Shop,String> {

}
