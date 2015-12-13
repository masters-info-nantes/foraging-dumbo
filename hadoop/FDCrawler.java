
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FDCrawler {
	
	private String startUrl;
	private int maxDepth;
	private String outputPath;
	
	public FDCrawler(String startUrl, int maxDepth, String outputPath) {
		this.startUrl = startUrl;
		this.maxDepth = maxDepth;
		this.outputPath = outputPath;
	}
	
	public File crawl() {
		// contains every known urls with list of all url it links in a balise
		HashMap<String, Set<String>> links = new HashMap<String, Set<String>>();
		// contains all waiting url, depts separate by a null value
		LinkedList<String> waiting = new LinkedList<String>();
		waiting.add(this.startUrl);
		waiting.add(null);
		
		Pattern pattern = Pattern.compile("<a href=\"\\S+\">");
		int currentDepth = 0;
		boolean levelEnd = false;
		while(!waiting.isEmpty() && currentDepth < this.maxDepth) {
			String currentUrl = waiting.removeFirst();
			if(links.get(currentUrl) != null) {
				continue;
			}
			
			if(currentUrl == null) {// new depth
				if(levelEnd) {
					// nothing in the last level, so nothing in all nexts
					break;
				}
				currentDepth++;
				waiting.addLast(null);// indicate start of a new depth level
				levelEnd = true;
			} else {
				levelEnd = false;
				String requestContent = readPage(currentUrl);
				Matcher matcher = pattern.matcher(requestContent);
				if(links.get(currentUrl) == null) {
					links.put(currentUrl,new HashSet<String>());
				}
				while (matcher.find()) {
					String newUrlWithBalise = matcher.group();
					System.out.println(" found "+newUrlWithBalise);
					int hrefIndex = newUrlWithBalise.indexOf("href=\"");
					int endHrefIndex = newUrlWithBalise.indexOf("\"",hrefIndex+6);
					String newUrl = newUrlWithBalise.substring(hrefIndex+6,endHrefIndex);
					if(newUrl.startsWith("http://") || newUrl.startsWith("https://")) {// do not take relative url
						links.get(currentUrl).add(newUrl);
						if(!links.containsKey(newUrl)) {
							waiting.addLast(newUrl);
						}
					}
				}
			}
		}
		return writeInFile(links);
	}
	
	private String readPage(String pageUrl) {
		try {
			URL url = new URL(pageUrl);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			
			InputStream is = conn.getInputStream();
			BufferedReader reader = new BufferedReader(
				new InputStreamReader(is)
			);
			
			StringBuilder buff = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				buff.append(line);
			}
			is.close();
			return buff.toString();
		} catch(Exception ex) {
			ex.printStackTrace();
			return "";
		}
	}
	
	private File writeInFile(HashMap<String, Set<String>> links) {
		File tmpFile = null;
		Writer fileWriter = null;
		BufferedWriter bufferedWriter = null;
		
		Set<String> keySet = links.keySet();
		try {
			tmpFile = new File(this.outputPath);
			fileWriter = new FileWriter(tmpFile);
			bufferedWriter = new BufferedWriter(fileWriter);
			for(String url : keySet) {
				bufferedWriter.write(url+"\t1.0\t");
				boolean first = true;
				for(String link : links.get(url)) {
					if(first) {
						first = false;
						bufferedWriter.write(link);
					} else {
						bufferedWriter.write(" "+link);
					}
				}
				bufferedWriter.newLine();
			}
		} catch(IOException ex) {
			ex.printStackTrace();
		} finally {
			if (bufferedWriter != null && fileWriter != null) {
				try {
					bufferedWriter.close();
					fileWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return tmpFile;
	}
}
