import java.util.Random;
import java.util.concurrent.ForkJoinPool;

public class MonteCarloMinimizationParallel{
	static long startTime,endTime;
    //This class will initialize and instantiate every data needed for class {TerrainArea,SearchParallel}
	private static void tick() {
		startTime = System.currentTimeMillis();
	}

	private static void tock() {
		endTime = System.currentTimeMillis();
	}
    public static void main(String [] args){
        ForkJoinPool pool = new ForkJoinPool();

        int rows, columns; //grid size
    	double xmin, xmax, ymin, ymax; //x and y terrain limits
   	double searches_density;	// Density - number of Monte Carlo  searches per grid position - usually less than 1!

     	int num_searches;		// Number of searches
    	SearchParallel [] searches;		// Array of searche    	
    	if (args.length!=7) {  
    		/*System.out.println("Incorrect number of command line arguments provided.");   	
    		System.exit(0);*/
			rows=6;columns=6;xmin=120;xmax=21;ymin=12;ymax=-120;searches_density=6;
    	}
    	/* Read argument values */
		else{
    	rows =Integer.parseInt( args[0] );
    	columns= Integer.parseInt( args[1] );
    	xmin = Double.parseDouble(args[2] );
    	xmax = Double.parseDouble(args[3] );
    	ymin = Double.parseDouble(args[4] );
    	ymax = Double.parseDouble(args[5] );
    	searches_density = Double.parseDouble(args[6] );}

        SearchParallel.terrain = new TerrainArea(rows, columns, xmin,xmax,ymin,ymax);
    	num_searches = (int)( rows * columns * searches_density );
    	SearchParallel.arr= new SearchParallel [num_searches];
		SearchParallel.SEQUENTIAL_CUT_OFF = (int)Math.max(Math.max(rows*columns,rows*searches_density),searches_density*columns)*2;
    	for (int i=0;i<num_searches;i++) 
    		SearchParallel.arr[i]=new SearchParallel(i);
		SearchParallel search = new SearchParallel(0,num_searches);

		tick();
		pool.invoke(search);
		tock();
		System.out.println("Global minimum: " + SearchParallel.getHeight());  
		System.out.printf("Time: %d ms\n", endTime - startTime);      
    }
}
