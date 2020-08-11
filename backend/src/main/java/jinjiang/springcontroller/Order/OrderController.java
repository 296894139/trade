package jinjiang.springcontroller.Order;


import io.swagger.annotations.*;
import jinjiang.blservice.account.UserBlService;
import jinjiang.blservice.order.OrderBlService;
import jinjiang.entity.account.User;
import jinjiang.entity.order.Order;
import jinjiang.exception.NotExistException;
import jinjiang.response.Response;
import jinjiang.response.Result;
import jinjiang.response.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderBlService orderBlService;

    @Autowired
    public OrderController(OrderBlService orderBlService) {
        this.orderBlService = orderBlService;
    }

    @ApiOperation(value = "新增订单", notes = "新增订单")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Result> addUser(@Valid @RequestBody Order order) throws NotExistException {
        Map<String, Object> result = new HashMap<>();
        result.put("items",orderBlService.addOrder(order));
        return new ResponseEntity<>(ResultGenerator.genSuccessResult(result), HttpStatus.OK);
    }

    @ApiOperation(value = "删除订单", notes = "删除订单")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Result> deleteUser(@RequestParam("id") String id) throws NotExistException {
        orderBlService.deleteOrder(id);
        return new ResponseEntity<>(ResultGenerator.genSuccessResult(),HttpStatus.OK);
    }

    @ApiOperation(value = "修改订单", notes = "修改订单")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Result> updateUser(@Valid @RequestBody Order order) throws NotExistException {
        orderBlService.updateOrder(order);
        return new ResponseEntity<>(ResultGenerator.genSuccessResult(),HttpStatus.OK);
    }

    @ApiOperation(value = "根据id查找订单", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/find/id", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Result> findUserById(@RequestParam("id") String id) throws NotExistException {
        Map<String, Object> result = new HashMap<>();
        result.put("items",orderBlService.findById(id));
        return new ResponseEntity<>(ResultGenerator.genSuccessResult(result),HttpStatus.OK);
    }

    @ApiOperation(value = "根据id查找订单", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/find/id/wx", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Result> findById(@RequestParam("id") String id) throws NotExistException {
        Map<String, Object> result = new HashMap<>();
        result.put("items",orderBlService.findByIdWX(id));
        return new ResponseEntity<>(ResultGenerator.genSuccessResult(result),HttpStatus.OK);
    }

    @ApiOperation(value = "根据id查找订单", notes = "")
    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Result> pay(@Valid @RequestBody Order order) throws NotExistException {
        Map<String, Object> result = new HashMap<>();
        orderBlService.pay(order);
        return new ResponseEntity<>(ResultGenerator.genSuccessResult(result),HttpStatus.OK);
    }

    @ApiOperation(value = "根据id查找订单", notes = "")
    @RequestMapping(value = "/back", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Result> back(@RequestParam("id") String id) throws NotExistException {
        Map<String, Object> result = new HashMap<>();
        orderBlService.back(id);
        return new ResponseEntity<>(ResultGenerator.genSuccessResult(result),HttpStatus.OK);
    }

    @ApiOperation(value = "根据id查找订单", notes = "")
    @RequestMapping(value = "/refund", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Result> refund(@RequestParam("id") String id) throws NotExistException {
        Map<String, Object> result = new HashMap<>();
        orderBlService.refund(id);
        return new ResponseEntity<>(ResultGenerator.genSuccessResult(result),HttpStatus.OK);
    }


    @ApiOperation(value = "所有订单", notes = "")
    @RequestMapping(value = "/find/all", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Result> getAllUser(Pageable pageable) {
        Map<String, Object> result = new HashMap<>();
        result.put("items",orderBlService.findAll(pageable));
        return new ResponseEntity<>(ResultGenerator.genSuccessResult(result),HttpStatus.OK);
    }

    @ApiOperation(value = "所有订单", notes = "")
    @RequestMapping(value = "/find/all/wx", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Result> getAll(@RequestParam("userId") String userId) {
        Map<String, Object> result = new HashMap<>();
        result.put("items",orderBlService.findAllWX(userId));
        return new ResponseEntity<>(ResultGenerator.genSuccessResult(result),HttpStatus.OK);
    }

    @ApiOperation(value = "所有订单", notes = "")
    @RequestMapping(value = "/find/status/wx", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Result> findByStatus(@RequestParam("userId") String userId,@RequestParam("status") String status) {
        Map<String, Object> result = new HashMap<>();
        result.put("items",orderBlService.findByStatusWX(userId,status));
        return new ResponseEntity<>(ResultGenerator.genSuccessResult(result),HttpStatus.OK);
    }

    @ApiOperation(value = "所有订单", notes = "")
    @RequestMapping(value = "/find/status/shopId", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Result> findByStatusAndShopId(@RequestParam("status") String status,@RequestParam("shopId") String shopId) {
        Map<String, Object> result = new HashMap<>();
        result.put("items",orderBlService.findByStatusAndShopId(status, shopId));
        return new ResponseEntity<>(ResultGenerator.genSuccessResult(result),HttpStatus.OK);
    }

    @ApiOperation(value = "所有订单", notes = "")
    @RequestMapping(value = "/find/all/admin", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Result> findAll() {
        Map<String, Object> result = new HashMap<>();
        result.put("items",orderBlService.findAll());
        return new ResponseEntity<>(ResultGenerator.genSuccessResult(result),HttpStatus.OK);
    }

    @ApiOperation(value = "所有订单", notes = "")
    @RequestMapping(value = "/find/status", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Result> findByStatus(@RequestParam("status") String status) {
        Map<String, Object> result = new HashMap<>();
        result.put("items",orderBlService.findByStatus(status));
        return new ResponseEntity<>(ResultGenerator.genSuccessResult(result),HttpStatus.OK);
    }

    @ApiOperation(value = "所有订单", notes = "")
    @RequestMapping(value = "/find/shopId", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Result> findByShopId(@RequestParam("shopId") String shopId) {
        Map<String, Object> result = new HashMap<>();
        result.put("items",orderBlService.findByShopId(shopId));
        return new ResponseEntity<>(ResultGenerator.genSuccessResult(result),HttpStatus.OK);
    }

    @ApiOperation(value = "", notes = "")
    @RequestMapping(value = "/cancel", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Result> cancel(@RequestParam("id") String id) throws NotExistException {
        orderBlService.cancel(id);
        return new ResponseEntity<>(ResultGenerator.genSuccessResult(),HttpStatus.OK);
    }

    @ApiOperation(value = "", notes = "")
    @RequestMapping(value = "/send", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Result> send(@RequestParam("id") String id) throws NotExistException {
        orderBlService.send(id);
        return new ResponseEntity<>(ResultGenerator.genSuccessResult(),HttpStatus.OK);
    }

    @ApiOperation(value = "", notes = "")
    @RequestMapping(value = "/take", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Result> take(@RequestParam("id") String id) throws NotExistException {
        orderBlService.take(id);
        return new ResponseEntity<>(ResultGenerator.genSuccessResult(),HttpStatus.OK);
    }

    @ApiOperation(value = "", notes = "")
    @RequestMapping(value = "/integralSend", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Result> integralSend(@RequestParam("id") String id) throws NotExistException {
        orderBlService.integralSend(id);
        return new ResponseEntity<>(ResultGenerator.genSuccessResult(),HttpStatus.OK);
    }

    @ApiOperation(value = "", notes = "")
    @RequestMapping(value = "/integralTake", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Result> integralTake(@RequestParam("id") String id) throws NotExistException {
        orderBlService.integralTake(id);
        return new ResponseEntity<>(ResultGenerator.genSuccessResult(),HttpStatus.OK);
    }

    @ApiOperation(value = "", notes = "")
    @RequestMapping(value = "/pass", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Result> pass(@RequestParam("id") String id) throws NotExistException {
        orderBlService.pass(id);
        return new ResponseEntity<>(ResultGenerator.genSuccessResult(),HttpStatus.OK);
    }

    @ApiOperation(value = "用户通过微信支付购买积分", notes = "用户通过微信支付购买积分")
    @RequestMapping(value = "/buyMyCredit", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Response> buyMyCredit(@Valid @RequestBody Order order) {
        return new ResponseEntity<>(orderBlService.paywx(order), HttpStatus.OK);
    }

    @ApiOperation(value = "此接口用户接收微信支付后台的支付结果通知", notes = "此接口用户接收微信支付后台的支付结果通知")
    @RequestMapping(value = "/getWxPayResult", method = RequestMethod.POST)
    @ResponseBody
    public String getWxPayResult(HttpServletRequest httpServletRequest) {
        return orderBlService.getWxPayResult(httpServletRequest);
    }

    @ApiOperation(value = "用户通过微信支付购买积分", notes = "用户通过微信支付购买积分")
    @RequestMapping(value = "/recharge", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Response> recharge(@RequestParam("id") String id,@RequestParam("price") double price) {
        return new ResponseEntity<>(orderBlService.recharge(id,price), HttpStatus.OK);
    }

    @ApiOperation(value = "此接口用户接收微信支付后台的支付结果通知", notes = "此接口用户接收微信支付后台的支付结果通知")
    @RequestMapping(value = "/getWxPayResult2", method = RequestMethod.POST)
    @ResponseBody
    public String getWxPayResult2(HttpServletRequest httpServletRequest) {
        return orderBlService.getWxPayResult2(httpServletRequest);
    }
}
