package com.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.entity.AreaCode;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author mifei
 * @create 2020-09-10 17:11
 **/
@Repository
public interface AreaCodeMapper extends BaseMapper<AreaCode> {



	@Select(" select * from area_code_2021  ")
	List<AreaCode> listPage();
}
