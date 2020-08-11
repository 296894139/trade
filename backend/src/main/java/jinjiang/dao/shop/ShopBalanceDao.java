package jinjiang.dao.shop;

import jinjiang.entity.shop.ShopBalance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShopBalanceDao extends JpaRepository<ShopBalance, String> {
    List<ShopBalance> findByType(String type);
    List<ShopBalance> findByTypeAndShopId(String type,String shopId);
}
