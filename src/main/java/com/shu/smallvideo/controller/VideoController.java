package com.shu.smallvideo.controller;

import com.shu.smallvideo.base.Platform;
import com.shu.smallvideo.service.VideoService;
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
 * @date 2019/12/28 21:09
 * @uint d9lab
 * @Description:
 */
@RestController
@RequestMapping("/video")
@Api(tags = "video",description = "video通用api",produces = MediaType.APPLICATION_JSON_VALUE)
public class VideoController {


    private static Logger logger= LoggerFactory.getLogger(VideoController.class);

    @Autowired
    private VideoService videoService;
    @ApiOperation("获取视频列表信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="username",value = "姓名",paramType ="form",dataType = "string",required = true)
    })
    @RequestMapping(value = "/getListByUserName",method = RequestMethod.POST)
    public List<VideoVo> getListByUserName(String username, Platform platform)throws Exception{
        logger.info(username);
        return videoService.getListByUserName(username,platform);
    }

    @ApiOperation("更新数据库视频播放量")
    @ApiImplicitParams({
            @ApiImplicitParam(name="username",value = "姓名",paramType ="form",dataType = "string",required = true)
    })
    @RequestMapping(value = "/updatePlayByUserName",method = RequestMethod.POST)
    public List<VideoVo> updatePlayByUserName(String username,Platform platform)throws Exception{
        logger.info(username);
        return videoService.updatePlayByUserName(username,platform);
    }

    @ApiOperation("获取每月播放量")
    @ApiImplicitParams({
            @ApiImplicitParam(name="username",value = "姓名",paramType ="form",dataType = "string",required = true),
            @ApiImplicitParam(name="userId",value = "用户id",paramType ="form",dataType = "int",required = true)
    })
    @RequestMapping(value = "/countMonthPlay",method = RequestMethod.POST)
    public PlayVo countMonthPlay(String username, Integer userId,Platform platform)throws Exception{
        logger.info(username);
        return videoService.countMonthPlay(username,userId,platform);
    }

}
