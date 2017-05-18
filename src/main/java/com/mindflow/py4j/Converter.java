package com.mindflow.py4j;

import com.mindflow.py4j.exception.IllegalPinyinException;

/**
 * @author Ricky Fung
 */
public interface Converter {

    String[] getPinyin(char ch) throws IllegalPinyinException;

    String getPinyin(String chinese) throws IllegalPinyinException;
}
