package com.ricky.java.pinyin;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

public class PinyinUtils {

	private static final HashMap<String,String> duoYinZiMap = new HashMap<>(1024);

	public static final String pinyin_sep = "#";
	
	public static final String word_sep = "/";
	
	//加载多音字词典
	static{
		String filename = "duoyinzi_pinyin.txt";
		System.out.println("load dict:"+filename);
		BufferedReader br = null;
		try {
			InputStream in = PinyinUtils.class.getClassLoader().getResourceAsStream(filename);
			br = new BufferedReader(new InputStreamReader(in,"UTF-8"));
			
			String line = null;
			while((line=br.readLine())!=null){
				
				String[] arr = line.split(pinyin_sep);
				
				if(StringUtils.isNotEmpty(arr[1])){
					String[] sems = arr[1].split(word_sep);
					for (String sem : sems) {
						if(StringUtils.isNotEmpty(sem)){
							duoYinZiMap.put(sem , arr[0]);
						}
					}
				}
			}
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			IOUtils.closeQuietly(br);
		}

	}
	
	public static String[] chineseToPinYin(char chineseCharacter) throws BadHanyuPinyinOutputFormatCombination{
		HanyuPinyinOutputFormat outputFormat = new HanyuPinyinOutputFormat();
		outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		outputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		outputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
		
		if(chineseCharacter>=32 && chineseCharacter<=125){	//ASCII >=33 ASCII<=125的直接返回 ,ASCII码表：http://www.asciitable.com/
			return new String[]{String.valueOf(chineseCharacter)};
		}
		
		return PinyinHelper.toHanyuPinyinStringArray(chineseCharacter, outputFormat);
	}
	
	/**
	 * get chinese words pin yin
	 * @param chinese
	 * @param fullPy
	 * @return
	 * @throws BadHanyuPinyinOutputFormatCombination
	 */
	public static String getPinYin(String chinese) throws BadHanyuPinyinOutputFormatCombination{
		if(StringUtils.isEmpty(chinese)){
			return null;
		}
		
		chinese = chinese.replaceAll("[\\.，\\,！·\\!？\\?；\\;\\(\\)（）\\[\\]\\:： ]+", " ").trim();
		
		char[] chs = chinese.toCharArray();
		StringBuilder py_sb = new StringBuilder(20);
		
		for(int i=0;i<chs.length;i++){
			String[] arr = chineseToPinYin(chs[i]);
			if(arr==null || arr.length<1){
				if(duoYinZiMap.containsKey(String.valueOf(chs[i]))){
					String py = duoYinZiMap.get(String.valueOf(chs[i]));
					py_sb.append(convertInitialToUpperCase(py));
					continue;
				}
				throw new BadHanyuPinyinOutputFormatCombination("pin yin array is empty, char:"+chs[i]+",chinese:"+chinese);
			}
			if(arr.length==1){
				py_sb.append(convertInitialToUpperCase(arr[0]));
			}else if(arr.length==2 && arr[0].equals(arr[1])){
				py_sb.append(convertInitialToUpperCase(arr[0]));
			}else{
				String resultPy = null;
				for (String py : arr) {
					
					String left = null;	//向左多取一个字,例如 银[行]
					if(i>=1 && i+1<=chinese.length()){
						left = chinese.substring(i-1,i+1);
						if(duoYinZiMap.containsKey(left) && duoYinZiMap.get(left).equals(py)){
							resultPy = py;
							break;
						}
					}
					
					String right = null;	//向右多取一个字,例如 [长]沙
					if(i<=chinese.length()-2){
						right = chinese.substring(i,i+2);
						if(duoYinZiMap.containsKey(right) && duoYinZiMap.get(right).equals(py)){
							resultPy = py;
							break;
						}
					}
					
					String middle = null;	//左右各多取一个字,例如 龙[爪]槐
					if(i>=1 && i+2<=chinese.length()){
						middle = chinese.substring(i-1,i+2);
						if(duoYinZiMap.containsKey(middle) && duoYinZiMap.get(middle).equals(py)){
							resultPy = py;
							break;
						}
					}
					String left3 = null;	//向左多取2个字,如 芈月[传],列车长
					if(i>=2 && i+1<=chinese.length()){
						left3 = chinese.substring(i-2,i+1);
						if(duoYinZiMap.containsKey(left3) && duoYinZiMap.get(left3).equals(py)){
							resultPy = py;
							break;
						}
					}
					
					String right3 = null;	//向右多取2个字,如 [长]孙无忌
					if(i<=chinese.length()-3){
						right3 = chinese.substring(i,i+3);
						if(duoYinZiMap.containsKey(right3) && duoYinZiMap.get(right3).equals(py)){
							resultPy = py;
							break;
						}
					}
				}
				
				if(StringUtils.isEmpty(resultPy)){
					if(duoYinZiMap.containsKey(String.valueOf(chs[i]))){	//默认拼音
						resultPy = duoYinZiMap.get(String.valueOf(chs[i]));
					}else{
						resultPy = arr[0];
					}
				}
				py_sb.append(convertInitialToUpperCase(resultPy));
			}
		}
		
		return py_sb.toString();
	}
	
	public static String convertInitialToUpperCase(String str) {
		if (str == null || str.length()==0) {
			return "";
		}
		return str.substring(0, 1).toUpperCase()+str.substring(1);
	}
}

