package jinjiang.bl.admin;

import jinjiang.blservice.admin.AdService;
import jinjiang.dao.admin.AdDao;
import jinjiang.entity.admin.Ad;
import jinjiang.exception.NotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class AdBlServiceImpl implements AdService {
    private final AdDao addao;

    @Autowired
    public AdBlServiceImpl(AdDao addao){
        this.addao=addao;
    }

    @Override
    public void addAd(Ad Ad) {
        addao.save(Ad);
    }

    @Override
    public void deleteAd(String id) throws NotExistException {
        Optional<Ad> admin=addao.findById(id);
        if (admin.isPresent()){
            addao.deleteById(id);
        }else {
            throw new NotExistException("ad ID", id);
        }
    }

    @Override
    public void updateAd(Ad Ad) throws NotExistException {
        Optional<Ad> adminone=addao.findById(Ad.getId());
        if (adminone.isPresent()){
            Ad newAdmin=adminone.get();
            newAdmin.setChecked(Ad.isChecked());
            newAdmin.setImage(Ad.getImage());
            newAdmin.setShowPlace(Ad.getShowPlace());
            newAdmin.setLink(Ad.getLink());
            addao.save(newAdmin);
        }else {
            throw new NotExistException("address ID", Ad.getId());
        }
    }

    @Override
    public Ad findById(String id) throws NotExistException {
        Optional<Ad> admin=addao.findById(id);
        if (admin.isPresent()){
            return admin.get();
        }else {
            throw new NotExistException("ad ID", id);
        }
    }

    @Override
    public Ad findByshowPlace(String showPlace) throws NotExistException {
        Optional<Ad> admin=addao.findByShowPlace(showPlace);
        if (admin.isPresent()){
            return admin.get();
        }else {
            throw new NotExistException("ad ID", showPlace);
        }
    }


    @Override
    public Page<Ad> findAll(Pageable pageable) {
        return addao.findAll(pageable);
    }


}
