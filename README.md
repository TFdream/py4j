# py4j [![Build Status](https://travis-ci.org/TiFG/mario.svg?branch=master)](https://travis-ci.org/TiFG/py4j)
A open-source Java library for converting Chinese to Pinyin.

## Feature
* solve Chinese polyphone
* support external custom extension dictionary

## maven dependency
```
<dependency>
    <groupId>com.bytebeats</groupId>
    <artifactId>py4j</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

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

output:
```
长	[zhang, chang]
行	[xing, hang, hang, xing, heng]
藏	[zang, cang]
度	[du, duo, duo]
阿	[a, e, a, a, a]
佛	[fo, fu]
2	[2]
A	[A]
a	[a]
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

output:
```
肯德基	KenDeJi
重庆银行	ChongQingYinHang
长沙银行	ChangShaYinHang
便宜坊	BianYiFang
西藏	XiZang
藏宝图	CangBaoTu
出差	ChuChai
参加	CanJia
列车长	LieCheZhang
```

## Performance Tips
Py4j instances are Thread-safe so you can reuse them freely across multiple threads.
```
final String[] arr = {"大夫", "重庆银行", "长沙银行", "便宜坊", "西藏", "藏宝图", "出差", "参加", "列车长"};
final Py4j py4j = new Py4j();

int threadNum = 20;
ExecutorService pool = Executors.newFixedThreadPool(threadNum);
for(int i=0;i<threadNum;i++){
    pool.submit(new Callable<Void>() {
        @Override
        public Void call() throws Exception {

            System.out.println("thread "+Thread.currentThread().getName()+" start");
            for(int i=0;i<1000;i++){
                py4j.getPinyin(arr[i%arr.length]);
            }
            System.out.println("thread "+Thread.currentThread().getName()+" over");
            return null;
        }
    });
}

pool.shutdown();
```