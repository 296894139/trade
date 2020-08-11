package jinjiang.dao.order;

import jinjiang.entity.order.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartDao extends JpaRepository<Cart,String> {
    List<Cart> findByUserOpenid(String UserOpenid);
}
