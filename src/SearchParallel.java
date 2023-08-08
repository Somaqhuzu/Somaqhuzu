import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.ThreadLocalRandom;

public class SearchParallel extends java.util.concurrent.RecursiveAction{
    static SearchParallel[] arr;
    private int id;
    private int posX,posY;
    static TerrainArea terrain;
    boolean stopped;
    private static int steps=0;

    private static int min_height=Integer.MAX_VALUE;
    static int x,y;

    private int start,end;
    static Integer SEQUENTIAL_CUT_OFF;
//does it have to be static?Yes I think.
    public SearchParallel(int id) {
		this.id = id;
        posX=ThreadLocalRandom.current().nextInt(terrain.getRow());
        posY= ThreadLocalRandom.current().nextInt(terrain.getCol());
		this.stopped = false;
	}

    public SearchParallel(int start,int end){
        id=0;
        posX=0;posY=0;
        this.start=start;this.end=end;
    }
    public static int find_valleys(SearchParallel search) {
		int height=Integer.MAX_VALUE;
        if(search==null)return 	height;
        int x = (int)search.posX;
        int y = (int) search.posY;
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

    static void setHeight(int min,int xpos,int ypos){
        min_height=min;
        x =xpos;
        y = ypos;
    }

    @Override
    protected void compute() {
        if(end-start<=SEQUENTIAL_CUT_OFF){
            for (int i=start;i<end;i++){
                int height=SearchParallel.find_valleys(SearchParallel.arr[i]);
                if(height<SearchParallel.min_height)
                    {SearchParallel.setHeight(height,SearchParallel.arr[i].posX,SearchParallel.arr[i].posY);}
            }
        }
        else{
            int mid = (end+start)/2;
            SearchParallel left = (new SearchParallel(start,mid));
            left.fork();
            (new SearchParallel(mid,end)).compute();
            left.join(); 
            }
        }
    }
