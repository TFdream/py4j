package com.bytebeats.py4j;

import com.bytebeats.py4j.util.StringUtils;
import com.google.common.collect.ArrayListMultimap;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * ${DESCRIPTION}
 *
 * @author Ricky Fung
 * @date 2017-02-16 20:16
 */
public class Py4jDictionary {

    private final ArrayListMultimap<String,String> duoYinZiMap = ArrayListMultimap.create(512, 16);

    private static final String PINYIN_SEPARATOR = "#";

    private static final String WORD_SEPARATOR = "/";

    private volatile boolean inited;

    private Py4jDictionary(){

    }

    public void init(){
        if(inited){
            return;
        }
        String dict = "py4j/py4j_core.dic";
        System.out.println("load dict:"+dict);
        BufferedReader br = null;
        try {
            InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(dict);
            br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            String line = null;
            while ((line = br.readLine()) != null) {

                String[] arr = line.split(PINYIN_SEPARATOR);

                if (StringUtils.isNotEmpty(arr[1])) {
                    String[] dyzs = arr[1].split(WORD_SEPARATOR);
                    for (String dyz : dyzs) {
                        if (StringUtils.isNotEmpty(dyz)) {
                            duoYinZiMap.put(arr[0], dyz.trim());
                        }
                    }
                }
            }
        } catch (Exception e){
            throw new IllegalArgumentException("");
        }
    }

    ArrayListMultimap<String,String> getDuoYinZiMap(){
        return duoYinZiMap;
    }

    public static Py4jDictionary getDefault(){
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final Py4jDictionary INSTANCE = new Py4jDictionary();
    }
}
