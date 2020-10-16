package cn.pertech.healthwalk.controller;

import cn.pertech.common.abs.BaseController;
import cn.pertech.common.spring.JsonResult;
import cn.pertech.healthwalk.base.entity.WalkLog;
import cn.pertech.healthwalk.base.service.WalkLogService;
import cn.pertech.healthwalk.service.WxApiServiceExt;
import cn.pertech.healthwalk.utils.WechatDecryptDataUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@RestController
@RequestMapping("/api/walkLog")
public class WalkLogController extends BaseController {
    @Value("${wx.AppId}")
    private String appId;
    
    @Autowired
    private WalkLogService walkLogService;
    @Autowired
    private WxApiServiceExt wxApiServiceExt;

    @PostMapping("/insert")
    public WalkLog insert(@RequestBody WalkLog walkLog){
        walkLogService.insert(walkLog);
        return walkLog;
    }

    @PutMapping("/update")
    public int update(@RequestBody WalkLog walkLog){
        return walkLogService.update(walkLog);
    }

    @DeleteMapping("/delete/{id}")
    public int deleteOne(@PathVariable Long id){
        return walkLogService.deleteById(id);
    }

    @DeleteMapping("/delete")
    public int deleteBatch(@RequestBody List<Long> ids){
        int result = 0;
        if (ids!=null&&ids.size()>0) result = walkLogService.deleteByIds(ids);
        return result;
    }

    @RequestMapping("/decrypt")
    public JsonResult decrypt(String encryptedData, String sessionKey, String iv) {
        String result = WechatDecryptDataUtil.decrypt(appId,encryptedData,sessionKey,iv);
        return sendSuccess(result);
    }

}