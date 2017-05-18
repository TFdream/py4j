package com.mindflow.py4j;

import com.mindflow.py4j.exception.IllegalPinyinException;
import org.junit.*;

import java.util.Arrays;

/**
 * Unit test for simple App.
 */
public class PinyinConverterTest {
	private Converter pinyinConverter;

	@Before
	public void init(){
		pinyinConverter = new PinyinConverter();
	}

	@Test
	public void testChinesePy() throws IllegalPinyinException {

		final String[] arr = {"肯德基", "重庆银行", "长沙银行", "便宜坊", "西藏", "藏宝图", "出差", "参加", "列车长"};
		
		for (String chinese : arr){
			String py = pinyinConverter.getPinyin(chinese);
			System.out.println(chinese+"\t"+py);
		}
	}
	
	@Test
	public void testCharPy() throws IllegalPinyinException {

		char[] chs = {'长', '行', '藏', '度', '阿', '佛', '2', 'A', 'a'};
		for(char ch : chs){
			String[] arr_py = pinyinConverter.getPinyin(ch);
			System.out.println(ch+"\t"+Arrays.toString(arr_py));
		}
	}

	@After
	public void destroy(){
		pinyinConverter = null;
	}
}
