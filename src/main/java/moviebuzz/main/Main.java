package moviebuzz.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import moviebuzz.profiling.ActorCSVConverter;
import moviebuzz.profiling.BusinessJSONCoverter;
import moviebuzz.profiling.GenreJSONCoverter;

/**
 * 
 * Main class which controls profiling of all types of data.
 * 
 * @author rajatpawar
 *
 */
public class Main {

	public static void main(String[] args) {

		// control the application : central commands- filter

		// let us store the args in a file and then read from there.

		// try to include some parallelization into the project. Perform
		// filtering
		// using parallel processing.

		// filtering: cut files using movie name param.

		// How does Hadoop take standard input? like, for the text files.

		// mapper has key, value and context, and the value is each line.

		// paths of files in HDFS.

		// let us generate csv files that contain key value pairs as a part of
		// data profiling operation. This can also be represented as one of the
		// data parallelization tasks.

		System.out.println("We are running!");

		String filePath = args[0];

		System.out.println("Args file path: " + args[0]);

		Scanner argsScanner;
		try {
			argsScanner = new Scanner(new File(filePath));

			int argsCount = 0;

			ArrayList<String> argsList = new ArrayList<String>();

			while (argsScanner.hasNextLine()) {
				argsCount++;
				argsList.add(argsScanner.nextLine());
			}

			String[] allArgs = new String[argsCount];
			Iterator<String> argsListIt = argsList.iterator();

			for (int i = 0; i < argsCount; i++) {
				allArgs[i] = argsListIt.next();
			}

			String[] myArgs = allArgs.clone();

			if (myArgs[0].equals("filter")) {

				// we try to filter a file with a certain parameter .

				String taskType = myArgs[0];

				String arg1 = myArgs[1];

				String arg2 = myArgs[2];

				String arg3 = myArgs[3];

				if (taskType.equals("filter")) {

					// we need to filter from the actors list
					if (arg1.equalsIgnoreCase("actor")) {

						ActorCSVConverter converter = new ActorCSVConverter();
						converter.convertToJSON(arg2, "" + moviebuzz.filter.ParamterType.ACTOR,
								arg3);
					} else if (arg1.equalsIgnoreCase("genre")) {

						GenreJSONCoverter genreConverter = new GenreJSONCoverter();
						genreConverter.convertToJSON(arg2,
								"" + moviebuzz.filter.ParamterType.GENRE, arg3);

					} else if (arg1.equalsIgnoreCase("moviegross")) {

						BusinessJSONCoverter businessConverter = new BusinessJSONCoverter();
						businessConverter.convertToJSON(arg2, ""
								+ moviebuzz.filter.ParamterType.MOVIE_GROSS, arg3);

					}
				}

			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
