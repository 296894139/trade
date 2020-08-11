package jinjiang.bl.shop;

import jinjiang.blservice.shop.Goods2BlService;
import jinjiang.blservice.shop.GoodsBlService;
import jinjiang.dao.shop.Goods2Dao;
import jinjiang.dao.shop.ShopDao;
import jinjiang.entity.shop.Goods2;
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
public class Goods2BlServiceImpl implements Goods2BlService {
    private final Goods2Dao goodsDao;
    private final ShopDao shopdao;

    @Autowired
    public Goods2BlServiceImpl(Goods2Dao goods, ShopDao shopdao){
        this.goodsDao=goods;
        this.shopdao=shopdao;
    }

    @Override
    public void addGoods(Goods2 goods) {
        goodsDao.save(goods);
    }

    @Override
    public void deleteGoods(String id) throws NotExistException {
       Optional<Goods2> goods=goodsDao.findById(id);
       if (goods.isPresent()){
           goodsDao.deleteById(id);
       }
    }

    @Override
    public void updateGoods(Goods2 goods) throws NotExistException {
        Optional<Goods2> good=goodsDao.findById(goods.getId());
        if (good.isPresent()){
           Goods2 goodsone=good.get();
            goodsone.setDiscountId(goods.getDiscountId());
            goodsone.setDetail(goods.getDetail());
            goodsone.setFreight(goods.getFreight());
            goodsone.setImageUrl(goods.getImageUrl());
            goodsone.setMemberPrice(goods.getMemberPrice());
            goodsone.setName(goods.getName());
            goodsone.setNumber(goods.getNumber());
            goodsone.setPrice(goods.getPrice());
            goodsone.setSales(goods.getSales());
            goodsone.setShopId(goods.getShopId());
            goodsone.setStockPrice(goods.getStockPrice());
            goodsone.setSwiperImgs(goods.getSwiperImgs());
            goodsone.setBrief(goods.getBrief());
            goodsone.setStandard(goods.getStandard());
            goodsDao.save(goodsone);
        }else {
            throw new NotExistException("Goods ID", goods.getId());
        }
    }


    @Override
    public Goods2 findById(String id) throws NotExistException {
        Optional<Goods2> goods=goodsDao.findById(id);
        if (goods.isPresent()){
            return goods.get();
        }else {
            throw new NotExistException("Goods ID", id);
        }

    }

    @Override
    public Page<Goods2> findAll(Pageable pageable) {
        return goodsDao.findAll(pageable);
    }

    @Override
    public Page<Goods2> find(String query, Pageable pageable) {
        List<Goods2> goodsList=goodsDao.findAll();
        List<Goods2> list=new ArrayList<>();
        for(Goods2 goods:goodsList){
            String shopName="***";
            Optional<Shop> shop=shopdao.findById(goods.getShopId());

            if(shop.isPresent()){
                shopName=shop.get().getName();
            }
            if(goods.getName().indexOf(query)!=(-1)||goods.getId().indexOf(query)!=(-1)||shopName.indexOf(query)!=(-1)){
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
    public Page<Goods2> findGoodsByShopId(String id,Pageable pageable)throws NotExistException {
        //验证shop是否存在，存在再验证下面是否有商品是在属于商家；
        System.out.println();
        Optional<Shop> shop=shopdao.findById(id);
        System.out.println("===shop==="+shop.get());
        if (shop.isPresent()){
            Page<Goods2> goods=goodsDao.findAllByShopId(id,pageable);
            if (goods.getContent()!=null){
                return goods;
            }else {
                throw new NotExistException("暂无商品", id);
            }
        }else {
            throw new NotExistException("shop ID", id);
        }

    }



}
