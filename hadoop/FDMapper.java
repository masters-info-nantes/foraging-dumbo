import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class FDMapper extends Mapper<LongWritable, Text, Text, Text>{
	
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		StringTokenizer itr = new StringTokenizer(value.toString());
		String pageUrl = itr.nextToken();
		float currentRank = Float.parseFloat(itr.nextToken());
		int nbLinks = itr.countTokens();
		
		StringBuilder pageLinks = new StringBuilder();
		pageLinks.append(">\t");
		
		Text currentPage = new Text(pageUrl+"\t"+currentRank+"\t"+nbLinks);
		Text link = new Text();
		String next = null;
		
		boolean first = true;
		while (itr.hasMoreTokens()) {
			next = itr.nextToken();
			link.set(next);
			context.write(link,currentPage);
			
			if(first) {
				first = false;
			} else {
				pageLinks.append(" ");
			}
			pageLinks.append(next);
		}
		context.write(new Text(pageUrl),new Text(pageLinks.toString()));
	}
}
