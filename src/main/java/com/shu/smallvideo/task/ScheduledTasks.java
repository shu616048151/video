package com.shu.smallvideo.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author shuxibing
 * @date 2019/12/30 15:06
 * @uint d9lab
 * @Description:
 */
@Component
public class ScheduledTasks {
    private static Logger logger= LoggerFactory.getLogger(ScheduledTasks.class);

//    @Scheduled(cron="0/10 * *  * * ?")
    public void tableTask(){
        logger.info("定时任务执行中。。。。");
    }
}
