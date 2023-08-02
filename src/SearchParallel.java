import java.util.Random;
import java.util.concurrent.ForkJoinTask;
public class SearchParallel extends java.util.concurrent.RecursiveTask<Integer> {
    private SearchParallel[] arr;
    private int id;
    private int posX,posY;
    private TerrainArea terrain;
    boolean stopped;
    private int steps=0;

    final Integer CUT_OFF = 500;

//does it have to be static?Yes I think.

    public SearchParallel(SearchParallel[] arr,int id, int pos_row, int pos_col,TerrainArea terrain) {
		this.id = id;
        this.arr = arr;
		this.posX = pos_row; //randomly allocated
		this.posY = pos_col; //randomly allocated
        this.terrain = terrain;

		this.stopped = false;
	}
    @Override
    //Returns the height of the minimum valley calculated
    protected Integer compute() {
        if (arr.length<CUT_OFF){
            Random rand = new Random();
            SearchParallel searcher;
            int x = (int)terrain.xmax;
            int y = (int)terrain.ymax;
            int local_min=Integer.MAX_VALUE;
            for (int i=0;i<arr.length;i++){
                searcher = new SearchParallel(null,i,rand.nextInt(x),rand.nextInt(y),terrain); 
                int temp = local_min;
                local_min = searcher.find_valleys();
                if(local_min>temp){
                    local_min = temp;
                }
                return local_min;
            }
            ForkJoinTask<Integer> right = (new SearchParallel(arr, x, y, local_min, terrain)).fork();
            int left = (new SearchParallel(arr, x, y, local_min, terrain)).join();
            int rightWing = right.join();
            return Math.min(left,rightWing);
        }
        
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'compute'");
        
    }

    public int find_valleys() {	
		int height=Integer.MAX_VALUE;
		Direction next = Direction.STAY_HERE;
		while(terrain.visited(posX, posY)==0) { // stop when hit existing path
			height=terrain.get_height(posX, posY);
			terrain.mark_visited(posY, posY, id); //mark current position as visited
			steps++;
			next = terrain.next_step(posX, posY);
			switch(next) {
				case STAY_HERE: return height; //found local valley
				case LEFT: 
					posX--;
					break;
				case RIGHT:
					posX=posX+1;
					break;
				case UP: 
					posY=-1;
					break;
				case DOWN: 
					posY=+1;
					break;
			}
		}
		stopped=true;
		return height;
	}
    //This class should extend RecursiveAction from ForkJoin Library
}
