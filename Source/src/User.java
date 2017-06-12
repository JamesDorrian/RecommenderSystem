import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;

public class User {
	private Integer userId;
	private LinkedHashMap<Integer, Rating> ratingMap; //movie, rating for each user
	
	public User(int userId){
		this.userId = userId;
		ratingMap = new LinkedHashMap<Integer, Rating>(); 
	}

	public int getUserId() {
		return this.userId;
	}


	public void addRating(int movie, Rating rating){
		ratingMap.put(movie, rating);
	}
	
	public double getRatingFor(int movie){
		return ratingMap.get(movie).getRating();
	}
	
	public boolean hasRated(int movie){
		if (ratingMap.get(movie)!= null){
			return true;
		} else {
			return false;
		}
	}
	
	//PART 1
	public double getMeanRating(){
			double total = 0;
			for(Rating r: ratingMap.values()){
				total = total + r.getRating();
			}
			if (ratingMap.values().size() > 0){
				total = total/ratingMap.values().size();
				return total;
			} else {
				return 0;
			}
	}
	
	public double getMedianRating(){
		double median = 0;
		ArrayList<Double> ratings = new ArrayList<Double>();
		for(Rating r: ratingMap.values()){
			ratings.add(r.getRating());
		} 
		if (ratings.size() > 1){
			Collections.sort(ratings);
		}
		if (ratings.size() % 2 == 0){ //if even number of elements 
			double med_1 = ratings.get(ratings.size()/2);
			double med_2 = ratings.get(ratings.size()/2);
			median = (med_1 + med_2)/2;
		} else {
			median = ratings.get( (int) ((ratings.size()/2) + 0.5) );
		}
		return median;
	}
	
	
	public double getStandardDeviationRatings(){
		double mean = getMeanRating();
		double sd = 0;
		for(Rating r: ratingMap.values()){
			sd += Math.pow(r.getRating() - mean, 2);
		}
		sd = sd * 1/ratingMap.size();
		sd = Math.sqrt(sd);
		return sd;
	}
	
	public int getMinRating(){
		int min;
		ArrayList<Integer> ratings = new ArrayList<Integer>();
		for (Rating r: ratingMap.values()){
			ratings.add((int) r.getRating());
		}
		Collections.sort(ratings);
		min = ratings.get(0);
		return min;
	}
	
	public int getMaxRating(){
		int max;
		ArrayList<Integer> ratings = new ArrayList<Integer>();
		for (Rating r: ratingMap.values()){
			ratings.add((int) r.getRating());
		}
		Collections.sort(ratings);
		max = ratings.get(ratings.size()-1);
		return max;
	}
	
	public Collection<Rating> getRatings(){
		return ratingMap.values();
	}
	
	//PART 3
	public HashSet<Movie> getCommonMovies(User other){
		HashSet<Movie> commonMovies = new HashSet<Movie>();
		for (Rating r: ratingMap.values()){
			for (Rating otherR: other.getRatings()){
				if (otherR.getMovie() == r.getMovie()){
					commonMovies.add(r.getMovie());
				}
			}
		}
		return commonMovies;
	}
	
}
