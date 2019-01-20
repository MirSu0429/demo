package com.example.demo.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.example.demo.entity.JobAndTrigger;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface JobAndTriggerMapper extends BaseMapper<JobAndTrigger> {
	 List<JobAndTrigger> getJobAndTriggerDetails();

	List<JobAndTrigger> selectJobAndTriggerDetails();

	JobAndTrigger getJobAndTriggerDetail(@Param("jobClassName") String jobClassName,
										 @Param("jobGroupName") String jobGroupName,
										 @Param("cronExpression") String cronExpression);
}
