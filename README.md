# Foraging Dumbo

PageRank implementation on the Hadoop plateform and with Pig Latin.

## Hadoop

### Prerequisites

- correctly installed Hadoop
- (optional) define a variable $HADOOP_ROOT containing path to hadoop installation :
```
export HADOOP_ROOT="/path/to/hadoop-2.7.1"
```
- make sure $HADOOP_CLASSPATH is set
```
export HADOOP_CLASSPATH=${JAVA_HOME}/lib/tools.jar
```

### Compile and make jar

```
$HADOOP_ROOT/bin/hadoop com.sun.tools.javac.Main App.java FDMapper.java FDReducer.java FDCrawler.java
jar cf ForagingDumbo.jar App.class FDMapper.class FDReducer.class FDCrawler.class
```

### Run the crawler

```
$HADOOP_ROOT/bin/hadoop jar ForagingDumbo.jar App crawl <url> <max_depth> <output file>
```

### Run the page rank operation

```
$HADOOP_ROOT/bin/hadoop jar ForagingDumbo.jar App rank <input path> <output path>
```


## Pig

### Prerequisites

- correctly installed Pig Latin
- (optional) define a variable $PIG_ROOT containing path to pig installation :
```
export PIG_ROOT="/path/to/pig-0.15.0"
```

## Run script

```
$PIG_ROOT/bin/pig -x local -p INPUT=<input path> -p OUTPUT=<output path> pagerank.pl
```

## Using examples 

```
cd input
python2 -m SimpleHTTPServer 8080
```

Then, when running application, you can pass "http://localhost:8080" as url.

## Results Given

You can find into hadoop_res and pig_res the result of each iteration of the pageRank.

Both start from crawl.stallman.txt, crawled with as starting url the Richard Stallman's personal page.

## Links

- [https://en.wikipedia.org/wiki/PageRank](https://en.wikipedia.org/wiki/PageRank)
- [http://hadoop.apache.org/docs/current/hadoop-mapreduce-client/hadoop-mapreduce-client-core/MapReduceTutorial.html](http://hadoop.apache.org/docs/current/hadoop-mapreduce-client/hadoop-mapreduce-client-core/MapReduceTutorial.html)

![Elephant honey](http://www.elephanthoney.com/wp-content/uploads/Elephant-Honey-wallpaper-1920x1200.jpg)
