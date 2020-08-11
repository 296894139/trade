package jinjiang.blservice.order;

import jinjiang.entity.order.Order;
import jinjiang.exception.NotExistException;
import jinjiang.response.OrderResponse;
import jinjiang.response.WxBuyCreditResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface OrderBlService {

    Order addOrder(Order order);

    void deleteOrder(String id) throws NotExistException;

    void updateOrder(Order order) throws NotExistException;

    Order findById(String id) throws NotExistException;

    Page<Order> findByStatus(String status, Pageable pageable);

    void cancel(String id) throws NotExistException;

    void send(String id) throws NotExistException;

    void take(String id) throws NotExistException;

    void integralSend(String id) throws NotExistException;

    void integralTake(String id) throws NotExistException;

    void pass(String id) throws NotExistException;

    void refund(String id) throws NotExistException;

    void pay(Order o) throws NotExistException;

    void back(String id) throws NotExistException;

    Page<Order> findAll(Pageable pageable);

    List<OrderResponse> findAllWX(String userId);

    List<OrderResponse> findAll();

    List<OrderResponse> findByStatusWX(String userId, String status);

    List<OrderResponse> findByShopId(String shopId);

    List<OrderResponse> findByStatus(String status);

    List<OrderResponse> findByStatusAndShopId(String status, String shopId);

    OrderResponse findByIdWX(String orderId);

    WxBuyCreditResponse paywx(Order order);

    String getWxPayResult(HttpServletRequest request);

    WxBuyCreditResponse recharge(String id, double price);

    String getWxPayResult2(HttpServletRequest request);
}
