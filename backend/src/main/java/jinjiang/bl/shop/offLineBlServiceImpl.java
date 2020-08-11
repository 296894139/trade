package jinjiang.bl.shop;

import jinjiang.blservice.shop.OffLineBlService;
import jinjiang.dao.account.BalanceDao;
import jinjiang.dao.account.UserDao;
import jinjiang.dao.admin.DeductDao;
import jinjiang.dao.shop.*;
import jinjiang.entity.account.Balance;
import jinjiang.entity.account.User;
import jinjiang.entity.admin.Deduct;
import jinjiang.entity.shop.*;
import jinjiang.exception.NotExistException;
import jinjiang.util.FormatDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class offLineBlServiceImpl implements OffLineBlService {

    private final UserDao userDao;
    private final OffLineDao offLineDao;
    private final BalanceDao balanceDao;
    private final ShopBalanceDao shopBalanceDao;
    private final DeductDao deductDao;
    private final GoodsDao goodsDao;
    private final Goods2Dao goods2Dao;
    @Autowired
    public offLineBlServiceImpl(UserDao userDao, OffLineDao offLineDao, BalanceDao balanceDao, ShopBalanceDao shopBalanceDao, DeductDao deductDao, GoodsDao goodsDao, Goods2Dao goods2Dao) {
        this.userDao = userDao;
        this.offLineDao = offLineDao;
        this.balanceDao = balanceDao;
        this.shopBalanceDao = shopBalanceDao;
        this.deductDao = deductDao;
        this.goodsDao = goodsDao;
        this.goods2Dao = goods2Dao;
    }


    @Override
    public OffLine addOffLine(OffLine offLine) {
        offLine.setTime(offLine.getTime());
        offLine.setStatus("已审批");
        String mobilePhone=offLine.getUserId();
        Optional<User> optionalUser=userDao.findByMobilePhone(mobilePhone);
        if(optionalUser.isPresent()){
            User user=optionalUser.get();
            offLine.setUserId(user.getId());
            offLine.setUsername(user.getUsername());
        }
        return offLineDao.save(offLine);
    }

    @Override
    public void deleteOffLine(String id) throws NotExistException {
        offLineDao.deleteById(id);
    }

    @Override
    public void updateOffLine(OffLine offLine) throws NotExistException {
        Optional<OffLine> optionalOffLine=offLineDao.findById(offLine.getId());
        if(optionalOffLine.isPresent()){
            OffLine newOffLine=optionalOffLine.get();
            newOffLine.setTime(offLine.getTime());
            newOffLine.setGoodsId(offLine.getGoodsId());
            newOffLine.setGoodsName(offLine.getGoodsName());
            newOffLine.setImageUrl(offLine.getImageUrl());
            newOffLine.setName(offLine.getName());
            newOffLine.setNumber(offLine.getNumber());
            newOffLine.setPrice(offLine.getPrice());
            newOffLine.setStaffId(offLine.getStaffId());
            newOffLine.setStatus(offLine.getStatus());
            newOffLine.setUserId(offLine.getUserId());
            newOffLine.setUsername(offLine.getUsername());
            offLineDao.save(newOffLine);
        }
    }

    @Override
    public OffLine findById(String id) throws NotExistException {
        Optional<OffLine> optionalOffLine=offLineDao.findById(id);
        if(optionalOffLine.isPresent()){
            return optionalOffLine.get();
        }
        return null;
    }

    @Override
    public void pass(String id) throws NotExistException {
        Optional<OffLine> optionalOffLine=offLineDao.findById(id);
        if(optionalOffLine.isPresent()){
            OffLine offLine=optionalOffLine.get();
            offLine.setStatus("已审批");
            offLineDao.save(offLine);
            Deduct deduct=new Deduct();
            Optional<Deduct> optionalDeduct=deductDao.findByShopId(offLine.getShopId());
            if(optionalDeduct.isPresent()){
                deduct=optionalDeduct.get();
            }
            double profit=0;
            double stock=0;
            List<String> goodsName=new ArrayList<>();
            Optional<Goods> optionalGoods=goodsDao.findById(offLine.getGoodsId());
            if(optionalGoods.isPresent()){
                Goods goods=optionalGoods.get();
                goods.setNumber(goods.getNumber()-1);
                goodsName.add(goods.getName());
                goodsDao.save(goods);
                stock+=goods.getStockPrice()*offLine.getNumber();
            }
            else{
                Optional<Goods2> optionalGoods2=goods2Dao.findById(offLine.getGoodsId());
                if(optionalGoods2.isPresent()){
                    Goods2 goods2=optionalGoods2.get();
                    goods2.setNumber(goods2.getNumber()-1);
                    goodsName.add(goods2.getName());
                    goods2Dao.save(goods2);
                    stock+=goods2.getStockPrice()*offLine.getNumber();
                }
            }
            profit=offLine.getPrice()-stock;
            Optional<User> optionalStaff=userDao.findById(offLine.getStaffId());
            if(optionalStaff.isPresent()){
                User staff=optionalStaff.get();
                staff.setBalance(staff.getBalance()+profit*deduct.getStaffRatio());
                userDao.save(staff);
                Balance balance=new Balance(staff.getId(),staff.getUsername(),"收入",profit*deduct.getStaffRatio(),"线下订单", FormatDateTime.toLongDateString(new Date()),goodsName);
                balanceDao.save(balance);
                ShopBalance shopBalance=new ShopBalance(offLine.getShopId(),offLine.getName(),"收入","",profit*(1-deduct.getStaffRatio())+stock,"会员"+offLine.getUsername()+"购买商品",FormatDateTime.toLongDateString(new Date()),goodsName);
                shopBalanceDao.save(shopBalance);
            }
        }
    }

    @Override
    public List<OffLine> findByShopId(String shopId) throws NotExistException {
        return offLineDao.findByShopId(shopId);
    }

    @Override
    public Page<OffLine> findAll(Pageable pageable) {
        return offLineDao.findAll(pageable);
    }
}
