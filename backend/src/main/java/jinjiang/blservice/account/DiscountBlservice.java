package jinjiang.blservice.account;

import jinjiang.entity.account.Discount;
import jinjiang.exception.NotExistException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface DiscountBlservice {
    void addDiscount(Discount diacount);

    void deleteDiscount(String id) throws NotExistException;

    void updateDiscount(Discount address) throws NotExistException;

    Discount findById(String id) throws NotExistException;

    Page<Discount> findAll(Pageable pageable);

    Page<Discount> findByShopId(String shopId, Pageable pageable);
}
