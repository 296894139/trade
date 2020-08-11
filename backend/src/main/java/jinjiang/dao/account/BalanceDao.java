package jinjiang.dao.account;

import jinjiang.entity.account.Address;
import jinjiang.entity.account.Balance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BalanceDao extends JpaRepository<Balance, String> {
    List<Balance> findByUserId(String userId);
    List<Balance> findByType(String type);
    List<Balance> findByTypeAndUserId(String type,String userId);
}
