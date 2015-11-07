# Foraging Dumbo

PageRank implementation on the Hadoop plateform.

## Prerequisites

- correctly installed Hadoop
- (optional) define a variable $HADOOP_ROOT containing path to hadoop installation :
```
export HADOOP_ROOT="/path/to/hadoop-2.7.1"
```
- make sure $HADOOP_CLASSPATH is set
```
export HADOOP_CLASSPATH=${JAVA_HOME}/lib/tools.jar
```

## Compile and make jar

```
$HADOOP_ROOT/bin/hadoop com.sun.tools.javac.Main App.java FDMapper.java FDReducer.java FDCrawler.java
jar cf ForagingDumbo.jar App.class FDMapper.class FDReducer.class FDCrawler.class
```

## Run the application

```
$HADOOP_ROOT/bin/hadoop jar ForagingDumbo.jar App <url> <max_depth>
```

## Using examples 

```
cd input
python2 -m SimpleHTTPServer 8080
```

Then, when running application, you can pass "http://localhost:8080" as url.

## Links

- [https://en.wikipedia.org/wiki/PageRank](https://en.wikipedia.org/wiki/PageRank)
- [http://hadoop.apache.org/docs/current/hadoop-mapreduce-client/hadoop-mapreduce-client-core/MapReduceTutorial.html](http://hadoop.apache.org/docs/current/hadoop-mapreduce-client/hadoop-mapreduce-client-core/MapReduceTutorial.html)

![Elephant honey](http://www.elephanthoney.com/wp-content/uploads/Elephant-Honey-wallpaper-1920x1200.jpg)
