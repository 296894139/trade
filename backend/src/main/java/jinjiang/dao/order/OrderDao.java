package jinjiang.dao.order;

import jinjiang.entity.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDao extends JpaRepository<Order,String> {
    Page<Order> findByStatusOrderByBuyTimeDesc(String status, Pageable pageable);
    List<Order> findByStatusOrderByBuyTimeAsc(String status);
    List<Order> findByUserIdOrderByBuyTimeAsc(String userId);
    List<Order> findByUserIdAndStatusOrderByBuyTimeAsc(String userId,String status);
}
