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
		if(args.length < 3) {
			System.out.println(
				"You must specify one of the following command :\n"
				+"\tcrawl <url> <depth> <output file>\n"
				+"\trank <input path> <output path>"
			);
			System.exit(0);
		}
		
		switch(args[0]) {
			case "crawl":
				if(args.length < 4) {
					System.out.println(
						"You must specify one of the following command :\n"
						+"\tcrawl <url> <depth> <output file>\n"
						+"\trank <input path> <output path>"
					);
					System.exit(0);
				}
				cmdCrawl(args);
				break;
			case "rank":
				cmdRank(args);
				break;
			default:
				System.out.println("unrecognize command");
				System.exit(0);
		}
	}
	
	public static void cmdCrawl(String[] args) throws Exception {
		try {
			URL url = new URL(args[1]);// just for check url validity
			int depth = Integer.parseInt(args[2]);
			
			FDCrawler crawler = new FDCrawler(url.toString(),depth,args[3]);
			File crawlOutput = crawler.crawl();
		} catch(MalformedURLException ex) {
			System.err.println("first argument must be a valid url");
		} catch(NumberFormatException ex) {
			System.err.println("second argument must be a number");
		}
	}
	
	public static void cmdRank(String[] args) throws Exception {
		String input = args[1];
		String output = args[2];
		
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "pageRank");
		job.setJarByClass(App.class);
		job.setMapperClass(FDMapper.class);
		job.setReducerClass(FDReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		FileInputFormat.addInputPath(job, new Path(input));
		FileOutputFormat.setOutputPath(job, new Path(output));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

}
