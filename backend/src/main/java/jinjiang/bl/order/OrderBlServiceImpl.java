package jinjiang.bl.order;

import jinjiang.blservice.account.CouponService;
import jinjiang.blservice.order.OrderBlService;
import jinjiang.dao.account.BalanceDao;
import jinjiang.dao.account.CouponDao;
import jinjiang.dao.account.DiscountDao;
import jinjiang.dao.account.UserDao;
import jinjiang.dao.admin.DeductDao;
import jinjiang.dao.order.OrderDao;
import jinjiang.dao.recommend.RecommendDao;
import jinjiang.dao.shop.*;
import jinjiang.entity.account.Balance;
import jinjiang.entity.account.Coupon;
import jinjiang.entity.account.Discount;
import jinjiang.entity.account.User;
import jinjiang.entity.admin.Deduct;
import jinjiang.entity.order.Order;
import jinjiang.entity.recommend.Recommend;
import jinjiang.entity.shop.*;
import jinjiang.exception.NotExistException;
import jinjiang.exception.SystemException;
import jinjiang.response.OrderResponse;
import jinjiang.response.GoodsItem;
import jinjiang.response.WxBuyCreditItem;
import jinjiang.response.WxBuyCreditResponse;
import jinjiang.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class OrderBlServiceImpl implements OrderBlService {
    private final OrderDao orderdoa;
    private final UserDao userDao;
    private final GoodsDao goodsDao;
    private final Goods2Dao goods2Dao;
    private final IntegraGoodsDao integraGoodsDao;
    private final DeductDao deductDao;
    private final ShopDao shopDao;
    private final BalanceDao balanceDao;
    private final ShopBalanceDao shopBalanceDao;
    private final RecommendDao recommendDao;
    private final DiscountDao discountDao;
    private final CouponDao couponDao;
    private final CouponService couponService;
    @Autowired
    public OrderBlServiceImpl(OrderDao orderdoa, UserDao userDao, GoodsDao goodsDao, Goods2Dao goods2Dao, IntegraGoodsDao integraGoodsDao, DeductDao deductDao, ShopDao shopDao, BalanceDao balanceDao, ShopBalanceDao shopBalanceDao, RecommendDao recommendDao, DiscountDao discountDao, CouponDao couponDao, CouponService couponService){
        this.orderdoa=orderdoa;
        this.userDao = userDao;
        this.goodsDao = goodsDao;
        this.goods2Dao = goods2Dao;
        this.integraGoodsDao = integraGoodsDao;
        this.deductDao = deductDao;
        this.shopDao = shopDao;
        this.balanceDao = balanceDao;
        this.shopBalanceDao = shopBalanceDao;
        this.recommendDao = recommendDao;
        this.discountDao = discountDao;
        this.couponDao = couponDao;
        this.couponService = couponService;
    }

    @Override
    public Order addOrder(Order order) {
        Date date=new Date();
        String time= FormatDateTime.toShortDateString(date);
        order.setBuyTime(time);

        if(order.getStatus().equals("积分待审核")){
            Optional<User> optionalUser=userDao.findById(order.getUserId());
            if(optionalUser.isPresent()){
                User user=optionalUser.get();
                List<String> goodsName=new ArrayList<>();
                List<String> goodsId=order.getGoodsList();
                for(String id:goodsId){
                    Optional<IntegralGoods> optionalIntegralGoods=integraGoodsDao.findById(id);
                    if(optionalIntegralGoods.isPresent()){
                        goodsName.add(optionalIntegralGoods.get().getName());
                    }
                }
                Balance balance=new Balance(user.getId(),user.getUsername(),"积分",Math.floor(order.getPrice()),"积分兑换",FormatDateTime.toLongDateString(new Date()),goodsName);
                balanceDao.save(balance);
            }

        }
        return orderdoa.save(order);
    }

    @Override
    public void deleteOrder(String id) throws NotExistException {
     Optional<Order> order=orderdoa.findById(id);
     if (order.isPresent()){
         orderdoa.deleteById(id);
     }else {
         throw new NotExistException("order ID", id);
     }
    }

    @Override
    public void updateOrder(Order order) throws NotExistException {
        Optional<Order> orderone=orderdoa.findById(order.getId());
        if (orderone.isPresent()){
            Order neworder=orderone.get();
            neworder.setAddress(order.getAddress());
            neworder.setBuyTime(order.getBuyTime());
            neworder.setDiscountPrice(order.getDiscountPrice());
            neworder.setGoodsList(order.getGoodsList());
            neworder.setPrice(order.getPrice());
            neworder.setStatus(order.getStatus());
            neworder.setUserId(order.getUserId());
            neworder.setType(order.getType());
            neworder.setRemark(order.getRemark());
            neworder.setMobilePone(order.getMobilePone());
            neworder.setFreight(order.getFreight());
            neworder.setPerson(order.getPerson());
            orderdoa.save(neworder);
        } else {
            throw new NotExistException("order ID", order.getId());
        }
    }

    @Override
    public Order findById(String id) throws NotExistException {
        Optional<Order> order=orderdoa.findById(id);
        if (order.isPresent()){
            return order.get();
        }else {
            throw new NotExistException("order ID", id);
        }
    }

    @Override
    public Page<Order> findByStatus(String status,Pageable pageable) {
        return orderdoa.findByStatusOrderByBuyTimeDesc(status,pageable);
    }

    @Override
    public void cancel(String id) throws NotExistException {
        Optional<Order> optionalOrder=orderdoa.findById(id);
        if (optionalOrder.isPresent()){
            Order order=optionalOrder.get();
            order.setStatus("已取消");
            orderdoa.save(order);
        }else {
            throw new NotExistException("order ID", id);
        }
    }

    @Override
    public void send(String id) throws NotExistException {
        Optional<Order> optionalOrder=orderdoa.findById(id);
        if (optionalOrder.isPresent()){
            Order order=optionalOrder.get();
            order.setStatus("待收货");
            orderdoa.save(order);
        }else {
            throw new NotExistException("order ID", id);
        }
    }

    @Override
    public void take(String id) throws NotExistException {
        Optional<Order> optionalOrder=orderdoa.findById(id);
        if (optionalOrder.isPresent()){
            Order order=optionalOrder.get();
            order.setStatus("已完成");
            orderdoa.save(order);
        }else {
            throw new NotExistException("order ID", id);
        }
    }

    @Override
    public void integralSend(String id) throws NotExistException {
        Optional<Order> optionalOrder=orderdoa.findById(id);
        if (optionalOrder.isPresent()){
            Order order=optionalOrder.get();
            order.setStatus("积分待收货");
            orderdoa.save(order);
        }else {
            throw new NotExistException("order ID", id);
        }
    }

    @Override
    public void integralTake(String id) throws NotExistException {
        Optional<Order> optionalOrder=orderdoa.findById(id);
        if (optionalOrder.isPresent()){
            Order order=optionalOrder.get();
            order.setStatus("积分已完成");
            orderdoa.save(order);
        }else {
            throw new NotExistException("order ID", id);
        }
    }

    @Override
    public void pass(String id) throws NotExistException {
        Optional<Order> optionalOrder=orderdoa.findById(id);
        if (optionalOrder.isPresent()){
            Order order=optionalOrder.get();
            order.setStatus("积分待发货");
            orderdoa.save(order);
        }else {
            throw new NotExistException("order ID", id);
        }
    }

    @Override
    public void refund(String id) throws NotExistException {
        Optional<Order> optionalOrder=orderdoa.findById(id);
        if (optionalOrder.isPresent()){
            Order order=optionalOrder.get();
            order.setStatus("退款中");
            orderdoa.save(order);
        }else {
            throw new NotExistException("order ID", id);
        }
    }


    @Override
    public void back(String id) throws NotExistException {
        Order o=orderdoa.getOne(id);
        double actualPrice=o.getPrice();
        List<String> goodsName=new ArrayList<>();
        Optional<Order> optionalOrder=orderdoa.findById(id);
        if (optionalOrder.isPresent()){
            String shopId="";
            o.setStatus("已退款");
            orderdoa.save(o);
            User user=userDao.getOne(o.getUserId());
            user.setBalance(user.getBalance()+actualPrice);

            if(user.getIdentity().equals("member")){
                user.setIntegral(user.getIntegral()-(int)actualPrice);
                userDao.save(user);
                double stock=0;
                double profit=0;
                List<String> goodsList=o.getGoodsList();
                for(int i=0;i<goodsList.size();i++){
                    Optional<Goods> optionalGoods=goodsDao.findById(goodsList.get(i));
                    if(optionalGoods.isPresent()){
                        Goods goods=optionalGoods.get();
                        goods.setNumber(goods.getNumber()+1);
                        goodsDao.save(goods);
                        goodsName.add(goods.getName());
                        shopId=goods.getShopId();
                        stock+=goods.getStockPrice();
                    }
                    else{
                        Optional<Goods2> optionalGoods2=goods2Dao.findById(goodsList.get(i));
                        if(optionalGoods2.isPresent()){
                            Goods2 goods2=optionalGoods2.get();
                            goods2.setNumber(goods2.getNumber()+1);
                            goods2Dao.save(goods2);
                            goodsName.add(goods2.getName());
                            shopId=goods2.getShopId();
                            stock+=goods2.getStockPrice();
                        }
                    }
                }
                Deduct deduct=new Deduct();
                Optional<Deduct> optionalDeduct=deductDao.findByShopId(shopId);
                if(optionalDeduct.isPresent()){
                    deduct=optionalDeduct.get();
                }
                profit=actualPrice-stock;
                Balance balance=new Balance(user.getId(),user.getUsername(),"收入",actualPrice,"商品退款",FormatDateTime.toLongDateString(new Date()),goodsName);
                balanceDao.save(balance);
                if(user.getShareholderId().equals("")){
                    Optional<User> optionalStaff=userDao.findById(user.getRemark());
                    if(optionalStaff.isPresent()){
                        User staff=optionalStaff.get();
                        staff.setBalance(staff.getBalance()-profit*deduct.getStaffRatio());
                        Balance balance1=new Balance(staff.getId(),staff.getUsername(),"支出",profit*deduct.getStaffRatio(),"会员"+user.getUsername()+"退款",FormatDateTime.toLongDateString(new Date()),goodsName);
                        balanceDao.save(balance1);
                        userDao.save(staff);
                        Optional<Shop> optionalShop=shopDao.findById(shopId);
                        if(optionalShop.isPresent()){
                            Shop shop=optionalShop.get();
                            shop.setBalance(shop.getBalance()-stock-profit*(1-deduct.getStaffRatio()));
                            shopDao.save(shop);
                            ShopBalance shopBalance=new ShopBalance(shop.getId(),shop.getName(),"支出","",stock+profit*(1-deduct.getStaffRatio()),"会员"+user.getUsername()+"退款",FormatDateTime.toLongDateString(new Date()),goodsName);
                            shopBalanceDao.save(shopBalance);
                        }
                    }
                    else{
                        Optional<Shop> optionalShop=shopDao.findById(shopId);
                        if(optionalShop.isPresent()){
                            Shop shop=optionalShop.get();
                            shop.setBalance(shop.getBalance()-stock-profit);
                            shopDao.save(shop);
                            ShopBalance shopBalance=new ShopBalance(shop.getId(),shop.getName(),"支出","",profit+stock,"会员"+user.getUsername()+"退款",FormatDateTime.toLongDateString(new Date()),goodsName);
                            shopBalanceDao.save(shopBalance);
                        }
                    }

                }
                else{
                    Optional<User> optionalShareholder=userDao.findById(user.getShareholderId());
                    if(optionalShareholder.isPresent()){
                        User shareholder=optionalShareholder.get();
                        shareholder.setBalance(shareholder.getBalance()-profit*deduct.getPersonal());
                        shareholder.setTakeBalance(shareholder.getTakeBalance()-profit*deduct.getTakeBalance());
                        userDao.save(shareholder);
                        Balance balance2=new Balance(shareholder.getId(),shareholder.getUsername(),"支出",profit*deduct.getPersonal()+profit*deduct.getTakeBalance(),"会员"+user.getUsername()+"退款",FormatDateTime.toLongDateString(new Date()),goodsName);
                        balanceDao.save(balance2);
                        Optional<Shop> optionalShop=shopDao.findById(shopId);
                        if(optionalShop.isPresent()){
                            Shop shop=optionalShop.get();
                            shop.setBalance(shop.getBalance()-stock-profit*(1-deduct.getTakeBalance()-deduct.getPersonal()));
                            shopDao.save(shop);
                            ShopBalance shopBalance=new ShopBalance(shop.getId(),shop.getName(),"支出","",stock+profit*(1-deduct.getTakeBalance()-deduct.getPersonal()),"会员"+user.getUsername()+"退款",FormatDateTime.toLongDateString(new Date()),goodsName);
                            shopBalanceDao.save(shopBalance);
                        }
                    }
                    else{
                        Optional<Shop> optionalShop=shopDao.findById(shopId);
                        if(optionalShop.isPresent()){
                            Shop shop=optionalShop.get();
                            shop.setBalance(shop.getBalance()-stock-profit);
                            shopDao.save(shop);
                            ShopBalance shopBalance=new ShopBalance(shop.getId(),shop.getName(),"支出","",profit+stock,"会员"+user.getUsername()+"退款",FormatDateTime.toLongDateString(new Date()),goodsName);
                            shopBalanceDao.save(shopBalance);
                        }
                    }

                }

            }
            else{
                userDao.save(user);
                double stock=0;
                double profit=0;
                List<String> goodsList=o.getGoodsList();
                for(int i=0;i<goodsList.size();i++){
                    Optional<Goods> optionalGoods=goodsDao.findById(goodsList.get(i));
                    if(optionalGoods.isPresent()){
                        Goods goods=optionalGoods.get();
                        goods.setNumber(goods.getNumber()+1);
                        goodsDao.save(goods);
                        goodsName.add(goods.getName());
                        shopId=goods.getShopId();
                        stock+=goods.getStockPrice();
                    }
                    else{
                        Optional<Goods2> optionalGoods2=goods2Dao.findById(goodsList.get(i));
                        if(optionalGoods2.isPresent()){
                            Goods2 goods2=optionalGoods2.get();
                            goods2.setNumber(goods2.getNumber()+1);
                            goods2Dao.save(goods2);
                            goodsName.add(goods2.getName());
                            shopId=goods2.getShopId();
                            stock+=goods2.getStockPrice();
                        }
                    }
                }
                profit=actualPrice-stock;
                Balance balance=new Balance(user.getId(),user.getUsername(),"收入",actualPrice,"退款",FormatDateTime.toLongDateString(new Date()),goodsName);
                balanceDao.save(balance);
                Optional<Shop> optionalShop=shopDao.findById(shopId);
                if(optionalShop.isPresent()){
                    Shop shop=optionalShop.get();
                    shop.setBalance(shop.getBalance()-stock-profit);
                    shopDao.save(shop);
                    ShopBalance shopBalance=new ShopBalance(shop.getId(),shop.getName(),"支出","",profit+stock,"人员"+user.getUsername()+"退款",FormatDateTime.toLongDateString(new Date()),goodsName);
                    shopBalanceDao.save(shopBalance);
                }
            }
        }else {
            throw new NotExistException("order ID", id);
        }
    }


    @Override
    public Page<Order> findAll(Pageable pageable) {

        return orderdoa.findAll(pageable);
    }

    @Override
    public List<OrderResponse> findAllWX(String userId) {
        List<Order> orders=orderdoa.findByUserIdOrderByBuyTimeAsc(userId);
        List<OrderResponse> orderResponses=new ArrayList<>();
        for(int i=orders.size()-1;i>=0;i--){
            List<GoodsItem> goodsItems=new ArrayList<>();
            for(int j=0;j<orders.get(i).getGoodsList().size();j++){
                String id=orders.get(i).getGoodsList().get(j);
                GoodsItem goodsItem=new GoodsItem();
                Optional<Goods> optionalGoods=goodsDao.findById(id);
                if(optionalGoods.isPresent()){
                    Goods goods=optionalGoods.get();
                    goodsItem=new GoodsItem(goods.getId(),goods.getName(),goods.getImageUrl(),goods.getStandard(),goods.getPrice());
                    goodsItems.add(goodsItem);
                }
                else{
                    Optional<Goods2> optionalGoods2=goods2Dao.findById(id);
                    if(optionalGoods2.isPresent()){
                        Goods2 goods2=optionalGoods2.get();
                        goodsItem=new GoodsItem(goods2.getId(),goods2.getName(),goods2.getImageUrl(),goods2.getStandard(),goods2.getPrice());
                        goodsItems.add(goodsItem);
                    }
                    else{
                        Optional<IntegralGoods> optionalIntegralGoods=integraGoodsDao.findById(id);
                        if(optionalIntegralGoods.isPresent()){
                            IntegralGoods integralGoods=optionalIntegralGoods.get();
                            goodsItem=new GoodsItem(integralGoods.getId(),integralGoods.getName(),integralGoods.getImageUrl(),integralGoods.getStandard(),integralGoods.getIntegral());
                            goodsItems.add(goodsItem);
                        }
                    }
                }
            }
            Order order=orders.get(i);
            OrderResponse orderResponse=new OrderResponse(order.getId(),order.getUserId(),order.getAddress(),order.getMobilePone(),order.getPerson(),order.getType(),order.getRemark(),order.getFreight(),order.getPrice(),order.getDiscountPrice(),goodsItems,order.getBuyTime(),order.getStatus());
            orderResponses.add(orderResponse);
        }
        return orderResponses;
    }

    @Override
    public List<OrderResponse> findAll() {
        List<Order> orders=orderdoa.findAll();
        List<OrderResponse> orderResponses=new ArrayList<>();
        for(int i=orders.size()-1;i>=0;i--){
            List<GoodsItem> goodsItems=new ArrayList<>();
            for(int j=0;j<orders.get(i).getGoodsList().size();j++){
                String id=orders.get(i).getGoodsList().get(j);
                GoodsItem goodsItem=new GoodsItem();
                Optional<Goods> optionalGoods=goodsDao.findById(id);
                if(optionalGoods.isPresent()){
                    Goods goods=optionalGoods.get();
                    goodsItem=new GoodsItem(goods.getId(),goods.getName(),goods.getImageUrl(),goods.getStandard(),goods.getPrice());
                    goodsItems.add(goodsItem);
                }
                else{
                    Optional<Goods2> optionalGoods2=goods2Dao.findById(id);
                    if(optionalGoods2.isPresent()){
                        Goods2 goods2=optionalGoods2.get();
                        goodsItem=new GoodsItem(goods2.getId(),goods2.getName(),goods2.getImageUrl(),goods2.getStandard(),goods2.getPrice());
                        goodsItems.add(goodsItem);
                    }
                    else{
                        Optional<IntegralGoods> optionalIntegralGoods=integraGoodsDao.findById(id);
                        if(optionalIntegralGoods.isPresent()){
                            IntegralGoods integralGoods=optionalIntegralGoods.get();
                            goodsItem=new GoodsItem(integralGoods.getId(),integralGoods.getName(),integralGoods.getImageUrl(),integralGoods.getStandard(),integralGoods.getIntegral());
                            goodsItems.add(goodsItem);
                        }
                    }
                }
            }
            Order order=orders.get(i);
            OrderResponse orderResponse=new OrderResponse(order.getId(),order.getUserId(),order.getAddress(),order.getMobilePone(),order.getPerson(),order.getType(),order.getRemark(),order.getFreight(),order.getPrice(),order.getDiscountPrice(),goodsItems,order.getBuyTime(),order.getStatus());
            orderResponses.add(orderResponse);
        }
        return orderResponses;
    }

    @Override
    public List<OrderResponse> findByStatusWX(String userId,String status) {
        List<Order> orders=orderdoa.findByUserIdAndStatusOrderByBuyTimeAsc(userId,status);
        List<OrderResponse> orderResponses=new ArrayList<>();
        for(int i=orders.size()-1;i>=0;i--){
            List<GoodsItem> goodsItems=new ArrayList<>();
            for(int j=0;j<orders.get(i).getGoodsList().size();j++){
                String id=orders.get(i).getGoodsList().get(j);
                GoodsItem goodsItem=new GoodsItem();
                Optional<Goods> optionalGoods=goodsDao.findById(id);
                if(optionalGoods.isPresent()){
                    Goods goods=optionalGoods.get();
                    goodsItem=new GoodsItem(goods.getId(),goods.getName(),goods.getImageUrl(),goods.getStandard(),goods.getPrice());
                    goodsItems.add(goodsItem);
                }
                else{
                    Optional<Goods2> optionalGoods2=goods2Dao.findById(id);
                    if(optionalGoods2.isPresent()){
                        Goods2 goods2=optionalGoods2.get();
                        goodsItem=new GoodsItem(goods2.getId(),goods2.getName(),goods2.getImageUrl(),goods2.getStandard(),goods2.getPrice());
                        goodsItems.add(goodsItem);
                    }
                    else{
                        Optional<IntegralGoods> optionalIntegralGoods=integraGoodsDao.findById(id);
                        if(optionalIntegralGoods.isPresent()){
                            IntegralGoods integralGoods=optionalIntegralGoods.get();
                            goodsItem=new GoodsItem(integralGoods.getId(),integralGoods.getName(),integralGoods.getImageUrl(),integralGoods.getStandard(),integralGoods.getIntegral());
                            goodsItems.add(goodsItem);
                        }
                    }
                }
            }
            Order order=orders.get(i);
            OrderResponse orderResponse=new OrderResponse(order.getId(),order.getUserId(),order.getAddress(),order.getMobilePone(),order.getPerson(),order.getType(),order.getRemark(),order.getFreight(),order.getPrice(),order.getDiscountPrice(),goodsItems,order.getBuyTime(),order.getStatus());
            orderResponses.add(orderResponse);
        }
        return orderResponses;
    }

    @Override
    public List<OrderResponse> findByShopId(String shopId) {
        List<Order> orders=orderdoa.findAll();
        List<OrderResponse> orderResponses=new ArrayList<>();
        for(int i=orders.size()-1;i>=0;i--){
            List<GoodsItem> goodsItems=new ArrayList<>();
            for(int j=0;j<orders.get(i).getGoodsList().size();j++){
                String id=orders.get(i).getGoodsList().get(j);
                GoodsItem goodsItem=new GoodsItem();
                Optional<Goods> optionalGoods=goodsDao.findById(id);
                if(optionalGoods.isPresent()){
                    Goods goods=optionalGoods.get();
                    if(goods.getShopId().equals(shopId)) {
                        goodsItem = new GoodsItem(goods.getId(), goods.getName(), goods.getImageUrl(), goods.getStandard(), goods.getPrice());
                        goodsItems.add(goodsItem);
                    }
                }
                else{
                    Optional<Goods2> optionalGoods2=goods2Dao.findById(id);
                    if(optionalGoods2.isPresent()){

                        Goods2 goods2=optionalGoods2.get();
                        if(goods2.getShopId().equals(shopId)) {
                            goodsItem = new GoodsItem(goods2.getId(), goods2.getName(), goods2.getImageUrl(), goods2.getStandard(), goods2.getPrice());
                            goodsItems.add(goodsItem);
                        }
                    }
                    else{
                        Optional<IntegralGoods> optionalIntegralGoods=integraGoodsDao.findById(id);
                        if(optionalIntegralGoods.isPresent()){
                            String userId=orders.get(i).getUserId();
                            Optional<User> optionalUser=userDao.findById(userId);
                            if(optionalUser.isPresent()){
                                User user=optionalUser.get();
                                if(user.getShopId().equals(shopId)){
                                    IntegralGoods integralGoods=optionalIntegralGoods.get();
                                    goodsItem=new GoodsItem(integralGoods.getId(),integralGoods.getName(),integralGoods.getImageUrl(),integralGoods.getStandard(),integralGoods.getIntegral());
                                    goodsItems.add(goodsItem);
                                }
                            }

                        }
                    }
                }
            }
            if(goodsItems.size()>0) {
                Order order = orders.get(i);
                OrderResponse orderResponse = new OrderResponse(order.getId(), order.getUserId(), order.getAddress(), order.getMobilePone(), order.getPerson(), order.getType(), order.getRemark(), order.getFreight(), order.getPrice(), order.getDiscountPrice(), goodsItems, order.getBuyTime(), order.getStatus());
                orderResponses.add(orderResponse);
            }
        }
        return orderResponses;
    }

    @Override
    public List<OrderResponse> findByStatus(String status) {
        List<Order> orders=orderdoa.findByStatusOrderByBuyTimeAsc(status);
        List<OrderResponse> orderResponses=new ArrayList<>();
        for(int i=orders.size()-1;i>=0;i--){
            List<GoodsItem> goodsItems=new ArrayList<>();
            for(int j=0;j<orders.get(i).getGoodsList().size();j++){
                String id=orders.get(i).getGoodsList().get(j);
                GoodsItem goodsItem=new GoodsItem();
                Optional<Goods> optionalGoods=goodsDao.findById(id);
                if(optionalGoods.isPresent()){
                    Goods goods=optionalGoods.get();
                    goodsItem = new GoodsItem(goods.getId(), goods.getName(), goods.getImageUrl(), goods.getStandard(), goods.getPrice());
                    goodsItems.add(goodsItem);
                }
                else{
                    Optional<Goods2> optionalGoods2=goods2Dao.findById(id);
                    if(optionalGoods2.isPresent()){

                        Goods2 goods2=optionalGoods2.get();
                        goodsItem = new GoodsItem(goods2.getId(), goods2.getName(), goods2.getImageUrl(), goods2.getStandard(), goods2.getPrice());
                        goodsItems.add(goodsItem);
                    }
                    else{
                        Optional<IntegralGoods> optionalIntegralGoods=integraGoodsDao.findById(id);
                        if(optionalIntegralGoods.isPresent()){
                            IntegralGoods integralGoods=optionalIntegralGoods.get();
                            goodsItem=new GoodsItem(integralGoods.getId(),integralGoods.getName(),integralGoods.getImageUrl(),integralGoods.getStandard(),integralGoods.getIntegral());
                            goodsItems.add(goodsItem);
                        }
                    }
                }
            }
            if(goodsItems.size()>0) {
                Order order = orders.get(i);
                OrderResponse orderResponse = new OrderResponse(order.getId(), order.getUserId(), order.getAddress(), order.getMobilePone(), order.getPerson(), order.getType(), order.getRemark(), order.getFreight(), order.getPrice(), order.getDiscountPrice(), goodsItems, order.getBuyTime(), order.getStatus());
                orderResponses.add(orderResponse);
            }
        }
        return orderResponses;
    }

    @Override
    public List<OrderResponse> findByStatusAndShopId(String status, String shopId) {
        List<Order> orders=orderdoa.findByStatusOrderByBuyTimeAsc(status);
        List<OrderResponse> orderResponses=new ArrayList<>();
        for(int i=orders.size()-1;i>=0;i--){
            List<GoodsItem> goodsItems=new ArrayList<>();
            for(int j=0;j<orders.get(i).getGoodsList().size();j++){
                String id=orders.get(i).getGoodsList().get(j);
                GoodsItem goodsItem=new GoodsItem();
                Optional<Goods> optionalGoods=goodsDao.findById(id);
                if(optionalGoods.isPresent()){
                    Goods goods=optionalGoods.get();
                    if(goods.getShopId().equals(shopId)) {
                        goodsItem = new GoodsItem(goods.getId(), goods.getName(), goods.getImageUrl(), goods.getStandard(), goods.getPrice());
                        goodsItems.add(goodsItem);
                    }
                }
                else{
                    Optional<Goods2> optionalGoods2=goods2Dao.findById(id);
                    if(optionalGoods2.isPresent()){

                        Goods2 goods2=optionalGoods2.get();
                        if(goods2.getShopId().equals(shopId)) {
                            goodsItem = new GoodsItem(goods2.getId(), goods2.getName(), goods2.getImageUrl(), goods2.getStandard(), goods2.getPrice());
                            goodsItems.add(goodsItem);
                        }
                    }
                    else{
                        Optional<IntegralGoods> optionalIntegralGoods=integraGoodsDao.findById(id);
                        if(optionalIntegralGoods.isPresent()){
                            String userId=orders.get(i).getUserId();
                            Optional<User> optionalUser=userDao.findById(userId);
                            if(optionalUser.isPresent()){
                                User user=optionalUser.get();
                                if(user.getShopId().equals(shopId)){
                                    IntegralGoods integralGoods=optionalIntegralGoods.get();
                                    goodsItem=new GoodsItem(integralGoods.getId(),integralGoods.getName(),integralGoods.getImageUrl(),integralGoods.getStandard(),integralGoods.getIntegral());
                                    goodsItems.add(goodsItem);
                                }
                            }

                        }
                    }
                }
            }
            if(goodsItems.size()>0) {
                Order order = orders.get(i);
                OrderResponse orderResponse = new OrderResponse(order.getId(), order.getUserId(), order.getAddress(), order.getMobilePone(), order.getPerson(), order.getType(), order.getRemark(), order.getFreight(), order.getPrice(), order.getDiscountPrice(), goodsItems, order.getBuyTime(), order.getStatus());
                orderResponses.add(orderResponse);
            }
        }
        return orderResponses;
    }

    @Override
    public OrderResponse findByIdWX(String orderId) {
        Optional<Order> optionalOrder=orderdoa.findById(orderId);
        OrderResponse orderResponse=new OrderResponse();
        if(optionalOrder.isPresent()) {
            Order order=optionalOrder.get();
            List<GoodsItem> goodsItems = new ArrayList<>();
            for (int j = 0; j < order.getGoodsList().size(); j++) {
                String id = order.getGoodsList().get(j);
                GoodsItem goodsItem = new GoodsItem();
                Optional<Goods> optionalGoods = goodsDao.findById(id);
                if (optionalGoods.isPresent()) {
                    Goods goods = optionalGoods.get();
                    goodsItem = new GoodsItem(goods.getId(), goods.getName(), goods.getImageUrl(), goods.getStandard(),goods.getPrice());
                    goodsItems.add(goodsItem);
                } else {
                    Optional<Goods2> optionalGoods2 = goods2Dao.findById(id);
                    if (optionalGoods2.isPresent()) {
                        Goods2 goods2 = optionalGoods2.get();
                        goodsItem = new GoodsItem(goods2.getId(), goods2.getName(), goods2.getImageUrl(), goods2.getStandard(),goods2.getPrice());
                        goodsItems.add(goodsItem);
                    } else {
                        Optional<IntegralGoods> optionalIntegralGoods = integraGoodsDao.findById(id);
                        if (optionalIntegralGoods.isPresent()) {
                            IntegralGoods integralGoods = optionalIntegralGoods.get();
                            goodsItem = new GoodsItem(integralGoods.getId(), integralGoods.getName(), integralGoods.getImageUrl(), integralGoods.getStandard(),integralGoods.getIntegral());
                            goodsItems.add(goodsItem);
                        }
                    }
                }
            }
            orderResponse = new OrderResponse(order.getId(), order.getUserId(), order.getAddress(), order.getMobilePone(), order.getPerson(), order.getType(), order.getRemark(), order.getFreight(), order.getPrice(), order.getDiscountPrice(), goodsItems, order.getBuyTime(), order.getStatus());
        }
        return orderResponse;
    }


    @Value(value = "${wechat.order_url}")
    private String ORDER_URL;
    @Value(value = "${wechat.id}")
    private String APP_ID;
    @Value(value = "${wechat.mch_id}")
    private String MCH_ID;
    @Value(value = "${wechat.body}")
    private String BODY;
    @Value(value = "${wechat.device_info}")
    private String DEVICE_INFO;
    @Value(value = "${wechat.notify_url}")
    private String NOTIFY_URL;
    @Value(value = "${wechat.trade_type}")
    private String TRADE_TYPE;
    @Value(value = "${wechat.api_key}")
    private String API_KEY;
    @Value(value = "${wechat.sign_type}")
    private String SIGN_TYPE;

    @Override
    public void pay(Order o) throws NotExistException {

        String id=o.getId();
        double actualPrice=o.getPrice();
        List<String> goodsName=new ArrayList<>();
        Optional<Order> optionalOrder=orderdoa.findById(id);
        if (optionalOrder.isPresent()){
            String shopId="";
            o.setStatus("待发货");
            orderdoa.save(o);
            User user=userDao.getOne(o.getUserId());
            user.setBalance(user.getBalance()-actualPrice);
            if(user.getIdentity().equals("member")){
                user.setIntegral(user.getIntegral()+(int)actualPrice);

                userDao.save(user);
                double stock=0;
                double profit=0;
                List<String> goodsList=o.getGoodsList();
                for(int i=0;i<goodsList.size();i++){
                    Optional<Goods> optionalGoods=goodsDao.findById(goodsList.get(i));
                    if(optionalGoods.isPresent()){
                        Goods goods=optionalGoods.get();
                        goods.setNumber(goods.getNumber()-1);
                        goodsDao.save(goods);
                        goodsName.add(goods.getName());
                        shopId=goods.getShopId();
                        stock+=goods.getStockPrice();
                    }
                    else{
                        Optional<Goods2> optionalGoods2=goods2Dao.findById(goodsList.get(i));
                        if(optionalGoods2.isPresent()){
                            Goods2 goods2=optionalGoods2.get();
                            goods2.setNumber(goods2.getNumber()-1);
                            goods2Dao.save(goods2);
                            goodsName.add(goods2.getName());
                            shopId=goods2.getShopId();
                            stock+=goods2.getStockPrice();
                        }
                    }
                }
                Optional<Recommend> optionalRecommend=recommendDao.findByUser(o.getUserId());
                if(optionalRecommend.isPresent()){
                    Recommend recommend=optionalRecommend.get();
                    recommend.setStatus(true);
                    recommendDao.save(recommend);
                    Optional<Discount> optionalDiscount=discountDao.findByGoodsTypeAndShopId("1",shopId);
                    if(optionalDiscount.isPresent()){
                        Discount discount=optionalDiscount.get();
                        Coupon coupon=new Coupon(o.getUserId(),discount.getId(),"未使用",discount.getStartTime(),discount.getEndTime());
                        //couponDao.save(coupon);
                        couponService.addCoupon(coupon);
                    }
                }
                profit=actualPrice-stock;
                Balance balance=new Balance(user.getId(),user.getUsername(),"支出",actualPrice,"购买商品",FormatDateTime.toLongDateString(new Date()),goodsName);
                balanceDao.save(balance);
                Deduct deduct=new Deduct();
                Optional<Deduct> optionalDeduct=deductDao.findByShopId(shopId);
                if(optionalDeduct.isPresent()){
                    deduct=optionalDeduct.get();
                }
                if(user.getShareholderId().equals("")){

                    Optional<User> optionalStaff=userDao.findById(user.getRemark());
                    if(optionalStaff.isPresent()){
                        User staff=optionalStaff.get();
                        staff.setBalance(staff.getBalance()+profit*deduct.getStaffRatio());
                        Balance balance1=new Balance(staff.getId(),staff.getUsername(),"收入",profit*deduct.getStaffRatio(),"会员"+user.getUsername()+"购买商品",FormatDateTime.toLongDateString(new Date()),goodsName);
                        balanceDao.save(balance1);
                        userDao.save(staff);
                        Optional<Shop> optionalShop=shopDao.findById(shopId);
                        if(optionalShop.isPresent()){
                            Shop shop=optionalShop.get();
                            shop.setBalance(shop.getBalance()+stock+profit*(1-deduct.getStaffRatio()));
                            shopDao.save(shop);
                            ShopBalance shopBalance=new ShopBalance(shop.getId(),shop.getName(),"收入","",profit*(1-deduct.getStaffRatio())+stock,"会员"+user.getUsername()+"购买商品",FormatDateTime.toLongDateString(new Date()),goodsName);
                            shopBalanceDao.save(shopBalance);
                        }
                    }
                    else{
                        Optional<Shop> optionalShop=shopDao.findById(shopId);
                        if(optionalShop.isPresent()){
                            Shop shop=optionalShop.get();
                            shop.setBalance(shop.getBalance()+stock+profit);
                            shopDao.save(shop);
                            ShopBalance shopBalance=new ShopBalance(shop.getId(),shop.getName(),"收入","",profit+stock,"会员"+user.getUsername()+"购买商品",FormatDateTime.toLongDateString(new Date()),goodsName);
                            shopBalanceDao.save(shopBalance);
                        }
                    }

                }
                else{

                    Optional<User> optionalShareholder=userDao.findById(user.getShareholderId());
                    if(optionalShareholder.isPresent()){
                        System.out.println(profit);
                        System.out.println(deduct.getPersonal());
                        System.out.println(profit*deduct.getPersonal());
                        User shareholder=optionalShareholder.get();
                        shareholder.setBalance(shareholder.getBalance()+profit*deduct.getPersonal());
                        shareholder.setTakeBalance(shareholder.getTakeBalance()+profit*deduct.getTakeBalance());
                        userDao.save(shareholder);
                        Balance balance2=new Balance(shareholder.getId(),shareholder.getUsername(),"收入",profit*deduct.getPersonal()+profit*deduct.getTakeBalance(),"会员"+user.getUsername()+"购买商品",FormatDateTime.toLongDateString(new Date()),goodsName);
                        balanceDao.save(balance2);
                        Optional<Shop> optionalShop=shopDao.findById(shopId);
                        if(optionalShop.isPresent()){
                            Shop shop=optionalShop.get();
                            shop.setBalance(shop.getBalance()+stock+profit*(1-deduct.getTakeBalance()-deduct.getPersonal()));
                            shopDao.save(shop);
                            ShopBalance shopBalance=new ShopBalance(shop.getId(),shop.getName(),"收入","",stock+profit*(1-deduct.getTakeBalance()-deduct.getPersonal()),"会员"+user.getUsername()+"购买商品",FormatDateTime.toLongDateString(new Date()),goodsName);
                            shopBalanceDao.save(shopBalance);
                        }
                    }
                    else{
                        Optional<Shop> optionalShop=shopDao.findById(shopId);
                        if(optionalShop.isPresent()){
                            Shop shop=optionalShop.get();
                            shop.setBalance(shop.getBalance()+stock+profit);
                            shopDao.save(shop);
                            ShopBalance shopBalance=new ShopBalance(shop.getId(),shop.getName(),"收入","",stock+profit,"会员"+user.getUsername()+"购买商品",FormatDateTime.toLongDateString(new Date()),goodsName);
                            shopBalanceDao.save(shopBalance);
                        }
                    }

                }

            }
            else{
                userDao.save(user);
                double stock=0;
                double profit=0;
                List<String> goodsList=o.getGoodsList();
                for(int i=0;i<goodsList.size();i++){
                    Optional<Goods> optionalGoods=goodsDao.findById(goodsList.get(i));
                    if(optionalGoods.isPresent()){
                        Goods goods=optionalGoods.get();
                        goods.setNumber(goods.getNumber()-1);
                        goodsDao.save(goods);
                        goodsName.add(goods.getName());
                        shopId=goods.getShopId();
                        stock+=goods.getStockPrice();
                    }
                    else{
                        Optional<Goods2> optionalGoods2=goods2Dao.findById(goodsList.get(i));
                        if(optionalGoods2.isPresent()){
                            Goods2 goods2=optionalGoods2.get();
                            goods2.setNumber(goods2.getNumber()-1);
                            goods2Dao.save(goods2);
                            goodsName.add(goods2.getName());
                            shopId=goods2.getShopId();
                            stock+=goods2.getStockPrice();
                        }
                    }
                }
                profit=actualPrice-stock;
                Balance balance=new Balance(user.getId(),user.getUsername(),"支出",actualPrice,"购买商品",FormatDateTime.toLongDateString(new Date()),goodsName);
                balanceDao.save(balance);
                Optional<Shop> optionalShop=shopDao.findById(shopId);
                if(optionalShop.isPresent()){
                    Shop shop=optionalShop.get();
                    shop.setBalance(shop.getBalance()+profit+stock);
                    shopDao.save(shop);
                    ShopBalance shopBalance=new ShopBalance(shop.getId(),shop.getName(),"收入","",profit+stock,"人员"+user.getUsername()+"购买商品",FormatDateTime.toLongDateString(new Date()),goodsName);
                    shopBalanceDao.save(shopBalance);
                }
            }
        }else {
            throw new NotExistException("order ID", id);
        }
    }

    @Override
    public WxBuyCreditResponse paywx(Order order) {
        Optional<User> optionalUser=userDao.findById(order.getUserId());
        String openid=optionalUser.get().getOpenid();
        orderdoa.save(order);
        SortedMap<String, String> packageParams = new TreeMap<>();
        packageParams.put("appid", APP_ID);
        packageParams.put("mch_id", MCH_ID);
        packageParams.put("nonce_str", RandomUtil.generateNonceStr());//时间戳
        packageParams.put("body", BODY);//支付主体
        packageParams.put("out_trade_no", order.getId() + "");//BuyCredit表编号
        packageParams.put("total_fee", (int)(order.getPrice()*100) + "");//人民币价格
        packageParams.put("notify_url", NOTIFY_URL);//支付返回地址，服务器收到之后将订单状态从"waiting"改为"finished"或"failed"
        packageParams.put("trade_type", TRADE_TYPE);//这个api有，固定的
        packageParams.put("openid", openid);//openid
        //获取sign
        String sign = PayCommonUtil.createSign("UTF-8", packageParams, API_KEY);//最后这个是自己设置的32位密钥
        packageParams.put("sign", sign);

        //发送请求，得到含有prepay_id的XML
        String requestXML = PayCommonUtil.getRequestXml(packageParams);
        System.out.println(requestXML);
        String resXml = null;
        try {
            resXml = HttpUtil.postData("https://api.mch.weixin.qq.com/pay/unifiedorder", requestXML);
            System.out.println("resXml");
            System.out.println(resXml);
        } catch (SystemException e) {
            e.printStackTrace();
        }

        //根据微信回复填写给小程序的回复
        String waitingTimeStamp = String.valueOf(System.currentTimeMillis()); //回复给微信小程序的时间戳
        String nonceStr = null;
        try {
            nonceStr = XMLUtil.parserXmlToGetNonceStr(resXml);
        } catch (SystemException e) {
            e.printStackTrace();
        }
        String packageContent = null;
        try {
            packageContent = "prepay_id=" + XMLUtil.parserXmlToGetPrepayId(resXml);
        } catch (SystemException e) {
            e.printStackTrace();
        }
        String signType = SIGN_TYPE;
        String apiKey = API_KEY;
        SortedMap<String, String> sortedMap = new TreeMap<>();
        sortedMap.put("appId", APP_ID);
        sortedMap.put("timeStamp", waitingTimeStamp);
        sortedMap.put("nonceStr", nonceStr);
        sortedMap.put("package", packageContent);
        sortedMap.put("signType", signType);
        String paySign = PayCommonUtil.createSign("UTF-8", sortedMap, apiKey);
        return new WxBuyCreditResponse(new WxBuyCreditItem(order.getId(), waitingTimeStamp, nonceStr, packageContent, signType, paySign));
    }

    @Override
    public String getWxPayResult(HttpServletRequest request) {
        System.out.println("Wx notification arrived");
        try {
            InputStream inStream = request.getInputStream();
            int _buffer_size = 1024;
            if (inStream != null) {
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                byte[] tempBytes = new byte[_buffer_size];
                int count = -1;
                while ((count = inStream.read(tempBytes, 0, _buffer_size)) != -1) {
                    outStream.write(tempBytes, 0, count);
                }
                tempBytes = null;
                outStream.flush();
                String resultXML = new String(outStream.toByteArray(), StandardCharsets.UTF_8); //将流转换成字符串
                SortedMap<Object, Object> sortedMap = XMLUtil.getSortedMapFromXML(resultXML);
                if (PayCommonUtil.isTenpaySign("UTF-8", sortedMap, API_KEY)) {
                    Order o = orderdoa.getOne((String)sortedMap.get("out_trade_no"));

                    if (sortedMap.get("return_code").equals("SUCCESS")) {
                        if (sortedMap.get("result_code").equals("SUCCESS")) {

                            String id=o.getId();
                            double actualPrice=o.getPrice();
                            List<String> goodsName=new ArrayList<>();
                            Optional<Order> optionalOrder=orderdoa.findById(id);
                            if (optionalOrder.isPresent()){
                                String shopId="";
                                o.setStatus("待发货");
                                orderdoa.save(o);
                                User user=userDao.getOne(o.getUserId());

                                if(user.getIdentity().equals("member")){
                                    user.setIntegral(user.getIntegral()+(int)actualPrice);
                                    userDao.save(user);
                                    double stock=0;
                                    double profit=0;
                                    List<String> goodsList=o.getGoodsList();
                                    for(int i=0;i<goodsList.size();i++){
                                        Optional<Goods> optionalGoods=goodsDao.findById(goodsList.get(i));
                                        if(optionalGoods.isPresent()){
                                            Goods goods=optionalGoods.get();
                                            goods.setNumber(goods.getNumber()-1);
                                            goodsDao.save(goods);
                                            goodsName.add(goods.getName());
                                            shopId=goods.getShopId();
                                            stock+=goods.getStockPrice();
                                        }
                                        else{
                                            Optional<Goods2> optionalGoods2=goods2Dao.findById(goodsList.get(i));
                                            if(optionalGoods2.isPresent()){
                                                Goods2 goods2=optionalGoods2.get();
                                                goods2.setNumber(goods2.getNumber()-1);
                                                goods2Dao.save(goods2);
                                                goodsName.add(goods2.getName());
                                                shopId=goods2.getShopId();
                                                stock+=goods2.getStockPrice();
                                            }
                                        }
                                    }
                                    Optional<Recommend> optionalRecommend=recommendDao.findByUser(o.getUserId());
                                    if(optionalRecommend.isPresent()){
                                        Recommend recommend=optionalRecommend.get();
                                        recommend.setStatus(true);
                                        recommendDao.save(recommend);
                                        Optional<Discount> optionalDiscount=discountDao.findByGoodsTypeAndShopId("1",shopId);
                                        if(optionalDiscount.isPresent()){
                                            Discount discount=optionalDiscount.get();
                                            Coupon coupon=new Coupon(o.getUserId(),discount.getId(),"未使用",discount.getStartTime(),discount.getEndTime());
                                            //couponDao.save(coupon);
                                            couponService.addCoupon(coupon);
                                        }
                                    }

                                    profit=actualPrice-stock;
                                    Balance balance=new Balance(user.getId(),user.getUsername(),"支出",actualPrice,"购买商品",FormatDateTime.toLongDateString(new Date()),goodsName);
                                    balanceDao.save(balance);
                                    Deduct deduct=new Deduct();
                                    Optional<Deduct> optionalDeduct=deductDao.findByShopId(shopId);
                                    if(optionalDeduct.isPresent()){
                                        deduct=optionalDeduct.get();
                                    }
                                    if(user.getShareholderId().equals("")){

                                        Optional<User> optionalStaff=userDao.findById(user.getRemark());
                                        if(optionalStaff.isPresent()){
                                            User staff=optionalStaff.get();
                                            staff.setBalance(staff.getBalance()+profit*deduct.getStaffRatio());
                                            Balance balance1=new Balance(staff.getId(),staff.getUsername(),"收入",profit*deduct.getStaffRatio(),"会员"+user.getUsername()+"购买商品",FormatDateTime.toLongDateString(new Date()),goodsName);
                                            balanceDao.save(balance1);
                                            userDao.save(staff);
                                            Optional<Shop> optionalShop=shopDao.findById(shopId);
                                            if(optionalShop.isPresent()){
                                                Shop shop=optionalShop.get();
                                                shop.setBalance(shop.getBalance()+stock+profit*(1-deduct.getStaffRatio()));
                                                shopDao.save(shop);
                                                ShopBalance shopBalance=new ShopBalance(shop.getId(),shop.getName(),"收入","",profit*(1-deduct.getStaffRatio())+stock,"会员"+user.getUsername()+"购买商品",FormatDateTime.toLongDateString(new Date()),goodsName);
                                                shopBalanceDao.save(shopBalance);
                                            }
                                        }
                                        else{
                                            Optional<Shop> optionalShop=shopDao.findById(shopId);
                                            if(optionalShop.isPresent()){
                                                Shop shop=optionalShop.get();
                                                shop.setBalance(shop.getBalance()+stock+profit);
                                                shopDao.save(shop);
                                                ShopBalance shopBalance=new ShopBalance(shop.getId(),shop.getName(),"收入","",profit+stock,"会员"+user.getUsername()+"购买商品",FormatDateTime.toLongDateString(new Date()),goodsName);
                                                shopBalanceDao.save(shopBalance);
                                            }
                                        }

                                    }
                                    else{

                                        Optional<User> optionalShareholder=userDao.findById(user.getShareholderId());
                                        if(optionalShareholder.isPresent()){
                                            System.out.println(profit);
                                            System.out.println(deduct.getPersonal());
                                            System.out.println(profit*deduct.getPersonal());
                                            User shareholder=optionalShareholder.get();
                                            shareholder.setBalance(shareholder.getBalance()+profit*deduct.getPersonal());
                                            shareholder.setTakeBalance(shareholder.getTakeBalance()+profit*deduct.getTakeBalance());
                                            userDao.save(shareholder);
                                            Balance balance2=new Balance(shareholder.getId(),shareholder.getUsername(),"收入",profit*deduct.getPersonal()+profit*deduct.getTakeBalance(),"会员"+user.getUsername()+"购买商品",FormatDateTime.toLongDateString(new Date()),goodsName);
                                            balanceDao.save(balance2);
                                            Optional<Shop> optionalShop=shopDao.findById(shopId);
                                            if(optionalShop.isPresent()){
                                                Shop shop=optionalShop.get();
                                                shop.setBalance(shop.getBalance()+stock+profit*(1-deduct.getTakeBalance()-deduct.getPersonal()));
                                                shopDao.save(shop);
                                                ShopBalance shopBalance=new ShopBalance(shop.getId(),shop.getName(),"收入","",stock+profit*(1-deduct.getTakeBalance()-deduct.getPersonal()),"会员"+user.getUsername()+"购买商品",FormatDateTime.toLongDateString(new Date()),goodsName);
                                                shopBalanceDao.save(shopBalance);
                                            }
                                        }
                                        else{
                                            Optional<Shop> optionalShop=shopDao.findById(shopId);
                                            if(optionalShop.isPresent()){
                                                Shop shop=optionalShop.get();
                                                shop.setBalance(shop.getBalance()+stock+profit);
                                                shopDao.save(shop);
                                                ShopBalance shopBalance=new ShopBalance(shop.getId(),shop.getName(),"收入","",stock+profit,"会员"+user.getUsername()+"购买商品",FormatDateTime.toLongDateString(new Date()),goodsName);
                                                shopBalanceDao.save(shopBalance);
                                            }
                                        }

                                    }

                                }
                                else{
                                    userDao.save(user);
                                    double stock=0;
                                    double profit=0;
                                    List<String> goodsList=o.getGoodsList();
                                    for(int i=0;i<goodsList.size();i++){
                                        Optional<Goods> optionalGoods=goodsDao.findById(goodsList.get(i));
                                        if(optionalGoods.isPresent()){
                                            Goods goods=optionalGoods.get();
                                            goods.setNumber(goods.getNumber()-1);
                                            goodsDao.save(goods);
                                            goodsName.add(goods.getName());
                                            shopId=goods.getShopId();
                                            stock+=goods.getStockPrice();
                                        }
                                        else{
                                            Optional<Goods2> optionalGoods2=goods2Dao.findById(goodsList.get(i));
                                            if(optionalGoods2.isPresent()){
                                                Goods2 goods2=optionalGoods2.get();
                                                goods2.setNumber(goods2.getNumber()-1);
                                                goods2Dao.save(goods2);
                                                goodsName.add(goods2.getName());
                                                shopId=goods2.getShopId();
                                                stock+=goods2.getStockPrice();
                                            }
                                        }
                                    }
                                    profit=actualPrice-stock;
                                    Balance balance=new Balance(user.getId(),user.getUsername(),"支出",actualPrice,"购买商品",FormatDateTime.toLongDateString(new Date()),goodsName);
                                    balanceDao.save(balance);
                                    Optional<Shop> optionalShop=shopDao.findById(shopId);
                                    if(optionalShop.isPresent()){
                                        Shop shop=optionalShop.get();
                                        shop.setBalance(shop.getBalance()+profit+stock);
                                        shopDao.save(shop);
                                        ShopBalance shopBalance=new ShopBalance(shop.getId(),shop.getName(),"收入","",profit+stock,"人员"+user.getUsername()+"购买商品",FormatDateTime.toLongDateString(new Date()),goodsName);
                                        shopBalanceDao.save(shopBalance);
                                    }
                                }
                            }else {
                                throw new NotExistException("order ID", id);
                            }
                        } else {
                            System.out.println("错误！");
                        }
                    } else {
                        throw new Exception("微信支付后台通信标识为FAIL！");
                    }
                } else {
                    throw new Exception("微信支付后台通信签名校验失败！");
                }
            }
            //通知微信支付系统接收到信息
            return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            //如果失败返回错误，微信会再次发送支付信息
            return "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[ERROR]]></return_msg></xml>";
        }
    }

    @Override
    public WxBuyCreditResponse recharge(String id,double price) {
        User user=userDao.getOne(id);
        Balance balance=new Balance(user.getId(),user.getUsername(),"充值",price,"用户充值",FormatDateTime.toLongDateString(new Date()),new ArrayList<>());
        balanceDao.save(balance);
        SortedMap<String, String> packageParams = new TreeMap<>();
        packageParams.put("appid", APP_ID);
        packageParams.put("mch_id", MCH_ID);
        packageParams.put("nonce_str", RandomUtil.generateNonceStr());//时间戳
        packageParams.put("body", BODY);//支付主体
        packageParams.put("out_trade_no", balance.getId() + "");//BuyCredit表编号
        packageParams.put("total_fee", (int)(balance.getPrice()*100) + "");//人民币价格
        packageParams.put("notify_url", "https://www.shaoshanlu.com:3389/order/getWxPayResult2");//支付返回地址，服务器收到之后将订单状态从"waiting"改为"finished"或"failed"
        packageParams.put("trade_type", TRADE_TYPE);//这个api有，固定的
        packageParams.put("openid", user.getOpenid());//openid
        //获取sign
        String sign = PayCommonUtil.createSign("UTF-8", packageParams, API_KEY);//最后这个是自己设置的32位密钥
        packageParams.put("sign", sign);

        //发送请求，得到含有prepay_id的XML
        String requestXML = PayCommonUtil.getRequestXml(packageParams);
        System.out.println(requestXML);
        String resXml = null;
        try {
            resXml = HttpUtil.postData("https://api.mch.weixin.qq.com/pay/unifiedorder", requestXML);
            System.out.println("resXml");
            System.out.println(resXml);
        } catch (SystemException e) {
            e.printStackTrace();
        }

        //根据微信回复填写给小程序的回复
        String waitingTimeStamp = String.valueOf(System.currentTimeMillis()); //回复给微信小程序的时间戳
        String nonceStr = null;
        try {
            nonceStr = XMLUtil.parserXmlToGetNonceStr(resXml);
        } catch (SystemException e) {
            e.printStackTrace();
        }
        String packageContent = null;
        try {
            packageContent = "prepay_id=" + XMLUtil.parserXmlToGetPrepayId(resXml);
        } catch (SystemException e) {
            e.printStackTrace();
        }
        String signType = SIGN_TYPE;
        String apiKey = API_KEY;
        SortedMap<String, String> sortedMap = new TreeMap<>();
        sortedMap.put("appId", APP_ID);
        sortedMap.put("timeStamp", waitingTimeStamp);
        sortedMap.put("nonceStr", nonceStr);
        sortedMap.put("package", packageContent);
        sortedMap.put("signType", signType);
        String paySign = PayCommonUtil.createSign("UTF-8", sortedMap, apiKey);
        return new WxBuyCreditResponse(new WxBuyCreditItem(balance.getId(), waitingTimeStamp, nonceStr, packageContent, signType, paySign));
    }


    @Override
    public String getWxPayResult2(HttpServletRequest request) {
        System.out.println("Wx notification arrived");
        try {
            InputStream inStream = request.getInputStream();
            int _buffer_size = 1024;
            if (inStream != null) {
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                byte[] tempBytes = new byte[_buffer_size];
                int count = -1;
                while ((count = inStream.read(tempBytes, 0, _buffer_size)) != -1) {
                    outStream.write(tempBytes, 0, count);
                }
                tempBytes = null;
                outStream.flush();
                String resultXML = new String(outStream.toByteArray(), StandardCharsets.UTF_8); //将流转换成字符串
                SortedMap<Object, Object> sortedMap = XMLUtil.getSortedMapFromXML(resultXML);
                if (PayCommonUtil.isTenpaySign("UTF-8", sortedMap, API_KEY)) {
                    Balance balance = balanceDao.getOne((String)sortedMap.get("out_trade_no"));
                    if (sortedMap.get("return_code").equals("SUCCESS")) {
                        if (sortedMap.get("result_code").equals("SUCCESS")) {
                            User user=userDao.getOne(balance.getUserId());
                            user.setBalance(user.getBalance()+balance.getPrice());
                            userDao.save(user);
                        } else {
                            System.out.println("错误！");
                        }
                    } else {
                        throw new Exception("微信支付后台通信标识为FAIL！");
                    }
                } else {
                    throw new Exception("微信支付后台通信签名校验失败！");
                }
            }
            //通知微信支付系统接收到信息
            return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            //如果失败返回错误，微信会再次发送支付信息
            return "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[ERROR]]></return_msg></xml>";
        }
    }

}
