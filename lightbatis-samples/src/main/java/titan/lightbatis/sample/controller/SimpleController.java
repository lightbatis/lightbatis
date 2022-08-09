package titan.lightbatis.sample.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import titan.lightbatis.common.BaseController;
import titan.lightbatis.common.CommonResult;
import titan.lightbatis.mapper.MapperManager;
import titan.lightbatis.result.Page;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/simple")
@Api("数据层测试管理")
@Slf4j
public class SimpleController extends BaseController {
    @Autowired
    private SqlSessionTemplate sessionTemplate = null;
    @Autowired
    private MapperManager mapperManager = null;
    @ApiOperation(value = "查询")
    @PostMapping("query")
    public CommonResult<List> query(@RequestParam String id, @RequestBody(required = false) JSONObject params) {
       System.out.println("================== " +  sessionTemplate.getConfiguration());
        Map<String,Object> queryParams = new HashMap<>();
        if (params != null) {
            queryParams.putAll(params);
        }
        RowBounds bounds = new RowBounds(0,10);
        List<?> userList = sessionTemplate.selectList(id, queryParams, Page.newPage(1));
        return success(userList);
    }

    @ApiOperation(value = "添加一个 Mapper")
    @PostMapping("/save/mapper")
    public CommonResult addMapper(@RequestParam(value = "id") String id, @RequestBody() String query) {
        try {
            mapperManager.addSelectMapper(id, query,  null);
        } catch (IOException e) {
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
        return success(MapperManager.DefaultNamespace + "." + id);
    }

}
