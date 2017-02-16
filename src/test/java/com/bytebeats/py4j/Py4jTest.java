package com.bytebeats.py4j;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

/**
 * Unit test for simple App.
 */
public class Py4jTest {

	private Py4j py4j;

	@Before
	public void init(){
		py4j = new Py4j();
	}

	@Test
	public void testChinesePy() {

		String chinese = "便宜坊";
		
		String py = py4j.getPinYin(chinese);
		System.out.println(py);
	}
	
	@Test
	public void testCharPy(){

		char ch = '冒';
		String[] arr_py = py4j.chineseToPinYin(ch);
		System.out.println(Arrays.toString(arr_py));
	}

	@After
	public void destroy(){
		py4j = null;
	}
}
