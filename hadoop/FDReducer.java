import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class FDReducer extends Reducer<Text,Text,Text,Text> {
	
	private static final double damping = 0.85D;
	
	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		double sumShareOtherPageRanks = 0.0d;
		String str = "";
		
		String pageLinks = "";
		
		for(Text linkRank : values) {
			String[] arrayLinkRank = linkRank.toString().split("\t");
			
			if(">".equals(arrayLinkRank[0])) {
				pageLinks = linkRank.toString().substring(2);
				continue;
			}

			String linkUrl = arrayLinkRank[0];
			double linkPageRank = Double.valueOf(arrayLinkRank[1]);
			int linkNumberOfOutLink = Integer.valueOf(arrayLinkRank[2]);
			
			sumShareOtherPageRanks += (linkPageRank/linkNumberOfOutLink);
		}
		
		double newRank = damping * sumShareOtherPageRanks + (1-damping);
		
		context.write(key,new Text(newRank+"\t"+pageLinks));
	}
	
}
