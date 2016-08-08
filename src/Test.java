import java.util.Arrays;
import java.util.Comparator;


public class Test {

	public static void main(String[] args) {
		PointMove[] moves = new PointMove[4];
		moves[0] = new PointMove();
		moves[1] = new PointMove();
		moves[2] = new PointMove();
		moves[3] = new PointMove();
		moves[0].setScore(2);
		moves[1].setScore(2);
		moves[2].setScore(4);
		moves[3].setScore(0);
		Arrays.sort(moves, new MoveComprator());
		for (int i = 0; i < moves.length; i++) {
			System.out.println(moves[i].getScore());
		}
		System.out.println(456);
	}
	
	
	

}

class MoveComprator implements Comparator<PointMove> {

	@Override
	public int compare(PointMove o1, PointMove o2) {
		return o1.getScore() < o2.getScore() ? 1 : -1;
	}
	
}