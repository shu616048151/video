package com.shu.smallvideo.service;

import com.shu.smallvideo.model.Video;
import com.shu.smallvideo.vo.PlayVo;
import com.shu.smallvideo.vo.VideoVo;

import java.util.List;

/**
 * @author shuxibing
 * @date 2019/12/22 16:22
 * @uint d9lab
 * @Description:
 */
public interface BiliService {
    List<VideoVo> getListByUserName(String username)throws Exception;

    List<VideoVo> updatePlayByUserName(String username) throws Exception;

    PlayVo countMonthPlay(String username,Integer userId) throws Exception;
}
