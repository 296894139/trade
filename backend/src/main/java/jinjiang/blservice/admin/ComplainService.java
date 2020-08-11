package jinjiang.blservice.admin;

import jinjiang.entity.admin.Complain;
import jinjiang.exception.NotExistException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ComplainService {

    void addComplain(Complain complain);

    void deleteComplain(String id) throws NotExistException;

    void updateComplain(Complain complain) throws NotExistException;

    Complain findById(String id) throws NotExistException;

    Page<Complain> findAll(Pageable pageable);

    Page<Complain> findByShopId(String shopId, Pageable pageable);

    Page<Complain> find(String query, Pageable pageable);
}
