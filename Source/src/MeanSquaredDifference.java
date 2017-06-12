import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;



public class MeanSquaredDifference {
	//global variables
	private static double MIN_RATING = 1;
	private static double MAX_RATING = 5;
	private double[][] matrix = null;
	private HashSet<User> allUsers;
	private int threshold;
	
	public MeanSquaredDifference(int threshold, HashSet<User> allUsers){
		this.threshold = threshold;
		this.allUsers = allUsers;
		setup();
	}
	
	private void setup(){
		matrix = new double[allUsers.size()][allUsers.size()];
		computeValues();
	}
	
	private void computeValues(){
		for (User x: allUsers){
			for (User y: allUsers){
				if (x.getUserId() == y.getUserId()){
				//do nothing
				}else {
					setMSD(x, y, computeSimilarity(x,y));
				}
			}
		}
	}
	
	public double computeSimilarity(final User x, final User y){
		HashSet<Movie> commonMovies = x.getCommonMovies(y);
		int numCommonMovies = commonMovies.size();
		double ratingX = 0;
		double ratingY = 0;
		double MSD = 0.0;
		if (numCommonMovies > 0){
			for (Movie currentMovie : commonMovies){
				ratingX = x.getRatingFor(currentMovie.getId());
				ratingY = y.getRatingFor(currentMovie.getId());
				MSD += (Math.pow((ratingX - ratingY),2));
			}
			MSD = (MSD/(double) numCommonMovies);
			return MSD;  
		}
		else {
			return 0;
		}
	}
	
	private void setMSD(final User x, final User y, final double value){
		matrix[x.getUserId()-1][y.getUserId()-1] = value;
		matrix[y.getUserId()-1][x.getUserId()-1] = value;
	}
	
	private double getMSD(User x, User y){
		return matrix[x.getUserId()-1][y.getUserId()-1];
	}
	
	public Set<User> computeNeighbours(User x){//this.threshold
		Set<User> neighbourhood = new HashSet<User>();
		ArrayList<Double> vals = new ArrayList<Double>();
		Map<Double, User> interim = new HashMap<Double, User>();
		for(User u: allUsers){
			if(u.getUserId() != x.getUserId()){
				if(!Double.isNaN(getMSD(x,u)) && getMSD(x,u) > 0 ){
					vals.add(getMSD(x,u));
					interim.put(getMSD(x,u), u);
				}
			}
		}
		Collections.sort(vals);
		while (neighbourhood.size() < threshold && neighbourhood.size() < interim.keySet().size()){
			neighbourhood.add(interim.get(vals.get(0))); //the key to the interim hashmap is the similarity rating
			vals.remove(0);
		}
		
		return neighbourhood;
	}
	
	public double predictRating(User u, Movie m, Set<User> neighbours){
		double top = 0;
		double bottom = 0;
		for (User user: neighbours) { //cycle through neighbourlist and compute top & bottom of equation
 			if(user.hasRated(m.getId())) { //if unrated, move to next neighbour
 				top += getMSD(u, user) * user.getRatingFor(m.getId());
 				bottom += getMSD(u, user);
			}
		}
		
		if(bottom > 0) { //if < 0, return -1
			double estimate = top / bottom; //make equation whole then do min max checks
			if(estimate < MIN_RATING) {
				estimate = MIN_RATING;
			}
			if(estimate > MAX_RATING) {
				estimate = MAX_RATING;
			}
			return estimate;
		}
		else {
			return Double.NaN;
		}
	}
}
