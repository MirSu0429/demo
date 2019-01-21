package com.example.demo.util;

import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.UUID;

/**
 * @Author: supengfei
 * @Date: 2019/1/21 10:57
 * @Description:
 */
@Component
public class ServiceUtil<T> {
    /**
     * general field(通用字段)
     */
    private static final String CREATE_BY = "createBy";

    private static final String CREATE_DATE = "createDate";

    private static final String UPDATE_BY = "updateBy";

    private static final String UPDATE_DATE = "updateDate";

    //系统默认 id 如果主键为其他字段 则需要自己手动 生成 写入 id
    private static final String ID = "id";

    private static final String STR = "java.lang.String";
    /**
     * 通用注入创建 更新信息 可通过super调用
     *
     * @param record
     * @param flag
     * @return
     */
    public T addValue(T record, boolean flag) {
        //CurrentUser currentUser = (CurrentUser) SecurityUtils.getSubject().getSession().getAttribute("curentUser");
        //统一处理公共字段
        Class<?> clazz = record.getClass();
        String operator, operateDate;
        try {
            if (flag) {
                //添加 id uuid支持
                Field idField = clazz.getDeclaredField(ID);
                idField.setAccessible(true);
                Object o = idField.get(record);
                Class<?> type = idField.getType();
                String name = type.getName();
                if ((o == null) && STR.equals(name)) {
                    //已经有值的情况下 不覆盖
                    idField.set(record, UUID.randomUUID().toString().replace("-", "").toLowerCase());
                }
                operator = CREATE_BY;
                operateDate = CREATE_DATE;
            } else {
                operator = UPDATE_BY;
                operateDate = UPDATE_DATE;
            }
            Field field = clazz.getDeclaredField(operator);
            field.setAccessible(true);
            //field.set(record, currentUser.getId());
            Field fieldDate = clazz.getDeclaredField(operateDate);
            fieldDate.setAccessible(true);
            fieldDate.set(record, new Date());

        } catch (NoSuchFieldException e) {
            //无此字段
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return record;
    }
}
