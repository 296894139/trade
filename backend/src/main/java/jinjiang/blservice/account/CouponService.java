package jinjiang.blservice.account;

import jinjiang.entity.account.Coupon;
import jinjiang.entity.recommend.Recommend;
import jinjiang.exception.NotExistException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CouponService {

    void addCoupon(Coupon coupon);

    void deleteCoupon(String id) throws NotExistException;

    void updateCoupon(Coupon coupon) throws NotExistException;

    Coupon findById(String id) throws NotExistException;

    Page<Coupon> findAll(Pageable pageable);

    List<Coupon> findByUser(String user);

    Page<Coupon> find(String query, Pageable pageable);
}
