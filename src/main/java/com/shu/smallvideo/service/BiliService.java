package com.shu.smallvideo.service;

import com.shu.smallvideo.model.vo.PlayVo;
import com.shu.smallvideo.model.vo.VideoVo;

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

    Integer biliSearchByMid(Integer mid)throws Exception;
}
