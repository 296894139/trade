package jinjiang.blservice.account;

import jinjiang.entity.account.Address;
import jinjiang.entity.account.Balance;
import jinjiang.exception.NotExistException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BalanceBlService {
    Balance addBalance(Balance balance);

    void deleteBalance(String id) throws NotExistException;

    void updateBalance(Balance balance) throws NotExistException;

    Balance findById(String id) throws NotExistException;

    List<Balance> findByShareholderId(String shareholderId) throws NotExistException;

    List<Balance> findByTypeAndUserId(String type, String userId) throws NotExistException;

    List<Balance> findByTypeAndShopId(String type, String shopId);

    List<Balance> findByType(String type);

    List<Balance> findByUserId(String userId);

    Page<Balance> findAll(Pageable pageable);

}
