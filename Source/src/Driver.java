import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

///////THIS IS WHEN I STARTED THIS BIG EDIT
public class Driver {
	static MovieLensReader mlr;
	static HashSet<User> allUsers;
	static HashSet<Movie> allMovies;
	static Evaluation eval;
	static MeanAverageRating mar;
	
	public static void main(String[] args) throws NumberFormatException, IOException{
		mlr = new MovieLensReader("/Users/JamesDorrian/Documents/workspace/RecommenderSystem/movielens/100k.dat.txt");
		mlr.loadData();
		allUsers = mlr.getUsers();
		allMovies = mlr.getMovies();
		eval = new Evaluation(allUsers, allMovies);
		mar = new MeanAverageRating();
		part1(true, true); //if true, true will print min,max,mean,median,sd for users/movies respectively
		part2();
		part3(40);//this is the number of neighbours
		part4(40);
	}
	
	private static void part4(int neighbour) throws IOException{
		System.out.println("==========PART4===========");
//		eval.L10_Resnick(true);
		calculateRuntimeRes(1, neighbour); //number of iterations to calc avg runtime, neighbourhood size
		System.out.println("==========================");
	}
	
	private static void part3(int neighbour) throws IOException{
		System.out.println("==========PART3===========");
//		eval.L10_MSD(50, true);
		calculateRuntimeMSD(1, neighbour);//number of times to average, number of neighbours
		System.out.println("==========================");
	}
	
	private static void part2() throws IOException{
		System.out.println("===========PART2==========");
		eval.L10_Mean(true);//print the values
		calculateRuntimeMean(10);//doesn't print the values but calculates mean runtime
		System.out.println("==========================");
	}
	
	private static void calculateRuntimeMean(int iterations) throws IOException{
		long netTime = 0;
		for (int i = 0; i < iterations; i++){
			long startTime = System.currentTimeMillis();
			eval.L10_Mean(false);
			long stopTime = System.currentTimeMillis();
			netTime += stopTime - startTime;
		}
		netTime = netTime / iterations;
		System.out.println("\n(MeanAverageRating) Avg time in mills:" + netTime);
	}
	
	private static void calculateRuntimeMSD(int iterations, int neighbourhoodSize) throws IOException{
		long netTime = 0;
		for (int i = 0; i < iterations; i++){
			long startTime = System.currentTimeMillis();
			eval.L10_MSD(neighbourhoodSize, true);
			long stopTime = System.currentTimeMillis();
			netTime += stopTime - startTime;
		}
		netTime = netTime / iterations;
		System.out.println("\n(MSD) Avg time in mills:" + netTime);
	}
	
	private static void calculateRuntimeRes(int iterations, int neighbourhoodSize) throws IOException{
		long netTime = 0;
		for (int i = 0; i < iterations; i++){
			long startTime = System.currentTimeMillis();
			eval.L10_Resnick(neighbourhoodSize, true);
			long stopTime = System.currentTimeMillis();
			netTime += stopTime - startTime;
		}
		netTime = netTime / iterations;
		System.out.println("\n(RES) Avg time in mills:" + netTime);
	}
	
	private static void part1(boolean printUsers, boolean printMovies){
				//get number of users
				System.out.println("==========PART1==========");
				double numberOfUsers = allUsers.size();
				System.out.println("number of users:" + numberOfUsers);
				
				//get number of movies
				double numberOfMovies = allMovies.size();
				System.out.println("number of movies:" + numberOfMovies);
				
				//get number of ratings
				double numberOfRatings = 0;
				Collection<User> userMap = allUsers;
				for (User u: userMap){
					numberOfRatings += u.getRatings().size();
				}
				System.out.println("number of ratings:" + numberOfRatings);
				
				//density metric
				double density = (numberOfRatings/(numberOfUsers*numberOfMovies))*100;
				System.out.println("Density Rating:" + density + "%");
				//how many 1,2,3,4,5 star titles
				int counter1, counter2, counter3, counter4, counter5;
				counter1 = counter2 = counter3 = counter4 = counter5 = 0;
					for (Movie m: allMovies){
						for (double r: m.getRatings()){
							int newVal = (int) r;
							if (newVal == 1){
								counter1 += 1;
							} else if (newVal == 2){
								counter2 += 1;
							} else if (newVal == 3){
								counter3 += 1;
							} else if (newVal == 4){
								counter4 += 1;
							} else if (newVal == 5){
								counter5 += 1;
							} else {
								System.out.println("ERROR!!");
							}
						}
				}
				System.out.printf("1-Star:%d\t 2-Star:%d\t 3-Star:%d\t 4-Star:%d\t 5-Star:%d\t", counter1, counter2, counter3, counter4,counter5);
				
				//Super simple coverage metric
				super_simple_coverage_metric();
				//get mean, median,max, min, standard deviation ratings of a user
				for (User u: allUsers){
					int max = u.getMaxRating();
					int min = u.getMinRating();
					double mean = u.getMeanRating();
					double median = u.getMedianRating();
					double stdDev = u.getStandardDeviationRatings();
					int userId = u.getUserId();
					
					if (printUsers == true && u.getUserId() == 3){
						System.out.printf("ID:%d \tMEAN: %.3f \t MEDIAN: %.1f \t MAX: %d \t MIN: %d \t STD_DEV:%.3f\n", userId, mean, median, max, min, stdDev);
						}
					}
				
				//get mean, median, max, min, standard deviation of each movie
				for (Movie m: allMovies){
					int id = m.getId();
					int max = m.getMaxRating(); //max rating of a given movie
					int min = m.getMinRating();
					double mean = m.getMeanRating();
					double median = m.getMedianRating();
					double stdDev = m.getStandardDeviation();
					if (printMovies == true && m.getId() == 10){
						System.out.printf("ID:%d \tMEAN: %.3f \tMEDIAN: %.1f \tMAX: %d \tMIN: %d \tSTD_DEV: %.3f\n", id, mean, median, max, min, stdDev);	
					}
					}
				System.out.println("==========================");
				//baseline prediction techinque
				//mean_item_rating(userID, movieID)
				//takes a given user, finds the rating for a film x
				//gets all instances of ratings of film x EXCEPT self and compares that average value
				//to their own actual value
				
	}

	
	private static void super_simple_coverage_metric(){
		//Using our mean_item_ratin above we consider a title to have 'no coverage'
		//when there are less than 3 users who have rated a title
		//therefore any movie with less than 3 titles has to be deemed uncovered
		//future coverage metrics will be calculated in accordance with a neighbourhood
		//but that is not required here
		int uncovered, total;
		uncovered = total = 0;
		for(Movie m: allMovies){
			if(m.getRatings().size() == 1){
				uncovered++;
				total+=m.getRatings().size()-1;
			} else {
				total+=m.getRatings().size();
			}
		}
		double percent = 100 - ((double) uncovered/(double) total) * 100;
		System.out.printf("\nPart 1 Coverage %.3f%%\n", percent);
	}
}

