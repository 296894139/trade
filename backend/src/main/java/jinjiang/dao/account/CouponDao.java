package jinjiang.dao.account;

import jinjiang.entity.account.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponDao extends JpaRepository<Coupon,String> {
    List<Coupon> findByUser(String user);
}
