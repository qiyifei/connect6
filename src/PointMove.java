
public class PointMove {
	private Point point1;
	private Point point2;
	private int score;
	
	public Point getPoint1() {
		return point1;
	}
	public void setPoint1(Point point1) {
		this.point1 = point1;
	}
	public Point getPoint2() {
		return point2;
	}
	public void setPoint2(Point point2) {
		this.point2 = point2;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	
	@Override
	public String toString() {
		return "PointMove [point1=" + point1.getX() + "," + point1.getY() + ", point2=" + point2.getX()
				+ "," + point2.getY() + ", score=" + score + "]";
	}
	
	
}
