package moviebuzz.filter;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ActorFilter implements MovieFilter {

	boolean found=false;
	
	/**
	 * 
	 * fixedParamType here means the name of the Actor. 
	 * 
	 */
	public List<Movie> getMovieList(int fixedParamterType, String fixedParamValue, String pathToDataFile) {
		
		LinkedList<Movie> movieList = new LinkedList<Movie>();
		Pattern actorStart = Pattern.compile("(.*)" + fixedParamValue + "(.*)");
		
		
		try {
			
			Scanner myScanner = new Scanner(new File(pathToDataFile));
			
			while(myScanner.hasNextLine()){
			
				// if next line confers to a "IMDB start" pattern,
				
				String nextLine = myScanner.nextLine();
				if(!found){
				Matcher m = actorStart.matcher(nextLine);
				if(m.matches())
					found=true;
				}
				
				
				if(found){
					// we should record the movie name! 
					
					Pattern movieName = Pattern.compile("(.*)((\t)+)(.+)");
					Matcher insideLine = movieName.matcher(nextLine);
					if(insideLine.matches()){
						Pattern oneTab = Pattern.compile("(.*)\t(.+)");
						Matcher oneTabMatcher = oneTab.matcher(nextLine);
						Pattern twoTab = Pattern.compile("(.*)(\t\t)(.+)");
						Matcher twoTabMatcher = twoTab.matcher(nextLine);
						Pattern threeTab = Pattern.compile("(.*)(\t\t\t)(.+)");
						Matcher threeTabMatcher = threeTab.matcher(nextLine);
						if(oneTabMatcher.matches()){
							movieList.add( new Movie(nextLine.split("\t")[1]));						
						} else if (twoTabMatcher.matches()){
							movieList.add( new Movie(nextLine.split("\t\t")[1]));
						} else if (threeTabMatcher.matches()){
							movieList.add( new Movie(nextLine.split("\t\t\t")[1]));
						}
					}
				}
				
				if(found){
					// if the flag is on, and we encounter movie details related to a new actor, switch off the flag. 
					
					Pattern actorsMoviesListEnds = Pattern.compile("()");
					
				}
			}
			myScanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return movieList;
	}

}
