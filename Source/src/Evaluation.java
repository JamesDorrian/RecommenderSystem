import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class Evaluation {
	private HashSet<User> allUsers;
	private HashSet<Movie> allMovies;
	static MeanAverageRating meanAvgRating = new MeanAverageRating();
	public Evaluation(HashSet<User> allUsers, HashSet<Movie> allMovies){
		this.allMovies = allMovies;
		this.allUsers = allUsers;
	}
	
	public void L10_Mean(boolean print) throws IOException{
		//Loop to do 3 things
		//1. write to file
		//2. calculate rsme of each user 
		//3. make avg 
		double avgRmse = 0;
		int numberOfRatings = 0;
		int uncovered = 0; 
		BufferedWriter writer = new BufferedWriter(new FileWriter("/Users/JamesDorrian/Documents/workspace/RecommenderSystem/movielens/MAR.csv"));
		
		ArrayList<Movie> movies = new ArrayList<Movie>(); 
		
		for(User u: allUsers){		
			movies = new ArrayList<Movie>();	
			for(Movie m: allMovies){	
				if(u.hasRated(m.getId())){	//creates list of all movies a user has created	
					movies.add(m);				
				}
			}
			for (Movie movie: movies){	//cycle through movies
				double estimate = meanAvgRating.mean_rating_avg(movie, u); 
				double actual = u.getRatingFor(movie.getId());
				double rmse = Math.sqrt(Math.pow(( estimate - actual ), 2));
				if (Double.isNaN(rmse)){ //do nothing
					uncovered++;
				} else {
					avgRmse = avgRmse + rmse;
					numberOfRatings++;
				}
				//do coverage calculation here
					writer.write(u.getUserId() + "," + movie.getId() + "," + actual + "," + estimate + ",," + rmse );
					writer.newLine();
			}
		}	//end of users
		//avg RMSE
		avgRmse = ((avgRmse)/(numberOfRatings));
		//coverage
		double coverage = 100 - (((double) uncovered)/ ((double) numberOfRatings)*100);
		if (print == true){
			System.out.printf("Average RSME: %f \nTotal coverage: %.3f%%", avgRmse, coverage);
		}
		writer.close();
	}
	
	public void L10_MSD(int threshold, boolean print) throws IOException{
		BufferedWriter writer = new BufferedWriter(new FileWriter("/Users/JamesDorrian/Documents/workspace/RecommenderSystem/movielens/MSD.csv"));
		MeanSquaredDifference msd = new MeanSquaredDifference(threshold, allUsers);
		int uncovered = 0;
		int numberOfRatings = 0;
		double avgRmse = 0.0;
		ArrayList<Movie> movies = new ArrayList<Movie>();	
		for (User u: allUsers){
			movies = new ArrayList<Movie>();	
			for(Movie m: allMovies){	
				if(u.hasRated(m.getId())){	//creates list of all movies a user has created	
					movies.add(m);				
				}
			}
			Set<User> neighbours = msd.computeNeighbours(u);
			
			for (Movie m: movies){
				double actual = u.getRatingFor(m.getId());
				double estimate = msd.predictRating(u, m, neighbours);
				double rmse = 0;
				if (Double.isNaN(estimate)){ //do nothing
					uncovered++;
					rmse = Double.NaN;
				} else {
					rmse = Math.sqrt(Math.pow(( estimate - actual ), 2));
					avgRmse = avgRmse + rmse;
				}
				numberOfRatings++;
				writer.write(u.getUserId() + "," + m.getId() + "," + actual + "," + estimate + "," + rmse );
				writer.newLine();
			}
			
			
		}
		avgRmse = avgRmse / numberOfRatings;
		double coverage = 100.0 - (((double) (uncovered)/ (double) (numberOfRatings))*100);
		if (print == true){
			System.out.printf("Average RSME: %f \nTotal coverage: %.3f%%", avgRmse, coverage);
		}
		writer.close();
	}
	
	public void L10_Resnick(int threshold, boolean print) throws IOException{
		BufferedWriter writer = new BufferedWriter(new FileWriter("/Users/JamesDorrian/Documents/workspace/RecommenderSystem/movielens/resnick.csv"));
		Resnick resnick = new Resnick(threshold, allUsers);
		int uncovered = 0;
		int numberOfRatings = 0;
		double avgRmse = 0.0;
		
		//create list of rated movies for L10 test
		ArrayList<Movie> movies = new ArrayList<Movie>();	
		for (User u: allUsers){
			movies = new ArrayList<Movie>();	
			for(Movie m: allMovies){	
				if(u.hasRated(m.getId())){
					movies.add(m);				
				}
			}
			
			Set<User> neighbours = resnick.computeNeighbours(u);//get neighbours
			for (Movie m: movies){//for all movies rated by a user
				double actual = u.getRatingFor(m.getId());
				double estimate = resnick.predictRating(u, m, neighbours);
				double rmse = Math.sqrt(Math.pow(( estimate - actual ), 2));
				if (Double.isNaN(estimate)){ //do nothing
					uncovered++;
					rmse = Double.NaN;
				} else {
					avgRmse = avgRmse + rmse;
				}
				numberOfRatings++;
				writer.write(u.getUserId() + "," + m.getId() + "," + actual + "," + estimate + "," + rmse );
				writer.newLine();
			}
		}
		avgRmse = avgRmse / (double) numberOfRatings;
		double coverage = 100 - (((double) uncovered)/ ((double) numberOfRatings))*100;
		if (print == true){
			System.out.printf("Average RSME: %f \nTotal coverage: %.3f%%", avgRmse, coverage);
		}
		writer.close();
	}
}
