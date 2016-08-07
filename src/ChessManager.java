

public class ChessManager {
	
	public static PointMove move(byte[][] chessStatus, int type) {
		SearchEngine searchEngine = SearchEngine.getInstance();
		searchEngine.setSearchDepth(5);
//		TranspositionTable.initializeHashKey();
		return searchEngine.searchAGoodMove(chessStatus, type);
	}
	
	public static void initHashKey() {
		TranspositionTable.initializeHashKey();
	}
}
