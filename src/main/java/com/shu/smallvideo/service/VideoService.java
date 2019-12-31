package com.shu.smallvideo.service;

import com.shu.smallvideo.base.Platform;
import com.shu.smallvideo.model.vo.PlayVo;
import com.shu.smallvideo.model.vo.VideoVo;

import java.util.List;

/**
 * @author shuxibing
 * @date 2019/12/22 16:22
 * @uint d9lab
 * @Description:
 */
public interface VideoService {
    List<VideoVo> getListByUserName(String username, Platform platform)throws Exception;

    List<VideoVo> updatePlayByUserName(String username,Platform platform) throws Exception;

    PlayVo countMonthPlay(String username, Integer userId,Platform platform) throws Exception;


}
