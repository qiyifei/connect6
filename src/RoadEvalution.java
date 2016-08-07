
public class RoadEvalution implements Evalution{

	private int[] numberOfMyRoad = new int[7];
	private int[] numberOfEnemyRoad = new int[7];
	private int[] scoreOfMyRoad = {0, 1, 5, 10, 35, 25, 100000};
	private int[] scoreOfEnemyRoad = {0, 1, 10, 15, 35, 25, 100000};
	private static final int BLACK = 1;
	private static final int WHITE = 7;
	
	@Override
	public int evalute(byte[][] position, int type, PointMove move) {
		int fScore = 0;
		int bScore = 0;
		int score = 0;
		
		for(int i = 1; i < 7; i++) {
			numberOfEnemyRoad[i] = 0;
			numberOfMyRoad[i] = 0;
		}
		
		analysisHorizon(position, type, move);
		analysisVertical(position, type, move);
		analysisLeft(position, type, move);
		analysisRight(position, type, move);
		
		for(int i = 1; i < 7; i++) {
			fScore += (numberOfMyRoad[i] * scoreOfMyRoad[i] -
					numberOfEnemyRoad[i] * scoreOfEnemyRoad[i]);
		}
		
		for(int i = 1; i < 7; i++) {
			numberOfEnemyRoad[i] = 0;
			numberOfMyRoad[i] = 0;
		}
		
		makeMove(position, move, type);
		
		analysisHorizon(position, type, move);
		analysisVertical(position, type, move);
		analysisLeft(position, type, move);
		analysisRight(position, type, move);
		
		for(int i = 1; i < 7; i++) {
			bScore += (numberOfMyRoad[i] * scoreOfMyRoad[i] -
					numberOfEnemyRoad[i] * scoreOfEnemyRoad[i]);
		}
		
		unMakeMove(position, move);
		
		score = (bScore - fScore);
		
		return score;
	}
	
	private int analysisHorizon(byte[][] position, int type, PointMove move) {
//		for (int i = 0; i < ROWS; i++) {
//			for (int j = 0; j < COLS - 5; j++) {
//				int number = position[i][j] + position[i][j+1] + position[i][j+2]
//						+ position[i][j+3] + position[i][j+4] + position[i][j+5];
//				if (number == 0 || (number > 6 && number % 7 != 0)) {
//					continue;
//				}
//				if (number < 7) {
//					numberOfMyRoad[number]++;
//				} else {
//					numberOfEnemyRoad[number/7]++;
//				}
//			}
//		}
		if (move.getPoint1() != null) {
			int x = move.getPoint1().getX();
			int y = move.getPoint1().getY();
			for (int i = y-5>0?y-5:0; i <= y && i + 5 < 19; i++) {
				int number = position[x][i] + position[x][i+1] + position[x][i+2]
						+ position[x][i+3] + position[x][i+4] + position[x][i+5];
				if (number == 0 || (number > 6 && number % 7 != 0)) {
					continue;
				}
				if (number < 7) {
					if (type == BLACK) {
						numberOfMyRoad[number]++;
					} else {
						numberOfEnemyRoad[number]++;
					}
					
				} else {
					if (type == BLACK) {
						numberOfEnemyRoad[number/7]++;
					} else {
						numberOfMyRoad[number/7]++;
					}
					
				}
			}
		}
		if (move.getPoint2() != null) {
			int x = move.getPoint2().getX();
			int y = move.getPoint2().getY();
			for (int i = y-5>0?y-5:0; i <= y && i + 5 < 19; i++) {
				int yy = move.getPoint1().getY();
				if (i == yy || i+1 == yy || i+2 == yy || i+3 == yy || i+4 == yy || i+5 == yy) {//去重复
					continue;
				}
				int number = position[x][i] + position[x][i+1] + position[x][i+2]
						+ position[x][i+3] + position[x][i+4] + position[x][i+5];
				if (number == 0 || (number > 6 && number % 7 != 0)) {
					continue;
				}
				if (number < 7) {
					if (type == BLACK) {
						numberOfMyRoad[number]++;
					} else {
						numberOfEnemyRoad[number]++;
					}
					
				} else {
					if (type == BLACK) {
						numberOfEnemyRoad[number/7]++;
					} else {
						numberOfMyRoad[number/7]++;
					}
					
				}
			}
		}
		if (type == BLACK && numberOfEnemyRoad[4] + numberOfEnemyRoad[5] > 0) {
			return 1;
		}
		if (type == WHITE && numberOfMyRoad[4] + numberOfMyRoad[5] > 0) {
			return 1;
		}
		return 0;
	}
	
	private int analysisVertical(byte[][] position, int type, PointMove move) {
//		for (int j = 0; j < COLS; j++) {
//			for (int i = 0; i < ROWS - 5; i++) {
//				int number = position[i][j] + position[i+1][j] + position[i+2][j]
//						+ position[i+3][j] + position[i+4][j] + position[i+5][j];
//				if (number == 0 || (number > 6 && number % 7 != 0)) {
//					continue;
//				}
//				if (number < 7) {
//					numberOfMyRoad[number]++;
//				} else {
//					numberOfEnemyRoad[number/7]++;
//				}
//			}
//		}
		if (move.getPoint1() != null) {
			int x = move.getPoint1().getX();
			int y = move.getPoint1().getY();
			for (int i = x-5>0?x-5:0; i <= x && i + 5 < 19; i++) {
				int number = position[i][y] + position[i+1][y] + position[i+2][y]
						+ position[i+3][y] + position[i+4][y] + position[i+5][y];
				if (number == 0 || (number > 6 && number % 7 != 0)) {
					continue;
				}
				if (number < 7) {
					if (type == BLACK) {
						numberOfMyRoad[number]++;
					} else {
						numberOfEnemyRoad[number]++;
					}
					
				} else {
					if (type == BLACK) {
						numberOfEnemyRoad[number/7]++;
					} else {
						numberOfMyRoad[number/7]++;
					}
					
				}
			}
		}
		if (move.getPoint2() != null) {
			int x = move.getPoint2().getX();
			int y = move.getPoint2().getY();
			for (int i = x-5>0?x-5:0; i <= x && i + 5 < 19; i++) {
				int xx = move.getPoint1().getX();
				if (i == xx || i+1 == xx || i+2 == xx || i+3 == xx || i+4 == xx || i+5 == xx) {//去重复
					continue;
				}
				int number = position[i][y] + position[i+1][y] + position[i+2][y]
						+ position[i+3][y] + position[i+4][y] + position[i+5][y];
				if (number == 0 || (number > 6 && number % 7 != 0)) {
					continue;
				}
				if (number < 7) {
					if (type == BLACK) {
						numberOfMyRoad[number]++;
					} else {
						numberOfEnemyRoad[number]++;
					}
					
				} else {
					if (type == BLACK) {
						numberOfEnemyRoad[number/7]++;
					} else {
						numberOfMyRoad[number/7]++;
					}
					
				}
			}
		}
		if (type == BLACK && numberOfEnemyRoad[4] + numberOfEnemyRoad[5] > 0) {
			return 1;
		}
		if (type == WHITE && numberOfMyRoad[4] + numberOfMyRoad[5] > 0) {
			return 1;
		}
		return 0;
	}
	
	private int analysisLeft(byte[][] position, int type, PointMove move) {
//		for (int i = 0; i < ROWS - 5; i++) {
//			for (int j = 5; j < COLS; j++) {
//				int number = position[i][j] + position[i+1][j-1] + position[i+2][j-2]
//						+ position[i+3][j-3] + position[i+4][j-4] + position[i+5][j-5];
//				if (number == 0 || (number > 6 && number % 7 != 0)) {
//					continue;
//				}
//				if (number < 7) {
//					numberOfMyRoad[number]++;
//				} else {
//					numberOfEnemyRoad[number/7]++;
//				}
//			}
//		}
		if (move.getPoint1() != null) {
			int x = move.getPoint1().getX();
			int y = move.getPoint1().getY();
			int len;
			if (x - 5 < 0 || y + 5 > 18) {
				len = x - 0 < 18 - y ? x - 0 : 18 - y;
			} else {
				len = 5;
			}
			for (int i = x - len, j = y + len; i <= x && j >= y && i+5 < 19 && j-5 >= 0; i++, j--) {
				int number = position[i][j] + position[i+1][j-1] + position[i+2][j-2]
						+ position[i+3][j-3] + position[i+4][j-4] + position[i+5][j-5];
				if (number == 0 || (number > 6 && number % 7 != 0)) {
					continue;
				}
				if (number < 7) {
					if (type == BLACK) {
						numberOfMyRoad[number]++;
					} else {
						numberOfEnemyRoad[number]++;
					}
					
				} else {
					if (type == BLACK) {
						numberOfEnemyRoad[number/7]++;
					} else {
						numberOfMyRoad[number/7]++;
					}
					
				}
			}
		}
		if (move.getPoint2() != null) {
			int x = move.getPoint2().getX();
			int y = move.getPoint2().getY();
			int len;
			if (x - 5 < 0 || y + 5 > 18) {
				len = x - 0 < 18 - y ? x - 0 : 18 - y;
			} else {
				len = 5;
			}
			for (int i = x - len, j = y + len; i <= x && j >= y && i+5 < 19 && j-5 >= 0; i++, j--) {
				int xx = move.getPoint1().getX();
				int yy = move.getPoint1().getY();
				if ((i == xx && j == yy) || (i+1 == xx && j-1 == yy) || (i+2 == xx && j-2 == yy)
						|| (i+3 == xx && j-3 == yy) || (i+4 == xx && j-4 == yy) || (i+5 == xx && j-5 == yy)) {//去重复
					continue;
				}
				int number = position[i][j] + position[i+1][j-1] + position[i+2][j-2]
						+ position[i+3][j-3] + position[i+4][j-4] + position[i+5][j-5];
				if (number == 0 || (number > 6 && number % 7 != 0)) {
					continue;
				}
				if (number < 7) {
					if (type == BLACK) {
						numberOfMyRoad[number]++;
					} else {
						numberOfEnemyRoad[number]++;
					}
					
				} else {
					if (type == BLACK) {
						numberOfEnemyRoad[number/7]++;
					} else {
						numberOfMyRoad[number/7]++;
					}
					
				}
			}
		}
		if (type == BLACK && numberOfEnemyRoad[4] + numberOfEnemyRoad[5] > 0) {
			return 1;
		}
		if (type == WHITE && numberOfMyRoad[4] + numberOfMyRoad[5] > 0) {
			return 1;
		}
		return 0;
	}
	
	private int analysisRight(byte[][] position, int type, PointMove move) {
//		for (int i = 0; i < ROWS - 5; i++) {
//			for (int j = 0; j < COLS - 5; j++) {
//				int number = position[i][j] + position[i+1][j+1] + position[i+2][j+2]
//						+ position[i+3][j+3] + position[i+4][j+4] + position[i+5][j+5];
//				if (number == 0 || (number > 6 && number % 7 != 0)) {
//					continue;
//				}
//				if (number < 7) {
//					numberOfMyRoad[number]++;
//				} else {
//					numberOfEnemyRoad[number/7]++;
//				}
//			}
//		}
		if (move.getPoint1() != null) {
			int x = move.getPoint1().getX();
			int y = move.getPoint1().getY();
			int len;
			if (x - 5 < 0 || y - 5 < 0) {
				len = x - 0 < y - 0 ? x - 0 : y - 0;
			} else {
				len = 5;
			}
			for (int i = x - len, j = y - len; i <= x && j <= y && i + 5 < 19 && j + 5 < 19; i++, j++) {
				int number = position[i][j] + position[i+1][j+1] + position[i+2][j+2]
						+ position[i+3][j+3] + position[i+4][j+4] + position[i+5][j+5];
				if (number == 0 || (number > 6 && number % 7 != 0)) {
					continue;
				}
				if (number < 7) {
					if (type == BLACK) {
						numberOfMyRoad[number]++;
					} else {
						numberOfEnemyRoad[number]++;
					}
					
				} else {
					if (type == BLACK) {
						numberOfEnemyRoad[number/7]++;
					} else {
						numberOfMyRoad[number/7]++;
					}
					
				}
			}
		}
		if (move.getPoint2() != null) {
			int x = move.getPoint2().getX();
			int y = move.getPoint2().getY();
			int len;
			if (x - 5 < 0 || y - 5 < 0) {
				len = x - 0 < y - 0 ? x - 0 : y - 0;
			} else {
				len = 5;
			}
			for (int i = x - len, j = y - len; i <= x && j <= y && i + 5 < 19 && j + 5 < 19; i++, j++) {
				int xx = move.getPoint1().getX();
				int yy = move.getPoint1().getY();
				if ((i == xx && j == yy) || (i+1 == xx && j+1 == yy) || (i+2 == xx && j+2 == yy)
						|| (i+3 == xx && j+3 == yy) || (i+4 == xx && j+4 == yy) || (i+5 == xx && j+5 == yy)) {//去重复
					continue;
				}
				int number = position[i][j] + position[i+1][j+1] + position[i+2][j+2]
						+ position[i+3][j+3] + position[i+4][j+4] + position[i+5][j+5];
				if (number == 0 || (number > 6 && number % 7 != 0)) {
					continue;
				}
				if (number < 7) {
					if (type == BLACK) {
						numberOfMyRoad[number]++;
					} else {
						numberOfEnemyRoad[number]++;
					}
					
				} else {
					if (type == BLACK) {
						numberOfEnemyRoad[number/7]++;
					} else {
						numberOfMyRoad[number/7]++;
					}
					
				}
			}
		}
		if (type == BLACK && numberOfEnemyRoad[4] + numberOfEnemyRoad[5] > 0) {
			return 1;
		}
		if (type == WHITE && numberOfMyRoad[4] + numberOfMyRoad[5] > 0) {
			return 1;
		}
		return 0;
	}
	
	private void makeMove(byte[][] position, PointMove move, int type) {
		if (move.getPoint1() != null) {
			position[move.getPoint1().getX()][move.getPoint1().getY()] = (byte)type;
		}
		if (move.getPoint2() != null) {
			position[move.getPoint2().getX()][move.getPoint2().getY()] = (byte)type;
		}
	}
	
	private void unMakeMove(byte[][] position, PointMove move) {
		if (move.getPoint1() != null) {
			position[move.getPoint1().getX()][move.getPoint1().getY()] = 0;
		}
		if (move.getPoint2() != null) {
			position[move.getPoint2().getX()][move.getPoint2().getY()] = 0;
		}
	}

	@Override
	public int evalute(byte[][] position, int type) {
		// TODO Auto-generated method stub
		return 0;
	}
}
