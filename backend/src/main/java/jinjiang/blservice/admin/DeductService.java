package jinjiang.blservice.admin;

import jinjiang.entity.admin.Admin;
import jinjiang.entity.admin.Deduct;
import jinjiang.exception.NotExistException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeductService {

    void addDeduct(Deduct deduct);

    void deleteDeduct(String id) throws NotExistException;

    void updateDeduct(Deduct deduct) throws NotExistException;

    Deduct findById(String id) throws NotExistException;

    Page<Deduct> findAll(Pageable pageable);

    Page<Deduct> find(String query, Pageable pageable);
}
