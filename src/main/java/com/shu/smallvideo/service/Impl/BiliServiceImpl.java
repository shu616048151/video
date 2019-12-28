package com.shu.smallvideo.service.Impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.shu.smallvideo.base.ParamsMap;
import com.shu.smallvideo.controller.BiliController;
import com.shu.smallvideo.mapper.BiliMapper;
import com.shu.smallvideo.model.Play;
import com.shu.smallvideo.model.Video;
import com.shu.smallvideo.service.BiliService;
import com.shu.smallvideo.vo.PlayVo;
import com.shu.smallvideo.vo.VideoVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Key;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author shuxibing
 * @date 2019/12/22 16:22
 * @uint d9lab
 * @Description:
 */
@Service
public class BiliServiceImpl implements BiliService {
    private static Logger logger= LoggerFactory.getLogger(BiliServiceImpl.class);

    @Autowired
    private BiliMapper biliMapper;

    @Override
    public List<VideoVo> getListByUserName(String username) throws Exception {
        return biliMapper.getListByUserName(username);
    }

    @Override
    public List<VideoVo> updatePlayByUserName(String username) throws Exception {
        List<VideoVo> videolist = biliMapper.getListByUserName(username);
        updateVideoPlay(videolist);
        return videolist;
    }

    @Override
    public PlayVo countMonthPlay(String username,Integer userId) throws Exception {
        List<VideoVo> videoVoList = getListByUserNameAndId(username,userId);
        PlayVo playVo = countMonthPlay(videoVoList,username,userId);
        return playVo;
    }

    private List<VideoVo> getListByUserNameAndId(String username,Integer id) throws Exception {
        ParamsMap map=ParamsMap.getPageInstance();
        logger.info("id:"+id);
        logger.info("username:"+username);
        map.put("id",id);
        map.put("username",username);
        return biliMapper.getAllListByUserName(map);
    }

    public void updateVideoPlay(List<VideoVo> videoList) throws Exception {
        for (VideoVo video : videoList) {
            ParamsMap map = ParamsMap.getPageInstance();
            map.put("id",video.getId());
            String play = video.getPlay();
            if (play!=null){
                Pattern pattern=Pattern.compile("[0-9]+.?[0-9]*万");
                Matcher matcher = pattern.matcher(play);
                if (matcher.find()){
                    String group = matcher.group();
                    logger.info(group);
                     map.put("play",group);
                }else{
                     pattern=Pattern.compile("[0-9]+");
                    matcher = pattern.matcher(play);
                    if (matcher.find()){
                        Integer num=Integer.valueOf(matcher.group());
                        double num1=(double)num;
                        map.put("play",(num1/10000)+"万");
                        logger.info((num1/10000)+"万");
                    }
                }
            }
            map.put("updateTime",new Date());
            biliMapper.updatePlayById(map);
        }
    }
    public PlayVo countMonthPlay(List<VideoVo> videos,String userName,Integer id){
        logger.info("videoList大小："+videos.size());
        PlayVo playVo=new PlayVo();
        Map<String,Double> playMap=new LinkedHashMap<>();
        Map<String,Integer> videoNumMap=new LinkedHashMap<>();
        playVo.setId(id);
        playVo.setUserName(userName);
        if (videos!=null&&videos.size()>0){
            playVo.setPlatform(videos.get(0).getPlatform());
        }
        List<Play> playList=new ArrayList<>();
        for (VideoVo video : videos) {
            String time = video.getTime();
            boolean b = time.matches("^[0-9]{4}.*");
            if (!b){
                Calendar calendar=Calendar.getInstance();
                int weekYear = calendar.getWeekYear();
                time=weekYear+"-"+time;
            }
            String month=time.substring(0,time.lastIndexOf("-"));
            if (playMap.containsKey(month)){
                Double num = playMap.get(month);
                String play = video.getPlay();
                Double PlayNum=Double.valueOf(play.substring(0,play.lastIndexOf("万")));
                Double totalNum=num+PlayNum;
                playMap.put(month,totalNum);
                videoNumMap.put(month,videoNumMap.get(month)+1);
            }else{
                String play = video.getPlay();
                Double PlayNum=Double.valueOf(play.substring(0,play.lastIndexOf("万")));
                playMap.put(month,PlayNum);
                videoNumMap.put(month,1);
            }
        }
        for (String key:playMap.keySet()){
            Double totalNum = playMap.get(key);
            Integer videoNum = videoNumMap.get(key);
            BigDecimal bigDecimal=new BigDecimal(totalNum);
            double value1 = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            bigDecimal=new BigDecimal(totalNum/videoNum);
            double value2 = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            Play play=new Play();
            play.setDate(key);
            play.setTotalPlayNum(value1);
            play.setAvgPlayNum(value2);
            play.setVideoNum(videoNum);
            playList.add(play);
        }
        playVo.setPlayList(playList);
        return playVo;
    }



}


