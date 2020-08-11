package jinjiang.bl.shop;

import jinjiang.blservice.shop.IntegraGoodsBlservice;
import jinjiang.dao.shop.IntegraGoodsDao;
import jinjiang.entity.shop.Goods;
import jinjiang.entity.shop.IntegralGoods;
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
public class IntegraGoodsBlServiceImpl implements IntegraGoodsBlservice {

    private final IntegraGoodsDao integraGoodsDao;

    @Autowired
    public IntegraGoodsBlServiceImpl(IntegraGoodsDao integraGoodsDao) {
        this.integraGoodsDao = integraGoodsDao;
    }

    @Override
    public void addIntegralGoods(IntegralGoods goods) {
        integraGoodsDao.save(goods);
    }

    @Override
    public void deleteIntegralGoods(String id) throws NotExistException {
        Optional<IntegralGoods> optionalUser = integraGoodsDao.findById(id);
        if (optionalUser.isPresent()) {
            integraGoodsDao.deleteById(id);
        } else {
            throw new NotExistException("integraGoods ID", id);
        }
    }

    @Override
    public void updateIntegralGoods(IntegralGoods goods) throws NotExistException {
        Optional<IntegralGoods> optionalUser = integraGoodsDao.findById(goods.getId());
        if (optionalUser.isPresent()){
            IntegralGoods integraGoods = optionalUser.get();
            integraGoods.setDetail(integraGoods.getDetail());
            integraGoods.setImageUrl(goods.getImageUrl());
            integraGoods.setIntegral(goods.getIntegral());
            integraGoods.setName(goods.getName());
            integraGoods.setNumber(goods.getNumber());
            integraGoods.setSwiperImgs(goods.getSwiperImgs());
            integraGoods.setBrief(goods.getBrief());
            integraGoods.setStandard(goods.getStandard());
            integraGoodsDao.save(integraGoods);
        }else {
            throw new NotExistException("address ID", goods.getId());
        }
    }

    @Override
    public Page<IntegralGoods> find(String query, Pageable pageable) {
        List<IntegralGoods> goodsList=integraGoodsDao.findAll();
        List<IntegralGoods> list=new ArrayList<>();
        for(IntegralGoods goods:goodsList){

            if(goods.getName().indexOf(query)!=(-1)||goods.getId().indexOf(query)!=(-1)||String.valueOf(goods.getIntegral()).indexOf(query)!=(-1)){
                list.add(goods);
            }
        }
        return listConvertToPage(list,pageable);

    }

    public <T> Page<T> listConvertToPage(List<T> list, Pageable pageable) {
        int start = (int)pageable.getOffset();
        int end = (start + pageable.getPageSize()) > list.size() ? list.size() : ( start + pageable.getPageSize());
        return new PageImpl<T>(list.subList(start, end), pageable, list.size());
    }
    @Override
    public IntegralGoods findById(String id) throws NotExistException {
        Optional<IntegralGoods> optionalUser = integraGoodsDao.findById(id);
        if (optionalUser.isPresent()) {
           return optionalUser.get();
        } else {
            throw new NotExistException("integraGoods ID", id);
        }
    }

    @Override
    public Page<IntegralGoods> findAll(Pageable pageable) {
        return integraGoodsDao.findAll(pageable);
    }


}
