package com.demo;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.demo.dao.AreaCodeMapper;
import com.demo.entity.AreaCode;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ExcelToAreaCodeDb {

	@Autowired
	private AreaCodeMapper areaCodeMapper;

	/**
	 * 从外部新增的
	 */
	@Test
	public void add() throws IOException {
		ExcelReader reader = ExcelUtil.getReader("D:/temp/202010/8月6位.xlsx");
		List<Map<String, Object>> readAll = reader.readAll();
		for (Map<String, Object> map : readAll) {
			String srcCode = map.get("code").toString();
			String name = map.get("name").toString();
			name = StrUtil.trim(name);
			String code = StrUtil.fillAfter(srcCode, '0', 12);
			AreaCode record = areaCodeMapper.selectById(Long.parseLong(code));
			if (record == null) {
				AreaCode areaCode = new AreaCode();
				areaCode.setMemo("2020新增");
				areaCode.setCode(Long.parseLong(code));
				areaCode.setName(name);
				int level = 3;
				if (StrUtil.endWith(srcCode, "0000")) {
					level = 1;
				}
				if (StrUtil.endWith(srcCode, "00")) {
					level = 2;
				}
				areaCode.setLevel(level);

				srcCode = srcCode.substring(0, srcCode.length() - 2);
				srcCode = StrUtil.fillAfter(srcCode, '0', 12);
				areaCode.setPcode(Long.parseLong(srcCode));

				AreaCode pRecord = areaCodeMapper.selectById(Long.parseLong(srcCode));
				if (pRecord == null) {
					throw new RuntimeException("没有父级");
				}
				areaCodeMapper.insert(areaCode);
			}

		}
	}

	/**
	 * 从外部删除的
	 */
	@Test
	public void delete() throws IOException {
		List<AreaCode> areaCodes = areaCodeMapper.selectList(new QueryWrapper<AreaCode>().last(" where CODE LIKE '______000000' "));

		ExcelReader reader = ExcelUtil.getReader("D:/temp/202010/8月6位.xlsx");
		List<Map<String, Object>> readAll = reader.readAll();
		List<String> rec = new ArrayList<>();
		for (AreaCode areaCode : areaCodes) {
			String code = (areaCode.getCode() + "").substring(0, 6);
			boolean flag = false;
			for (Map<String, Object> map : readAll) {
				String excelCode = map.get("code").toString();
				if (code.equals(excelCode)) {
					flag = true;
				}
			}
			if (!flag && !areaCode.getName().equals("市辖区")) {
				rec.add(StrUtil.format("{} {} ", code, areaCode.getName()));
				//System.out.println(StrUtil.format("{} {} 没找到", code, areaCode.getName()));
			}
		}

		for (String s : rec) {
			System.out.println(s);
		}

	}

	@Test
	public void xian() {
		ExcelReader reader = ExcelUtil.getReader("D:/temp/202010/工作簿2.xlsx");
		List<Map<String, Object>> readAll = reader.readAll();
		for (Map<String, Object> map : readAll) {
			if (map.get("变更原因").toString().indexOf("新设街道") >= 0) {
				AreaCode areaCode = new AreaCode();
				String code = map.get("现区划代码").toString();
				String name = map.get("现名称").toString();
				areaCode.setName(name);
				if (code.length() == 9) {
					areaCode.setLevel(4);
				}
				String pcode = code.substring(0, code.length() - 3);
				pcode = StrUtil.fillAfter(pcode, '0', 12);

				AreaCode pRecord = areaCodeMapper.selectById(Long.parseLong(pcode));
				if (pRecord == null) {
					throw new RuntimeException("没有父级");
				}
				areaCode.setPcode(Long.parseLong(pcode));
				code = StrUtil.fillAfter(code, '0', 12);
				areaCode.setCode(Long.parseLong(code));
				areaCode.setMemo("2020新增");
				areaCodeMapper.insert(areaCode);

			}
		}
	}

	public static void main(String[] args) {
		String s = StrUtil.fillAfter("abc", '0', 2);
		System.out.println(s);
	}

}
