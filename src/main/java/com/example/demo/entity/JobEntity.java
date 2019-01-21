package com.example.demo.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

/**
 * @author jxkj
 */
@TableName("sys_job")
@Data
public class JobEntity extends Model<JobEntity> {
    @TableId(value = "id")
    private String id;

    /**
     * 描述任务
     */
    @NotEmpty(message = "任务描述不能为空")
    @TableField("job_name")
    private String jobName;

    /**
     * 任务表达式
     */
    @TableField("cron")
    private String cron;

    /**
     * 状态:0未启动false/1启动true
     */
    private Integer status;

    /**
     * 任务执行方法
     */
    @NotEmpty(message = "执行方法不能未空")
    @TableField("clazz_path")
    private String clazzPath;

    /**
     * 其他描述
     */
    @TableField("job_desc")
    private String jobDesc;

    @TableField("create_by")
    private String createBy;

    @TableField("create_date")
    private Date createDate;

    @TableField("update_by")
    private String updateBy;

    @TableField("update_date")
    private Date updateDate;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}