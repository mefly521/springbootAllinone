package com.demo;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.demo.dao.BusiTableMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class DbTests {

    @Autowired
    private BusiTableMapper busiTableMapper;


    /**
     * 自动读取变更表,生成同步sql ,将测试库的数据同步到生产库
     */
    @Test
    public void generalAddInsertSql() throws IOException {
        String filePath = "d:\\temp\\00\\sql3.txt";
        PrintWriter pw = new PrintWriter(new FileWriter(filePath));
        List<Map> res = busiTableMapper.listByCustomSql("select * from t_z_log ");
        for (Map map :res) {
            generalSql(pw,map.get("tablename").toString(),map.get("range").toString());
        }
        pw.close();
    }

    private void generalSql(PrintWriter pw, String tableName, String subSql) {
        List<Map> res = busiTableMapper.listByCustomSql("select * from " + tableName+" where id "+subSql);
        for (Map map :res) {
            //System.out.println(getInsertSql(tableName,map));
            pw.println(getInsertSql(tableName,map));
        }
    }

    private String getInsertSql(String tableName,Map map) {
        String sql = "insert into `{}` ( {}) values ({})";
        List<String> keys = new ArrayList<>();
        List<String> vals = new ArrayList<>();
        for (Object key : map.keySet()) {
            keys.add("`"+key.toString()+"`");
            if (map.get(key) != null) {
                vals.add("'"+map.get(key).toString()+"'");
            }else {
                vals.add("null");
            }
        }
        sql = StrUtil.format(sql, tableName,ArrayUtil.join(keys.toArray(), ","),ArrayUtil.join(vals.toArray(), ","));
        return sql+";";
    }

    /**
     * 通用的excel导入db 的程序(适用所有表)
     */
    @Test
    public void generalImport() {
        ExcelReader reader = ExcelUtil.getReader("D:\\公司文件\\需求\\报价程序\\导入模板\\file\\新增电梯基价表-TO BIT.xlsx");
        String tableName = "t_base_price_equation";
        String title[] = {"category", "type", "载重", "速度", "层站", "提升高度", "价格表基价", "新成本价-执行"};
        String field[] = {"category", "type", "val1", "val2", "val3", "val4", "output", "cost_price"};

        Long maxId = busiTableMapper.longByCustomSql("select max(id) from " + tableName);
        log.info("导入前最大记录id为=" + maxId);
        List<Map<String, Object>> readAll = reader.readAll();
        for (Map<String, Object> map : readAll) {
            busiTableMapper.insert(getSql(tableName, map, title, field));
        }
    }

    private String getSql(String tableName, Map<String, Object> map, String[] title, String[] field) {
        List<String> fields = new ArrayList<>();
        List<String> values = new ArrayList<>();
        for (int i = 0; i < title.length; i++) {
            fields.add(field[i]);
            if (map.get(title[i]) == null) {
                values.add("null");
            } else {
                values.add("'" + map.get(title[i]) + "'");
            }
        }
        String res = StrUtil.format("INSERT INTO {} ({}) VALUES ({})", tableName,
                CollectionUtil.join(fields, ","), CollectionUtil.join(values, ","));
        return res;
    }
}
