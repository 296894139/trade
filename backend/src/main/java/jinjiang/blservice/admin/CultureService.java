package jinjiang.blservice.admin;

import jinjiang.entity.admin.Culture;
import jinjiang.exception.NotExistException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CultureService {

    void addCulture(Culture admin);

    void deleteCulture(String id) throws NotExistException;

    void updateCulture(Culture culture) throws NotExistException;

    Culture findById(String id) throws NotExistException;

    Page<Culture> findByType(String type, Pageable pageable);

    Page<Culture> findAll(Pageable pageable);

    Page<Culture> find(String query, Pageable pageable);
}
