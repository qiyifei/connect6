

public class TranspositionTable {

	private static final int ROWS=19;//棋盘行数  
	private static final int COLS=19;//棋盘列数
	
	private static int[][][] m_nHashKey32 = new int[2][ROWS][COLS];
	private static long[][][] m_ulHashKey64 = new long[2][ROWS][COLS];
	private static int m_HashKey32;
	private static long m_HashKey64;
	private static HashItem[][] m_pTT = new HashItem[2][1024 * 1024];
	//private OpenMove[] m_move = new OpenMove[2049];
	
	public static void initializeHashKey() {
		for (int k = 0; k < 2; k++) {
			for (int i = 0; i < ROWS; i++) {
				for (int j = 0; j < COLS; j++) {
					m_nHashKey32[k][i][j] = (int)(Math.random() * Integer.MAX_VALUE);
					m_ulHashKey64[k][i][j] = (long)(Math.random() * Long.MAX_VALUE);
				}
			}
		}
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 1024 * 1024; j++) {
				m_pTT[i][j] = new HashItem();
			}
		}
	}
	
	public static void calculateInitHashKey(byte[][] position) {
		int type;
		m_HashKey32 = 0;
		m_HashKey64 = 0;
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				type = position[i][j];
				if (type != 0) {
					if (type == 7) {
						type = 0;
					}
					m_HashKey32 = m_HashKey32 ^ m_nHashKey32[type][i][j];
					m_HashKey64 = m_HashKey64 ^ m_ulHashKey64[type][i][j];
				}
			}
		}
	}
	
	public static void hash_makeMove(PointMove move, byte[][] position, int type) {
		if (type == 7) {
			type = 0;
		}
		m_HashKey32 = m_HashKey32 ^ m_nHashKey32[type][move.getPoint1().getX()][move.getPoint1().getY()];
		m_HashKey64 = m_HashKey64 ^ m_ulHashKey64[type][move.getPoint1().getX()][move.getPoint1().getY()];
		m_HashKey32 = m_HashKey32 ^ m_nHashKey32[type][move.getPoint2().getX()][move.getPoint2().getY()];
		m_HashKey64 = m_HashKey64 ^ m_ulHashKey64[type][move.getPoint2().getX()][move.getPoint2().getY()];
	}
	
	public static void hash_unMakeMove(PointMove move, byte[][] position, int type) {
		if (type == 7) {
			type = 0;
		}
		m_HashKey32 = m_HashKey32 ^ m_nHashKey32[type][move.getPoint1().getX()][move.getPoint1().getY()];
		m_HashKey64 = m_HashKey64 ^ m_ulHashKey64[type][move.getPoint1().getX()][move.getPoint1().getY()];
		m_HashKey32 = m_HashKey32 ^ m_nHashKey32[type][move.getPoint2().getX()][move.getPoint2().getY()];
		m_HashKey64 = m_HashKey64 ^ m_ulHashKey64[type][move.getPoint2().getX()][move.getPoint2().getY()];
	}
	
	public static int lookupHashTable(int alpha, int beta, int depth, int type) {
		if (type == 7) {
			type = 0;
		}
		int x = m_HashKey32 & 0xFFFFF;
		HashItem pht = m_pTT[type][x];
		if (pht.getDepth() >= depth && pht.getChecksum() == m_HashKey64) {
			switch (pht.getEntryType()) {
			case EXACT:
				return pht.getEval();
			case LOWER_BOUND:
				if (pht.getEval() >= beta) {
					return pht.getEval();
				} else {
					break;
				}
			case UPPER_BOUND:
				if (pht.getEval() <= alpha) {
					return pht.getEval();
				} else {
					break;
				}

			}
		}
		return 66666;
	}
	
	public static void enterHashTable(EntryType entryType, int eval, int depth, int type) {
		if (type == 7) {
			type = 0;
		}
		int x = m_HashKey32 & 0xFFFFF;
		HashItem pht = m_pTT[type][x];
		pht.setChecksum(m_HashKey64);
		pht.setEntryType(entryType);
		pht.setEval(eval);
		pht.setDepth(depth);
	}
	
	public static void print_HashKeys() {
		for (int k = 0; k < 2; k++) {
			for (int i = 0; i < ROWS; i++) {
				for (int j = 0; j < COLS; j++) {
					System.out.print(m_nHashKey32[k][i][j] + " ");
					System.out.println(m_ulHashKey64[k][i][j]);
				}
			}
		}
	}
}
