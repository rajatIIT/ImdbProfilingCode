package moviebuzz.filter;

/**
 * an Imdb movie. 
 * 
 * @author cloudera
 *
 */
public class Movie {
	
	
	int ImdbID;
	String movieName;
	
	public Movie (int id, String name) {
		this.ImdbID=id;
		this.movieName=name;
	}
	
	public Movie(String name) {
		this.movieName=name;
	}

}
