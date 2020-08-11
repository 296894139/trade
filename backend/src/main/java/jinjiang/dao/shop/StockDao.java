package jinjiang.dao.shop;

import jinjiang.entity.shop.Shop;
import jinjiang.entity.shop.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockDao extends JpaRepository<Stock,String> {
    List<Stock> findByStatusAndShopId(String status,String shopId);
    Page<Stock> findByShopId(String shopId, Pageable pageable);
}
