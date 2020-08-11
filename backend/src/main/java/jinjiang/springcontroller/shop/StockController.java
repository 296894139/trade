package jinjiang.springcontroller.shop;


import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import jinjiang.blservice.shop.StockBlService;
import jinjiang.entity.shop.ShopBalance;
import jinjiang.entity.shop.Stock;
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
@RequestMapping("/stock")
public class StockController {
    private final StockBlService stockBlService;

        @Autowired
        public StockController(StockBlService stockBlService) {
            this.stockBlService = stockBlService;
        }

    //ok
        @ApiOperation(value = "新增地址", notes = "新增地址")
        @RequestMapping(value = "/add", method = RequestMethod.POST)
        @ResponseBody
        public ResponseEntity<Result> addUser(@Valid @RequestBody Stock stock) throws NotExistException {
            Map<String, Object> result = new HashMap<>();
            result.put("items",stockBlService.addStock(stock));
            return new ResponseEntity<>(ResultGenerator.genSuccessResult(result),HttpStatus.OK);
        }
        //ok
        @ApiOperation(value = "删除地址", notes = "删除地址")
        @RequestMapping(value = "/delete", method = RequestMethod.GET)
        @ResponseBody
        public ResponseEntity<Result> deleteUserById(@RequestParam("id") String id) throws NotExistException {
            stockBlService.deleteStock(id);
            return new ResponseEntity<>(ResultGenerator.genSuccessResult(), HttpStatus.OK);
        }

        //接通
        @ApiOperation(value = "修改地址", notes = "修改地址")
        @RequestMapping(value = "/update", method = RequestMethod.PUT)
        @ResponseBody
        public ResponseEntity<Result> updateUserById(@Valid @RequestBody Stock stock) throws NotExistException {
           stockBlService.updateStock(stock);
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
            result.put("items",stockBlService.findById(id));
            return new ResponseEntity<>(ResultGenerator.genSuccessResult(result),HttpStatus.OK);
        }

    @ApiOperation(value = "根据地址id查找地址详情", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/send", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Result> send(@RequestParam("id") String id) throws NotExistException {
        stockBlService.send(id);
        return new ResponseEntity<>(ResultGenerator.genSuccessResult(),HttpStatus.OK);
    }

    @ApiOperation(value = "根据地址id查找地址详情", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/grounding", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Result> grounding(@RequestParam("id") String id) throws NotExistException {
        stockBlService.grounding(id);
        return new ResponseEntity<>(ResultGenerator.genSuccessResult(),HttpStatus.OK);
    }

    @ApiOperation(value = "根据地址id查找地址详情", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/take", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Result> take(@RequestParam("id") String id) throws NotExistException {
        stockBlService.take(id);
        return new ResponseEntity<>(ResultGenerator.genSuccessResult(),HttpStatus.OK);
    }


    @ApiOperation(value = "根据地址id查找地址详情", notes = "")
    @RequestMapping(value = "/refund", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Result> refund(@RequestParam("id") String id) throws NotExistException {
        stockBlService.refund(id);
        return new ResponseEntity<>(ResultGenerator.genSuccessResult(),HttpStatus.OK);
    }


    @ApiOperation(value = "根据地址id查找地址详情", notes = "")
    @RequestMapping(value = "/back", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Result> back(@RequestParam("id") String id) throws NotExistException {
        stockBlService.back(id);
        return new ResponseEntity<>(ResultGenerator.genSuccessResult(),HttpStatus.OK);
    }

    @ApiOperation(value = "根据地址id查找地址详情", notes = "")
    @RequestMapping(value = "/find/status/shopId", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Result> getUserByUserId(@RequestParam("status") String status,@RequestParam("shopId") String shopId) throws NotExistException {
        Map<String, Object> result = new HashMap<>();
        result.put("items",stockBlService.findByTypeAndShopId(status,shopId));
        return new ResponseEntity<>(ResultGenerator.genSuccessResult(result),HttpStatus.OK);
    }

        //接通
        @ApiOperation(value = "所有地址", notes = "")
        @RequestMapping(value = "/find/all", method = RequestMethod.GET)
        @ResponseBody
        public ResponseEntity<Result> getAllUser(Pageable pageable) {
            Map<String, Object> result = new HashMap<>();
            result.put("items",stockBlService.findAll(pageable));
            return new ResponseEntity<>(ResultGenerator.genSuccessResult(result),HttpStatus.OK);
        }

    //接通
    @ApiOperation(value = "所有地址", notes = "")
    @RequestMapping(value = "/find/shopId", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Result> getAllUser(@RequestParam("shopId") String shopId,Pageable pageable) {
        Map<String, Object> result = new HashMap<>();
        result.put("items",stockBlService.findByShopId(shopId,pageable));
        return new ResponseEntity<>(ResultGenerator.genSuccessResult(result),HttpStatus.OK);
    }




}
