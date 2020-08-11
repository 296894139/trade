package jinjiang.springcontroller.shop;


import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import jinjiang.blservice.shop.OffLineBlService;
import jinjiang.entity.shop.OffLine;
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
@RequestMapping("/offLine")
public class OffLineController {
    private final OffLineBlService offLineBlService;

        @Autowired
        public OffLineController(OffLineBlService offLineBlService) {

            this.offLineBlService = offLineBlService;
        }

    //ok
        @ApiOperation(value = "新增地址", notes = "新增地址")
        @RequestMapping(value = "/add", method = RequestMethod.POST)
        @ResponseBody
        public ResponseEntity<Result> addUser(@Valid @RequestBody OffLine offLine) throws NotExistException {
            Map<String, Object> result = new HashMap<>();
            result.put("items",offLineBlService.addOffLine(offLine));
            return new ResponseEntity<>(ResultGenerator.genSuccessResult(result),HttpStatus.OK);
        }
        //ok
        @ApiOperation(value = "删除地址", notes = "删除地址")
        @RequestMapping(value = "/delete", method = RequestMethod.GET)
        @ResponseBody
        public ResponseEntity<Result> deleteUserById(@RequestParam("id") String id) throws NotExistException {
            offLineBlService.deleteOffLine(id);
            return new ResponseEntity<>(ResultGenerator.genSuccessResult(), HttpStatus.OK);
        }

        //接通
        @ApiOperation(value = "修改地址", notes = "修改地址")
        @RequestMapping(value = "/update", method = RequestMethod.PUT)
        @ResponseBody
        public ResponseEntity<Result> updateUserById(@Valid @RequestBody OffLine offLine) throws NotExistException {
           offLineBlService.updateOffLine(offLine);
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
            result.put("items",offLineBlService.findById(id));
            return new ResponseEntity<>(ResultGenerator.genSuccessResult(result),HttpStatus.OK);
        }



    @ApiOperation(value = "根据地址id查找地址详情", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/pass", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Result> pass(@RequestParam("id") String id) throws NotExistException {
        offLineBlService.pass(id);
        return new ResponseEntity<>(ResultGenerator.genSuccessResult(),HttpStatus.OK);
    }



    @ApiOperation(value = "根据地址id查找地址详情", notes = "")
    @RequestMapping(value = "/find/shopId", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Result> getUserByUserId(@RequestParam("shopId") String shopId) throws NotExistException {
        Map<String, Object> result = new HashMap<>();
        result.put("items",offLineBlService.findByShopId(shopId));
        return new ResponseEntity<>(ResultGenerator.genSuccessResult(result),HttpStatus.OK);
    }

        //接通
        @ApiOperation(value = "所有地址", notes = "")
        @RequestMapping(value = "/find/all", method = RequestMethod.GET)
        @ResponseBody
        public ResponseEntity<Result> getAllUser(Pageable pageable) {
            Map<String, Object> result = new HashMap<>();
            result.put("items",offLineBlService.findAll(pageable));
            return new ResponseEntity<>(ResultGenerator.genSuccessResult(result),HttpStatus.OK);
        }

}
