package com.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 业务模板名称表 Mapper 接口
 * </p>
 */
@Mapper
public interface BusiTableMapper extends BaseMapper<Object> {

    @Select(" ${sql}  ")
    List<Map> listByCustomSql(@Param("sql") String sql);

    Map selectOneBySql(@Param("sql") String sql);

    @Select(" ${sql}  ")
    Long longByCustomSql(@Param("sql") String sql);

    @Select("select * from ${tableName} where ${column}=${value} limit 1")
    Map findOne(@Param("tableName") String tableName,
                @Param("column") String column,
                @Param("value") Object value);

    /**
     * @param tableName
     * @param column
     * @param value
     * @param id
     * @return
     */
    @Update({" update ${params.tableName}  set ${params.column} = #{params.value} where id =#{params.id} "})
    int update(@Param("params") Map<String, Object> map);

    @Delete({" delete from ${tableName} where id =#{id} "})
    int delete(@Param("id") Long id, @Param("tableName") String tableName);

    @Select(" select count(*) from ${tableName}")
    Long count(@Param("tableName") String tableName);

    @Insert(" ${sql}")
    Long insert(@Param("sql") String sql);

    @Update(" ${sql}")
    Integer updateCustomSql(@Param("sql") String sql);

}
