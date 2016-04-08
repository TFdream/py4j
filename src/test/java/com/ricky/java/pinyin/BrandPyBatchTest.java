package com.ricky.java.pinyin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class BrandPyBatchTest {

	private File dir = new File("C:/Users/YULORE-USER/Downloads");
	
	public static void main(String[] args) {
		
		new BrandPyBatchTest().test();
	}

	public void test(){
		
		BufferedReader br = null;
		try {
			InputStream in = new FileInputStream(new File(dir, "name"));
			br = new BufferedReader(new InputStreamReader(in,"UTF-8"));
			
			String line = null;
			while((line=br.readLine())!=null){
				
				try {
					System.out.println(line+"\t"+PinyinUtils.getPinYin(line));
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
