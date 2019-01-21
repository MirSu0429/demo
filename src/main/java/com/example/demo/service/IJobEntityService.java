package com.example.demo.service;

import com.baomidou.mybatisplus.service.IService;
import com.example.demo.entity.JobEntity;

import java.util.List;

/**
 * @Author: supengfei
 * @Date: 2019/1/21 10:47
 * @Description:
 */
public interface IJobEntityService extends IService<JobEntity> {
    Integer insertJob(JobEntity job);

    Integer startJob(String id);

    Integer pauseJob(String id);

    List<JobEntity> selectListJob();
}
