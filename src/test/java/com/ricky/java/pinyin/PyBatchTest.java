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
import org.apache.commons.io.IOUtils;

public class PyBatchTest {

	private File dir = new File("C:/Users/YULORE-USER/Downloads");
	
	public static void main(String[] args) {
		
		new PyBatchTest().test();
	}

	public void test(){
		
		BufferedReader br = null;
		try {
			InputStream in = new FileInputStream(new File(dir, "词库.txt"));
			br = new BufferedReader(new InputStreamReader(in,"UTF-8"));
			
			String line = null;
			while((line=br.readLine())!=null){
				
				String[] arr = line.split(" ");
				if(arr.length<2){
					continue;
				}
				for(int i=1;i<arr.length;i++){
					try {
//						System.out.println(arr[i]+"\t"+PinyinUtils.getPinYin(arr[i], true));
						PinyinUtils.getPinYin(arr[i], true);
					} catch (BadHanyuPinyinOutputFormatCombination e) {
						e.printStackTrace();
					}catch (Exception e) {
						e.printStackTrace();
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
}
