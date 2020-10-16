package cn.pertech.healthwalk.controller;

import cn.pertech.common.abs.BaseController;
import cn.pertech.common.spring.JsonResult;
import cn.pertech.healthwalk.base.data.UserSession;
import cn.pertech.healthwalk.base.data.query.IntegralLogQuery;
import cn.pertech.healthwalk.base.entity.IntegralLog;
import cn.pertech.healthwalk.base.service.IntegralLogService;
import cn.pertech.healthwalk.service.IntegralLogServiceExt;
import cn.pertech.healthwalk.spring.annotation.LoginedAuth;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@RestController
@RequestMapping("/api/integralLog")
public class IntegralLogController extends BaseController {
    
    @Autowired
    private IntegralLogServiceExt integralLogServiceExt;

    @GetMapping("/get")
    public JsonResult getByEntity(IntegralLog integralLog) {
        IntegralLog resultIntegeralLog = integralLogServiceExt.getByEntity(integralLog);
        return sendSuccess(resultIntegeralLog);
    }

    @RequestMapping("/list")
    public JsonResult list(@LoginedAuth UserSession session, IntegralLogQuery query) {
        if(session.getId() > 0) query.setUserId(session.getId());
        PageInfo<IntegralLog> pageInfo = integralLogServiceExt.list(query);
        return sendSuccess(pageInfo);
    }

    @PostMapping("/insert")
    public JsonResult insert(@RequestBody IntegralLog integralLog){
        int result = integralLogServiceExt.insert(integralLog);
        return sendSuccess(result);
    }


}