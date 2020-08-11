package jinjiang.blservice.order;


import jinjiang.entity.order.Cart;
import jinjiang.exception.NotExistException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CartBlService {

    void addCart(Cart cart);

    void deleteCart(String id) throws NotExistException;

    void updateCart(Cart cart) throws NotExistException;

    Cart findById(String id) throws NotExistException;

    Page<Cart> findAll(Pageable pageable);

    List<Cart> findByOpenid(String openid) throws NotExistException;
}
