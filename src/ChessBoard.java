import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.util.Stack;

import javax.swing.JOptionPane;
import javax.swing.JPanel;





public class ChessBoard extends JPanel implements MouseListener, Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int MARGIN=30;//边距  
	private static final int GRID_SPAN=30;//网格间距
	private static final int DIAMETER=30;//直径
	private static final int ROWS=19;//棋盘行数  
	private static final int COLS=19;//棋盘列数
	private static final int WINCOUNT=6;//赢棋连子数
	private static final int BLACK = 1;
	private static final int WHITE = 7;
	
	//Point[] chessList=new Point[(ROWS+1)*(COLS+1)];//初始每个数组元素为null
	byte[][] chessStatus = new byte[ROWS][COLS];
    int type = BLACK;//默认开始是黑棋先
    boolean gameOver = false;//游戏是否结束
    boolean isStart = true;//是否是开局第一步棋
    int step = 0;//每个人下棋落子数
    PointMove curMove;
    Stack<PointMove> moves = new Stack<PointMove>();
   
    Image img;

    
    public ChessBoard(){
    	// setBackground(Color.blue);//设置背景色为橘黄色
    	//img=Toolkit.getDefaultToolkit().getImage("board.jpg");
    	//shadows=Toolkit.getDefaultToolkit().getImage("shadows.jpg");
    	restartGame();
    	addMouseListener(this);
    	addMouseMotionListener(new MouseMotionListener(){
    		public void mouseDragged(MouseEvent e){
    			
    		}
  		   
			public void mouseMoved(MouseEvent e){
				int x1=(e.getX()-MARGIN+GRID_SPAN/2)/GRID_SPAN;
				//将鼠标点击的坐标位置转成网格索引
				int y1=(e.getY()-MARGIN+GRID_SPAN/2)/GRID_SPAN;
				//游戏已经结束不能下
				//落在棋盘外不能下
				//x，y位置已经有棋子存在，不能下
				if(x1<0||x1>=ROWS||y1<0||y1>=COLS||gameOver||findChess(x1,y1))
					setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				//设置成默认状态
				else setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
		});
    	
    	ChessManager.initHashKey();
    }

    
    public void paintComponent(Graphics g){
   	 
 	   super.paintComponent(g);//画棋盘
 	 
// 	   int imgWidth= img.getWidth(this);
// 	   int imgHeight=img.getHeight(this);//获得图片的宽度与高度
// 	   int FWidth=getWidth();
// 	   int FHeight=getHeight();//获得窗口的宽度与高度
// 	   int x=(FWidth-imgWidth)/2;
// 	   int y=(FHeight-imgHeight)/2;
// 	   g.drawImage(img, x, y, null);
 	
 	  int xPos;
 	  int yPos;
 	   
 	   for(int i=0;i<ROWS;i++){//画横线
 		   g.drawLine(MARGIN, MARGIN+i*GRID_SPAN, MARGIN+(COLS-1)*GRID_SPAN, MARGIN+i*GRID_SPAN);
 	   }
 	   for(int i=0;i<COLS;i++){//画竖线
 		   g.drawLine(MARGIN+i*GRID_SPAN, MARGIN, MARGIN+i*GRID_SPAN, MARGIN+(ROWS-1)*GRID_SPAN);
 		   
 	   }
 	   
 	   xPos=9*GRID_SPAN+MARGIN;
 	   yPos=9*GRID_SPAN+MARGIN;
 	   g.fillOval(xPos - 5, yPos - 5, 10, 10);
 	   
 	   xPos=3*GRID_SPAN+MARGIN;
	   yPos=3*GRID_SPAN+MARGIN;
	   g.fillOval(xPos - 5, yPos - 5, 10, 10);
	   
	   xPos=15*GRID_SPAN+MARGIN;
 	   yPos=15*GRID_SPAN+MARGIN;
 	   g.fillOval(xPos - 5, yPos - 5, 10, 10);
 	   
 	   xPos=3*GRID_SPAN+MARGIN;
	   yPos=15*GRID_SPAN+MARGIN;
	   g.fillOval(xPos - 5, yPos - 5, 10, 10);
	   
	   xPos=15*GRID_SPAN+MARGIN;
 	   yPos=3*GRID_SPAN+MARGIN;
 	   g.fillOval(xPos - 5, yPos - 5, 10, 10);
 	   
 	   //画棋子
 	   for(int i=0;i<ROWS;i++) {
 		   for(int j = 0; j < COLS; j++) {
				//网格交叉点x，y坐标
				xPos=i*GRID_SPAN+MARGIN;
				yPos=j*GRID_SPAN+MARGIN;
				if(chessStatus[i][j] == BLACK){
					RadialGradientPaint paint = new RadialGradientPaint(xPos-DIAMETER/2+25, yPos-DIAMETER/2+10, 20, new float[]{0f, 1f}
					, new Color[]{Color.WHITE, Color.BLACK});
				    ((Graphics2D) g).setPaint(paint);
				    ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				    ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT);
				    Ellipse2D e = new Ellipse2D.Float(xPos-DIAMETER/2, yPos-DIAMETER/2, DIAMETER, DIAMETER);
					((Graphics2D) g).fill(e);
				}
				else if(chessStatus[i][j] == WHITE){
				   RadialGradientPaint paint = new RadialGradientPaint(xPos-DIAMETER/2+25, yPos-DIAMETER/2+10, 70, new float[]{0f, 1f}
				   , new Color[]{Color.WHITE, Color.BLACK});
				   ((Graphics2D) g).setPaint(paint);
				   ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				   ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT);
				   Ellipse2D e = new Ellipse2D.Float(xPos-DIAMETER/2, yPos-DIAMETER/2, DIAMETER, DIAMETER);
				   ((Graphics2D) g).fill(e);
				}
				if (curMove != null) {
//					if(i*ROWS+j == moves.peek()){//如果是最后一个棋子
//			 			   g.setColor(Color.red);
//			 			   g.drawRect(xPos-DIAMETER/2, yPos-DIAMETER/2,
//			 				           DIAMETER, DIAMETER);
//			 		}
					if ((curMove.getPoint1() != null && i == curMove.getPoint1().getX() && j == curMove.getPoint1().getY())
							|| (curMove.getPoint2() != null && i == curMove.getPoint2().getX() && j == curMove.getPoint2().getY())) {
						g.setColor(Color.RED);
						g.drawRect(xPos-DIAMETER/2, yPos-DIAMETER/2,
		 				           DIAMETER, DIAMETER);
					}
				}
 		   }
  	   }
    }
	
	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		//游戏结束时，不再能下
		if(gameOver) return;
		Point point = getCurMovePoint(e);
		if (point == null) {
			return;
		}		
		playerMove(point);
		//computerMove();
	}


	private void playerMove(Point point) {
		if (step%2 == 0) {
			curMove = new PointMove();
			curMove.setPoint1(point);
		} else {
			curMove.setPoint2(point);
		}
		makeMove();
		step++;
		
		if (isWin()) {
			showWinMessage();
			gameOver = true;
			return;
		}
		
		if (!isStart) {
			if (step%2 == 1) {
				return;
			}
		}
		
		isStart = false;
		changeType();
	}


	private void changeType() {
		step = 0;
		type = 8 - type;
	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}
	
	private Point getCurMovePoint(MouseEvent e) {
		int xIndex;
		int yIndex;
		//String colorName=isBlack?"黑棋":"白棋";
		   
		//将鼠标点击的坐标位置转换成网格索引
		xIndex=(e.getX()-MARGIN+GRID_SPAN/2)/GRID_SPAN;
		yIndex=(e.getY()-MARGIN+GRID_SPAN/2)/GRID_SPAN;
		   
		if(!isValidPoint(xIndex, yIndex)) {
			return null;
		}
		   
		//可以进行时的处理
		Point point = new Point(xIndex, yIndex);
		return point;
	}
	
	private boolean isValidPoint(int xIndex, int yIndex) {
		//落在棋盘外不能下
		if(xIndex<0||xIndex>=ROWS||yIndex<0||yIndex>=COLS)
			return false;
		   
		//如果x，y位置已经有棋子存在，不能下
		if(findChess(xIndex,yIndex))
			return false;
		
		return true;
	}
	
	private boolean findChess(int x,int y){
	   if (chessStatus[x][y] != 0) {
		   return true;
	   }
	   return false;
    }
	
	private void makeMove() {
		if (curMove.getPoint1() != null) {
			chessStatus[curMove.getPoint1().getX()][curMove.getPoint1().getY()] = (byte)type;
		}
		if (curMove.getPoint2() != null) {
			chessStatus[curMove.getPoint2().getX()][curMove.getPoint2().getY()] = (byte)type;
		}
		repaint();
	}
	
	private boolean isWin(){
		int continueCount = 1;//连续棋子的个数
		int c = type;//最后一步棋子颜色
		int xIndex;
		int yIndex;
		for (int i = 0; i < 2; i++) {
			if (i == 0) {
				xIndex = curMove.getPoint1().getX();
				yIndex = curMove.getPoint1().getY();
			} else {
				if (curMove.getPoint2() == null) {
					return false;
				}
				xIndex = curMove.getPoint2().getX();
				yIndex = curMove.getPoint2().getY();
			}
			
			//横向向西寻找
			for(int x=xIndex-1;x>=0;x--){
				if(chessStatus[x][yIndex]==c){
					continueCount++;
				}else
					break;
			}
			//横向向东寻找
			for(int x=xIndex+1;x<COLS;x++){
				if(chessStatus[x][yIndex]==c){
					continueCount++;
				}else
					break;
			}
			if(continueCount>=WINCOUNT){
				return true;
			}else
				continueCount=1;
			//继续另一种搜索纵向
			//向上搜索
			for(int y=yIndex-1;y>=0;y--){
				if(chessStatus[xIndex][y]==c){
					continueCount++;
				}else
					break;
			}
			//纵向向下寻找
			for(int y=yIndex+1;y<ROWS;y++){
				if(chessStatus[xIndex][y]==c)
					continueCount++;
				else
					break;
			}
			if(continueCount>=WINCOUNT)
				return true;
			else
				continueCount=1;
			//继续另一种情况的搜索：斜向
			//东北寻找
			for(int x=xIndex+1,y=yIndex-1;y>=0&&x<COLS;x++,y--){
				if(chessStatus[x][y]==c){
					continueCount++;
				}
				else break;
			}
			//西南寻找
			for(int x=xIndex-1,y=yIndex+1;x>=0&&y<ROWS;x--,y++){
				if(chessStatus[x][y]==c){
					continueCount++;
				}
				else break;
			}
			if(continueCount>=WINCOUNT)
				return true;
			else continueCount=1;
			//继续另一种情况的搜索：斜向
			//西北寻找
			for(int x=xIndex-1,y=yIndex-1;x>=0&&y>=0;x--,y--){
				if(chessStatus[x][y]==c)
					continueCount++;
				else break;
			}
			//东南寻找
			for(int x=xIndex+1,y=yIndex+1;x<COLS&&y<ROWS;x++,y++){
				if(chessStatus[x][y]==c)
					continueCount++;
				else break;
			}
			if(continueCount>=WINCOUNT)
				return true;
			else
				continueCount = 1;
		}
		
		return false;
	}
	
	private void showWinMessage() {
		String colorName;
		if (type == BLACK) {
			colorName = "黑棋";
		} else {
			colorName = "白棋";
		}
		String msg=String.format("恭喜，%s赢了！", colorName);
		JOptionPane.showMessageDialog(this, msg);
	}
	
	public void computerMove() {
		curMove = ChessManager.move(chessStatus, type);
		makeMove();
		if (isWin()) {
			showWinMessage();
			gameOver = true;
		}
		changeType();
	}
	
	public void restartGame(){
		//清除棋子
		for(int i=0;i<ROWS;i++){
			for (int j = 0; j < COLS; j++) {
				chessStatus[i][j] = 0;
			}
		}
		//恢复游戏相关的变量值
		type = BLACK;
		gameOver=false; //游戏是否结束
		isStart = true;
		step = 0;
		moves.clear(); //清空栈
		curMove = null;
		repaint();
	}
	   
	//悔棋
	public void goback(){
		if(moves.empty())
			return ;
		curMove = moves.pop();
		chessStatus[curMove.getPoint1().getX()][curMove.getPoint1().getY()] = 0;
		chessStatus[curMove.getPoint2().getX()][curMove.getPoint2().getY()] = 0;
		if(!moves.empty()){
			curMove = moves.peek();
		}
		type = 8 - type;
		repaint();
	}

	public void startGame(int startType) {
		if (startType == BLACK) {
			curMove = new PointMove();
			curMove.setPoint1(new Point(9, 9));
			makeMove();
			isStart = !isStart;
			type = 8 - type;
		}
	}
	   
	//矩形Dimension
	public Dimension getPreferredSize(){
		return new Dimension(MARGIN*2+GRID_SPAN*(COLS-1),MARGIN*2
				+GRID_SPAN*(ROWS-1));
	}


	@Override
	public void run() {
		computerMove();
	}
}
