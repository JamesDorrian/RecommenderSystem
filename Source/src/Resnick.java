import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class Resnick {
	private double[][] matrix = null;
	private Set<User> allUsers;
	private int MIN_RATING = 1;
	private int MAX_RATING = 5;
	private int threshold;
	
	public Resnick(int threshold, final HashSet<User> allUsers){
		this.threshold = threshold;
		this.allUsers = allUsers;
		setup();
	}
	
	private void setup(){ //set matrix size
		matrix = new double[allUsers.size()][allUsers.size()];
		computeAllSimilarity();
	}
	
	private void setPearson(final User x, final User y, final double value){
		matrix[x.getUserId() - 1][y.getUserId() - 1] = value;
		matrix[y.getUserId() - 1][x.getUserId() - 1] = value;
	}
	
	private double getPearson(User x, User y){
		return matrix[x.getUserId() - 1][y.getUserId() - 1];
	}
	
	private void computeAllSimilarity(){
		for (User x: allUsers){
			for (User y: allUsers){
				if (x.getUserId() == y.getUserId()){
					
				} else {
					setPearson(x, y, computeSimilarity(x,y));
				}
			}
		}
	}
	
	public double computeSimilarity(final User x, final User y){
		//get common films
		Set<Movie> commonMovies = x.getCommonMovies(y);
		//instantiate variables needed by loops
		int top = 0;
		double btmA = 0.0;
		double btmB = 0.0;
		//create movie list
		for (Movie m: commonMovies) {
			if(y.hasRated(m.getId())){
				//apply algorithm
				double trueX = (double)  x.getRatingFor(m.getId()) - x.getMeanRating();
				double trueY = (double) y.getRatingFor(m.getId()) - y.getMeanRating();
				
				top += (trueX * trueY);
				btmA += (trueX * trueX);
				btmB += (trueY * trueY);
			}
		}
		return top / (Math.sqrt(btmA * btmB));
		}
		

	
	public HashSet<User> computeNeighbours(User x){
		HashSet<User> neighbourhood = new HashSet<User>();
		ArrayList<Double> vals = new ArrayList<Double>();//used to sort values
		Map<Double, User> interim = new HashMap<Double, User>();//maps values to users
		for(User u: allUsers){
			if(u.getUserId() != x.getUserId()){
				if(!Double.isNaN(getPearson(x,u)) && getPearson(x,u) > 0 ){
					vals.add(getPearson(x,u));//add to sorting list
					interim.put(getPearson(x,u), u);//add to map of sorting->users
				}
			}
		}
		//after all users added sort vals and add top THRESHOLD to interim
		Collections.sort(vals);//sort values
		
		while (neighbourhood.size() < threshold && neighbourhood.size() < interim.keySet().size()){ //interim array = list of all neighbours
			neighbourhood.add(interim.get(vals.get(vals.size() -1))); //the key to the interim hashmap is the similarity rating
			vals.remove(vals.size()-1);
		}
		
		return neighbourhood;
	}
	
	public double predictRating(User x, Movie m, Set<User> neighbours){
		double top = 0;
		double bottom = 0;
		double sim = 0;
		for (User u: neighbours){//through users
			if(u.hasRated(m.getId())){//if rated
				sim = getPearson(u,x);
				top += (sim) * (u.getRatingFor(m.getId()) - u.getMeanRating());
				bottom += Math.abs(sim);
			}
		}//EO for
		if(bottom > 0) {
			double estimate = x.getMeanRating() + top / bottom;
			if(estimate < MIN_RATING) {
				estimate = MIN_RATING;
			}
			if(estimate > MAX_RATING) {
				estimate = MAX_RATING;
			}
			return estimate;
		} else {
			return Double.NaN;
		}
	}
}
