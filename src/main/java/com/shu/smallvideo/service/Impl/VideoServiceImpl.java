package com.shu.smallvideo.service.Impl;

import com.shu.smallvideo.base.ParamsMap;
import com.shu.smallvideo.base.Platform;
import com.shu.smallvideo.mapper.BiliMapper;
import com.shu.smallvideo.mapper.XiGuaMapper;
import com.shu.smallvideo.model.Play;
import com.shu.smallvideo.service.VideoService;
import com.shu.smallvideo.util.DateUtil;
import com.shu.smallvideo.model.vo.PlayVo;
import com.shu.smallvideo.model.vo.VideoVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
@Transactional
public class VideoServiceImpl implements VideoService {
    private static Logger logger= LoggerFactory.getLogger(VideoServiceImpl.class);

    @Autowired
    private BiliMapper biliMapper;
    @Autowired
    private XiGuaMapper xiGuaMapper;

    @Override
    public List<VideoVo> getListByUserName(String username, Platform platform) throws Exception {
        List<VideoVo> videoVolist=new ArrayList<>();
        if (platform==Platform.BiliBili){
            videoVolist=biliMapper.getListByUserName(username);
        }
        if (platform==Platform.Youtube){
        }
        if (platform==Platform.XiGua){
            videoVolist=xiGuaMapper.getListByUserName(username);
        }
        return videoVolist;
    }

    @Override
    public List<VideoVo> updatePlayByUserName(String username,Platform platform) throws Exception {
        List<VideoVo> videoVolist=new ArrayList<>();
        if (platform==Platform.BiliBili){
            videoVolist=biliMapper.getListByUserName(username);
        }
        if (platform==Platform.Youtube){
        }
        if (platform==Platform.XiGua){
            videoVolist=xiGuaMapper.getListByUserName(username);
        }
        updateVideoPlay(videoVolist,platform);
        return videoVolist;
    }

    @Override
    public PlayVo countMonthPlay(String username,Integer userId,Platform platform) throws Exception {
        //新进行数据格式化
        updatePlayByUserName(username,platform);

        ParamsMap map=ParamsMap.getPageInstance();
        logger.info("id:"+userId);
        logger.info("username:"+username);
        map.put("id",userId);
        map.put("username",username);
        List<VideoVo> videoVoList=new ArrayList<>();
        if (platform==Platform.BiliBili){
            videoVoList=biliMapper.getAllListByUserName(map);
        }
        if (platform==Platform.Youtube){
        }
        if (platform==Platform.XiGua) {
            videoVoList = xiGuaMapper.getAllListByUserName(map);
        }
        PlayVo playVo = countMonthPlay(videoVoList,username,userId,platform);
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

    public void updateVideoPlay(List<VideoVo> videoList,Platform platform) throws Exception {
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
            if (platform==Platform.XiGua){
                xiGuaMapper.updatePlayById(map);
            }
            if (platform==Platform.BiliBili){
                biliMapper.updatePlayById(map);
            }
        }
    }
    public PlayVo countMonthPlay(List<VideoVo> videos,String userName,Integer id,Platform platform) throws Exception {
        logger.info("videoList大小："+videos.size());
        PlayVo playVo=new PlayVo();
        Map<String,Double> playMap=new LinkedHashMap<>();
        Map<String,Integer> videoNumMap=new LinkedHashMap<>();
        if (videos!=null&&videos.size()>0){
            playVo.setId(videos.get(0).getUser());
            playVo.setUserName(videos.get(0).getUsername());
            playVo.setPlatform(videos.get(0).getPlatform());
        }
        List<Play> playList=new ArrayList<>();
        for (VideoVo video : videos) {
            String time = video.getTime();
            boolean b = time.matches("^[0-9]{4}.*");
            if (!b){
                Calendar calendar=Calendar.getInstance();
                if (time.equals("昨天")){
                    Date lastDay = DateUtil.getLastDay(new Date());
                    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
                    time = simpleDateFormat.format(lastDay);
                }else if (time.equals("前天")){
                    Date lastDay = DateUtil.getDayBefore(new Date(),2);
                    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
                    time = simpleDateFormat.format(lastDay);
                }else {
                    int weekYear = calendar.getWeekYear();
                    time=weekYear+"-"+time;
                }
                //将格式化的的日期更新到数据库中
                ParamsMap map=ParamsMap.getPageInstance();
                map.put("id",video.getId());
                map.put("time",time);
                map.put("updateTime",new Date());
                if (platform==Platform.BiliBili){
                    biliMapper.updateTimeById(map);
                }
                if (platform==Platform.Youtube){
                }
                if (platform==Platform.XiGua) {
                    xiGuaMapper.updateTimeById(map);
                }

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


