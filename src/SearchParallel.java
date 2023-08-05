import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.ThreadLocalRandom;

public class SearchParallel extends java.util.concurrent.RecursiveAction{
    private SearchParallel[] arr;
    private int id;
    private int posX,posY;
    static TerrainArea terrain;
    boolean stopped;
    private static int steps=0;

    private static int min_height=Integer.MAX_VALUE;

    private int start,end;
    private boolean worker = false;
    final Integer SEQUENTIAL_CUT_OFF = 5000;

//does it have to be static?Yes I think.

    public SearchParallel(int id) {
		this.id = id;
        this.arr = null; //randomly allocated
        posX=ThreadLocalRandom.current().nextInt(terrain.getRow());
        posY= ThreadLocalRandom.current().nextInt(terrain.getCol());
		this.stopped = false;
        worker = true;
	}

    public SearchParallel(SearchParallel[] arr,int start,int end){
        this.arr = arr;
        id=0;
        posX=0;posY=0;
        this.start=start;this.end=end;
    }
    

    public static int find_valleys(SearchParallel search) {
		int height=Integer.MAX_VALUE;
        if(search==null)return 	height;
        int x = search.posX;
        int y = search.posY;
		Direction next = Direction.STAY_HERE;
		while(terrain.visited(x, y)==0) { // stop when hit existing path
			height=terrain.get_height(x, y);
			terrain.mark_visited(x,y, search.id); //mark current position as visited
            steps++;
			next = terrain.next_step(x, y);
			switch(next) {
				case STAY_HERE: return height; //found local valley
				case LEFT: 
					x--;
                    if(x<0){return height;}
					break;
				case RIGHT:
					x=x+1;
					break;
				case UP: 
					y-=1;
                    if(y<0) return height;
					break;
				case DOWN: 
					y=+1;
					break;
			}
		}
		search.stopped=true;
		return height;
	}

    static int getHeight(){return min_height;}

    static void setHeight(int x){min_height=x;}
    //This class should extend RecursiveAction from ForkJoin Library

    @Override
    protected void compute() {
        if(arr.length<=SEQUENTIAL_CUT_OFF){
            for (int i=0;i<arr.length;i++){
                int height=SearchParallel.find_valleys(arr[i]);
                if(height<SearchParallel.min_height)
                    {SearchParallel.setHeight(height);}
            }
        }
        else{
            int mid = arr.length/2;
            SearchParallel[] first = new SearchParallel[mid];
            SearchParallel[] second = new SearchParallel[mid];
            
            for(int i=0;i<mid;i++){
                try{
                first[i] = arr[i];}
                catch(ArrayIndexOutOfBoundsException e){continue;}

            }
            for(int i=0;i<mid;i++){
                second[i] = arr[mid+i];
            }
            
            

            SearchParallel left = (new SearchParallel(first,start,end));
            left.fork();
            (new SearchParallel(second,mid,end)).compute();
            left.join();
        }
    }
}
