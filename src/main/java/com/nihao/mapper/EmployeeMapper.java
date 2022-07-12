package com.nihao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nihao.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}
