import java.util.ArrayList;

public class MeanAverageRating {
	public double mean_rating_avg(Movie m, User u){
		ArrayList<Integer> totalRatings  = m.getRatings(); //all ratings for a given film
		//System.out.println(totalRatings);
		double ratingRemove = 0.0;
		double avg = 0;
		ratingRemove = u.getRatingFor(m.getId()); //store that value
		//get average
		for (int singleRating: totalRatings){
			avg += singleRating;
		}
		
		if (totalRatings.size() > 0){
			return ((avg-ratingRemove)/(totalRatings.size()-1));
		}
		
		else {
			return -1;
		}
	}
}
