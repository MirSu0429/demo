package com.example.demo.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.demo.dao.JobAndTriggerMapper;
import com.example.demo.entity.JobAndTrigger;
import com.example.demo.job.BaseJob;
import com.example.demo.service.IJobAndTriggerService;
import com.example.demo.util.redis.RedisUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service("jobAndTriggerImpl")
public class JobAndTriggerImpl extends ServiceImpl<JobAndTriggerMapper,JobAndTrigger> implements IJobAndTriggerService{

	@Resource
	private JobAndTriggerMapper jobAndTriggerMapper;
	@Autowired
	private RedisUtil redisUtil;

	@Autowired
	private Scheduler scheduler;
	
	@Override
	public PageInfo<JobAndTrigger> getJobAndTriggerDetails(int pageNum, int pageSize) {
		PageInfo<JobAndTrigger> page = null;
		PageHelper.startPage(pageNum, pageSize);
		/*List<Object> list1 = redisUtil.lGet("list1", 0, -1);
		List<JobAndTrigger> jobAndTriggers;
		if (list1 != null && list1.size() > 0) {
			jobAndTriggers = (List<JobAndTrigger>) list1.get(0);
			page = new PageInfo<JobAndTrigger>(jobAndTriggers);
		} else {
			List<JobAndTrigger> list = jobAndTriggerMapper.getJobAndTriggerDetails();
			redisUtil.lSet("list1",list);
			page = new PageInfo<JobAndTrigger>(list);
		}*/
		List<JobAndTrigger> list = jobAndTriggerMapper.getJobAndTriggerDetails();
		page = new PageInfo<JobAndTrigger>(list);
		return page;
	}

	@Override
	public List<JobAndTrigger> selectJobAndTriggerDetails() {
		List<Object> redisList = redisUtil.lGet("selectJobAndTriggerDetails", 0, -1);
		if (redisList != null && redisList.size() > 0) {
			return (List<JobAndTrigger>) redisList.get(0);
		} else {
			List<JobAndTrigger> jobAndTriggers = baseMapper.selectJobAndTriggerDetails();
			redisUtil.lSet("selectJobAndTriggerDetails", jobAndTriggers);
			return jobAndTriggers;
		}
	}

	@Override
	public JobAndTrigger getJobAndTriggerDetail(String jobClassName, String jobGroupName, String cronExpression) {
		return baseMapper.getJobAndTriggerDetail( jobClassName,  jobGroupName,  cronExpression);
	}

	@Override
	public void addJob(String jobClassName, String jobGroupName, String cronExpression) throws Exception {
		// 启动调度器
		scheduler.start();
		//构建job信息
		JobDetail jobDetail = JobBuilder.newJob(getClass(jobClassName).getClass()).withIdentity(jobClassName, jobGroupName).build();
		//表达式调度构建器(即任务执行的时间)
		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
		//按新的cronExpression表达式构建一个新的trigger
		CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobClassName, jobGroupName)
											.withSchedule(scheduleBuilder).build();
		try {
			scheduler.scheduleJob(jobDetail, trigger);
			//删除结果集缓存
			redisUtil.del("selectJobAndTriggerDetails");
		} catch (SchedulerException e) {
			System.out.println("创建定时任务失败" + e);
			throw new Exception("创建定时任务失败");
		}
	}

	@Override
	public void jobResume(String jobClassName, String jobGroupName) throws Exception {
		scheduler.resumeJob(JobKey.jobKey(jobClassName, jobGroupName));
		//删除结果集缓存
		redisUtil.del("selectJobAndTriggerDetails");
	}

	@Override
	public void jobPause(String jobClassName, String jobGroupName) throws Exception {
		scheduler.pauseJob(JobKey.jobKey(jobClassName, jobGroupName));
		//删除结果集缓存
		redisUtil.del("selectJobAndTriggerDetails");
	}

	@Override
	public void jobDelete(String jobClassName, String jobGroupName) throws Exception {
		scheduler.pauseTrigger(TriggerKey.triggerKey(jobClassName, jobGroupName));
		scheduler.unscheduleJob(TriggerKey.triggerKey(jobClassName, jobGroupName));
		scheduler.deleteJob(JobKey.jobKey(jobClassName, jobGroupName));
		//删除结果集缓存
		redisUtil.del("selectJobAndTriggerDetails");
	}

	@Override
	public void jobReschedule(String jobClassName, String jobGroupName, String cronExpression) throws Exception {
		try {
			TriggerKey triggerKey = TriggerKey.triggerKey(jobClassName, jobGroupName);
			// 表达式调度构建器
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
			// 按新的cronExpression表达式重新构建trigger
			trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
			// 按新的trigger重新设置job执行
			scheduler.rescheduleJob(triggerKey, trigger);
			//删除结果集缓存
			redisUtil.del("selectJobAndTriggerDetails");
		} catch (SchedulerException e) {
			System.out.println("更新定时任务失败"+e);
			throw new Exception("更新定时任务失败");
		}
	}


	public static BaseJob getClass(String classname) throws Exception {
		Class<?> class1 = Class.forName(classname);
		return (BaseJob) class1.newInstance();
	}
}