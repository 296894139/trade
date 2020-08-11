package jinjiang.blservice.account;

import jinjiang.entity.account.Address;
import jinjiang.exception.NotExistException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AddressBlService {
    Address addAddress(Address address);

    void deleteAddress(String id) throws NotExistException;

    void updateAddress(Address address) throws NotExistException;

    Address findById(String id) throws NotExistException;

    List<Address> findByUserId(String userId) throws NotExistException;

    Page<Address> findAll(Pageable pageable);

}
