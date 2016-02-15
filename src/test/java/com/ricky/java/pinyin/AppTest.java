package com.ricky.java.pinyin;

import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {

	private String str = "中信银行(重庆旅行社分行)";

	@Test
	public void testPy() {

		try {
			System.out.println(str + " pyf="
					+ PinyinUtils.getPinYin(str, true));
			System.out.println(str + " pys="
					+ PinyinUtils.getPinYin(str, false));
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
	}
}
