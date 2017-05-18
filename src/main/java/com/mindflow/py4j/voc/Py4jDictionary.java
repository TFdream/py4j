package com.mindflow.py4j.voc;

import com.mindflow.py4j.util.IoUtils;
import com.mindflow.py4j.util.StringUtils;
import com.google.common.collect.ArrayListMultimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;

/**
 *
 * @author Ricky Fung
 */
public class Py4jDictionary {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String PREFIX = "META-INF/vocabulary/";

    private ArrayListMultimap<String,String> duoYinZiMap;

    private static final String CONFIG_NAME = "py4j.txt";

    private static final String PINYIN_SEPARATOR = "#";

    private static final String WORD_SEPARATOR = "/";

    private volatile boolean initialized;

    public Py4jDictionary(){

    }

    public static Py4jDictionary getDefault(){
        return SingletonHolder.INSTANCE;
    }

    public ArrayListMultimap<String,String> getDuoYinZiMap(){
        checkInit();
        return duoYinZiMap;
    }

    private void checkInit() {
        if (!initialized) {
            loadVocabulary();
        }
    }

    private synchronized void loadVocabulary() {
        if(initialized){
            return;
        }
        this.duoYinZiMap = loadVocabulary0(CONFIG_NAME);
        initialized = true;
    }

    private ArrayListMultimap<String, String> loadVocabulary0(String name) {
        debug("******start load py4j config******");
        ArrayListMultimap<String,String> duoYinZiMap = ArrayListMultimap.create(512, 32);
        String filename = PREFIX + name;
        try{
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            Enumeration<URL> urls = cl.getResources(filename);
            if(urls!=null){
                while (urls.hasMoreElements()) {
                    URL url = urls.nextElement();
                    parseURL(url, duoYinZiMap);
                }
            }
        } catch (Exception e){
            error("caught exception when load py4j vocabulary", e);
            throw new RuntimeException("caught exception when load py4j vocabulary", e);
        }
        debug("******load py4j config over******");
        debug("py4j map key size:{}", duoYinZiMap.keySet().size());
        return duoYinZiMap;
    }

    private void parseURL(URL url, ArrayListMultimap<String, String> duoYinZiMap){
        debug("load py4j dictionary file:{}", url.getPath());
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
            throw new RuntimeException(String.format("load py4j config:%s error", url), e);
        } finally {
            IoUtils.closeQuietly(br);
            IoUtils.closeQuietly(in);
        }
    }

    private void error(String msg, Throwable err){
        logger.error(msg, err);
    }

    private void debug(String msg, Object... args) {
        if(logger.isDebugEnabled()) {
            logger.debug(msg, args);
        }
    }

    private static class SingletonHolder {
        private static final Py4jDictionary INSTANCE = new Py4jDictionary();
    }
}
