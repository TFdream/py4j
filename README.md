# py4j
A Chinese pinyin library for Java.

## maven dependency
```
<dependency>
    <groupId>com.bytebeats</groupId>
    <artifactId>py4j</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

## Feature
* solve Chinese polyphone
* support external custom extension dictionary

## Usage
### 1. single char
```
Py4j py4j = new Py4j();

char[] chs = {'长', '行', '藏', '度', '阿', '佛', '2', 'A', 'a'};
for(char ch : chs){
    String[] arr_py = py4j.getPinyin(ch);
    System.out.println(ch+"\t"+Arrays.toString(arr_py));
}
```

### 2. Chinese word
```
Py4j py4j = new Py4j();

final String[] arr = {"肯德基", "重庆银行", "长沙银行", "便宜坊", "西藏", "藏宝图", "出差", "参加", "列车长"};
for (String chinese : arr){
    String py = py4j.getPinyin(chinese);
    System.out.println(chinese+"\t"+py);
}
```

## Performance Tips
Py4j instances are Thread-safe so you can reuse them freely across multiple threads.
