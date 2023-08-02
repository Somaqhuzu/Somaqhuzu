import java.util.Random;
import java.util.concurrent.ForkJoinPool;

public class MonteCarloMinimizationParallel{
    //This class will initialize and instantiate every data needed for class {TerrainArea,SearchParallel}
    public static void main(String [] args){
        ForkJoinPool pool = new ForkJoinPool();

        int rows, columns; //grid size
    	double xmin, xmax, ymin, ymax; //x and y terrain limits

        TerrainArea terrain;  //object to store the heights and grid points visited by searches
    	double searches_density;	// Density - number of Monte Carlo  searches per grid position - usually less than 1!

     	int num_searches;		// Number of searches
    	SearchParallel [] searches;		// Array of searches
    	Random rand = new Random();  //the random number generator
    	
    	/*if (args.length!=7) {  
    		System.out.println("Incorrect number of command line arguments provided.");   	
    		System.exit(0);
    	}*/
    	/* Read argument values */
    	rows =10; //Integer.parseInt( args[0] );
    	columns = 20; //Integer.parseInt( args[1] );
    	xmin = -19;//Double.parseDouble(args[2] );
    	xmax = 200;//Double.parseDouble(args[3] );
    	ymin = 12;//Double.parseDouble(args[4] );
    	ymax = 5;//Double.parseDouble(args[5] );
    	searches_density = 4;//Double.parseDouble(args[6] );

        terrain = new TerrainArea(rows, columns, xmin,xmax,ymin,ymax);
    	num_searches = (int)( rows * columns * searches_density );
    	searches= new SearchParallel [num_searches];
    	for (int i=0;i<num_searches;i++) 
    		searches[i]=new SearchParallel(null,i, rand.nextInt(rows),rand.nextInt(columns),terrain);
		SearchParallel search = new SearchParallel(searches, columns, rows, num_searches, terrain);

		int min = pool.invoke(search);
		System.out.println("Global minimum: " + min);        
    }
}
