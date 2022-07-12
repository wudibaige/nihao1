package com.nihao.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import javax.servlet.annotation.WebFilter;
import java.io.Serializable;
import java.time.LocalDateTime;

/*
*员工实体类
 */
@Data
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String name;

    private String password;

    private String phone;

    private String sex;

    private String idNumber;

    private Integer status;

    //插入时更新字段
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    //更新时查询字段
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    //插入时查询字段
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    //插入和更新
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

}
