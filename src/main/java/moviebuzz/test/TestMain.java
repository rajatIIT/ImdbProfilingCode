package moviebuzz.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestMain {
    
    public static void main(String[] args) {
        
        String samplePattern = "'El Franc?s', Jos?	Alma gitana (1996)  <45>";
        samplePattern = "'El de Chipiona', Antonio	La guitarra muda (1953)  [Himself]";
      //  samplePattern="			Gala de verano (2000) (TV)  [Himself]";
  //     samplePattern= "			";
 //   samplePattern = "			Ghetto Physics (2010)";
        
       Pattern actorPlusMovie = Pattern.compile(".+\\t+.+");
        Pattern movieOnly = Pattern.compile("\\t\\t\\t.+");
        
        boolean aPlusm=false,movie=false;
        
        
        Matcher actorPlusMovieCheck = actorPlusMovie.matcher(samplePattern);
        if (actorPlusMovieCheck.matches()){
            aPlusm=true;
        }
        
       Matcher movieOnlyCheck = movieOnly.matcher(samplePattern);
    	   if(movieOnlyCheck.matches()){
    		   movie=true;
    	   }
       
    
        
    if(aPlusm && movie){
    	System.out.println("Just movie!");
    }
    
    if(aPlusm && !movie){
    	System.out.println("actor plus movie!");
    	
    	// separate actor from movie 
    	System.out.println(samplePattern.split("\t")[0]);
    	
    }
    
    
    }

}
