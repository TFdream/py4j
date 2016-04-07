package com.ricky.java.pinyin;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.ArrayListMultimap;

public class PinyinUtils {

	private static final ArrayListMultimap<String,String> duoYinZiMap = ArrayListMultimap.create(1024, 16);

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
	public static String getPinYin(String chinese, boolean fullPy) throws BadHanyuPinyinOutputFormatCombination{
		if(StringUtils.isEmpty(chinese)){
			return null;
		}
		
		chinese = chinese.replace(" ", "");//消除空格
		
		char[] chs = chinese.toCharArray();
		StringBuilder result = new StringBuilder(20);
		
		for(int i=0;i<chs.length;i++){
			String[] arr = chineseToPinYin(chs[i]);
			if(arr==null || arr.length<1){
				if(duoYinZiMap.containsKey(String.valueOf(chs[i]))){
					String py = duoYinZiMap.get(String.valueOf(chs[i])).get(0);
					result.append(fullPy ? py:py.charAt(0));
					continue;
				}
				throw new RuntimeException("not find pin yin for:"+chs[i]);
			}
			if(arr.length==1){
				result.append(fullPy ? arr[0]:arr[0].charAt(0));
			}else if(arr.length==2 && arr[0].equals(arr[1])){
				result.append(fullPy ? arr[0]:arr[0].charAt(0));
			}else{
				
				String resultPy = null;
				for (String py : arr) {
					
					String lst = null;
					if(i>=1 && i+1<=chinese.length()){
						lst = chinese.substring(i-1,i+1);
						if(duoYinZiMap.containsKey(lst) && duoYinZiMap.get(lst).contains(py)){
							resultPy = py;
							break;
						}
					}
					
					String rst = null;
					if(i<=chinese.length()-2){
						rst = chinese.substring(i,i+2);
						if(duoYinZiMap.containsKey(rst) && duoYinZiMap.get(rst).contains(py)){
							resultPy = py;
							break;
						}
					}
					
				}
				
				if(StringUtils.isEmpty(resultPy)){
					if(duoYinZiMap.containsKey(String.valueOf(chs[i]))){
						resultPy = duoYinZiMap.get(String.valueOf(chs[i])).get(0);
					}else{
						resultPy = arr[0];
					}
				}
				result.append(fullPy ? resultPy:resultPy.charAt(0));
			}
		}
		
		return result.toString().toLowerCase();
	}
			
}

