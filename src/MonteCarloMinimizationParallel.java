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
    	SearchParallel [] searches;		// Array of searche    	
    	/*if (args.length!=7) {  
    		System.out.println("Incorrect number of command line arguments provided.");   	
    		System.exit(0);
    	}*/
    	/* Read argument values */
    	rows =Integer.parseInt( args[0] );
    	columns= Integer.parseInt( args[1] );
    	xmin = Double.parseDouble(args[2] );
    	xmax = Double.parseDouble(args[3] );
    	ymin = Double.parseDouble(args[4] );
    	ymax = Double.parseDouble(args[5] );
    	searches_density = Double.parseDouble(args[6] );

        SearchParallel.terrain = new TerrainArea(rows, columns, xmin,xmax,ymin,ymax);
    	num_searches = (int)( rows * columns * searches_density );
    	searches= new SearchParallel [num_searches];
    	for (int i=0;i<num_searches;i++) 
    		searches[i]=new SearchParallel(i);
		SearchParallel search = new SearchParallel(searches,0,num_searches);

		pool.invoke(search);
		System.out.println("Global minimum: " + SearchParallel.getHeight());        
    }
}
