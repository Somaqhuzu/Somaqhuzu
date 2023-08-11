import java.util.concurrent.ForkJoinPool;
import java.util.Scanner;
import java.io.*;



public class MonteCarloMinimizationParallel{
	static long startTime,endTime;
    //This class will initialize and instantiate every data needed for class {TerrainArea,SearchParallel}
	private static void tick() {
		startTime = System.currentTimeMillis();
	}

	private static void tock() {
		endTime = System.currentTimeMillis();
	}
    public static void main(String [] args) throws IOException{
        ForkJoinPool pool = new ForkJoinPool();

		BufferedWriter wr=null;
		BufferedWriter rr=null;
		File file=null;
		File hist = null;
		FileWriter writer=null;
		FileWriter right=null;
		try{
			file = new File("ParallelTime.txt");
			hist = new File("histogram.txt");
			if(!file.exists()){
				file.createNewFile();
			}
			if(!hist.exists()){
				hist.createNewFile();
			}
			right = new FileWriter(hist,true);
			rr= new BufferedWriter(right);
			writer = new FileWriter(file,true);
			wr = new BufferedWriter(writer);

		}
		catch(FileNotFoundException e){
			System.out.println("File not found");
		}

        int rows, columns; //grid size
    	double xmin, xmax, ymin, ymax; //x and y terrain limits
   		double searches_density;	// Density - number of Monte Carlo  searches per grid position - usually less than 1!

     	int num_searches;		// Number of searches
    	SearchParallel [] searches;		// Array of searche    	
    	if (args.length!=8) {  
    		/*System.out.println("Incorrect number of command line arguments provided.");   	
    		System.exit(0);*/
			rows=6;columns=6;xmin=120;xmax=21;ymin=12;ymax=-120;searches_density=6;
			SearchParallel.SEQUENTIAL_CUT_OFF = 1000;
    	}
    	/* Read argument values */
		else{
    	rows =Integer.parseInt( args[0] );
    	columns= Integer.parseInt( args[1] );
    	xmin = Double.parseDouble(args[2] );
    	xmax = Double.parseDouble(args[3] );
    	ymin = Double.parseDouble(args[4] );
    	ymax = Double.parseDouble(args[5] );
    	searches_density = Double.parseDouble(args[6] );
		SearchParallel.SEQUENTIAL_CUT_OFF= Integer.parseInt(args[7]);}

        SearchParallel.terrain = new TerrainArea(rows, columns, xmin,xmax,ymin,ymax);
    	num_searches = (int)( rows * columns * searches_density );
    	SearchParallel.arr= new SearchParallel [num_searches];
		//SearchParallel.SEQUENTIAL_CUT_OFF = (int)Math.max(Math.max(rows*columns,rows*searches_density),searches_density*columns)*2;
    	for (int i=0;i<num_searches;i++) 
    		SearchParallel.arr[i]=new SearchParallel(i);
		SearchParallel search = new SearchParallel(0,num_searches);

		tick();
		pool.invoke(search);
		tock();
		String time = (endTime-startTime) + "\n";
		int tmp = SearchParallel.terrain.getGrid_points_visited();
		double percent = ((tmp / (rows * columns * 1.0)) * 100.0);
		rr.write(tmp + "\t"  + percent + "% \n");
		rr.close();
		wr.write(time);
		wr.close();

		/*System.out.printf("Run parameters\n"); 
		System.out.printf("\t Rows: %d, Columns: %d\n", rows, columns);
		System.out.printf("\t x: [%f, %f], y: [%f, %f]\n", xmin, xmax, ymin, ymax);
		System.out.printf("\t Search density: %f (%d searches)\n", searches_density, num_searches);
		 
		System.out.printf("Time: %d ms\n", endTime - startTime);
		int tmp = SearchParallel.terrain.getGrid_points_visited();
		System.out.printf("Grid points visited: %d  (%2.0f%s)\n", tmp, (tmp / (rows * columns * 1.0)) * 100.0, "%");
		tmp = SearchParallel.terrain.getGrid_points_evaluated();
		System.out.printf("Grid points evaluated: %d  (%2.0f%s)\n", tmp, (tmp / (rows * columns * 1.0)) * 100.0, "%");
		System.out.printf("Global minimum: %d at x=%.1f y=%.1f\n\n ",SearchParallel.getHeight(),SearchParallel.terrain.getXcoord(SearchParallel.x),SearchParallel.terrain.getYcoord(SearchParallel.y));*/       
    }
}
