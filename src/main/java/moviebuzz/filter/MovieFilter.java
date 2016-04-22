package moviebuzz.filter;
import java.util.List;



public interface MovieFilter {

	
	public List<Movie> getMovieList(int fixedParamterType, String fixedParamValue, String pathToDataFile);
	
}
