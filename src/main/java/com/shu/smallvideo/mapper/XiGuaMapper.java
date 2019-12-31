package com.shu.smallvideo.mapper;

import com.shu.smallvideo.base.ParamsMap;
import com.shu.smallvideo.model.vo.VideoVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shuxibing
 * @date 2019/12/22 16:02
 * @uint d9lab
 * @Description:
 */
public interface XiGuaMapper {

    List<VideoVo> getListByUserName(@Param("username") String username)throws  Exception;

    List<VideoVo> getAllList()throws  Exception;

    Integer updatePlayById(ParamsMap map)throws Exception;


    List<VideoVo> getListByUserNameAndId(ParamsMap map)throws Exception;

    List<VideoVo> getAllListByUserName(ParamsMap map)throws Exception;

    Integer updateTimeById(ParamsMap map)throws Exception;
}
