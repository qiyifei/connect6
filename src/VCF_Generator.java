

public class VCF_Generator {
	public PointMove[][] VCFList = new PointMove[10][10000];
	private PointMove[][] VCFList_doublethreat_bydoublestep = new PointMove[10][50];//能用两步走成一个活四生成两个威胁度的走法
	private Point[][] VCFList_doublethreat_bysinglestep = new Point[10][50];//能用一步走成一个活四生成两个威胁度的走法
	private Point[][] VCFList_singlethreat_bysinglestep = new Point[10][50];//能用一步走成一个眠四生成一个威胁度的走法
	private int moveCount0;
	private int moveCount1;
	private int moveCount2;
	public int moveCount;
	private int i_min, i_max, j_min, j_max;
	
	public int VCF_CreatePossibleMove(byte[][] position, int nPly, int type) 
	{	//参数初始化
		moveCount = 0;
		moveCount0 = 0;
		moveCount1 = 0;
		moveCount2 = 0;
		i_min = 19;
		i_max = 0;
		j_min = 19;
		j_max = 0;
		//为落子范围确定边界，每一次调用VCF_CreatePossibleMove（）方法时，都要重新确定边界
		for (int i = 0; i < 19; i++)
		{	for (int j = 0; j < 19; j++) 
			{	if (position[i][j] != 0) 
				{	if (i < i_min) 
					{	
						i_min = i;
					}
					if (i > i_max) 
					{
						i_max = i;
					}
					if (j < j_min) 
					{
						j_min = j;
					}
					if (j > j_max) 
					{
						j_max = j;
					}
				}
			}
		}
		//在这个范围的基础上再向左扩2向右扩2，向上扩2，向下扩2
		i_min -= 2;
		i_max += 2;
		j_min -= 2;
		j_max += 2;
		//确保不越界
		if (i_min <= 0) {
			i_min = 0;
		}
		if (i_max >= 18) {
			i_max = 18;
		}
		if (j_min <= 0) {
			j_min = 0;
		}
		if (j_max >= 18) {
			j_max = 18;
		}
		Analys_Vertical(position,type,nPly);
		Analys_Horizontal(position,type,nPly);
		Analys_Left(position,type,nPly);
		Analys_Right(position,type,nPly);
		//至此，“双威胁度/双着点”，“双威胁度/单着点”，“单威胁度/单着点”三个名单已经整合完毕
		//现将三个名单最后汇总到走法中
		//首先，明显一步形成两个威胁度的点更具威胁，因此，先将这样的点加入走法
		if(moveCount1 >= 2)//这种情况存在连五的点，最后还需要估值判别（两个威胁度）
		{				 //若非连五的点大概就是必胜走法了吧（四个威胁度）
			for(int i = 0; i <= moveCount1 - 1; i++)
				for(int j = i + 1; j <= moveCount1 - 1; j++)
				{
					VCFList[nPly][moveCount] = new PointMove();
					VCFList[nPly][moveCount].setPoint1(VCFList_doublethreat_bysinglestep[nPly][i]);
					VCFList[nPly][moveCount].setPoint1(VCFList_doublethreat_bysinglestep[nPly][j]);
					moveCount++;
				}
		}
		if(moveCount1 > 0 && moveCount2 > 0)//“双威胁度/单着点”与“单威胁度/单着点”一一组合，即一个走法形成3个威胁度
		{				  //同样存在不被期望的组合，后需通过估值函数评判舍掉
			for(int i = 0; i <= moveCount1 - 1; i++)
				for(int j = 0; j <= moveCount2 - 1; j++)
				{
					VCFList[nPly][moveCount] = new PointMove();
					VCFList[nPly][moveCount].setPoint1(VCFList_doublethreat_bysinglestep[nPly][i]);
					VCFList[nPly][moveCount].setPoint1(VCFList_singlethreat_bysinglestep[nPly][j]);
					moveCount++;
				}
		}
		if(moveCount0 > 0)//“双威胁度/双着点”加入走法
		{				  //不存在不被期望的走法，但是走法间肯定也存在更优的走法，许估值判别
			for(int i = 0; i <= moveCount0 - 1; i++)
			{
				VCFList[nPly][moveCount] = new PointMove();
				VCFList[nPly][moveCount] = VCFList_doublethreat_bydoublestep[nPly][i];
				moveCount++;
			}
		}
		if(moveCount2 > 0)//“单威胁度/单着点”一一组合加入走法
		{				  //同样存在不被期望的组合，后需通过估值函数评判舍掉	
			for(int i = 0; i <= moveCount2 - 1; i++)
				for(int j = i + 1; j <= moveCount2 - 1; j++)
				{
					VCFList[nPly][moveCount] = new PointMove();
					VCFList[nPly][moveCount].setPoint1(VCFList_singlethreat_bysinglestep[nPly][i]);
					VCFList[nPly][moveCount].setPoint1(VCFList_singlethreat_bysinglestep[nPly][j]);
					moveCount++;
				}
		}
		return moveCount;
	}


	private void Analys_Vertical(byte[][] position, int type, int nPly)
	{	for(int i = i_min; i <= i_max; i++)
		{	for(int j = j_min; j <= j_max - 5; j++)
			{
				int number = position[i][j] + position[i][j+1] + position[i][j+2] + position[i][j+3]
							+ position[i][j+4] + position[i][j+5];
				if(number == 2 * type)
				{//以6为长度的队列是否有2个同色子，而且其余为空
					for(int m = j; m <= j + 3; m++)
					{//如果这2个子在一个长度为4的队列里，或者说，这2个子之间的距离不超过2，那么这两个子符合连成活4的最基本条件
						if((position[i][m] + position[i][m+1] + position[i][m+2] + position[i][m+3]) == 2 * type)
						{	//现在毫无疑问，这个以4为长度的队列再补充2个子即可形成连四，但是该连四是活四还是眠四？还需要进一步鉴定
							if(m >= 1 && m + 3 <= 17)//之前的i_min，i_max，j_min，j_max，从各个方向上扩充了2格，但并非强制性扩充，因此这里仍存在数组越界的可能性
							{                    //尽管越界的可能性极低，但是一旦发生将是程序停止运行的致命性错误，这里还是防范一下
								//如果符合要求的队列4中只有两个同色子，那么这个队列补充完整后必须要实现一个双迫着，即为活四
								//VCF战术不考虑用两步棋只换取一个迫着点的方法
								if(position[i][m-1] == 0 && position[i][m+4] == 0)
								{	//如果这个长度为4的队列头部一个位置和尾部一个位置都是空的，那么显然这个队列补充完整后就是一个活四
									//并且该条件为充要条件
									boolean flag = false;//用于确定movepoint第一个子是否已赋值的标志位
									for(int k = m; k <= m + 3; k++)
									{
										if(position[i][k] == 0)
										{	//将该点加入“双威胁度/双着点”名单
											//因为四个位置中已经有两个子，另外两个落子只有一种可能，所以不存在重复性，这里不需要甄别
											if(!flag)
											{
												VCFList_doublethreat_bydoublestep[nPly][moveCount0].setPoint1(new Point(i, k));
												flag = true;
											}
											else
												VCFList_doublethreat_bydoublestep[nPly][moveCount0].setPoint2(new Point(i, k));
										}
									}
									moveCount0++;
								}
							}
						}
						else
							continue;//如果这两个子不能形成活四，则不符合VCF战术的最基本条件“2个着点形成2个威胁度”，跳到下一次循环
					}
				}
				else if(number == 3 * type)//现在考虑队列中有三个同色子的情况
				{	//3个同色子的情况较两个同色子更复杂，因为两个同色子只需考虑活四的情况，2个着点必须要换取2个威胁度，3个同色子只需一步即可形成“四”的棋型
					//因此，3个同色子应分别考虑一步形成一个威胁度和一步形成两个威胁度的情形
					//相对于2个同色子来说，如果3个同色子要形成“四”棋型，那么3个子间的总距离不能超过3
					//由于以6为长度的队列中，3个同色子的距离不可能超过3，因此，3个同色子本身的情况就符合分析条件
					//3个同色子的情况本身就符合“不论着子点在队列的何处，都能造成起码一个威胁度”，所以现在仅需分析，能否出现“着一子造成两个威胁度”
					boolean flag = false;//用于判断这3个子能不能造成2个威胁度
					for(int m = j; m <= j + 3; m++)
					{//如果这3个子能被框在一个长度为4的队列里，或者说，这3个子之间的距离不超过1，那么这3个子符合连成活4的最基本条件
						if((position[i][m] + position[i][m+1] + position[i][m+2] + position[i][m+3]) == 3 * type)
						{	//现在毫无疑问，这个以4为长度的队列再补充1个子即可形成连四，但是该连四是活四还是眠四？还需要进一步鉴定
							if(m >= 1 && m + 3 <= 17)//之前的i_min，i_max，j_min，j_max，从各个方向上扩充了2格，但并非强制性扩充，因此这里仍存在数组越界的可能性
							{	//尽管越界的可能性极低，但是一旦发生将是程序停止运行的致命性错误，这里还是防范一下
								if(position[i][m-1] == 0 && position[i][m+4] == 0)
								{	//如果这个长度为4的队列头部一个位置和尾部一个位置都是空的，那么显然这个队列补充完整后就是一个活四
									//并且该条件为充要条件
									for(int k = m; k <= m + 3; k++)
									{
										if(position[i][k] == 0)
										{	//将该点加入“双威胁度/单着点”名单
											//因为四个位置中已经有3个子，另外1个落子只有1种可能，所以不存在重复性，这里不需要甄别
											Point pos = new Point(i, k);
											VCFList_doublethreat_bysinglestep[nPly][moveCount1++] = pos;
											flag = true;
										}
									}
								}
							}
						}
					}
					if(!flag)
					{	//如果不能找到一个点形成两个威胁度
						//那么这个队列中的每个空点都可以加入“单威胁度/单着点”名单
						//因为6个位置中只有3个子，另外1个落子有3种可能，存在重复性，这里需要甄别
						boolean flag_repeat = false;
						for(int m = j; m <= j + 5; m++)
						{
							if(position[i][m] == 0)
							{	//将该点加入“单威胁度/单着点”名单
								for(int n = 0; n <= moveCount2 - 1; n++)
									if(VCFList_singlethreat_bysinglestep[nPly][n].getX() == i 
									&& VCFList_singlethreat_bysinglestep[nPly][n].getY() == m)
										flag_repeat =true;
								if(!flag_repeat)//如果已有名单中没有这个点，那么才将这个点加入
								{
									Point pos = new Point(i, m);
									VCFList_singlethreat_bysinglestep[nPly][moveCount2++] = pos;
								}
							}
						}
					}
				}	
			}	
		}	
	}//至此，纵向分析结束
	
	private void Analys_Horizontal(byte[][] position, int type, int nPly)
	{	for(int i = i_min; i <= i_max; i++)
		{	for(int j = j_min; j <= j_max - 5; j++)
			{
				int number = position[i][j] + position[i+1][j] + position[i+2][j] + position[i+3][j]
							+ position[i+4][j] + position[i+5][j];
				if(number == 2 * type)
				{//以6为长度的队列是否有2个同色子，而且其余为空
					for(int m = i; m <= i + 3; m++)
					{//如果这2个子在一个长度为4的队列里，或者说，这2个子之间的距离不超过2，那么这两个子符合连成活4的最基本条件
						if((position[m][j] + position[m+1][j] + position[m+2][j] + position[m+3][j]) == 2 * type)
						{	//现在毫无疑问，这个以4为长度的队列再补充2个子即可形成连四，但是该连四是活四还是眠四？还需要进一步鉴定
							if(m >= 1 && m + 3 <= 17)//之前的i_min，i_max，j_min，j_max，从各个方向上扩充了2格，但并非强制性扩充，因此这里仍存在数组越界的可能性
							{                    //尽管越界的可能性极低，但是一旦发生将是程序停止运行的致命性错误，这里还是防范一下
								//如果符合要求的队列4中只有两个同色子，那么这个队列补充完整后必须要实现一个双迫着，即为活四
								//VCF战术不考虑用两步棋只换取一个迫着点的方法
								if(position[m-1][j] == 0 && position[m+4][j] == 0)
								{	//如果这个长度为4的队列头部一个位置和尾部一个位置都是空的，那么显然这个队列补充完整后就是一个活四
									//并且该条件为充要条件
									boolean flag = false;//用于确定movepoint第一个子是否已赋值的标志位
									for(int k = m; k <= m + 3; k++)
									{
										if(position[k][j] == 0)
										{
											//将该点加入“双威胁度/双着点”名单
											if(!flag)
											{
												VCFList_doublethreat_bydoublestep[nPly][moveCount0].setPoint1(new Point(k, j));
												flag = true;
											}
											else
												VCFList_doublethreat_bydoublestep[nPly][moveCount0].setPoint2(new Point(k, j));
										}
									}
									moveCount0++;
								}
							}
						}
					}
				}
				else if(number == 3 * type)//现在考虑队列中有三个同色子的情况
				{	//3个同色子的情况较两个同色子更复杂，因为两个同色子只需考虑活四的情况，2个着点必须要换取2个威胁度，3个同色子只需一步即可形成“四”的棋型
					//因此，3个同色子应分别考虑一步形成一个威胁度和一步形成两个威胁度的情形
					//相对于2个同色子来说，如果3个同色子要形成“四”棋型，那么3个子间的总距离不能超过3
					//由于以6为长度的队列中，3个同色子的距离不可能超过3，因此，3个同色子本身的情况就符合分析条件
					//3个同色子的情况本身就符合“不论着子点在队列的何处，都能造成起码一个威胁度”，所以现在仅需分析，能否出现“着一子造成两个威胁度”
					boolean flag = false;//用于判断这3个子能不能造成2个威胁度
					for(int m = i; m <= i + 3; m++)
					{//如果这3个子能被框在一个长度为4的队列里，或者说，这3个子之间的距离不超过1，那么这3个子符合连成活4的最基本条件
						if((position[m][j] + position[m+1][j] + position[m+2][j] + position[m+3][j]) == 3 * type)
						{	//现在毫无疑问，这个以4为长度的队列再补充1个子即可形成连四，但是该连四是活四还是眠四？还需要进一步鉴定
							if(m >= 1 && m + 3 <= 17)//之前的i_min，i_max，j_min，j_max，从各个方向上扩充了2格，但并非强制性扩充，因此这里仍存在数组越界的可能性
							{	//尽管越界的可能性极低，但是一旦发生将是程序停止运行的致命性错误，这里还是防范一下
								if(position[m-1][j] == 0 && position[m+4][j] == 0)
								{	//如果这个长度为4的队列头部一个位置和尾部一个位置都是空的，那么显然这个队列补充完整后就是一个活四
									//并且该条件为充要条件
									for(int k = m; k <= m + 3; k++)
									{
										if(position[k][j] == 0)
										{	//将该点加入“双威胁度/单着点”名单
											Point pos = new Point(k, j);
											VCFList_doublethreat_bysinglestep[nPly][moveCount1++] = pos;
											flag = true;
										}
									}
								}
							}
						}
					}
					if(!flag)
					{	//如果不能找到一个点形成两个威胁度
						//那么这个队列中的每个空点都可以加入“单威胁度/单着点”名单
						boolean flag_repeat = false;
						for(int m = i; m <= i + 5; m++)
						{
							if(position[m][j] == 0)
							{	//将该点加入“单威胁度/单着点”名单
								for(int n = 0; n <= moveCount2 - 1; n++)
									if(VCFList_singlethreat_bysinglestep[nPly][n].getX() == m 
									&& VCFList_singlethreat_bysinglestep[nPly][n].getY() == j)
										flag_repeat =true;
								if(!flag_repeat)
								{
									Point pos = new Point(m, j);
									VCFList_singlethreat_bysinglestep[nPly][moveCount2++] = pos;
								}
							}
						}
					}
				}	
			}	
		}	
	}//至此，横向分析结束
	
	private void Analys_Left(byte[][] position, int type, int nPly)
	{	for(int i = i_min; i <= i_max - 5; i++)
		{	for(int j = j_min; j <= j_max - 5; j++)
			{
				int number = position[i][j] + position[i+1][j+1] + position[i+2][j+2] + position[i+3][j+3]
							+ position[i+4][j+4] + position[i+5][j+5];
				if(number == 2 * type)
				{//以6为长度的队列是否有2个同色子，而且其余为空
					for(int m = 0; m <= 3; m++)
					{//如果这2个子在一个长度为4的队列里，或者说，这2个子之间的距离不超过2，那么这两个子符合连成活4的最基本条件
						if((position[i+m][j+m] + position[i+m+1][j+m+1] + position[i+m+2][j+m+2] + position[i+m+3][j+m+3]) == 2 * type)
						{	//现在毫无疑问，这个以4为长度的队列再补充2个子即可形成连四，但是该连四是活四还是眠四？还需要进一步鉴定
							if((i+m >= 1) && (j+m>=1) && (i+m+3<=17) &&(j+m+3<=17))//之前的i_min，i_max，j_min，j_max，从各个方向上扩充了2格，但并非强制性扩充，因此这里仍存在数组越界的可能性
							{    //尽管越界的可能性极低，但是一旦发生将是程序停止运行的致命性错误，这里还是防范一下
								//如果符合要求的队列4中只有两个同色子，那么这个队列补充完整后必须要实现一个双迫着，即为活四
								//VCF战术不考虑用两步棋只换取一个迫着点的方法
								if(position[i+m-1][j+m-1] == 0 && position[i+m+4][j+m+4] == 0)
								{	//如果这个长度为4的队列头部一个位置和尾部一个位置都是空的，那么显然这个队列补充完整后就是一个活四
									//并且该条件为充要条件
									boolean flag = false;//用于确定movepoint第一个子是否已赋值的标志位
									for(int k = m; k <= m + 3; k++)
									{
										if(position[i+k][j+k] == 0)
										{	//将该点加入“双威胁度/双着点”名单
											//因为四个位置中已经有两个子，另外两个落子只有一种可能，所以不存在重复性，这里不需要甄别
											if(!flag)
											{
												VCFList_doublethreat_bydoublestep[nPly][moveCount0].setPoint1(new Point(i+k, j+k));
												flag = true;
											}
											else
												VCFList_doublethreat_bydoublestep[nPly][moveCount0].setPoint2(new Point(i+k, j+k));
										}
									}
									moveCount0++;
								}
							}
						}
						else
							continue;//如果这两个子不能形成活四，则不符合VCF战术的最基本条件“2个着点形成2个威胁度”，跳到下一次循环
					}
				}
				else if(number == 3 * type)//现在考虑队列中有三个同色子的情况
				{	//3个同色子的情况较两个同色子更复杂，因为两个同色子只需考虑活四的情况，2个着点必须要换取2个威胁度，3个同色子只需一步即可形成“四”的棋型
					//因此，3个同色子应分别考虑一步形成一个威胁度和一步形成两个威胁度的情形
					//相对于2个同色子来说，如果3个同色子要形成“四”棋型，那么3个子间的总距离不能超过3
					//由于以6为长度的队列中，3个同色子的距离不可能超过3，因此，3个同色子本身的情况就符合分析条件
					//3个同色子的情况本身就符合“不论着子点在队列的何处，都能造成起码一个威胁度”，所以现在仅需分析，能否出现“着一子造成两个威胁度”
					boolean flag = false;//用于判断这3个子能不能造成2个威胁度
					for(int m = 0; m <= 3; m++)
					{//如果这3个子能被框在一个长度为4的队列里，或者说，这3个子之间的距离不超过1，那么这3个子符合连成活4的最基本条件
						if((position[i+m][j+m] + position[i+m+1][j+m+1] + position[i+m+2][j+m+2] + position[i+m+3][j+m+3]) == 3 * type)
						{	//现在毫无疑问，这个以4为长度的队列再补充1个子即可形成连四，但是该连四是活四还是眠四？还需要进一步鉴定
							if((i+m >= 1) && (j+m>=1) && (i+m+3<=17) &&(j+m+3<=17))//之前的i_min，i_max，j_min，j_max，从各个方向上扩充了2格，但并非强制性扩充，因此这里仍存在数组越界的可能性
							{	//尽管越界的可能性极低，但是一旦发生将是程序停止运行的致命性错误，这里还是防范一下
								if(position[i+m-1][j+m-1] == 0 && position[i+m+4][j+m+4] == 0)
								{	//如果这个长度为4的队列头部一个位置和尾部一个位置都是空的，那么显然这个队列补充完整后就是一个活四
									//并且该条件为充要条件
									for(int k = m; k <= m + 3; k++)
									{
										if(position[i+k][j+k] == 0)
										{	//将该点加入“双威胁度/单着点”名单
											//因为四个位置中已经有3个子，另外1个落子只有1种可能，所以不存在重复性，这里不需要甄别
											Point pos = new Point(i+k, j+k);
											VCFList_doublethreat_bysinglestep[nPly][moveCount1++] = pos;
											flag = true;
										}
									}
								}
							}
						}
					}
					if(!flag)
					{	//如果不能找到一个点形成两个威胁度
						//那么这个队列中的每个空点都可以加入“单威胁度/单着点”名单
						//因为6个位置中只有3个子，另外1个落子有3种可能，存在重复性，这里需要甄别
						boolean flag_repeat = false;
						for(int m = 0; m <= 5; m++)
						{
							if(position[i+m][j+m] == 0)
							{	//将该点加入“单威胁度/单着点”名单
								for(int n = 0; n <= moveCount2 - 1; n++)
									if(VCFList_singlethreat_bysinglestep[nPly][n].getX() == i+m 
									&& VCFList_singlethreat_bysinglestep[nPly][n].getY() == j+m)
										flag_repeat =true;
								if(!flag_repeat)//如果已有名单中没有这个点，那么才将这个点加入
								{
									Point pos = new Point(i+m, j+m);
									VCFList_singlethreat_bysinglestep[nPly][moveCount2++] = pos;
								}
							}
						}
					}
				}	
			}	
		}	
	}//至此，左斜向分析结束
	
	private void Analys_Right(byte[][] position, int type, int nPly)
	{	for(int i = i_min + 5; i <= i_max; i++)
		{	for(int j = j_min; j <= j_max - 5; j++)
			{
				int number = position[i][j] + position[i-1][j+1] + position[i-2][j+2] + position[i-3][j+3]
							+ position[i-4][j+4] + position[i-5][j+5];
				if(number == 2 * type)
				{//以6为长度的队列是否有2个同色子，而且其余为空
					for(int m = 0; m <= 3; m++)
					{//如果这2个子在一个长度为4的队列里，或者说，这2个子之间的距离不超过2，那么这两个子符合连成活4的最基本条件
						if((position[i-m][j+m] + position[i-m-1][j+m+1] + position[i-m-2][j+m+2] + position[i-m-3][j+m+3]) == 2 * type)
						{	//现在毫无疑问，这个以4为长度的队列再补充2个子即可形成连四，但是该连四是活四还是眠四？还需要进一步鉴定
							if((i-m <= 17) && (j+m>=1) && (i-m-3>=1) &&(j+m+3<=17))//之前的i_min，i_max，j_min，j_max，从各个方向上扩充了2格，但并非强制性扩充，因此这里仍存在数组越界的可能性
							{    //尽管越界的可能性极低，但是一旦发生将是程序停止运行的致命性错误，这里还是防范一下
								//如果符合要求的队列4中只有两个同色子，那么这个队列补充完整后必须要实现一个双迫着，即为活四
								//VCF战术不考虑用两步棋只换取一个迫着点的方法
								if(position[i-m+1][j+m-1] == 0 && position[i-m-4][j+m+4] == 0)
								{	//如果这个长度为4的队列头部一个位置和尾部一个位置都是空的，那么显然这个队列补充完整后就是一个活四
									//并且该条件为充要条件
									boolean flag = false;//用于确定movepoint第一个子是否已赋值的标志位
									for(int k = m; k <= m + 3; k++)
									{
										if(position[i-k][j+k] == 0)
										{	//将该点加入“双威胁度/双着点”名单
											//因为四个位置中已经有两个子，另外两个落子只有一种可能，所以不存在重复性，这里不需要甄别
											if(!flag)
											{
												VCFList_doublethreat_bydoublestep[nPly][moveCount0].setPoint1(new Point(i-k, j+k));
												flag = true;
											}
											else
												VCFList_doublethreat_bydoublestep[nPly][moveCount0].setPoint2(new Point(i-k, j+k));
										}
									}
									moveCount0++;
								}
							}
						}
						else
							continue;//如果这两个子不能形成活四，则不符合VCF战术的最基本条件“2个着点形成2个威胁度”，跳到下一次循环
					}
				}
				else if(number == 3 * type)//现在考虑队列中有三个同色子的情况
				{	//3个同色子的情况较两个同色子更复杂，因为两个同色子只需考虑活四的情况，2个着点必须要换取2个威胁度，3个同色子只需一步即可形成“四”的棋型
					//因此，3个同色子应分别考虑一步形成一个威胁度和一步形成两个威胁度的情形
					//相对于2个同色子来说，如果3个同色子要形成“四”棋型，那么3个子间的总距离不能超过3
					//由于以6为长度的队列中，3个同色子的距离不可能超过3，因此，3个同色子本身的情况就符合分析条件
					//3个同色子的情况本身就符合“不论着子点在队列的何处，都能造成起码一个威胁度”，所以现在仅需分析，能否出现“着一子造成两个威胁度”
					boolean flag = false;//用于判断这3个子能不能造成2个威胁度
					for(int m = 0; m <= 3; m++)
					{//如果这3个子能被框在一个长度为4的队列里，或者说，这3个子之间的距离不超过1，那么这3个子符合连成活4的最基本条件
						if((position[i-m][j+m] + position[i-m-1][j+m+1] + position[i-m-2][j+m+2] + position[i-m-3][j+m+3]) == 3 * type)
						{	//现在毫无疑问，这个以4为长度的队列再补充1个子即可形成连四，但是该连四是活四还是眠四？还需要进一步鉴定
							if((i-m <= 17) && (j+m>=1) && (i-m-3>=1) &&(j+m+3<=17))//之前的i_min，i_max，j_min，j_max，从各个方向上扩充了2格，但并非强制性扩充，因此这里仍存在数组越界的可能性
							{	//尽管越界的可能性极低，但是一旦发生将是程序停止运行的致命性错误，这里还是防范一下
								if(position[i-m+1][j+m-1] == 0 && position[i-m-4][j+m+4] == 0)
								{	//如果这个长度为4的队列头部一个位置和尾部一个位置都是空的，那么显然这个队列补充完整后就是一个活四
									//并且该条件为充要条件
									for(int k = m; k <= m + 3; k++)
									{
										if(position[i-k][j+k] == 0)
										{	//将该点加入“双威胁度/单着点”名单
											//因为四个位置中已经有3个子，另外1个落子只有1种可能，所以不存在重复性，这里不需要甄别
											Point pos = new Point(i-k, j+k);
											VCFList_doublethreat_bysinglestep[nPly][moveCount1++] = pos;
											flag = true;
										}
									}
								}
							}
						}
					}
					if(!flag)
					{	//如果不能找到一个点形成两个威胁度
						//那么这个队列中的每个空点都可以加入“单威胁度/单着点”名单
						//因为6个位置中只有3个子，另外1个落子有3种可能，存在重复性，这里需要甄别
						boolean flag_repeat = false;
						for(int m = 0; m <= 5; m++)
						{
							if(position[i-m][j+m] == 0)
							{	//将该点加入“单威胁度/单着点”名单
								for(int n = 0; n <= moveCount2 - 1; n++)
									if(VCFList_singlethreat_bysinglestep[nPly][n].getX() == i-m 
									&& VCFList_singlethreat_bysinglestep[nPly][n].getY() == j+m)
										flag_repeat =true;
								if(!flag_repeat)//如果已有名单中没有这个点，那么才将这个点加入
								{
									Point pos = new Point(i-m, j+m);
									VCFList_singlethreat_bysinglestep[nPly][moveCount2++] = pos;
								}
							}
						}
					}
				}	
			}	
		}	
	}//至此，右斜向分析结束
	
}
	