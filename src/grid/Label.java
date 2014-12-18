package grid;

public class Label {
	private double time;
	private double cost;
	private double speed;
	private int x;
	private int y;
	private Label pred; //precedent
	
	public Label(){
		time = 0;
		cost = 0;
		speed = 0;
		x = 0;
		y = 0;
		pred = null;
	}
	
	public Label(int x, int y, double cost, double time, double speed, Label label){
		this.time = time;
		this.cost = cost;
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.pred = label;
	}
	
	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	public Label getPred() {
		return pred;
	}

	public void setPred(Label pred) {
		this.pred = pred;
	}

	public double getTime() {
		return time;
	}
	public double getCost() {
		return cost;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public void setTime(double time) {
		this.time = time;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	public void setX(int x) {
		this.x = x;
	}
	public void setY(int y) {
		this.y = y;
	}	
}