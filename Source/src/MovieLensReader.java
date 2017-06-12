import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.StringTokenizer;


public class MovieLensReader {
	private String address;
	private HashSet<User> allUsers = new HashSet<User>();
	private HashSet<Movie> allMovies = new HashSet<Movie>();
	
	public MovieLensReader(String address){
		this.address = address;
	}
	
	public void loadData() throws NumberFormatException, IOException{
		BufferedReader in = new BufferedReader(new FileReader(new File(address)));
		String line;
		while ((line = in.readLine()) != null) {
			StringTokenizer st = new StringTokenizer(line, ",");
			//System.out.println(line);
			Integer userId = Integer.valueOf(st.nextToken());
			Integer movieId = Integer.valueOf(st.nextToken());
			Integer ratingNum = Integer.valueOf(st.nextToken());
			Movie m = null;
			for (Movie movie: allMovies){
				if (movie.getId() == movieId){
					m = movie;
					m.addRating(ratingNum);
					allMovies.add(m);
				}
			}
			if (m == null){
				m = new Movie(movieId);
				m.addRating(ratingNum);
				allMovies.add(m);
			}
			//check if userId already exists
			//if yes -> add rating where 
			User userObj = null;
			for (User u: allUsers){
				if (u.getUserId() == userId){
					userObj = u;
				}
			}
			if (userObj == null){
				userObj = new User(userId);
			}
			Rating ratingObj = new Rating(userObj, ratingNum, m);
			userObj.addRating(m.getId(), ratingObj);
			ratingObj.setUser(userObj);
			allUsers.add(userObj);
		}
		in.close();
	}
	
	public HashSet<Movie> getMovies(){
		return allMovies ;
	}
	
	public HashSet<User> getUsers(){
		return allUsers;
	}
}
