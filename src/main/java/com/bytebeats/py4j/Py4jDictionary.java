package com.bytebeats.py4j;

import com.bytebeats.py4j.util.IoUtils;
import com.bytebeats.py4j.util.StringUtils;
import com.google.common.collect.ArrayListMultimap;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;

/**
 * ${DESCRIPTION}
 *
 * @author Ricky Fung
 * @date 2017-02-16 20:16
 */
public class Py4jDictionary {

    private final ArrayListMultimap<String,String> duoYinZiMap = ArrayListMultimap.create(512, 16);

    private static final String PREFIX = "py4j/dictionary/";

    private static final String CONFIG_NAME = "py4j.dic";

    private static final String PINYIN_SEPARATOR = "#";

    private static final String WORD_SEPARATOR = "/";

    private volatile boolean inited;

    private Py4jDictionary(){

    }

    public void init(){
        if(inited){
            return;
        }
        System.out.println("******start load py4j config******");
        Enumeration<URL> configs = null;
        try{
            String fullName = PREFIX + CONFIG_NAME;
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            configs = cl.getResources(fullName);
        } catch (Exception e){
            e.printStackTrace();
        }

        if(configs!=null){
            duoYinZiMap.clear();
            while (configs.hasMoreElements()) {
                parse(configs.nextElement(), duoYinZiMap);
            }
        }
        inited = true;

        System.out.println("******load py4j config over******");
        System.out.println("py4j map key size:"+duoYinZiMap.keySet().size());
    }

    private void parse(URL url, ArrayListMultimap<String, String> duoYinZiMap){
        System.out.println("parse py4j dictionary:"+url.getPath());
        InputStream in = null;
        BufferedReader br = null;
        try {
            in = url.openStream();
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
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IoUtils.closeQuietly(br);
            IoUtils.closeQuietly(in);
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
