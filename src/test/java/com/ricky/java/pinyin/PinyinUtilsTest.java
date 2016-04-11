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
		//
		String str = "便宜坊";
		
		try {
			System.out.println(str + " pyf="
					+ PinyinUtils.getPinYin(str));
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testChar(){
		char ch = '冒';
		try {
			String[] arr_py = PinyinUtils.chineseToPinYin(ch);
			System.out.println(Arrays.toString(arr_py));
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testUp(){
		
		System.out.println(PinyinUtils.convertInitialToUpperCase("t"));
	}
}
