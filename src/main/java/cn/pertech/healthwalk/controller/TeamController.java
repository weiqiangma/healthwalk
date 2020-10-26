package cn.pertech.healthwalk.controller;

import cn.pertech.common.abs.BaseController;
import cn.pertech.common.spring.JsonResult;
import cn.pertech.common.utils.DateUtils;
import cn.pertech.common.utils.RequestUtils;
import cn.pertech.common.utils.StringUtils;
import cn.pertech.healthwalk.base.data.UserSession;
import cn.pertech.healthwalk.base.data.query.StatQuery;
import cn.pertech.healthwalk.base.data.vo.TeamStatVo;
import cn.pertech.healthwalk.base.data.vo.UserStatVo;
import cn.pertech.healthwalk.base.entity.Team;
import cn.pertech.healthwalk.base.entity.User;
import cn.pertech.healthwalk.base.service.TeamService;
import cn.pertech.healthwalk.service.TeamServiceExt;
import cn.pertech.healthwalk.service.UserServiceExt;
import cn.pertech.healthwalk.spring.annotation.LoginedAuth;
import cn.pertech.healthwalk.utils.PageUtils;
import cn.pertech.healthwalk.utils.TimeUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/team")
public class TeamController extends BaseController {
    
    @Resource
    private TeamServiceExt teamServiceExt;
    @Resource
    private UserServiceExt userServiceExt;

    @RequestMapping("/getTeamRank")
    public JsonResult getTeamRank(@LoginedAuth UserSession session, Integer type, Integer pageNum, Integer pageSize) {
        if(pageNum == null) pageNum = 1;
        if(pageSize == null) pageSize = 10;
        User user = new User();
        if(session.getId() > 0) {
            user = userServiceExt.getById(session.getId());
        }
        StatQuery query = createQueryStateVo();
        List<TeamStatVo> list = teamServiceExt.getTeamRank(user, query);
        PageInfo<TeamStatVo> pageInfo = PageUtils.list2PageInfo(pageNum, pageSize, list);
        return sendSuccess(pageInfo);
    }

    private StatQuery createQueryStateVo() {
        int type = getIntPar("type",1);
        int kind = getIntPar("kind", 1);
        String format = "%H";
        Date start;
        Date end;

        if(type ==  1) {
            start = new Date();
            end = DateUtils.addDay(start, 1);
        } else if(type == 2) {
            start = TimeUtils.getMonthStart();
            end = TimeUtils.getMonthEnd();
        } else if(type == 3) {
            start = TimeUtils.getCurrentQuarterStartTime();
            end = TimeUtils.getCurrentQuarterEndTime();
        } else {
            start = TimeUtils.getCurrentYearStartTime();
            end = TimeUtils.getCurrentYearEndTime();
        }
        StatQuery query = new StatQuery();
        query.setTimeStart(start);
        query.setTimeEnd(end);
        query.setType(type);
        query.setKind(kind);
        query.setFormatCode(format);
        return query;
    }
}