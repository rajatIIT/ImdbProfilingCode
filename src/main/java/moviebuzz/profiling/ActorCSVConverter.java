package moviebuzz.profiling;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;





public class ActorCSVConverter implements JSONConverter{
    
    String currentName;
    String currentMovie;
    JsonGenerator generator;
    FileWriter writer;
    final int LINE_TYPE_MOVIE_ONLY=0;
    final int LINE_TYPE_ACTOR_PLUS_MOVIE=1;
    final int LINE_NOT_KNOWN_TYPE=2;


    public void convertToJSON(String inputFilePath, String parameterType, String outputFilePath) {
            
        // conversion to CSV is a one time task and has to be done sequentially.
        System.out.println("Start conversion of actors to JSON.");
        
        try {
        	(new File(outputFilePath)).createNewFile();
            writer = new FileWriter((new File(outputFilePath)));
            
        
        
        if(parameterType.equals("" + moviebuzz.filter.ParamterType.ACTOR)){
        	
        	System.out.println("Working on actors file.");
           
         // perform sequential processing of the actor file.
            File readFile = new File(inputFilePath);
         
            
         // Open the file
            FileInputStream fstream = new FileInputStream(inputFilePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

            String strLine;


            
            
            
            
            while ((strLine = br.readLine()) != null){
             
           
               String nextLine = strLine;
               System.out.println(nextLine);
                
               // if next Line confers to the movie + actor regex, set/rename the current actor. 
               
               //Pattern actorPlusMovie = Pattern.compile("([a-zA-Z_0-9]+)((\t)+)([a-zA-Z_0-9]+)");
               
               int currentLineType = getLineType(nextLine);
               
               if(currentLineType==LINE_TYPE_MOVIE_ONLY){
            	   // just a movie name.
            	   // TODO : write current movie.
            	   currentMovie = nextLine.trim();
            	   System.out.println("Movie set to " + currentMovie);
            	   writeToFile();
               } else if (currentLineType == LINE_TYPE_ACTOR_PLUS_MOVIE ){
            	   //TODO : change current movie name
            	   // TODO : write current movie. 
            	   //currentMovie
            	   currentName = (nextLine.split("\t")[0]).trim();
            	   currentMovie = (nextLine.split("\t")[(nextLine.split("\t").length -1)]).trim();
            	   System.out.println(Arrays.toString(nextLine.split("\t")));
            	   System.out.println("actor set to " + currentName);
            	   System.out.println("Movie set to " + currentMovie);
            	   writeToFile();
               }
               writer.flush(); 
            }
            br.close();
            
            
        }
        
        writer.flush();
        writer.close();
        
        } catch (IOException e) {
        	System.out.println("IOEXception!");
            e.printStackTrace();
        }
        
        // specifications of map and reduce tasks 
        
        // map -> input line by line 
        // line in format (./)/t(./) etc.
        
        /**
         * 
         * Mapper Code
         * 
         * while(we have more lines){
         * 
         * String l = getNextLine();
         * 
         * now l can be of two types : (actor name + movie) or (movie name)
         * 
         * String currentName=null;
         * 
         * if(a+m){
         * 
         * currentName=a;
         * 
         * 
         * }
         * 
         * 
         * if (movie name) {
         * 
         * writeCSV(currentName,movie);
         * 
         * }
         * 
         * 
         * }
         * 
         * 
         * 
         */
        
    }
    
    
    public int getLineType(String line) {
    	
    	
        Pattern actorPlusMovie = Pattern.compile(".+\\t+.+");
        Pattern movieOnly = Pattern.compile("\\t\\t\\t.+");
        
        boolean aPlusm=false,movie=false;
        
        Matcher actorPlusMovieCheck = actorPlusMovie.matcher(line);
        if (actorPlusMovieCheck.matches()){
            aPlusm=true;
        }
        
       Matcher movieOnlyCheck = movieOnly.matcher(line);
    	   if(movieOnlyCheck.matches()){
    		   movie=true;
    	   }
    
        
    if(aPlusm && movie){
    	System.out.println("Just movie!");
    	return LINE_TYPE_MOVIE_ONLY;
    }
    
    if(aPlusm && !movie){
    	System.out.println("actor plus movie!");
    	return LINE_TYPE_ACTOR_PLUS_MOVIE;
    }
    	return LINE_NOT_KNOWN_TYPE;
    	
    }
    
    public void writeToFile() {
        
        StringWriter actorLineWriter = new StringWriter();
        JsonFactory factory = new JsonFactory();
       
        try {
        	 generator = factory.createJsonGenerator(actorLineWriter);
             generator.writeStartObject();
			generator.writeStringField(currentName, currentMovie);
			generator.writeEndObject();
	        generator.flush();
	        generator.close();
	        String writeLine = actorLineWriter.toString();
	        writer.write(writeLine);
	        System.out.println("write " + writeLine);
	        writer.write("\n");
	        writer.flush();
	        // write to the file.
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        
        
    }

}
