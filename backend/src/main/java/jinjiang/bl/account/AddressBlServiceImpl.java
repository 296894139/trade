package jinjiang.bl.account;

import jinjiang.blservice.account.AddressBlService;
import jinjiang.dao.account.AddressDao;
import jinjiang.entity.account.Address;
import jinjiang.exception.NotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressBlServiceImpl implements AddressBlService {

    private final AddressDao addressDao;

    @Autowired
    public AddressBlServiceImpl(AddressDao addressDao) {
        this.addressDao = addressDao;
    }

    @Override
    public Address addAddress(Address address) {
        return addressDao.save(address);
    }

    @Override
    public void deleteAddress(String id) throws NotExistException {
        Optional<Address> optionalUser = addressDao.findById(id);
        if (optionalUser.isPresent()) {
            addressDao.deleteById(id);
        } else {
            throw new NotExistException("Address ID", id);
        }
    }

    @Override
    public void updateAddress(Address address) throws NotExistException {
        Optional<Address> optionalUser = addressDao.findById(address.getId());
        if (optionalUser.isPresent()){
            Address newUser = optionalUser.get();
            newUser.setCity(address.getCity());
            newUser.setDetail(address.getDetail());
            newUser.setDistrict(address.getDistrict());
            newUser.setMobilePhone(address.getMobilePhone());
            newUser.setPerson(address.getPerson());
            newUser.setProvince(address.getProvince());
            newUser.setUserId(address.getUserId());
            newUser.setDefault(address.isDefault());
            addressDao.save(newUser);
        }else {
            throw new NotExistException("address ID", address.getId());
        }

    }

    @Override
    public Address findById(String id) throws NotExistException {
        return addressDao.findById(id).get();
    }

    @Override
    public List<Address> findByUserId(String userId) {
        return addressDao.findByUserId(userId);
    }

    @Override
    public Page<Address> findAll(Pageable pageable) {
        return addressDao.findAll(pageable);
    }
}
