package jinjiang.dao.account;

import jinjiang.entity.account.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressDao extends JpaRepository<Address, String> {
    List<Address> findByUserId(String userId);
}
