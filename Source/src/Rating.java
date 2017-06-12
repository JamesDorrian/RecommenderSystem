
public class Rating {
	private User user; //who gave rating
	private Movie movie; //what movie was rated
	private double rating; //rating
	
	public Rating(final User user, final double rating, final Movie movie){
		this.user = user;
		this.rating = rating;
		this.movie = movie;
	}
	
	public double getRating(){
		return this.rating;
	}
	
	public Movie getMovie(){
		return this.movie;
	}
	
	public int getMovieId(){
		return this.movie.getId();
	}
	
	public void setUser(User user){
		this.user = user;
	}
	
	public User getUser(){
		return this.user;
	}
}
