package jinjiang.blservice.admin;

import jinjiang.entity.admin.Ad;
import jinjiang.exception.NotExistException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdService   {

    void addAd(Ad Ad);

    void deleteAd(String id) throws NotExistException;

    void updateAd(Ad Ad) throws NotExistException;

    Ad findById(String id) throws NotExistException;

    Ad findByshowPlace(String showPalce) throws NotExistException;

    Page<Ad> findAll(Pageable pageable);
}
