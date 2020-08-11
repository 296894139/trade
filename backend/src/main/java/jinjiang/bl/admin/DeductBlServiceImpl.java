package jinjiang.bl.admin;

import jinjiang.blservice.admin.DeductService;
import jinjiang.dao.admin.DeductDao;
import jinjiang.dao.shop.ShopDao;
import jinjiang.entity.admin.Deduct;
import jinjiang.entity.shop.Shop;
import jinjiang.exception.NotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DeductBlServiceImpl implements DeductService {
    private final DeductDao deductDao;
    private final ShopDao shopDao;

    @Autowired
    public DeductBlServiceImpl(DeductDao deductDao, ShopDao shopDao){

        this.deductDao = deductDao;
        this.shopDao = shopDao;
    }

    @Override
    public void addDeduct(Deduct deduct) {
         deductDao.save(deduct);
    }

    @Override
    public void deleteDeduct(String id) throws NotExistException {
       Optional<Deduct> deduct=deductDao.findById(id);
       if (deduct.isPresent()){
           deductDao.deleteById(id);
       }else {
           throw new NotExistException("address ID", id);
       }
    }

    @Override
    public void updateDeduct(Deduct deduct) throws NotExistException {
        Optional<Deduct> optionalDeduct=deductDao.findById(deduct.getId());
        if (optionalDeduct.isPresent()){
           Deduct newDeduct=optionalDeduct.get();
            newDeduct.setOther(deduct.getOther());
            newDeduct.setPersonal(deduct.getPersonal());
            newDeduct.setShopId(deduct.getShopId());
            newDeduct.setStaffRatio(deduct.getStaffRatio());
            newDeduct.setTakeBalance(deduct.getTakeBalance());
            deductDao.save(newDeduct);
        }else {
            throw new NotExistException("address ID", deduct.getId());
        }
    }

    @Override
    public Deduct findById(String id) throws NotExistException {
        Optional<Deduct> optionalDeduct=deductDao.findById(id);
        if (optionalDeduct.isPresent()){
            return optionalDeduct.get();
        }else {
            throw new NotExistException("address ID", id);
        }
    }

    @Override
    public Page<Deduct> findAll(Pageable pageable) {
        return deductDao.findAll(pageable);
    }


    @Override
    public Page<Deduct> find(String query, Pageable pageable) {
        List<Deduct> deducts=deductDao.findAll();
        List<Deduct> list=new ArrayList<>();
        for(Deduct deduct:deducts){
            String shopName="***";
            Optional<Shop> shop=shopDao.findById(deduct.getShopId());
            if(shop.isPresent()){
                shopName=shop.get().getName();
            }
            if(deduct.getId().indexOf(query)!=(-1)||shopName.indexOf(query)!=(-1)){
                list.add(deduct);
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
