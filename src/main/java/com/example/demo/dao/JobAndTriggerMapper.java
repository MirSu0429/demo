package com.example.demo.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.example.demo.entity.JobAndTrigger;

import java.util.List;

public interface JobAndTriggerMapper extends BaseMapper<JobAndTrigger> {
	 List<JobAndTrigger> getJobAndTriggerDetails();

	List<JobAndTrigger> selectJobAndTriggerDetails();
}
