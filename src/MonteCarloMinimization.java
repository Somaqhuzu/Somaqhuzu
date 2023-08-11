

/* Serial  program to use Monte Carlo method to 
 * locate a minimum in a function
 * This is the reference sequential version (Do not modify this code)
 * Michelle Kuttel 2023, University of Cape Town
 * Adapted from "Hill Climbing with Montecarlo"
 * EduHPC'22 Peachy Assignment" 
 * developed by Arturo Gonzalez Escribano  (Universidad de Valladolid 2021/2022)
 */
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

class MonteCarloMinimization {
	static final boolean DEBUG = false;

	static long startTime = 0;
	static long endTime = 0;

	// timers - note milliseconds
	private static void tick() {
		startTime = System.currentTimeMillis();
	}

	private static void tock() {
		endTime = System.currentTimeMillis();
	}

	public static void main(String[] args) throws IOException {

		/*Speed= totalSearches/timeTook for all searches
		 *Percent = numberofSearches/totalSearches
		 * each line on file speed will be in this format --> {Speed,Percent}
		*/
		File speed = null;
		FileWriter speedWriter=null;
		PrintWriter speedPrinter=null;		
		
		try{
			speed = new File("SerialSpeed.csv");
			if (!speed.exists()){
				speedWriter = new FileWriter(speed);
				speedPrinter = new PrintWriter(speedWriter);
				speedPrinter.print("Speed,Percent");
			}
			else{
				speedWriter = new FileWriter(speed,true);
				speedPrinter = new PrintWriter(speedWriter);
			}
		}
		catch(IOException e){System.exit(0);}

		int rows, columns; // grid size
		double xmin, xmax, ymin, ymax; // x and y terrain limits
		TerrainArea terrain; // object to store the heights and grid points visited by searches
		double searches_density; // Density - number of Monte Carlo searches per grid position - usually less
		// than 1!

		int num_searches; // Number of searches
		Search[] searches; // Array of searches
		Random rand = new Random(); // the random number generator

		if (args.length != 8) {
			System.out.println("Incorrect number of command line arguments provided.");
			rows = 100;
			columns = 100;
			xmin = 50;
			xmax = 75;
			ymin = 12;
			ymax = 80;
			searches_density = 20;
		} else {
			/* Read argument values */
			rows = Integer.parseInt(args[0]);
			columns = Integer.parseInt(args[1]);
			xmin = Double.parseDouble(args[2]);
			xmax = Double.parseDouble(args[3]);
			ymin = Double.parseDouble(args[4]);
			ymax = Double.parseDouble(args[5]);
			searches_density = Double.parseDouble(args[6]);
		}

		if (DEBUG) {
			/* Print arguments */
			System.out.printf("Arguments, Rows: %d, Columns: %d\n", rows, columns);
			System.out.printf("Arguments, x_range: ( %f, %f ), y_range( %f, %f )\n", xmin, xmax, ymin, ymax);
			System.out.printf("Arguments, searches_density: %f\n", searches_density);
			System.out.printf("\n");
		}

		// Initialize
		terrain = new TerrainArea(rows, columns, xmin, xmax, ymin, ymax);
		num_searches = (int) (rows * columns * searches_density);
		searches = new Search[num_searches];
		for (int i = 0; i < num_searches; i++)
			searches[i] = new Search(i + 1, rand.nextInt(rows), rand.nextInt(columns), terrain);

		if (DEBUG) {
			/* Print initial values */
			System.out.printf("Number searches: %d\n", num_searches);
			// terrain.print_heights();
		}

		// start timer
		tick();

		// all searches
		int min = Integer.MAX_VALUE;
		int local_min = Integer.MAX_VALUE;
		int finder = 0;
		for (int i = 0; i < num_searches; i++) {
			local_min = searches[i].find_valleys();
			if ((!searches[i].isStopped()) && (local_min < min)) { // don't look at those who stopped because hit
				// exisiting path
				min = local_min;
				finder = i; // keep track of who found it
			}
			if (DEBUG)
				System.out.println("Search " + searches[i].getID() + " finished at  " + local_min + " in "
						+ searches[i].getSteps());
		}
		// end timer
		tock();

		if (DEBUG) {
			/* print final state */
			terrain.print_heights();
			terrain.print_visited();
		}

		/*System.out.printf("Run parameters\n");
		System.out.printf("\t Rows: %d, Columns: %d\n", rows, columns);
		System.out.printf("\t x: [%f, %f], y: [%f, %f]\n", xmin, xmax, ymin, ymax);
		System.out.printf("\t Search density: %f (%d searches)\n", searches_density, num_searches);

		/* Total computation time */
		String time = endTime - startTime +"\n";
		int tmp = terrain.getGrid_points_visited();
		double percent = Math.round(((tmp / (rows * columns * 1.0)) * 100.0));
		double sprint = tmp/(endTime-startTime);

		speedPrinter.printf("\n%s,%s",sprint,percent);
		speedPrinter.close();
		System.out.printf("The speed = %.2f searches/ms\n",sprint);
		/*int tmp = terrain.getGrid_points_visited();
		System.out.printf("Grid points visited: %d  (%2.0f%s)\n", tmp, (tmp / (rows * columns * 1.0)) * 100.0, "%");
		tmp = terrain.getGrid_points_evaluated();
		System.out.printf("Grid points evaluated: %d  (%2.0f%s)\n", tmp, (tmp / (rows * columns * 1.0)) * 100.0, "%");

		/* Results 
		System.out.printf("Global minimum: %d at x=%.1f y=%.1f\n\n", min,
				terrain.getXcoord(searches[finder].getPos_row()), terrain.getYcoord(searches[finder].getPos_col())); */

	}
}