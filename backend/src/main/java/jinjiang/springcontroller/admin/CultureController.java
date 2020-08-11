package jinjiang.springcontroller.admin;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import jinjiang.blservice.admin.CultureService;
import jinjiang.entity.admin.Admin;
import jinjiang.entity.admin.Culture;
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
@RequestMapping("/culture")
public class CultureController {

    private final CultureService cultureService;

    @Autowired
    public CultureController(CultureService cultureService) {
        this.cultureService = cultureService;
    }

    @ApiOperation(value = "", notes = "")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Result> addUser(@Valid @RequestBody Culture culture) throws NotExistException {
        cultureService.addCulture(culture);
        return new ResponseEntity<>(ResultGenerator.genSuccessResult(), HttpStatus.OK);
    }

    @ApiOperation(value = "", notes = "")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Result> deleteUser(@RequestParam("id") String id) throws NotExistException {
        cultureService.deleteCulture(id);
        return new ResponseEntity<>(ResultGenerator.genSuccessResult(),HttpStatus.OK);
    }

    @ApiOperation(value = "修改管理员", notes = "修改管理员")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Result> updateUser(@Valid @RequestBody Culture culture) throws NotExistException {
        cultureService.updateCulture(culture);
        return new ResponseEntity<>(ResultGenerator.genSuccessResult(),HttpStatus.OK);
    }

    @ApiOperation(value = "", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/find/id", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Result> findUserById(@RequestParam("id") String id) throws NotExistException {
        Map<String, Object> result = new HashMap<>();
        result.put("items",cultureService.findById(id));
        return new ResponseEntity<>(ResultGenerator.genSuccessResult(result),HttpStatus.OK);
    }

    @ApiOperation(value = "所有管理员", notes = "")
    @RequestMapping(value = "/find/type", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Result> findByType(@RequestParam("type") String type,Pageable pageable) {
        Map<String, Object> result = new HashMap<>();
        result.put("items",cultureService.findByType(type,pageable));
        return new ResponseEntity<>(ResultGenerator.genSuccessResult(result),HttpStatus.OK);
    }

    @ApiOperation(value = "所有管理员", notes = "")
    @RequestMapping(value = "/find/all", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Result> getAllUser(Pageable pageable) {
        Map<String, Object> result = new HashMap<>();
        result.put("items",cultureService.findAll(pageable));
        return new ResponseEntity<>(ResultGenerator.genSuccessResult(result),HttpStatus.OK);
    }


    @ApiOperation(value = "所有管理员", notes = "")
    @RequestMapping(value = "/find/query", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Result> get(@RequestParam("query") String query,Pageable pageable) {
        Map<String, Object> result = new HashMap<>();
        result.put("items",cultureService.find(query, pageable));
        return new ResponseEntity<>(ResultGenerator.genSuccessResult(result),HttpStatus.OK);
    }

}
