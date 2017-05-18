package com.mindflow.py4j;

import com.mindflow.py4j.exception.IllegalPinyinException2;

/**
 * @author Ricky Fung
 */
public interface Converter {

    String[] getPinyin(char ch) throws IllegalPinyinException2;

    String getPinyin(String chinese) throws IllegalPinyinException2;
}
