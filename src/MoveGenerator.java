import java.util.ArrayList;
import java.util.List;


public class MoveGenerator {
	
	private static final int ROWS=19;//棋盘行数  
	private static final int COLS=19;//棋盘列数
	private static final int WINCOUNT=6;//赢棋连子数

	public PointMove[][] moveList = new PointMove[7][100000];
	
	private int moveCount;

	
	public int sort_CreatePossibleMove(byte[][] position, int nPly, int type) {
		int[] bestPos = new int[4];
		List<Point> needPos = new ArrayList<Point>();
		moveCount = 0;
		if (analysisHorizon(position, type, bestPos, needPos)) {
			moveList[nPly][0] = new PointMove();
			moveList[nPly][0].setPoint1(new Point(bestPos[0], bestPos[1]));
			moveList[nPly][0].setPoint2(new Point(bestPos[2], bestPos[3]));
			moveList[nPly][0].setScore(100000);
			return -1;
		}
		if (analysisVertical(position, type, bestPos, needPos)) {
			moveList[nPly][0] = new PointMove();
			moveList[nPly][0].setPoint1(new Point(bestPos[0], bestPos[1]));
			moveList[nPly][0].setPoint2(new Point(bestPos[2], bestPos[3]));
			moveList[nPly][0].setScore(100000);
			return -1;
		}
		if (analysisLeft(position, type, bestPos, needPos)) {
			moveList[nPly][0] = new PointMove();
			moveList[nPly][0].setPoint1(new Point(bestPos[0], bestPos[1]));
			moveList[nPly][0].setPoint2(new Point(bestPos[2], bestPos[3]));
			moveList[nPly][0].setScore(100000);
			return -1;
		}
		if (analysisRight(position, type, bestPos, needPos)) {
			moveList[nPly][0] = new PointMove();
			moveList[nPly][0].setPoint1(new Point(bestPos[0], bestPos[1]));
			moveList[nPly][0].setPoint2(new Point(bestPos[2], bestPos[3]));
			moveList[nPly][0].setScore(100000);
			return -1;
		}
		int i_min = 19, i_max = 0, j_min = 19, j_max = 0;
		for (int i = 0; i < 19; i++) {
			for (int j = 0; j < 19; j++) {
				if (position[i][j] != 0) {
					if (i < i_min) {
						i_min = i;
					}
					if (i > i_max) {
						i_max = i;
					}
					if (j < j_min) {
						j_min = j;
					}
					if (j > j_max) {
						j_max = j;
					}
				}
			}
		}
		i_min -= 2;
		i_max += 3;
		j_min -= 2;
		j_max += 3;
		if (i_min <= 0) {
			i_min = 0;
		}
		if (i_max >= 19) {
			i_max = 19;
		}
		if (j_min <= 0) {
			j_min = 0;
		}
		if (j_max >= 19) {
			j_max = 19;
		}
		
		if (!needPos.isEmpty()) {
			for (int i = 0; i < needPos.size(); i++) {
				for (int j = i+1; j < needPos.size(); j++) {
					moveList[nPly][moveCount] = new PointMove();
					moveList[nPly][moveCount].setPoint1(new Point(needPos.get(i).getX(), needPos.get(i).getY()));
					moveList[nPly][moveCount].setPoint2(new Point(needPos.get(j).getX(), needPos.get(j).getY()));
					moveCount++;
				}
			}
			
			for (int it = 0; it < needPos.size(); it++) {
				for (int i = i_min; i < i_max; i++) {
					for (int j = j_min; j < j_max; j++) {
						if (position[i][j] == 0 && (i != needPos.get(it).getX() || j != needPos.get(it).getY())) {
							position[needPos.get(it).getX()][needPos.get(it).getY()] = (byte)type;
							if (isValidPosition(position, i, j)) {
								moveList[nPly][moveCount] = new PointMove();
								moveList[nPly][moveCount].setPoint1(new Point(needPos.get(it).getX(), needPos.get(it).getY()));
								moveList[nPly][moveCount].setPoint2(new Point(i, j));
								moveCount++;
							}
						}
						position[needPos.get(it).getX()][needPos.get(it).getY()] = 0;
					}
					
				}
			}
			return moveCount;
		} else {
			for (int i1 = i_min; i1 < i_max; i1++) {
				for (int j1 = j_min; j1 < j_max; j1++) {
					if (position[i1][j1] == 0 && isValidPosition(position, i1, j1)) {
						position[i1][j1] = (byte)type;
						for (int i2 = i_min; i2 < i_max; i2++) {
							for (int j2 = j_min; j2 < j_max; j2++) {
								if (position[i2][j2] == 0 && isValidPosition(position, i2, j2)) {
									moveList[nPly][moveCount] = new PointMove();
									moveList[nPly][moveCount].setPoint1(new Point(i1, j1));
									moveList[nPly][moveCount].setPoint2(new Point(i2, j2));
									moveCount++;
								}
							}
						}
						position[i1][j1] = 0;
					}
				}
			}
			return moveCount;
		}
	}
	
	public int createPossibleMove(byte[][] position, int nPly, int type) {
		return 0;
	}
	
	public void printPointList() {
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 20000; j++) {
				if (moveList[i][j] != null) {
					System.out.println(moveList[i][j]);
				}
			}
		}
	}
	
	private boolean isValidPosition(byte[][] position, int i, int j) {
		
		if (j-2 >= 0 && position[i][j-2] != 0) {
			return true;
		}
		if (j-1 >= 0 && position[i][j-1] != 0) {
			return true;
		}
		if (j+2 < 19 && position[i][j+2] != 0) {
			return true;
		}
		if (j+1 < 19 && position[i][j+1] != 0) {
			return true;
		}
		if (i-2 >= 0 && position[i-2][j] != 0) {
			return true;
		}
		if (i-1 >= 0 && position[i-1][j] != 0) {
			return true;
		}
		if (i+2 < 19 && position[i+2][j] != 0) {
			return true;
		}
		if (i+1 < 19 && position[i+1][j] != 0) {
			return true;
		}
		if (i-2 >= 0 && j-2 >= 0 && position[i-2][j-2] != 0) {
			return true;
		}
		if (i-1 >= 0 && j-1 >= 0 && position[i-1][j-1] != 0) {
			return true;
		}
		if (i+2 < 19 && j+2 < 19 && position[i+2][j+2] != 0) {
			return true;
		}
		if (i+1 < 19 && j+1 < 19 && position[i+1][j+1] != 0) {
			return true;
		}
		if (i-2 >= 0 && j+2 < 19 && position[i-2][j+2] != 0) {
			return true;
		}
		if (i-1 >= 0 && j+1 < 19 && position[i-1][j+1] != 0) {
			return true;
		}
		if (i+2 < 19 && j-2 >= 0 && position[i+2][j-2] != 0) {
			return true;
		}
		if (i+1 < 19 && j-1 >= 0 && position[i+1][j-1] != 0) {
			return true;
		}
		return false;
	}
	
	private boolean analysisHorizon(byte[][] position, int type, int bestPos[], List<Point> needPos) {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS - 5; j++) {
				int number = position[i][j] + position[i][j+1] + position[i][j+2] + position[i][j+3]
						+ position[i][j+4] + position[i][j+5];
				if (number == 4 * type) {  //判断我方是否有4路
					int index = 0;
					for (int k = 0; k < WINCOUNT; k++) {
						if (position[i][j+k] == 0) {
							bestPos[index] = i;
							bestPos[index+1] = j + k;
							index += 2;
						}
					}
					return true;
				} else if (number == 5 * type) { //判断我方是否有5路
					for (int k = 0; k < WINCOUNT; k++) {
						if (position[i][j+k] == 0) {
							bestPos[0] = i;
							bestPos[1] = j + k;
							for (int x = 0; x < ROWS; x++) {
								for (int y = 0; y < COLS; y++) {
									if (position[x][y] == 0) {
										bestPos[2] = x;
										bestPos[3] = y;
										return true;
									}
								}
							}
						}
					}
				}
				
				if (number == 4 * (8 - type) || number == 5 * (8 - type)) {  //判断对方是否有4路或5路
					for (int k = 0; k < WINCOUNT; k++) {
						if (position[i][j+k] == 0) {
							Point pos = new Point(i, j+k);
							needPos.add(pos);
						}
					}
				}
			}
		}
		return false;
	}
	
	private boolean analysisVertical(byte[][] position, int type, int bestPos[], List<Point> needPos) {
		for (int j = 0; j < COLS; j++) {
			for (int i = 0; i < ROWS - 5; i++) {
				int number = position[i][j] + position[i+1][j] + position[i+2][j] + position[i+3][j]
						+ position[i+4][j] + position[i+5][j];
				if (number == 4 * type) {
					int index = 0;
					for (int k = 0; k < WINCOUNT; k++) {
						if (position[i+k][j] == 0) {
							bestPos[index] = i + k;
							bestPos[index+1] = j;
							index += 2;
						}
					}
					return true;
				} else if (number == 5 * type) {
					for (int k = 0; k < WINCOUNT; k++) {
						if (position[i+k][j] == 0) {
							bestPos[0] = i + k;
							bestPos[1] = j;
							for (int x = 0; x < ROWS; x++) {
								for (int y = 0; y < COLS; y++) {
									if (position[x][y] == 0) {
										bestPos[2] = x;
										bestPos[3] = y;
										return true;
									}
								}
							}
						}
					}
				}
				
				if (number == 4 * (8 - type) || number == 5 * (8 - type)) {
					for (int k = 0; k < WINCOUNT; k++) {
						if (position[i+k][j] == 0) {
							Point pos = new Point(i+k, j);
							needPos.add(pos);
						}
					}
				}
			}
		}
		return false;
	}
	
	private boolean analysisLeft(byte[][] position, int type, int bestPos[], List<Point> needPos) {
		for (int i = 0; i < ROWS - 5; i++) {
			for (int j = 5; j < COLS; j++) {
				int number = position[i][j] + position[i+1][j-1] + position[i+2][j-2] + position[i+3][j-3]
						+ position[i+4][j-4] + position[i+5][j-5];
				if (number == 4 * type) {
					int index = 0;
					for (int k = 0; k < WINCOUNT; k++) {
						if (position[i+k][j-k] == 0) {
							bestPos[index] = i + k;
							bestPos[index+1] = j - k;
							index += 2;
						}
					}
					return true;
				} else if (number == 5 * type) {
					for (int k = 0; k < WINCOUNT; k++) {
						if (position[i+k][j-k] == 0) {
							bestPos[0] = i + k;
							bestPos[1] = j - k;
							for (int x = 0; x < ROWS; x++) {
								for (int y = 0; y < COLS; y++) {
									if (position[x][y] == 0) {
										bestPos[2] = x;
										bestPos[3] = y;
										return true;
									}
								}
							}
						}
					}
				}
				
				if (number == 4 * (8 - type) || number == 5 * (8 - type)) {
					for (int k = 0; k < WINCOUNT; k++) {
						if (position[i+k][j-k] == 0) {
							Point pos = new Point(i+k, j-k);
							needPos.add(pos);
						}
					}
				}
			}
		}
		return false;
	}
	
	private boolean analysisRight(byte[][] position, int type, int bestPos[], List<Point> needPos) {
		for (int i = 0; i < ROWS - 5; i++) {
			for (int j = 0; j < COLS - 5; j++) {
				int number = position[i][j] + position[i+1][j+1] + position[i+2][j+2] + position[i+3][j+3]
						+ position[i+4][j+4] + position[i+5][j+5];
				if (number == 4 * type) {
					int index = 0;
					for (int k = 0; k < WINCOUNT; k++) {
						if (position[i+k][j+k] == 0) {
							bestPos[index] = i + k;
							bestPos[index+1] = j + k;
							index += 2;
						}
					}
					return true;
				} else if (number == 5 * type) {
					for (int k = 0; k < WINCOUNT; k++) {
						if (position[i+k][j+k] == 0) {
							bestPos[0] = i + k;
							bestPos[1] = j + k;
							for (int x = 0; x < ROWS; x++) {
								for (int y = 0; y < COLS; y++) {
									if (position[x][y] == 0) {
										bestPos[2] = x;
										bestPos[3] = y;
										return true;
									}
								}
							}
						}
					}
				}
				
				if (number == 4 * (8 - type) || number == 5 * (8 - type)) {
					for (int k = 0; k < WINCOUNT; k++) {
						if (position[i+k][j+k] == 0) {
							Point pos = new Point(i+k, j+k);
							needPos.add(pos);
						}
					}
				}
			}
		}
		return false;
	}
}

