package jinjiang.bl.account;

import jinjiang.blservice.account.DiscountBlservice;
import jinjiang.dao.account.DiscountDao;
import jinjiang.entity.account.Discount;
import jinjiang.exception.NotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
public class DiscountBlServiceImpl implements DiscountBlservice {

    private final DiscountDao disDao;

    @Autowired
    public DiscountBlServiceImpl(DiscountDao Dao) {
        this.disDao = Dao;
    }


    @Override
    public void addDiscount(Discount diacount) {

        disDao.save(diacount);
    }

    @Override
    public void deleteDiscount(String id) throws NotExistException {
      Optional<Discount> dis= disDao.findById(id);
        if (dis.isPresent()) {
            disDao.deleteById(id);
        } else {
            throw new NotExistException("diacount ID", id);
        }
    }

    @Override
    public void updateDiscount(Discount address) throws NotExistException {
        Optional<Discount> dis= disDao.findById(address.getId());
        if (dis.isPresent()){
           Discount count=dis.get();
           count.setDays(address.getDays());
            count.setDesc(address.getDesc());
            count.setDiscount(address.getDiscount());
            count.setEndTime(address.getEndTime());
            count.setGoodsType(address.getGoodsType());
            count.setLimit(address.getLimit());
            count.setMin(address.getMin());
            count.setName(address.getName());
            count.setStartTime(address.getStartTime());
            count.setTag(address.getTag());
            count.setTimeType(address.getTimeType());
            count.setType(address.getType());
            count.setTotal(address.getTotal());
            count.setShopId(address.getShopId());
           disDao.save(count);
        }else {
            throw new NotExistException("diacount ID",address.getId());
        }
    }

    @Override
    public Discount findById(String id) throws NotExistException {
        Optional<Discount> optionaldis= disDao.findById(id);
        if(optionaldis.isPresent()) {
            return optionaldis.get();
        }else {
            throw new NotExistException("DisCount ID", id);
        }
    }

    @Override
    public Page<Discount> findAll(Pageable pageable) {
        return disDao.findAll(pageable);
    }


    @Override
    public Page<Discount> findByShopId(String shopId,Pageable pageable) {
        return disDao.findByShopId(shopId,pageable);
    }

}
