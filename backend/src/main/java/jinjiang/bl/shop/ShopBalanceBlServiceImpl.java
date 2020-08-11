package jinjiang.bl.shop;

import jinjiang.blservice.shop.ShopBalanceBlService;
import jinjiang.dao.account.BalanceDao;
import jinjiang.dao.account.UserDao;
import jinjiang.dao.shop.ShopBalanceDao;
import jinjiang.dao.shop.ShopDao;
import jinjiang.entity.account.Balance;
import jinjiang.entity.account.User;
import jinjiang.entity.shop.Shop;
import jinjiang.entity.shop.ShopBalance;
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
public class ShopBalanceBlServiceImpl implements ShopBalanceBlService {

    private final ShopBalanceDao shopBalanceDao;
    private final UserDao  userDao;
    private final ShopDao shopDao;
    private final BalanceDao balanceDao;
    @Autowired
    public ShopBalanceBlServiceImpl(ShopBalanceDao shopBalanceDao, UserDao userDao, ShopDao shopDao, BalanceDao balanceDao) {

        this.shopBalanceDao = shopBalanceDao;
        this.userDao = userDao;
        this.shopDao = shopDao;
        this.balanceDao = balanceDao;
    }

    @Override
    public ShopBalance addShopBalance(ShopBalance shopBalance) {
        shopBalance.setTime(FormatDateTime.toLongDateString(new Date()));
        return shopBalanceDao.save(shopBalance);
    }

    @Override
    public void deleteShopBalance(String id) throws NotExistException {
        Optional<ShopBalance> optionalBalance = shopBalanceDao.findById(id);
        if (optionalBalance.isPresent()) {
            shopBalanceDao.deleteById(id);
        } else {
            throw new NotExistException("Address ID", id);
        }
    }

    @Override
    public void updateShopBalance(ShopBalance balance) throws NotExistException {
        Optional<ShopBalance> optionalBalance = shopBalanceDao.findById(balance.getId());
        if (optionalBalance.isPresent()){
            ShopBalance newBalance = optionalBalance.get();
            newBalance.setDetail(balance.getDetail());
            newBalance.setGoodsList(balance.getGoodsList());
            newBalance.setPrice(balance.getPrice());
            newBalance.setTime(balance.getTime());
            newBalance.setType(balance.getType());
            newBalance.setExpenseType(balance.getExpenseType());
            newBalance.setName(balance.getName());
            newBalance.setShopId(balance.getShopId());
            shopBalanceDao.save(newBalance);
        }else {
            throw new NotExistException("address ID", balance.getId());
        }

    }

    @Override
    public ShopBalance findById(String id) throws NotExistException {
        return shopBalanceDao.findById(id).get();
    }

    @Override
    public void pass(String id) throws NotExistException {
        Optional<ShopBalance> optionalShopBalance=shopBalanceDao.findById(id);
        if(optionalShopBalance.isPresent()){
            ShopBalance shopBalance=optionalShopBalance.get();
            List<String> goodsList=shopBalance.getGoodsList();
            if(goodsList.size()>1) {
                goodsList.set(2, "已审批");
            }
            shopBalance.setGoodsList(goodsList);
            shopBalanceDao.save(shopBalance);
            Optional<User> optionalUser=userDao.findById(goodsList.get(0));
            if(optionalUser.isPresent()){
                User user=optionalUser.get();
                user.setBalance(user.getBalance()+shopBalance.getPrice());
                userDao.save(user);
                Balance balance=new Balance(user.getId(),user.getUsername(),"收入",shopBalance.getPrice(),"报销:"+shopBalance.getDetail(),FormatDateTime.toLongDateString(new Date()),new ArrayList<>());
                balanceDao.save(balance);
            }
            Optional<Shop> optionalShop=shopDao.findById(shopBalance.getShopId());
            if(optionalShop.isPresent()){
                Shop shop=optionalShop.get();
                shop.setBalance(shop.getBalance()-shopBalance.getPrice());
                shopDao.save(shop);
            }

        }

    }

    @Override
    public List<ShopBalance> findByTypeAndShopId(String type, String shopId){
        return shopBalanceDao.findByTypeAndShopId(type,shopId);
    }

    @Override
    public List<ShopBalance> findByType(String type){
        return shopBalanceDao.findByType(type);
    }

    @Override
    public Page<ShopBalance> findAll(Pageable pageable) {
        return shopBalanceDao.findAll(pageable);
    }
}
