package jinjiang.bl.account;

import jinjiang.blservice.account.CouponService;
import jinjiang.dao.account.CouponDao;
import jinjiang.dao.account.DiscountDao;
import jinjiang.entity.account.Coupon;
import jinjiang.entity.account.Discount;
import jinjiang.exception.NotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
public class CouponBlServiceImpl implements CouponService {
    private final CouponDao couponDao;
    private final DiscountDao discountDao;
    @Autowired
    public CouponBlServiceImpl(CouponDao couponDao, DiscountDao discountDao){


        this.couponDao = couponDao;
        this.discountDao = discountDao;
    }

    @Override
    public void addCoupon(Coupon coupon) {
        Optional<Discount> optionalDiscount=discountDao.findById(coupon.getDiscount());
        coupon.setStatus("未使用");
        if(optionalDiscount.isPresent()){
            Discount diacount=optionalDiscount.get();
            if(diacount.getTimeType()==0){
                int days=diacount.getDays();
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Calendar c = Calendar.getInstance();
                System.out.println("当前日期:"+sf.format(c.getTime()));
                coupon.setStartTime(sf.format(c.getTime()));
                c.add(Calendar.DAY_OF_MONTH, days);
                System.out.println("增加一天后日期:"+sf.format(c.getTime()));
                coupon.setEndTime(sf.format(c.getTime()));
            }
            else{
                coupon.setStartTime(diacount.getStartTime());
                coupon.setEndTime(diacount.getEndTime());
            }
        }
        couponDao.save(coupon);
    }

    @Override
    public void deleteCoupon(String id) throws NotExistException {
       Optional<Coupon> optionalCoupon=couponDao.findById(id);
       if (optionalCoupon.isPresent()){
           couponDao.deleteById(id);
       }else {
           throw new NotExistException("address ID", id);
       }
    }

    @Override
    public void updateCoupon(Coupon coupon) throws NotExistException {
        Optional<Coupon> optionalCoupon=couponDao.findById(coupon.getId());
        if (optionalCoupon.isPresent()){
           Coupon newCoupon=optionalCoupon.get();
           newCoupon.setDiscount(coupon.getDiscount());
           newCoupon.setEndTime(coupon.getEndTime());
           newCoupon.setStartTime(coupon.getStartTime());
           newCoupon.setStatus(coupon.getStatus());
           newCoupon.setUser(coupon.getUser());
           couponDao.save(newCoupon);
        }else {
            throw new NotExistException("address ID", coupon.getId());
        }
    }

    @Override
    public Coupon findById(String id) throws NotExistException {
        Optional<Coupon> optionalCoupon=couponDao.findById(id);
        if (optionalCoupon.isPresent()){
            return optionalCoupon.get();
        }else {
            throw new NotExistException("address ID", id);
        }
    }

    @Override
    public Page<Coupon> findAll(Pageable pageable) {
        return couponDao.findAll(pageable);
    }


    @Override
    public List<Coupon> findByUser(String user) {
        return couponDao.findByUser(user);
    }


    @Override
    public Page<Coupon> find(String query, Pageable pageable) {
        List<Coupon> coupons=couponDao.findAll();
        List<Coupon> list=new ArrayList<>();
        for(Coupon coupon:coupons){
            if(coupon.getDiscount().indexOf(query)!=(-1)||coupon.getEndTime().indexOf(query)!=(-1)||coupon.getId().indexOf(query)!=(-1)||coupon.getStatus().indexOf(query)!=(-1)||coupon.getUser().indexOf(query)!=(-1)||coupon.getStartTime().indexOf(query)!=(-1)){
                list.add(coupon);
            }
        }
        return listConvertToPage(list,pageable);
    }

    public <T> Page<T> listConvertToPage(List<T> list, Pageable pageable) {
        int start = (int)pageable.getOffset();
        int end = (start + pageable.getPageSize()) > list.size() ? list.size() : ( start + pageable.getPageSize());
        return new PageImpl<T>(list.subList(start, end), pageable, list.size());
    }

}
