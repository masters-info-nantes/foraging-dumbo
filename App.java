import java.io.IOException;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class App {

	public static void main(String[] args) throws Exception {
		if(args.length < 2) {
			System.err.println("Must be call with a url and a depth in parameter");
			System.exit(0);
		}
		try {
			URL url = new URL(args[0]);// just for check url validity
			int depth = Integer.parseInt(args[1]);
			
			FDCrawler crawler = new FDCrawler(url.toString(),depth);
			File input = crawler.crawl();
			
			//~ wordCount("input_word","output");
		} catch(MalformedURLException ex) {
			System.err.println("first argument must be a valid url");
		} catch(NumberFormatException ex) {
			System.err.println("second argument must be a number");
		}
	}
	
	public static void wordCount(String input, String output) throws Exception {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "word count");
		job.setJarByClass(App.class);
		job.setMapperClass(FDMapper.class);
		job.setCombinerClass(FDReducer.class);
		job.setReducerClass(FDReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		FileInputFormat.addInputPath(job, new Path(input));
		FileOutputFormat.setOutputPath(job, new Path(output));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
