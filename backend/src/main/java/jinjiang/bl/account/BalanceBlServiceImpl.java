package jinjiang.bl.account;

import jinjiang.blservice.account.BalanceBlService;
import jinjiang.dao.account.BalanceDao;
import jinjiang.dao.account.UserDao;
import jinjiang.entity.account.Balance;
import jinjiang.entity.account.User;
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
public class BalanceBlServiceImpl implements BalanceBlService {

    private final BalanceDao balanceDao;
    private final UserDao userDao;

    @Autowired
    public BalanceBlServiceImpl(BalanceDao balanceDao, UserDao userDao) {
        this.balanceDao = balanceDao;
        this.userDao = userDao;
    }

    @Override
    public Balance addBalance(Balance balance) {
        balance.setTime(FormatDateTime.toLongDateString(new Date()));
        return balanceDao.save(balance);
    }

    @Override
    public void deleteBalance(String id) throws NotExistException {
        Optional<Balance> optionalBalance = balanceDao.findById(id);
        if (optionalBalance.isPresent()) {
            balanceDao.deleteById(id);
        } else {
            throw new NotExistException("Address ID", id);
        }
    }

    @Override
    public void updateBalance(Balance balance) throws NotExistException {
        Optional<Balance> optionalBalance = balanceDao.findById(balance.getId());
        if (optionalBalance.isPresent()){
            Balance newBalance = optionalBalance.get();
            newBalance.setDetail(balance.getDetail());
            newBalance.setGoodsList(balance.getGoodsList());
            newBalance.setPrice(balance.getPrice());
            newBalance.setTime(balance.getTime());
            newBalance.setType(balance.getType());
            newBalance.setUserId(balance.getUserId());
            newBalance.setUsername(balance.getUsername());
        }else {
            throw new NotExistException("address ID", balance.getId());
        }

    }

    @Override
    public Balance findById(String id) throws NotExistException {
        return balanceDao.findById(id).get();
    }

    @Override
    public List<Balance> findByShareholderId(String shareholderId) throws NotExistException {
        List<User>  users=userDao.findByShareholderId(shareholderId);
        List<Balance> balances=new ArrayList<>();
        for(int i=0;i<users.size();i++){
            List<Balance> balance=balanceDao.findByTypeAndUserId("支出",users.get(i).getId());
            for(int j=0;j<balance.size();j++){
                balances.add(balance.get(j));
            }
        }
        return balances;
    }

    @Override
    public List<Balance> findByTypeAndUserId(String type, String userId){
        return balanceDao.findByTypeAndUserId(type,userId);
    }

    @Override
    public List<Balance> findByTypeAndShopId(String type, String shopId){
        System.out.println(shopId);
        List<User> users=userDao.findByIdentityAndShopId("shareholder",shopId);
        System.out.println("users");
        List<Balance> balances=new ArrayList<>();
        for(User user:users){
            List<Balance> balanceList=balanceDao.findByTypeAndUserId(type,user.getId());
            for(Balance balance:balanceList){
                balances.add(balance);
            }
        }
        return balances;
    }

    @Override
    public List<Balance> findByType(String type){
        return balanceDao.findByType(type);
    }


    @Override
    public List<Balance> findByUserId(String userId){
        return balanceDao.findByUserId(userId);
    }

    @Override
    public Page<Balance> findAll(Pageable pageable) {
        return balanceDao.findAll(pageable);
    }
}
