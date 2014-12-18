package grid;

public class Segment {
	private int x; 
	private int y;
	private char letter; // V or H (for 'horizontal' or 'vertical')
	private double speed;
	private double time;
	private double cost;
	
	public Segment(){
	}
	
	public Segment(int x, int y, char letter){
		this.x = x;
		this.y = y;
		this.letter = letter;
	}
	
	public Segment(int x, int y, char letter, double speed, double time, double cost){
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.time = time;
		this.cost = cost;
	}
	
	public static boolean checkLetter(char Letter){
		if ((Letter != 'V') && (Letter != 'H'))
			return false;
		else
			return true;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public char getLetter(){
		return letter;
	}
	
	public double getSpeed(){
		return this.speed;
	}
	
	public double getTime(){
		return this.time;
	}
	
	public double getCost(){
		return this.cost;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	};
	
	public void setLetter(char l){
		this.letter = l;
	}
	
	public void setSpeed(double speed){
		this.speed = speed;
	}
	
	public void setTime(double time){
		this.time = time;
	}
	
	public void setCost(double cost){
		this.cost = cost;
	}
	
	public String toString(){
		return "(" + x + ", " + y + ", " + letter + ")";
	}
}