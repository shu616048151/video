package com.shu.smallvideo.service.Impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.shu.smallvideo.base.ParamsMap;
import com.shu.smallvideo.base.Platform;
import com.shu.smallvideo.mapper.BiliMapper;
import com.shu.smallvideo.model.Bili;
import com.shu.smallvideo.model.Play;
import com.shu.smallvideo.model.User;
import com.shu.smallvideo.model.vo.BiliVo;
import com.shu.smallvideo.repository.UserRepository;
import com.shu.smallvideo.service.BiliService;
import com.shu.smallvideo.model.vo.PlayVo;
import com.shu.smallvideo.model.vo.VideoVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
public class BiliServiceImpl implements BiliService {
    private static Logger logger= LoggerFactory.getLogger(BiliServiceImpl.class);

    @Autowired
    private BiliMapper biliMapper;
    
    @Autowired
    private UserRepository userRepository;

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

    @Override
    public Integer biliSearchByMid(Integer mid) throws Exception {
        Integer page = spiderPage(mid, 30, 1, "");
        Integer user=null;
        for (int i=1;i<=page;i++){
            List<Bili> bilis = spiderVideo(mid, 30, i, "");
            if (bilis!=null&&bilis.size()>0){
                for (Bili bili : bilis) {
                    BiliVo biliVo = new BiliVo().convertFrom(bili);
                    biliVo.setPlatform(Platform.BiliBili);
                    biliVo.setUrl("https://www.bilibili.com/video/av"+biliVo.getAid());
                    String author = bili.getAuthor();
                    if (user==null||user==0){
                        List<User> userList = userRepository.findByUserName(author);
                        if (userList!=null&&userList.size()>0){
                            for (User userObject : userList) {
                                Platform platform = userObject.getPlatform();
                                if (platform==Platform.BiliBili){
                                    user=userObject.getId();
                                    break;
                                }
                            }
                        }else {
                            User user2=new User(author,new Date(),Platform.BiliBili.getName(),Platform.BiliBili,mid);
                            user2= userRepository.save(user2);
                            user=user2.getId();
                            logger.info("user:"+user);
                        }
                    }
                    userRepository.findByUserName(author);
                    biliVo.setUser(user);
                    biliVo.setCreateTime(new Date());
                    biliVo.setUpdateTime(new Date());
                    if (biliVo.getPlay().matches("[0-9]+")){
                        //播放量正常的数据，视频没有被删除
                        saveOrUpdate(biliVo);
                    }
                }
            }
        }
        return 1;
    }

    /**
     * 用户存储和更新数据
     * @param biliVo
     */
    private void saveOrUpdate(BiliVo biliVo) throws Exception {
        BiliVo biliVoNew=biliMapper.getBiliByAid(biliVo.getAid());
        if (biliVoNew!=null){
            biliMapper.updateBiliByAid(biliVo);
        }else {
            biliMapper.addBili(biliVo);
        }

    }

    /**
     *
     * @param mid  作者的mid
     * @param ps 每页条数
     * @param pn 页码
     * @param keyword 搜素关键词
     * @return
     */
    public  List<Bili> spiderVideo(long mid, int ps, int pn, String keyword){
        List<Bili> bilis=new ArrayList<>();
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("https://api.bilibili.com/x/space/arc/search")
                .append("?mid="+mid)
                .append("&ps="+ps)
                .append("&tid=0")
                .append("&pn="+pn)
                .append("&keyword="+keyword)
                .append("&order=pubdate&jsonp=jsonp");
        String url = stringBuilder.toString();
        System.out.println(url);
        HttpRequest httpRequest = HttpUtil.createGet(url);
        HttpResponse execute = httpRequest.execute();
        if (execute.getStatus()==200){
            String body = execute.body();
            JSON parse = JSONUtil.parse(body);
            Object data = parse.getByPath("data");
            JSONObject dataObject = JSONUtil.parseObj(data);
            Object list = dataObject.get("list");
            JSON parse1 = JSONUtil.parse(list);
            JSONArray jsonArray1 = JSONUtil.parseArray(parse1.getByPath("vlist"));
            logger.info("vlist:"+jsonArray1);
            bilis= jsonArray1.toList(Bili.class);
            logger.info("spiderVideo的bilis大小："+bilis.size());
        }
        return bilis;
    }

    public  Integer spiderPage(long mid, int ps, int pn, String keyword){
        Integer countNum=0;
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("https://api.bilibili.com/x/space/arc/search")
                .append("?mid="+mid)
                .append("&ps="+ps)
                .append("&tid=")
                .append("&pn="+pn)
                .append("&keyword="+keyword)
                .append("&order=pubdate&jsonp=jsonp");
        String url = stringBuilder.toString();
        System.out.println(url);
        HttpRequest httpRequest = HttpUtil.createGet(url);
        HttpResponse execute = httpRequest.execute();
        if (execute.getStatus()==200){
            String body = execute.body();
            JSON parse = JSONUtil.parse(body);
            Object data = parse.getByPath("data");

            JSONObject jsonObject = JSONUtil.parseObj(data);
            Object page = jsonObject.get("page");
            System.out.println(jsonObject.get("page"));

            JSON parse2 = JSONUtil.parse(page);
            String count = parse2.getByPath("count").toString();
            countNum=Integer.parseInt(count)/ps+1;
            System.out.println("总共："+countNum);
        }
        return countNum;
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
            String time = video.getUploadTime();
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
//                Double PlayNum=Double.valueOf(play.substring(0,play.lastIndexOf("万")));
                Double PlayNum=Double.valueOf(play);
                Double totalNum=num+PlayNum;
                playMap.put(month,totalNum);
                videoNumMap.put(month,videoNumMap.get(month)+1);
            }else{
                String play = video.getPlay();
                Double PlayNum=Double.valueOf(play);
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


