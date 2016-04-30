package moviebuzz.profiling;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;


/**
 * 
 * A utility used to profile the static "movie business information" provided by IMDB.
 * Using JSON as output format for consistency, simplicity and speed.
 * 
 * This utility converts the business info from IMDB's "tab-separated format" to 
 * key-value format respected by JSON and Hadoop. Specifically, it writes info in the 
 * following format : 
 * { "<Movie Name>" : "<Gross Revenue>" }
 * 
 * Since time provided to implement this project was limited and this project is more meant for 
 * illustrating working of Hadoop, we include only one Movie Gross info per movie. 
 * (which means that gross distribution for countries is ignored : Either one country gross or worldwide gross)
 * 
 * We write one JSON object per line as opposed to writing a large single 
 * object in the entire output file to make it suitable for parallel processing by Hadoop.
 * Hadoop divides the file into chunks and then processes them "line-by-line".
 *  
 * 
 * @author rajatpawar
 *
 */
public class BusinessJSONCoverter implements JSONConverter{

	JsonGenerator generator;
	FileWriter outputJSONWriter;
	String currentMovie;
	boolean writeGross;
	String currentGross;
	int LINE_TYPE_MOVIE=1,LINE_TYPE_GROSS=2,LINE_TYPE_UNKOWN=3;
	
	
	
	public void convertToJSON(String inputFilePath, String parameterType, String outputFilePath) {
	
	
		
		// read the business file and convert to string.
		try {
		System.out.println("execute convert business to JSON!");
			(new File(outputFilePath)).createNewFile();
            outputJSONWriter = new FileWriter(new File(outputFilePath));
		
			FileInputStream fstream = new FileInputStream(inputFilePath);
	        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			
			
	        String strLine;
	     
	        // process each line
	        while((strLine = br.readLine()) != null){
	        	
	        	String nextLine = strLine;
	        	
	        	// the business list contains a movie pattern and a gross pattern.
	        	
	        	
	        	int currrentType = getLineType(nextLine);
	        	
	        	if(currrentType==this.LINE_TYPE_MOVIE){
	        		//set the boolean flag
	        		writeGross=false;
	        		// set the movie name and set it.
	        		
	        		currentMovie = nextLine.split(":")[(nextLine.split(":")).length -1].trim();
	        		
	        		
	        	} else if (currrentType==this.LINE_TYPE_GROSS){
	        		
	        		// update current gross string
	        		currentGross = nextLine.split(":")[(nextLine.split(":")).length-1].trim();
	        		
	        		if(!writeGross){
	        		// write entry to JSON file.
	        		writeEntryToFile();
	        		// switch off flag
	        		writeGross=true;
	        		}
	        	}
	        	 
	        	
	        	
	        }
		
		
		
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
        
		
		
	
	}
	
	
	public void writeEntryToFile() {
		
		
		StringWriter grossLineWriter = new StringWriter();
        JsonFactory factory = new JsonFactory();
		
        try {
        	generator = factory.createJsonGenerator(grossLineWriter);
            generator.writeStartObject();
            generator.writeStringField(currentMovie, currentGross);
            generator.writeEndObject();
	        generator.flush();
	        generator.close();
	        String writeLine = grossLineWriter.toString();
	        outputJSONWriter.write(writeLine);
	        outputJSONWriter.write("\n");
	        outputJSONWriter.flush();
	        
	        // writing of line to the file complete.
		
	} catch (JsonGenerationException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
		
	}
	
	public int getLineType(String line) {
		
		
		
		Pattern moviePattern = Pattern.compile("MV:.+");
		
		Pattern grossPattern = Pattern.compile("GR:.+");
		
		Matcher movieMatcher = moviePattern.matcher(line);
		Matcher grossMatcher = grossPattern.matcher(line);
		
		if(movieMatcher.matches()){
			return this.LINE_TYPE_MOVIE;
		} else if (grossMatcher.matches()){
			return this.LINE_TYPE_GROSS;
		}
		
		return LINE_TYPE_UNKOWN;
	}

}
