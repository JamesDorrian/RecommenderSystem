import java.util.ArrayList;
import java.util.Collections;

//Movie should only have the movie id and be able to return that id



public class Movie {
	private int id;
	private ArrayList<Integer> allRatings = new ArrayList<Integer>();
	
	public Movie(int movie){
		this.id = movie;
	}
	
	public int getId(){
		return this.id;
	}
	
	public void addRating(int rating){
		allRatings.add(rating);
	}
	
	public ArrayList<Integer> getRatings(){
		return allRatings;
	}
	
	public double getMeanRating(){
		double mean = 0;
		for (int x: allRatings){
			mean += x;
		}
		mean = mean/allRatings.size();
		return mean;
	}
	
	public int getMinRating(){
		Collections.sort(allRatings);
		int min = allRatings.get(0);
		return min;	
	}
	
	public int getMaxRating(){
		Collections.sort(allRatings);
		int max = allRatings.get(allRatings.size()-1);
		return max;	
	}
	
	public double getMedianRating(){
		double median = 0;
		Collections.sort(allRatings);
		if (allRatings.size() % 2 == 0){
			double med_1 = allRatings.get(allRatings.size()/2);
			double med_2 = allRatings.get(allRatings.size()/2);
			median = (med_1 + med_2)/2;
		} else {
			median = allRatings.get( (int) ((allRatings.size()/2) + 0.5) );
		}
		return median;
	}
	
	public double getStandardDeviation(){
		double mean = getMeanRating();
		double squareError=0;
		for(Integer r: allRatings){
			squareError += + Math.pow(mean - r, 2);
		}
		if (allRatings.size() > 0){
			squareError = squareError/(allRatings.size());
		}
		if (squareError < 0){
			squareError = squareError * -1;
		}
		return squareError;
		
	}
}
