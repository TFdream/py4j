package com.ricky.java.pinyin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class SolrPyTest {
	
	public static void main(String[] args) {
		
		SolrPyTest solrPyTest = new SolrPyTest();
		solrPyTest.initPinyin("duoyinzi_pinyin.txt");
		
		String chinese = "263网络通信";
		System.out.println(solrPyTest.convertChineseToPinyin(chinese));
	}
	
	private Map<String, String> pinyinMap = new HashMap<String, String>();
	
	public void initPinyin(String fileName) {
		try {
			File f = new File(fileName);
			FileInputStream is = new FileInputStream(f);

			BufferedReader br = new BufferedReader(new InputStreamReader(is,
					"UTF-8"));

			String s = null;

			while ((s = br.readLine()) != null) {
				if (s != null) {
					String[] arr = s.split("#");
					String pinyin = arr[0];
					String[] chinese = arr[1].split("/");
					for (int i = 0; i < chinese.length; i++)
						pinyinMap.put(chinese[i], pinyin);
				}
			}

			br.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String convertChineseToPinyin(String chinese) {

		StringBuffer pinyin = new StringBuffer();

		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

		char[] arr = chinese.toCharArray();
		//System.out.println(pinyinMap);
		for (int i = 0; i < arr.length; i++) {
			
			char ch = arr[i];
			System.out.println("CH"+"["+i+"] "+ch);

			if (ch > 128) {
				try {
					String[] results = PinyinHelper.toHanyuPinyinStringArray(
							ch, defaultFormat);

					if (results == null) {
						// non-Chinese, non-ASCII
					} else {
						for (int r=0;r<results.length;r++)
						{
							System.out.println("results"+"["+r+"] "+results[r]);
						}
						System.out.println("chinese:"+chinese);
						if (results.length == 1) // single pronunciation
							pinyin.append(convertInitialToUpperCase(results[0]));
						else if (results[0].equals(results[1]))
							pinyin.append(convertInitialToUpperCase(results[0]));
						else if (pinyinMap.containsKey(chinese))
							pinyin.append(convertInitialToUpperCase(pinyinMap
									.get(chinese)));
						else if (pinyinMap.containsKey(chinese + "-" + (i + 1)))
							pinyin.append(convertInitialToUpperCase(pinyinMap
									.get(chinese + "-" + (i + 1))));
						else
							pinyin.append(convertInitialToUpperCase(results[0]));
					}

				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			} else {
				pinyin.append(Character.toUpperCase(ch)); // ASCII
			}
		}
		System.out.println(pinyin.toString());
		return pinyin.toString();
	}

	public String convertInitialToUpperCase(String str) {
		if (str == null) {
			return null;
		}

		StringBuffer sb = new StringBuffer();
		char[] arr = str.toCharArray();
		for (int i = 0; i < arr.length; i++) {
			char ch = arr[i];
			if (i == 0) {
				sb.append(String.valueOf(ch).toUpperCase());
			} else {
				sb.append(ch);
			}
		}

		return sb.toString();
	}
}
