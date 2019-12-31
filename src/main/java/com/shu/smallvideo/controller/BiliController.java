package com.shu.smallvideo.controller;

import com.shu.smallvideo.base.ResponseMap;
import com.shu.smallvideo.model.User;
import com.shu.smallvideo.repository.UserRepository;
import com.shu.smallvideo.service.BiliService;
import com.shu.smallvideo.model.vo.PlayVo;
import com.shu.smallvideo.model.vo.VideoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author shuxibing
 * @date 2019/12/22 16:24
 * @uint d9lab
 * @Description:
 */
@RestController
@RequestMapping("/Bili")
@Api(tags = "Bili",description = "B站video的api",produces = MediaType.APPLICATION_JSON_VALUE)
public class BiliController {

    private static Logger logger= LoggerFactory.getLogger(BiliController.class);

    @Autowired
    private BiliService biliService;

    @Autowired
    private UserRepository userRepository;
    @ApiOperation("获取视频列表信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="username",value = "姓名",paramType ="form",dataType = "string",required = true)
    })
    @RequestMapping(value = "/getListByUserName",method = RequestMethod.POST)
    public List<VideoVo> getListByUserName(String username)throws Exception{
        logger.info(username);
        return biliService.getListByUserName(username);
    }

    @ApiOperation("更新数据库视频播放量")
    @ApiImplicitParams({
            @ApiImplicitParam(name="username",value = "姓名",paramType ="form",dataType = "string",required = true)
    })
    @RequestMapping(value = "/updatePlayByUserName",method = RequestMethod.POST)
    public List<VideoVo> updatePlayByUserName(String username)throws Exception{
        logger.info(username);
        return biliService.updatePlayByUserName(username);
    }

    @ApiOperation("获取每月播放量")
    @ApiImplicitParams({
            @ApiImplicitParam(name="username",value = "姓名",paramType ="form",dataType = "string",required = true),
            @ApiImplicitParam(name="userId",value = "用户id",paramType ="form",dataType = "int",required = true)
    })
    @RequestMapping(value = "/countMonthPlay",method = RequestMethod.POST)
    public PlayVo countMonthPlay(String username,Integer userId)throws Exception{
        logger.info(username);
        return biliService.countMonthPlay(username,userId);
    }
    @ApiOperation("获取每月播放量")
    @ApiImplicitParams({
            @ApiImplicitParam(name="mid",value = "mid",paramType ="form",dataType = "int",required = true)
    })
    @RequestMapping(value = "/countMonthPlayByMid",method = RequestMethod.POST)
    public PlayVo countMonthPlayByMid(Integer mid)throws Exception{
        logger.info(""+mid);
        User user = userRepository.findByMid(mid);
        return biliService.countMonthPlay(user.getUserName(),user.getId());
    }


    @ApiOperation("用户空间视频爬虫")
    @ApiImplicitParams({
            @ApiImplicitParam(name="mid",value = "mid",paramType ="form",dataType = "int",required = true)
    })
    @RequestMapping(value = "/biliUserSpaceSearch",method = RequestMethod.POST)
    public ResponseMap biliUserSpaceSearch(Integer mid)throws Exception{
        ResponseMap map = ResponseMap.getInstance();
        logger.info(""+mid);
        Integer flag = biliService.biliSearchByMid(mid);
        if (flag==1){
            return map.putSuccess("爬取成功");
        }else {
            return map.putFailure("爬取失败",-1);
        }
    }







}
