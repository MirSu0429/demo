package com.example.demo.controller;

import com.example.demo.entity.JobEntity;
import com.example.demo.exception.MyException;
import com.example.demo.service.IJobEntityService;
import com.example.demo.util.BeanUtil;
import com.example.demo.util.ResponseUtil;
import com.example.demo.util.StatusUtil;
import com.example.demo.util.quertz.JobTaskUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Author: supengfei
 * @Date: 2019/1/21 10:28
 * @Description:
 */
@Controller
public class JobController {

    @Autowired
    private IJobEntityService jobEntityService;

    @Autowired
    private JobTaskUtil jobTaskUtil;
    /**
     * @return
     * @Description //TODO 查询
     * @Param
     **/
    @GetMapping(value = "/queryJob")
    @ResponseBody
    public List<JobEntity> queryJob() {
        List<JobEntity> jobEntityList = jobEntityService.selectListJob();
        return jobEntityList;

    }
    /**
     * @return
     * @Description 打开新增窗口
     * @Param
     **/
    @RequestMapping("/open_insert")
    public String openInsert() {
        return "view/insert.html";
    }
    /**
     * @return
     * @Description //TODO 添加任务
     * @Param
     **/
    @PostMapping("/insert")
    @ResponseBody
    public ResponseUtil insertJob(JobEntity job) {
        ResponseUtil responseUtil = new ResponseUtil();
        String msg = "保存成功";
        job.setStatus(StatusUtil.DISABLE);
        try {
            jobEntityService.insertJob(job);
        } catch (MyException e) {
            msg = "保存失败";
            responseUtil.setFlag(false);
            e.printStackTrace();
        }
        responseUtil.setMsg(msg);
        return responseUtil;
    }
    /**
     * @Description //TODO 启动任务
     * @Param 
     * @return 
     **/
    @PostMapping(value = "/start_job")
    @ResponseBody
    public ResponseUtil startJob(String id) {
        ResponseUtil responseUtil = new ResponseUtil();
        String msg = null;
        if (StringUtils.isEmpty(id)) {
            responseUtil.setMsg("获取数据失败");
            responseUtil.setFlag(false);
            return responseUtil;
        }
        try {
            jobEntityService.startJob(id);
            msg = "启动成功";
        } catch (MyException e) {
            e.printStackTrace();
        }
        responseUtil.setMsg(msg);
        return responseUtil;
    }

    /**
     * @return
     * @Description //TODO 暂停任务
     * @Param
     **/
    @PostMapping("/pause_job")
    @ResponseBody
    public ResponseUtil pauseJob(String id) {
        ResponseUtil responseUtil = new ResponseUtil();
        String msg = null;
        if (StringUtils.isEmpty(id)) {
            responseUtil.setMsg("获取数据失败");
            responseUtil.setFlag(false);
            return responseUtil;
        }
        try {
            jobEntityService.pauseJob(id);
            msg = "停止成功";
        } catch (MyException e) {
            e.printStackTrace();
        }
        responseUtil.setMsg(msg);
        return responseUtil;
    }

    /**
     * @return
     * @Description //TODO 删除任务
     * @Param
     **/
    @PostMapping("/delete_job")
    @ResponseBody
    public ResponseUtil deleteJob(String id) {
        ResponseUtil responseUtil = new ResponseUtil();
        responseUtil.setFlag(false);
        if (StringUtils.isEmpty(id)) {
            responseUtil.setMsg("获取数据失败");
            return responseUtil;
        }
        try {
            JobEntity job = jobEntityService.selectById(id);
            boolean flag = jobTaskUtil.checkJob(job);
            if ((flag && !(job.getStatus().equals(StatusUtil.ENABLE))) || !flag && (job.getStatus().equals(StatusUtil.ENABLE))) {
                responseUtil.setMsg("您任务表状态和web任务状态不一致,无法删除");
                return responseUtil;
            }
            if (flag) {
                responseUtil.setMsg("该任务处于启动中，无法删除");
                return responseUtil;
            }
            jobEntityService.deleteById(id);
            responseUtil.setFlag(true);
            responseUtil.setMsg("任务删除成功");
        } catch (MyException e) {
            responseUtil.setMsg("任务删除异常");
            e.printStackTrace();
        }
        return responseUtil;
    }

    /**
     * @Description 打开修改页面 数据回填
     * @Param
     * @return
     **/
    @RequestMapping("/open_update")
    public String openUpdate(String id, Model model){
        JobEntity jobEntity = jobEntityService.selectById(id);
        model.addAttribute("item", jobEntity);
        return "view/update.html";
    }

    /**
     * @Description //修改
     * @Param
     * @return
     **/
    @PostMapping("/update_job")
    @ResponseBody
    public ResponseUtil updateJob(JobEntity job) throws Exception{
        ResponseUtil responseUtil = new ResponseUtil();
        responseUtil.setFlag(false);
        if (job == null) {
            responseUtil.setMsg("获取数据失败");
            return responseUtil;
        }
        if (jobTaskUtil.checkJob(job)) {
            responseUtil.setMsg("已经启动任务无法更新,请停止后更新");
            return responseUtil;
        }
        try {
            JobEntity oldJob = jobEntityService.selectById(job.getId());
            BeanUtil.copyNotNullBean(job, oldJob);
            jobEntityService.updateById(oldJob);
            responseUtil.setFlag(true);
            responseUtil.setMsg("修改成功");
        } catch (MyException e) {
            responseUtil.setMsg("更新失败");
            e.printStackTrace();
        }
        return responseUtil;
    }
}
