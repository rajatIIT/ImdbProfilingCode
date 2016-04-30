package moviebuzz.profiling;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;


/**
 * 
 * Simple data profiling utility to convert tab-separated static info from imdb to the 
 * key-value format respected by Hadoop. 
 * {"<MovieName>":"<Genre>"}
 * 
 * 
 * @author rajatpawar
 *
 */
public class GenreJSONCoverter implements JSONConverter {
	
	JsonGenerator generator;
	FileWriter outputJSONWriter; 
	String currentGenre;
	String currentMovie;
    

	public void convertToJSON(String inputFilePath, String parameterType, String outputFilePath) {
		
		
	// read the input file, process each line and write it to the output 
		
		try {
			
			//Scanner inputFileScanner = new Scanner(new File(inputFilePath));
			
			// initiate writer to new file
			
			outputJSONWriter = new FileWriter(new File(outputFilePath));
			
			// use fstream and bufferedReader instead of scanner because scanner may not
			// be able to handle large files.
			FileInputStream fstream = new FileInputStream(inputFilePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			
			
            String strLine;
            
			while((strLine = br.readLine()) != null){
				
				
				String nextLine = strLine;
				// check if next line corresponds to the regex
				Pattern genrePattern = Pattern.compile(".+\\t+.+");
				Matcher patternMatcher = genrePattern.matcher(nextLine);
				if(patternMatcher.matches()){
					
					// extract the movie and the genre.
					
					// the movie name.
					currentMovie = nextLine.split("\t")[0].trim();
					currentGenre = nextLine.split("\t")[(nextLine.split("\t")).length-1].trim();
					
					writeToJSONFile();
					//outputJSONWriter.write(str);
					
				}
				outputJSONWriter.flush();
				
				
			}
			// all lines processed.
			br.close();
			outputJSONWriter.flush();
			outputJSONWriter.close();
			
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	
	public void writeToJSONFile() {
		// write the movie and the genre in the movie,genre format
		
		StringWriter genreJSONString = new StringWriter();
		JsonFactory factory = new JsonFactory();
		
		try {
			
			generator = factory.createJsonGenerator(genreJSONString);
			generator.writeStartObject();
			generator.writeStringField(currentMovie, currentGenre);
			generator.writeEndObject();
			generator.flush();
			generator.close();
			
			outputJSONWriter.write(genreJSONString.toString());
			outputJSONWriter.write("\n");
			outputJSONWriter.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

}
