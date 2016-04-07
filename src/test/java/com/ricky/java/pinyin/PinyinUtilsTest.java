package com.ricky.java.pinyin;

import java.util.Arrays;

import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class PinyinUtilsTest {

	@Test
	public void testPy() {
		
		String str = "长沙银行绿色 出行KFC";
		
		try {
			System.out.println(str + " pyf="
					+ PinyinUtils.getPinYin(str, true));
			System.out.println(str + " pys="
					+ PinyinUtils.getPinYin(str, false));
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testChar(){
		char ch = '芈';
		try {
			String[] arr_py = PinyinUtils.chineseToPinYin(ch);
			System.out.println(Arrays.toString(arr_py));
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
	}
}
