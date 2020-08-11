package jinjiang.springcontroller.shop;


import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import jinjiang.blservice.shop.ShopBalanceBlService;
import jinjiang.entity.account.Balance;
import jinjiang.entity.shop.ShopBalance;
import jinjiang.exception.NotExistException;
import jinjiang.response.Result;
import jinjiang.response.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/shopBalance")
public class ShopBalanceController {
    private final ShopBalanceBlService shopBalanceBlService;

        @Autowired
        public ShopBalanceController(ShopBalanceBlService shopBalanceBlService) {
            this.shopBalanceBlService = shopBalanceBlService;
        }

    //ok
        @ApiOperation(value = "新增地址", notes = "新增地址")
        @RequestMapping(value = "/add", method = RequestMethod.POST)
        @ResponseBody
        public ResponseEntity<Result> addUser(@Valid @RequestBody ShopBalance shopBalance) throws NotExistException {
            Map<String, Object> result = new HashMap<>();
            result.put("items",shopBalanceBlService.addShopBalance(shopBalance));
            return new ResponseEntity<>(ResultGenerator.genSuccessResult(result),HttpStatus.OK);
        }
        //ok
        @ApiOperation(value = "删除地址", notes = "删除地址")
        @RequestMapping(value = "/delete", method = RequestMethod.GET)
        @ResponseBody
        public ResponseEntity<Result> deleteUserById(@RequestParam("id") String id) throws NotExistException {
            shopBalanceBlService.deleteShopBalance(id);
            return new ResponseEntity<>(ResultGenerator.genSuccessResult(), HttpStatus.OK);
        }

        //接通
        @ApiOperation(value = "修改地址", notes = "修改地址")
        @RequestMapping(value = "/update", method = RequestMethod.PUT)
        @ResponseBody
        public ResponseEntity<Result> updateUserById(@Valid @RequestBody ShopBalance shopBalance) throws NotExistException {
            shopBalanceBlService.updateShopBalance(shopBalance);
            return new ResponseEntity<>(ResultGenerator.genSuccessResult(),HttpStatus.OK);
        }

        //接通
        @ApiOperation(value = "根据地址id查找地址详情", notes = "")
        @ApiImplicitParams({
                @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "String")
        })
        @RequestMapping(value = "/find/id", method = RequestMethod.GET)
        @ResponseBody
        public ResponseEntity<Result> getUserById(@RequestParam("id") String id) throws NotExistException {
            Map<String, Object> result = new HashMap<>();
            result.put("items",shopBalanceBlService.findById(id));
            return new ResponseEntity<>(ResultGenerator.genSuccessResult(result),HttpStatus.OK);
        }

    @ApiOperation(value = "根据地址id查找地址详情", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/pass", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Result> pass(@RequestParam("id") String id) throws NotExistException {
        shopBalanceBlService.pass(id);
        return new ResponseEntity<>(ResultGenerator.genSuccessResult(),HttpStatus.OK);
    }

    @ApiOperation(value = "根据地址id查找地址详情", notes = "")
    @RequestMapping(value = "/find/type/shopId", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Result> getUserByUserId(@RequestParam("type") String type,@RequestParam("shopId") String shopId) throws NotExistException {
        Map<String, Object> result = new HashMap<>();
        result.put("items",shopBalanceBlService.findByTypeAndShopId(type,shopId));
        return new ResponseEntity<>(ResultGenerator.genSuccessResult(result),HttpStatus.OK);
    }

    @ApiOperation(value = "根据地址id查找地址详情", notes = "")
    @RequestMapping(value = "/find/type", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Result> getUserByType(@RequestParam("type") String type) throws NotExistException {
        Map<String, Object> result = new HashMap<>();
        result.put("items",shopBalanceBlService.findByType(type));
        return new ResponseEntity<>(ResultGenerator.genSuccessResult(result),HttpStatus.OK);
    }

        //接通
        @ApiOperation(value = "所有地址", notes = "")
        @RequestMapping(value = "/find/all", method = RequestMethod.GET)
        @ResponseBody
        public ResponseEntity<Result> getAllUser(Pageable pageable) {
            Map<String, Object> result = new HashMap<>();
            result.put("items",shopBalanceBlService.findAll(pageable));
            return new ResponseEntity<>(ResultGenerator.genSuccessResult(result),HttpStatus.OK);
        }


       /* @ApiOperation(value = "修改默认地址", notes = "")
        @RequestMapping(value = "/setDefaultAddress", method = RequestMethod.GET)
        @ResponseBody
        public ResponseEntity<Result> setDefaultAddress(@RequestParam("userId")String userId,@RequestParam("addressId")String addressId) throws NotExistException {
            addressBlService.setDefaultAddress(userId,addressId);
            return new ResponseEntity<>(ResultGenerator.genSuccessResult(),HttpStatus.OK);
        }*/



}
