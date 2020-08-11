package jinjiang.dao.shop;

import jinjiang.entity.shop.OffLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OffLineDao extends JpaRepository<OffLine,String> {
    List<OffLine> findByShopId(String shopId);
}
