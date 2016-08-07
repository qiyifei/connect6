import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;


public class SearchEngine {
	
	private Evalution evalution;
	private MoveGenerator moveGenerator;
	private byte[][] position;
	private PointMove bestMove;
	private static SearchEngine engine;
	private static StringBuilder builder;
	private static int totalCalCount;
	private static int totalSearchCount;
	private static long totalCalTime;
	private static long totalGenTime;
	private static int moveNum = 0;
	
//	private TranspositionTable transTable;
	
	private int maxDepth = 6;
	
	public SearchEngine() {
		evalution = new RoadEvalution();
		moveGenerator = new MoveGenerator();
//		transTable = new TranspositionTable();
	}
	
	public static SearchEngine getInstance() {
		if (engine == null) {
			engine = new SearchEngine();
		}
		return engine;
	}
	
	public PointMove searchAGoodMove(byte[][] position, int type) {
		TranspositionTable.calculateInitHashKey(position);
		//m_move = lookupHashTable();
//		if(m_move.getPoint1.getX() != 0) {
//			makeMove(m_move, type);
//			return m_move;
//		}
		this.position = position;
		builder = new StringBuilder();
		totalCalCount = 0;
		totalSearchCount = 0;
		totalCalTime = 0;
		totalGenTime = 0;
		Date fDate = new Date();
		alphaBeta(maxDepth, type, -200000, 200000);
		Date bDate = new Date();
		//makeMove(bestMove, type);
		builder.append("Total calcount: " + totalCalCount).append(" Total searchcount: " + totalSearchCount).append('\n');
		builder.append("Total time: " + (bDate.getTime() - fDate.getTime())).append(" Total gentime: " + totalGenTime).append(" Total caltime: " + totalCalTime);
		moveNum++;
		print_moves(builder.toString(), moveNum);
		return bestMove;
	}
	
	public void setEvalution(Evalution evalution) {
		this.evalution = evalution;
	}
	
	public void setSearchDepth(int depth) {
		this.maxDepth = depth;
	}
	
	private int alphaBeta(int depth, int type, int alpha, int beta) {
		int score, count;
		score = TranspositionTable.lookupHashTable(alpha, beta, depth, type);
		if (score != 66666) {
			return score;
		}
		
		if (depth == 0) {
			score = -evalution.evalute(position, 8-type);
			TranspositionTable.enterHashTable(EntryType.EXACT, score, depth, type);
			return score;
		}
		
		Date fDate = new Date();
		count = moveGenerator.sort_CreatePossibleMove(position, depth, type);
		Date bDate = new Date();
		totalGenTime += bDate.getTime() - fDate.getTime();
		
		if (count < 0) {
			alpha = 100000;
			if (depth == maxDepth) {
				bestMove = moveGenerator.moveList[depth][0];
			}
			return alpha;
		}
		
		totalCalCount += count;
		
		fDate = new Date();
		for (int i = 0; i < count; i++) {
			//makeMove(moveGenerator.moveList[depth][i], type);
			score = evalution.evalute(position, type, moveGenerator.moveList[depth][i]);
			moveGenerator.moveList[depth][i].setScore(score);
			//unMakeMove(moveGenerator.moveList[depth][i]);
			
		}
		bDate = new Date();
		totalCalTime += bDate.getTime() - fDate.getTime();
		
		Arrays.sort(moveGenerator.moveList[depth], 0, count, new MoveComprator());//mergeSort 对走法数组按score的降序排序
		
		
		builder.append("count: ").append(count).append(" depth: ").append(depth).append('\n');
//		for (int i = 0; i < count; i++) {
//			PointMove move = moveGenerator.moveList[depth][i];
//			
//			if (move.getPoint1() != null) {
//				builder.append("Point1: ").append("(").append(move.getPoint1().getX()).append(",")
//				.append(move.getPoint1().getY()).append(") ");
//			}
//			if (move.getPoint2() != null) {
//				builder.append("Point2: ").append("(").append(move.getPoint2().getX()).append(",")
//				.append(move.getPoint2().getY()).append(") ");
//			}
//			builder.append("Score: " + move.getScore());
//			builder.append('\n');
//		}
		
		int eval_is_exact = 0;
		int width = 20;
		if (width > count) {
			width = count;
		}
		for (int i = 0; i < width; i++) {  //根据棋局改变width得值
			totalSearchCount++;
			makeMove(moveGenerator.moveList[depth][i], type);
			TranspositionTable.hash_makeMove(moveGenerator.moveList[depth][i], position, type);
			score = -alphaBeta(depth-1, 8-type, -beta, -alpha);
			unMakeMove(moveGenerator.moveList[depth][i]);
			TranspositionTable.hash_unMakeMove(moveGenerator.moveList[depth][i], position, type);
			if (score > alpha) {
				eval_is_exact = 1;
				alpha = score;
				if (depth == maxDepth) {
					bestMove = moveGenerator.moveList[depth][i];
				}
			}
			if (alpha >= beta) {
				TranspositionTable.enterHashTable(EntryType.UPPER_BOUND, alpha, depth, type);
				return alpha;
			}
		}
		
		if (eval_is_exact == 1) {
			TranspositionTable.enterHashTable(EntryType.LOWER_BOUND, alpha, depth, type);
		}
		return alpha;
	}
	
	private void makeMove(PointMove move, int type) {
		position[move.getPoint1().getX()][move.getPoint1().getY()] = (byte)type;
		position[move.getPoint2().getX()][move.getPoint2().getY()] = (byte)type;
	}
	
	private void unMakeMove(PointMove move) {
		position[move.getPoint1().getX()][move.getPoint1().getY()] = 0;
		position[move.getPoint2().getX()][move.getPoint2().getY()] = 0;
	}
	
	private void print_moves(String move, int moveNum) {
		BufferedWriter out = null;
		StringBuilder filename = new StringBuilder();
		filename.append("moves/move_").append(moveNum).append(".txt");
		try {
			out = new BufferedWriter(new FileWriter(filename.toString()));
			out.write(move);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	class MoveComprator implements Comparator<PointMove> {

		@Override
		public int compare(PointMove o1, PointMove o2) {
			if (o1.getScore() == o2.getScore()) {
				return 0;
			}
			return o1.getScore() < o2.getScore() ? 1 : -1;
		}
		
	}
}



