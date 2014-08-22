package hust.wzb.iceline;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class GameScreen implements Screen, GestureListener {
	private MyGdxGame game;
	private int currentLevel; // 当前玩耍的等级
	private int currentType; // 当前玩耍的难度

	// 背景
	private SpriteBatch batch;
	private Sprite background; // 游戏背景
	private Sprite iceBg; // 存放所有蛋糕的背景
	private Sprite iceCellBg; // 单个蛋糕的背景

	private Stage stage;
	private GestureDetector gestrueDetector;

	// 游戏主界面
	private int count = 0;
	private Group group;
	private IceActor[][] cells;
	private boolean ifSelected = false; // 用于显示当前界面上存不存在已经选中的块
	private Sprite selectedSprite; // 选中的图标
	private int lastSelectedRow, lastSelectedColumn; // 上一个选中的坐标，行列
	private boolean isRemoved = false; // 判断是否存在可以消去的蛋糕，ture表示存在
	private Image paopao; // 泡泡标记

	private boolean notTouched = false; // true表示当前不接受点击动作，当在执行滑动or消去时设置为true
	private boolean locked = false; // 当交换两个蛋糕不能消除时，locked设置为true，此时再过一段时间后用于重置notTouched为false
	private int locktime = 0;

	// 滑动的相关参数
	private float firstX, firstY; // 用于滑动时记录第一位置的坐标
	private boolean paned = false; // false 表示开始滑动

	// time
	private Sprite infoBack;
	private Sprite timeSprite;
	private Sprite timebar;
	private Sprite timebarfill;
	private float leftTime;
	private float totalTime;

	private LabelStyle style;
	private LabelStyle style2;
	private Label targetLabel;
	private int target;
	private Label scoreLabel;
	private int score;
	private Image pauseButton;

	// result 框
	private Group resultGroup; // 存放result元素的group
	private boolean isPlay = true;

	public GameScreen(MyGdxGame game, int level, int type) {
		this.game = game;
		this.currentLevel = level;
		this.currentType = type;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);// 设置背景为白色
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);// 清屏

		// TODO Auto-generated method stub
		if (isPlay) { // 游戏中时，时间进度减少
			leftTime -= Gdx.graphics.getDeltaTime();
			if (leftTime <= 0) { // 失败弹出失败框
				showResultDialog(2);
			}
		}
		// 绘制背景
		batch.begin();
		background.draw(batch);
		iceBg.draw(batch);
		for (int i = 0; i < Constants.cellRow[currentType]; i++) {
			for (int j = 0; j < Constants.cellColumn[currentType]; j++) {
				iceCellBg.setPosition(Constants.cellX + j
						* Constants.cellWidth[currentType], Constants.cellY + i
						* Constants.cellHeight[currentType]);
				iceCellBg.draw(batch);
			}
		}

		infoBack.draw(batch);

		// 绘制time
		timeSprite.draw(batch);
		timebar.draw(batch);
		timebarfill.draw(batch);
		timebarfill.setSize(Constants.timeBarWidth * (leftTime / totalTime),
				Constants.timeBarHeight);

		// 选中时，绘制选中框图标
		if (ifSelected) {
			selectedSprite.draw(batch);
		}
		batch.end();

		// // 返回
		// if (Gdx.input.isKeyPressed(Input.Keys.A)
		// || Gdx.input.isKeyPressed(Input.Keys.BACK)) {
		// game.setScreen(new LevelScreen(game, currentType));
		// }

		// game 动画
		if (isRemoved) { // 执行交换动画时
			if (++count == Constants.actionTime) { // 交换动作结束，执行消去方块action
				removeIce();
				if (isPlay && score >= target) {
					showResultDialog(1); // 胜利
					isPlay = false;
				}
			}
			if (count == 2 * Constants.actionTime + Constants.actionTime) { // 消去动作结束
				isRemoved = judge(); // 判断新添加方块后是否存在可消去方块
				if (isRemoved) { // 存在 ，则在下次进入后直接消去
					count = Constants.actionTime / 2;
				} else { // 不存在可直接消去的方块，则预判
					if (!preJudge()) {
						// 预判不存在, 则重排并并在下次进入后直接判断是否可消去
						addPaopao();
					} else {
						count = 0;
					}
					notTouched = false; // 设置为可以接受点击
				}
			}
		}

		if (locked) { // 锁住后，开始计时
			if (locktime == 2 * Constants.actionTime) {
				locked = false;
				notTouched = false;
				locktime = 0;
			}
			locktime++;
		}

		stage.draw();
		stage.act();

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		batch = new SpriteBatch();
		if (currentLevel < Constants.LEVEL_COLUMN * Constants.LEVEL_ROW) {
			background = MyAssetsManager.getInstance().background3;
			iceCellBg = MyAssetsManager.getInstance().iceBg[0];
		} else if (currentLevel < 2 * Constants.LEVEL_COLUMN
				* Constants.LEVEL_ROW) {
			background = MyAssetsManager.getInstance().background2;
			iceCellBg = MyAssetsManager.getInstance().iceBg[1];
		} else {
			background = MyAssetsManager.getInstance().background;
			iceCellBg = MyAssetsManager.getInstance().iceBg[0];
		}
		background.setSize(Constants.screenWidth, Constants.screenHeight);
		iceBg = MyAssetsManager.getInstance().iceBg[2];
		iceBg.setBounds(Constants.iceBgX, Constants.iceBgY,
				Constants.iceBgWidth, Constants.iceBgHeight);
		// iceCellBg = MyAssetsManager.getInstance().iceCellbg[1];
		iceCellBg.setSize(Constants.cellWidth[currentType],
				Constants.cellHeight[currentType]);

		selectedSprite = MyAssetsManager.getInstance().iceSelected;
		selectedSprite.setSize(Constants.cellWidth[currentType],
				Constants.cellHeight[currentType]);

		stage = new Stage(Constants.screenWidth, Constants.screenHeight, false);
		updateCells(); // 添加蛋糕到界面上

		// infoBack
		infoBack = MyAssetsManager.getInstance().infoBg;
		infoBack.setBounds(Constants.infoBackX, Constants.infoBackY,
				Constants.infoBackWidth, Constants.infoBackHeight);

		// time 绘制
		totalTime = Constants.totalTime + Constants.addTime * currentLevel;
		leftTime = totalTime;
		timeSprite = MyAssetsManager.getInstance().timeSprite;
		timeSprite.setBounds(Constants.timeSpriteX, Constants.timeSpriteY,
				Constants.timeSpriteWidth, Constants.timeSpriteHeight);
		timebar = MyAssetsManager.getInstance().timebar;
		timebar.setBounds(Constants.timeBarX, Constants.timeBarY,
				Constants.timeBarWidth, Constants.timeBarHeight);
		timebarfill = MyAssetsManager.getInstance().timebarfill;
		timebarfill.setBounds(Constants.timeBarX, Constants.timeBarY,
				Constants.timeBarWidth, Constants.timeBarHeight);

		// target, score, pause button
		BitmapFont font = MyAssetsManager.getInstance().font;
		font.setScale(Constants.wrate);
		style = new LabelStyle(font, Color.WHITE);
		BitmapFont font2 = new BitmapFont();
		font2.setScale(Constants.screenHeight / 480f);
		style2 = new LabelStyle(font2, Color.WHITE);
		target = Constants.target + currentLevel * Constants.addTarget;
		targetLabel = new Label("Target: " + target, style);
		targetLabel.setAlignment(Align.center);
		targetLabel.setBounds(Constants.targetSpriteX, Constants.targetSpriteY,
				Constants.targetSpriteWidth, Constants.targetSpriteHeight);

		scoreLabel = new Label("Score: " + score, style);
		scoreLabel.setAlignment(Align.center);
		scoreLabel.setBounds(Constants.scoreSpriteX, Constants.scoreSpriteY,
				Constants.scoreSpriteWidth, Constants.scoreSpriteHeight);

		pauseButton = new Image(MyAssetsManager.getInstance().pauseButtonSprite);
		pauseButton.setBounds(Constants.pauseButtonX, Constants.pauseButtonY,
				Constants.pauseButtonWidth, Constants.pauseButtonHeight);

		stage.addActor(targetLabel);
		stage.addActor(scoreLabel);
		stage.addActor(pauseButton);

		gestrueDetector = new GestureDetector(this);
		Gdx.input.setInputProcessor(gestrueDetector);

		// 开始进入时，检测是否存在可消去的蛋糕
		isRemoved = true;
		count = Constants.actionTime / 2; // 10

		// 结果框
		resultGroup = new Group();
		stage.addActor(resultGroup);
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		batch.dispose();
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		firstX = x;
		firstY = y;
		paned = false;
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		// TODO Auto-generated method stub
		// 点击暂停按钮时
		if (x > pauseButton.getX()
				&& x < pauseButton.getX() + pauseButton.getWidth()
				&& Constants.screenHeight - y > pauseButton.getY()
				&& Constants.screenHeight - y < pauseButton.getY()
						+ pauseButton.getHeight()) {
			showResultDialog(0);
			return true;
		}

		if (!inBox(x, Constants.screenHeight - y) || notTouched) {
			// 点击盘外或者不准点击时
		} else {
			int column = getColumn(x);
			int row = getRow(Constants.screenHeight - y);
			System.out.println(column + " out: " + row);
			if (cells[row][column].getEffectType() == 4) { // 表示点击了泡泡，直接显示消去动画
				isRemoved = true;
				count = Constants.actionTime - 1;
				paopao.remove();
				return false;
			}

			if (ifSelected
					&& (lastSelectedColumn == column
							&& Math.abs(lastSelectedRow - row) == 1 || (lastSelectedRow == row && Math
							.abs(lastSelectedColumn - column) == 1))) {// 表示当前选中的和之前的相邻，则互换位置
				// 对象互换
				IceActor tem = cells[row][column];
				cells[row][column] = cells[lastSelectedRow][lastSelectedColumn];
				cells[lastSelectedRow][lastSelectedColumn] = tem;

				isRemoved = judgeRowColumn(row, column, lastSelectedRow,
						lastSelectedColumn); // 判断互换后是否存在可消去的蛋糕

				notTouched = true; // 锁住不让触摸
				if (Constants.isMusic) {
					MyAssetsManager.getInstance().panMusic.play();
				}

				if (isRemoved) { // 互换后，判断存在可消去的对象时
					cells[row][column].setChangeAction(
							cells[lastSelectedRow][lastSelectedColumn].getX(),
							cells[lastSelectedRow][lastSelectedColumn].getY());
					cells[lastSelectedRow][lastSelectedColumn].setChangeAction(
							cells[row][column].getX(),
							cells[row][column].getY());
				} else { // 互换后不存在可消除对象
					locked = true; // 锁住，locktime后解锁
					cells[row][column].setExChangeAction(
							cells[lastSelectedRow][lastSelectedColumn].getX(),
							cells[lastSelectedRow][lastSelectedColumn].getY());
					cells[lastSelectedRow][lastSelectedColumn]
							.setExChangeAction(cells[row][column].getX(),
									cells[row][column].getY());
					tem = cells[row][column];
					cells[row][column] = cells[lastSelectedRow][lastSelectedColumn];
					cells[lastSelectedRow][lastSelectedColumn] = tem;
				}

				ifSelected = false;
				lastSelectedRow = -1;
				lastSelectedColumn = -1;

			} else { // 将当前点击的蛋糕设置为选中
				ifSelected = true;
				selectedSprite.setPosition(cells[row][column].getX(),
						cells[row][column].getY());
				lastSelectedColumn = column;
				lastSelectedRow = row;
			}
		}
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		if (notTouched) {// 表示正在执行动画
			return false;
		}

		y = Constants.screenHeight - y;
		firstY = Constants.screenHeight - firstY;
		if (!(inBox(firstX, firstY) && inBox(x, y))) { // 有一个不在盘内时
			return false;
		}

		// 将坐标转换为行列
		int column = getColumn(x);
		int row = getRow(y);

		int column2 = getColumn(firstX);
		int row2 = getRow(firstY);

		if (!paned
				&& ((column == column2 && Math.abs(row - row2) == 1) || (row == row2 && Math
						.abs(column - column2) == 1))) { // 两个相邻则互換

			paned = true; // 表示已经进行过一次互换了，后面的滑动不执行互换
			// 对象互换
			IceActor tem = cells[row][column];
			cells[row][column] = cells[row2][column2];
			cells[row2][column2] = tem;
			isRemoved = judgeRowColumn(row, column, row2, column2); // 判断互换后是否存在可消去的蛋糕

			notTouched = true;
			if (Constants.isMusic) {
				MyAssetsManager.getInstance().panMusic.play();
			}

			if (isRemoved) { // 互换后，存在可消去的对象时
				cells[row][column].setChangeAction(cells[row2][column2].getX(),
						cells[row2][column2].getY());
				cells[row2][column2].setChangeAction(cells[row][column].getX(),
						cells[row][column].getY());
			} else { // 互换后不存在可消除对象
				locked = true; // 锁住，locktime后解锁not touch
				cells[row][column].setExChangeAction(
						cells[row2][column2].getX(),
						cells[row2][column2].getY());
				cells[row2][column2].setExChangeAction(
						cells[row][column].getX(), cells[row][column].getY());
				tem = cells[row][column];
				cells[row][column] = cells[row2][column2];
				cells[row2][column2] = tem;
			}

			ifSelected = false;
			lastSelectedRow = -1;
			lastSelectedColumn = -1;
		}
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 重排安置方块的二维数组
	 */
	public void updateCells() {
		Random rand = new Random();
		if (cells == null) {
			group = new Group();
			cells = new IceActor[Constants.cellRow[currentType]][Constants.cellColumn[currentType]];
			stage.addActor(group);
			group.clear();
		} else {
			// System.out.println("===重排====");
			group.clear();
		}
		if (currentLevel <= Constants.LEVEL_ROW * Constants.LEVEL_COLUMN) {
			Constants.cellNum[currentType] = 5;
		} else if (currentLevel <= Constants.LEVEL_ROW * Constants.LEVEL_COLUMN
				* 2) {
			Constants.cellNum[currentType] = 6;
		} else {
			Constants.cellNum[currentType] = 7;
		}

		for (int i = 0; i < Constants.cellRow[currentType]; i++) {
			for (int j = 0; j < Constants.cellColumn[currentType]; j++) {
				int index = rand.nextInt(Constants.cellNum[currentType]);
				cells[i][j] = new IceActor(
						MyAssetsManager.getInstance().iceCells[index],
						index + 1, currentType);
				cells[i][j].setSize(Constants.cellWidth[currentType],
						Constants.cellHeight[currentType]);
				cells[i][j].setMoveInAction(Constants.cellX + j
						* Constants.cellWidth[currentType], Constants.cellY + i
						* Constants.cellHeight[currentType]);
				group.addActor(cells[i][j]);
			}
		}
	}

	public void addPaopao() {
		// 不存在时，添加泡泡
		paopao = new Image(MyAssetsManager.getInstance().toolSprite[4]);
		int temp = new Random().nextInt(Constants.cellRow[currentType]
				* Constants.cellColumn[currentType]);
		int tempC = temp / Constants.cellRow[currentType];
		int tempR = temp % Constants.cellColumn[currentType];
		setBomb(tempR, tempC, 4);// 设置该蛋糕被泡泡覆盖
		// System.out.println("tempR : " + tempR + " tempC : " + tempC
		// + "  add paiooa");
		paopao.setBounds(cells[tempR][tempC].getX(), Constants.screenHeight,
				cells[tempR][tempC].getWidth(), cells[tempR][tempC].getHeight());
		paopao.addAction(Actions.moveTo(cells[tempR][tempC].getX(),
				cells[tempR][tempC].getY(), Gdx.graphics.getDeltaTime()
						* Constants.actionTime));
		group.addActor(paopao);

		count = 0;
	}

	/**
	 * 用于判断是否存在可以被消除的对象
	 * 
	 * @return
	 */
	public boolean judge() {
		boolean ans = false; // 表示是否存在可消去的蛋糕

		for (int i = 0; i < Constants.cellRow[currentType]; i++) {
			if (judgeRow(i, -1) || ans) {
				ans = true;
			}
		}

		for (int j = 0; j < Constants.cellColumn[currentType]; j++) {
			if (judgeColumn(-1, j) || ans) {
				ans = true;
			}
		}
		return ans;
	}

	/**
	 * 判断(row, column)是否可以被消去
	 * 
	 * @param row
	 *            待消去蛋糕的行
	 * @param column
	 *            待消去蛋糕的列
	 * @return true 表示存在
	 */
	public boolean judgeRowColumn(int row, int column) {
		boolean rans = judgeRow(row, column);
		boolean cans = judgeColumn(row, column);
		if (rans && cans && cells[row][column].getEffectType() < 0) {
			cells[row][column]
					.setValue(Math.abs(cells[row][column].getValue()));
			cells[row][column].setEffect(3); // 添加炸弹
		}

		return rans || cans;
	}

	/**
	 * 判断(row, column),(row2,column2) 是否可以被消去
	 * 
	 * @param row
	 * @param column
	 * @param row2
	 * @param column2
	 * @return
	 */
	public boolean judgeRowColumn(int row, int column, int row2, int column2) {
		boolean tag = false; // 标记，true表示(row,column)的炸弹是刚加上去的，那么在检测(row2,column2)行列时，需要对(row,column)恢复为不消除
		boolean rans = judgeRow(row, column);
		boolean cans = judgeColumn(row, column);
		if (rans && cans) { // 表示所在行列都有消除的
			cells[row][column]
					.setValue(Math.abs(cells[row][column].getValue()));
			cells[row][column].setEffect(3); // 添加炸弹
			tag = true;
		}

		boolean rans2 = judgeRow(row2, column2);
		boolean cans2 = judgeColumn(row2, column2);
		if (rans2 && cans2 && cells[row][column].getEffectType() != 3) {
			cells[row2][column2].setValue(Math.abs(cells[row2][column2]
					.getValue()));
			cells[row2][column2].setEffect(3); // 添加炸弹
		}

		if (tag == true) { // 因为在检测(row2,column2)时，会将(row,column)设置为消去，这里恢复为不消除
			cells[row][column]
					.setValue(Math.abs(cells[row][column].getValue()));
		}

		return rans || cans || rans2 || cans2;
	}

	/**
	 * 判断row行是否存在可以消去的蛋糕
	 * 
	 * @param row
	 *            要判断的行
	 * @param tj
	 *            为正时，表示若要添加tool，则添加到(row, tj)的蛋糕上；否则添加到第四个相同的蛋糕上
	 * @return true 表示存在可消去的蛋糕
	 */
	public boolean judgeRow(int row, int tj) {
		int count = 0; // 用于一行or一列记录最多消去的个数
		boolean tag = false;
		boolean ans = false; // 表示是否存在可消去对象
		boolean effected = false; // 表示是否有添加tool

		int scoreColumn = -1; // 用于记录当前行显示加分的蛋糕列

		for (int j = 0; j < Constants.cellColumn[currentType]; j++) {
			if (tag) {
				if (Math.abs(cells[row][j].getValue()) == Math
						.abs(cells[row][j - 1].getValue())) { // 该颜色可以消除
					++count; // 计数，表示此行可以消去的个数
					if (count == Constants.cellColumn[currentType]) { // 表示消去一行时,添加十字标记
						cells[row][tj].setEffect(-1); // 去除原来的标记
						cells[row][tj].setValue(-Math.abs(cells[row][j]
								.getValue()));
						cells[row][tj].addScore += 10; // 多消去一个则多加10分

						cells[row][count / 2].setEffect(2); // 在中间添加十字标记
						cells[row][count / 2].setValue(Math
								.abs(cells[row][count / 2].getValue()));

						cells[row][count / 2].addScore = (count - 2) * 10;
						break;
					}

					if (!effected) {// 没有添加标记时, 添加闪电标记
						effected = true; // 添加了标记，再次判断时只计数，不添加
						if (tj < 0) { // 表示没有指定将tool标记放在哪个确定的蛋糕上时
							tj = j;
						} else {
							cells[row][j].setValue(-Math.abs(cells[row][j]
									.getValue()));
						}
						if (cells[row][tj].getEffectType() >= 0) { // 本来就有标记时，不再添加标记
							cells[row][tj].setValue(Math.abs(cells[row][tj]
									.getValue()));
						} else {
							cells[row][tj].setEffect(0); // 表示为第四个or更多的一个，添加横向闪电
							cells[row][tj].setValue(Math.abs(cells[row][tj]
									.getValue()));
						}
					} else { // 已经添加标记时，该蛋糕直接消去
						cells[row][scoreColumn].addScore += 10; // 多消去一个则多加10分
						cells[row][j].setValue(-Math.abs(cells[row][j]
								.getValue()));
					}

				} else { // 该颜色和之前的不相同, 重置参数
					tj = -1;
					effected = false;
					count = 0;
					tag = false;
				}

			} else if (row >= 0
					&& j < Constants.cellColumn[currentType] - 2
					&& Math.abs(cells[row][j].getValue()) == Math
							.abs(cells[row][j + 1].getValue())) { // 判断时倒数第三个截止

				if (Math.abs(cells[row][j + 1].getValue()) == Math
						.abs(cells[row][j + 2].getValue())) {
					// 表示找到三个相同的
					cells[row][j].setValue(-Math.abs(cells[row][j].getValue()));
					cells[row][j + 1].setValue(-Math.abs(cells[row][j + 1]
							.getValue()));
					cells[row][j + 2].setValue(-Math.abs(cells[row][j + 2]
							.getValue()));

					// 添加用于显示加分项的蛋糕
					if (tj == j + 1) {
						scoreColumn = j;
					} else {
						scoreColumn = j + 1;
					}
					cells[row][scoreColumn].addScore += 30;

					tag = true;
					ans = true;
					j += 2;
					count = 3; // 计数，已经去掉三个
				} else {
					j += 1;
				}
			}
		}
		return ans;
	}

	/**
	 * 判断column列是否存在可以消去的蛋糕
	 * 
	 * @param ti
	 *            为正时，表示若要添加tool，则添加到(ti, column)的蛋糕上；否则添加到第四个相同的蛋糕上
	 * @param column
	 *            要判断的列
	 * @return true 表示存在可消去的蛋糕
	 */
	public boolean judgeColumn(int ti, int column) {
		int count = 0; // 用于一行or一列记录最多消去的个数
		boolean tag = false;
		boolean ans = false; // 表示是否存在可消去对象
		boolean effected = false; // 表示是否有添加tool

		int scoreRow = -1;

		for (int i = 0; i < Constants.cellRow[currentType]; i++) {
			if (tag) {
				if (Math.abs(cells[i][column].getValue()) == Math
						.abs(cells[i - 1][column].getValue())) { // 這個可以被消除掉
					++count;
					if (count == Constants.cellRow[currentType]) {
						// 表示消去一列了
						cells[ti][column].setEffect(-1);
						cells[ti][column].setValue(-Math.abs(cells[i][column]
								.getValue()));
						cells[ti][column].addScore += 10;

						cells[count / 2][column].setEffect(2);
						cells[count / 2][column].setValue(Math
								.abs(cells[count / 2][column].getValue()));

						cells[count / 2][column].addScore = (count - 2) * 10;
						// 重置参数，进行下一列判断
						continue;
					}

					if (!effected) { // 没有添加标记时
						if (ti < 0) { // 若是没有将标记添加到指定的蛋糕上时，选择第四个作为添加标记项，否则选中的蛋糕作为标记项
							ti = i;
						} else {
							cells[i][column].setValue(-Math
									.abs(cells[i][column].getValue()));
						}

						if (cells[ti][column].getEffectType() >= 0) {
							cells[ti][column].setValue(-Math
									.abs(cells[ti][column].getValue()));
						} else {
							cells[ti][column].setValue(Math
									.abs(cells[ti][column].getValue()));
							cells[ti][column].setEffect(1);
						}
						effected = true;
					} else {
						cells[i][column].setValue(-Math.abs(cells[i][column]
								.getValue()));
						cells[scoreRow][column].addScore += 10;
					}

				} else { // 這個不能不消除
					ti = -1;
					effected = false;
					count = 0;
					tag = false;
				}
			} else if (column >= 0
					&& i < Constants.cellRow[currentType] - 2
					&& Math.abs(cells[i][column].getValue()) == Math
							.abs(cells[i + 1][column].getValue())) {

				if (Math.abs(cells[i + 1][column].getValue()) == Math
						.abs(cells[i + 2][column].getValue())) { // 可以消除三個

					cells[i][column].setValue(-Math.abs(cells[i][column]
							.getValue()));
					cells[i + 1][column].setValue(-Math
							.abs(cells[i + 1][column].getValue()));
					cells[i + 2][column].setValue(-Math
							.abs(cells[i + 2][column].getValue()));

					// 添加显示加分项的蛋糕,该蛋糕不能为检测的蛋糕
					if (ti == i + 1) {
						scoreRow = i;
					} else {
						scoreRow = i + 1;
					}
					cells[scoreRow][column].addScore += 30;

					tag = true;
					ans = true;
					i += 2;
					count = 3;
				} else {
					i += 1;
				}
			}
		}
		return ans;
	}

	/**
	 * 表示存在消去的蛋糕时，首先根据消去的蛋糕中的效果来重新设置所有的蛋糕
	 */
	public void resetCells() {
		// System.out.println("reset cells!");
		for (int i = 0; i < Constants.cellRow[currentType]; i++) {
			for (int j = 0; j < Constants.cellColumn[currentType]; j++) {
				if (cells[i][j].getValue() < 0) { // 可消去时
					switch (cells[i][j].getEffectType()) {
					case 0: // 横闪电
						cells[i][j].addScore += (Constants.cellColumn[currentType] - 3) * 20;
						setRow(i, -3); // 单行消去
						if (Constants.isMusic) {
							MyAssetsManager.getInstance().shandianMusic.play();
						}
						break;
					case 1: // 列闪电
						cells[i][j].addScore += (Constants.cellRow[currentType] - 3) * 20;
						setColumn(j, -4); // 单列消去
						if (Constants.isMusic) {
							MyAssetsManager.getInstance().shandianMusic.play();
						}
						break;
					case 2: // 十字
						cells[i][j].addScore += (Constants.cellColumn[currentType] * 2 - 4) * 20;
						setRow(i, -5);
						setColumn(j, -6);
						if (Constants.isMusic) {
							MyAssetsManager.getInstance().shiziMusic.play();
						}
						break;
					case 3: // 炸弹
						cells[i][j].addScore += 100;
						setArround(i, j);
						if (Constants.isMusic) {
							MyAssetsManager.getInstance().bombMusic.play();
						}
						break;
					case 4: // 泡泡
						cells[i][j].addScore += Constants.cellColumn[currentType]
								* (Constants.cellRow[currentType] / 2 + (j + 1) % 2)
								* 10;
						setPaopao(j);
						if (Constants.isMusic) {
							MyAssetsManager.getInstance().shandianMusic.play();
						}
						break;
					default: // 其他
						if (Constants.isMusic) {
							MyAssetsManager.getInstance().removeMusic.play();
						}
						break;
					}
				}
			}
		}
	}

	public void setRow(int row, int effect) {
		for (int j = 0; j < Constants.cellColumn[currentType]; j++) {
			setBomb(row, j, -2);
		}
		cells[row][0].setEffect(effect);
	}

	public void setColumn(int column, int effect) {
		for (int i = 0; i < Constants.cellRow[currentType]; i++) {
			setBomb(i, column, -2);
		}
		cells[0][column].setEffect(effect);
	}

	public void setArround(int row, int column) {
		if (row > 0) {
			setBomb(row - 1, column, -1);
			setBomb(row - 1, column, -1);
			if (column > 0) {
				setBomb(row - 1, column - 1, -1);
				setBomb(row, column - 1, -1);
			}
			if (column < Constants.cellColumn[currentType] - 1) {
				setBomb(row - 1, column + 1, -1);
				setBomb(row, column + 1, -1);
			}
		}
		if (row < Constants.cellRow[currentType] - 1) {
			setBomb(row + 1, column, -1);
			if (column > 0)
				setBomb(row + 1, column - 1, -1);
			if (column < Constants.cellColumn[currentType] - 1)
				setBomb(row + 1, column + 1, -1);
		}
	}

	public void setPaopao(int column) {
		int tc = column;
		while (tc >= 0) {
			setColumn(tc, -4);
			tc -= 2;
		}
		while (column < Constants.cellColumn[currentType] - 2) {
			column += 2;
			setColumn(column, -4);
		}
	}

	// 将row、column处蛋糕设置为消去，且效果为effect
	public void setBomb(int row, int column, int effect) {
		cells[row][column].setValue(-Math.abs(cells[row][column].getValue()));
		cells[row][column].setEffect(effect);
	}

	/**
	 * 消除三个or三个以上同色的方块，并随机添加新的方块
	 */
	public void removeIce() {
		int tempScore = 0;
		resetCells();
		// System.out.println("remove ice");

		Random rand = new Random();
		for (int i = 0; i < Constants.cellRow[currentType]; i++) {
			for (int j = 0; j < Constants.cellColumn[currentType]; j++) {
				if (cells[i][j] == null || cells[i][j].getValue() < 0) { // 消去当前元素时
					if (cells[i][j] != null) { // 若当前元素非空则加入move out行为
						if (cells[i][j].addScore > 0) { // 当前蛋糕可以消去，且消去加分时
							Tip tip = new Tip("+" + cells[i][j].addScore,
									style2);
							tempScore += cells[i][j].addScore;
							tip.setPosition(
									cells[i][j].getX()
											+ (cells[i][j].getWidth() - tip
													.getWidth()) / 2,
									cells[i][j].getY());
							tip.setAction();
							stage.addActor(tip);
						}
						cells[i][j].setMoveOutAction();
						// 添加消去动画
						addEffect(cells[i][j].getEffectType(), i, j);
					}
					int tem = 1;
					while (i + tem < Constants.cellRow[currentType]
							&& (cells[i + tem][j] == null || cells[i + tem][j]
									.getValue() < 0)) { // 寻找该元素上不被消除的元素
						tem++;
					}

					if (i + tem == Constants.cellRow[currentType]) {// 表示该元素上都是空的，新生成一个添加到该位置
						// System.out.println("add ice");
						int index = rand
								.nextInt(Constants.cellNum[currentType]);
						// 修改
						IceActor temp = new IceActor(
								MyAssetsManager.getInstance().iceCells[index],
								index + 1, currentType);
						temp.setSize(Constants.cellWidth[currentType],
								Constants.cellHeight[currentType]);
						temp.setMoveInAction(Constants.cellX + j
								* Constants.cellWidth[currentType],
								Constants.cellY + i
										* Constants.cellHeight[currentType]);
						group.addActor(temp);
						cells[i][j] = temp;

					} else { // 上面的非消去元素下移
						// System.out
						// .println("move ice: " + (i + tem) + " , " + j);
						cells[i + tem][j].setMoveDownAction(Constants.cellX + j
								* Constants.cellWidth[currentType],
								Constants.cellY + i
										* Constants.cellHeight[currentType]);
						cells[i][j] = cells[i + tem][j];
						cells[i + tem][j] = null;
					}
				}
			}
		}
		score += tempScore;
		scoreLabel.setText("Score: " + score);
	}

	/**
	 * 判斷是否存在可以被消去的對象
	 * 
	 * @return true表示存在可被消去的對象
	 */
	public boolean preJudge() {
		for (int i = 0; i < Constants.cellRow[currentType]; i++) {
			for (int j = 0; j < Constants.cellColumn[currentType]; j++) {
				// 上下交换
				if (i + 1 < Constants.cellRow[currentType]) {
					// 判断列消除
					if (i + 3 < Constants.cellRow[currentType]) {
						if (cells[i][j].getValue() == cells[i + 2][j]
								.getValue()
								&& cells[i][j].getValue() == cells[i + 3][j]
										.getValue()) {
							// System.out.println("==>i: " + i + " ===>j: " +
							// j);
							return true;
						}

					}
					if (i > 1) {
						if (cells[i + 1][j].getValue() == cells[i - 1][j]
								.getValue()
								&& cells[i + 1][j].getValue() == cells[i - 2][j]
										.getValue()) {
							// System.out.println("==>i: " + i + " ===>j: " +
							// j);
							return true;
						}
					}

					// 消除行判断
					if (j > 0) {
						// 作为消去的中间块
						if (j + 1 < Constants.cellColumn[currentType]
								&& (cells[i][j].getValue() == cells[i + 1][j + 1]
										.getValue()
										&& cells[i][j].getValue() == cells[i + 1][j - 1]
												.getValue() || (cells[i + 1][j]
										.getValue() == cells[i][j + 1]
										.getValue() && cells[i + 1][j]
										.getValue() == cells[i][j - 1]
										.getValue()))) {
							// System.out.println("==>i: " + i + " ===>j: " +
							// j);
							return true;
						}
						// 作为消去的右边块
						if (j > 1) {
							if (cells[i][j].getValue() == cells[i + 1][j - 1]
									.getValue()
									&& cells[i][j].getValue() == cells[i + 1][j - 2]
											.getValue()
									|| (cells[i + 1][j].getValue() == cells[i][j - 1]
											.getValue() && cells[i + 1][j]
											.getValue() == cells[i][j - 2]
											.getValue())) {
								// System.out.println("==>i: " + i + " ===>j: "
								// + j);
								return true;
							}
						}
					}
					// 作为消去的左边块
					if (j + 2 < Constants.cellColumn[currentType]) {
						if (cells[i][j].getValue() == cells[i + 1][j + 1]
								.getValue()
								&& cells[i][j].getValue() == cells[i + 1][j + 2]
										.getValue()
								|| (cells[i + 1][j].getValue() == cells[i][j + 1]
										.getValue() && cells[i + 1][j]
										.getValue() == cells[i][j + 2]
										.getValue())) {
							// System.out.println("==>i: " + i + " ===>j: " +
							// j);
							return true;
						}
					}

				}

				// 左右交换
				if (j + 1 < Constants.cellColumn[currentType]) {
					// 判断列消除
					if (i > 0 && i + 1 < Constants.cellRow[currentType]) {
						if (cells[i][j].getValue() == cells[i + 1][j + 1]
								.getValue()
								&& cells[i][j].getValue() == cells[i - 1][j + 1]
										.getValue()
								|| (cells[i][j + 1].getValue() == cells[i + 1][j]
										.getValue() && cells[i][j + 1]
										.getValue() == cells[i - 1][j]
										.getValue())) {
							// System.out.println("==>i: " + i + " ===>j: " +
							// j);
							return true;
						}
					}

					if (i + 2 < Constants.cellRow[currentType]) {
						if (cells[i][j].getValue() == cells[i + 1][j + 1]
								.getValue()
								&& cells[i][j].getValue() == cells[i + 2][j + 1]
										.getValue()
								|| (cells[i][j + 1].getValue() == cells[i + 1][j]
										.getValue() && cells[i][j + 1]
										.getValue() == cells[i + 2][j]
										.getValue())) {
							// System.out.println("==>i: " + i + " ===>j: " +
							// j);
							return true;
						}
					}

					if (i > 1) {
						if (cells[i][j].getValue() == cells[i - 1][j + 1]
								.getValue()
								&& cells[i][j].getValue() == cells[i - 2][j + 1]
										.getValue()
								|| (cells[i][j + 1].getValue() == cells[i - 1][j]
										.getValue() && cells[i][j + 1]
										.getValue() == cells[i - 2][j]
										.getValue())) {
							// System.out.println("==>i: " + i + " ===>j: " +
							// j);
							return true;
						}
					}

					// 判断行消除
					if (j + 3 < Constants.cellColumn[currentType]) {
						if (cells[i][j].getValue() == cells[i][j + 2]
								.getValue()
								&& cells[i][j].getValue() == cells[i][j + 3]
										.getValue()) {
							// System.out.println("==>i: " + i + " ===>j: " +
							// j);
							return true;
						}
					}

					if (j > 1) {
						if (cells[i][j + 1].getValue() == cells[i][j - 1]
								.getValue()
								&& cells[i][j + 1].getValue() == cells[i][j - 2]
										.getValue()) {
							// System.out.println("==>i: " + i + " ===>j: " +
							// j);
							return true;
						}
					}
				}

			}
		}

		return false;
	}

	/**
	 * 判断(x,y)是否在游戏盘内 是返回true
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean inBox(float x, float y) {
		if ((x >= Constants.cellX && x <= Constants.cellX
				+ Constants.cellWidth[currentType]
				* Constants.cellColumn[currentType])
				&& y > Constants.cellY
				&& y <= Constants.cellY + Constants.cellHeight[currentType]
						* Constants.cellRow[currentType]) {
			return true;
		}
		return false;
	}

	/**
	 * 将坐标y转换为在游戏界面上的行
	 * 
	 * @param y
	 * @return
	 */
	public int getRow(float y) {
		return (int) ((y - Constants.cellY) / Constants.cellHeight[currentType]);
	}

	/**
	 * 将坐标x转换为游戏界面上的列
	 * 
	 * @param x
	 * @return
	 */
	public int getColumn(float x) {
		return (int) ((x - Constants.cellX) / Constants.cellWidth[currentType]);
	}

	/**
	 * 根据effectType来添加消去效果
	 * 
	 * @param effectType
	 *            表示消去效果的参数
	 * @param i
	 *            添加消去效果蛋糕 行
	 * @param j
	 *            添加消去效果蛋糕 列
	 */
	public void addEffect(int effectType, int i, int j) {
		Effect effect = new Effect();
		if (effectType == -2) {
			return;
		} else if (effectType == -3) { // 单行消去
			effect.setType(0);
			effect.setBounds(cells[i][j].getX(), cells[i][j].getY(),
					Constants.iceBgWidth - Constants.iceBgAdd,
					cells[i][j].getHeight());
		} else if (effectType == -4) { // 单列消去
			effect.setType(1);
			effect.setBounds(cells[i][j].getX(), cells[i][j].getY(),
					cells[i][j].getWidth(), Constants.iceBgHeight
							- Constants.iceBgAdd);
		} else if (effectType == -5) { // 十字行去
			effect.setType(2);
			effect.setBounds(cells[i][j].getX(), cells[i][j].getY(),
					Constants.iceBgWidth - Constants.iceBgAdd,
					cells[i][j].getHeight());
		} else if (effectType == -6) { // 十字列去
			effect.setType(3);
			effect.setBounds(cells[i][j].getX(), cells[i][j].getY(),
					cells[i][j].getWidth(), Constants.iceBgHeight
							- Constants.iceBgAdd);
		} else { // 其他时，采用默认的炸弹动画
			effect.setType(4);
			effect.setBounds(cells[i][j].getX(), cells[i][j].getY(),
					cells[i][j].getWidth(), cells[i][j].getHeight());
		}
		group.addActor(effect);
	}

	/**
	 * type: 0 表示暂停， 1表示胜利， 2表示失败
	 */
	public synchronized void showResultDialog(final int type) {
		isPlay = false; // 游戏暂停
		Gdx.input.setInputProcessor(stage);

		ImageButton continueButton;
		ImageButton menuButton;
		ImageButton restartButton;
		ImageButton retryButton;
		Label infoLabel;
		TextureRegionDrawable trd[];

		Image resultDialog = new Image(MyAssetsManager.getInstance().resultbg);
		resultDialog.setBounds(Constants.resultX, Constants.resultY
				+ Constants.screenHeight, Constants.resultWidth,
				Constants.resultHeight);
		Action a1 = Actions.moveTo(Constants.resultX, Constants.resultY,
				Constants.actionTime * Gdx.graphics.getDeltaTime());
		resultDialog.addAction(a1);

		resultGroup.addActor(resultDialog);

		trd = new TextureRegionDrawable[6];
		for (int i = 0; i < 6; i++) {
			trd[i] = new TextureRegionDrawable(
					MyAssetsManager.getInstance().resultButtons[i]);
		}

		infoLabel = new Label("hello", new LabelStyle(
				MyAssetsManager.getInstance().font, Color.BLACK));
		infoLabel.setBounds(Constants.infoLabelX, Constants.infoLabelY
				+ Constants.screenHeight, Constants.infoLabelWidth,
				Constants.infoLabelHeight);
		infoLabel.setAlignment(Align.center);
		Action a2 = Actions.moveTo(Constants.infoLabelX, Constants.infoLabelY,
				Constants.actionTime * Gdx.graphics.getDeltaTime());
		infoLabel.addAction(a2);
		resultGroup.addActor(infoLabel);

		if (type == 0) { // 暂停时，显示继续按钮
		// infoLabel.setText("休息一下，宝贝儿");
			infoLabel.setText("Have a rest!");
			continueButton = new ImageButton(trd[0], trd[1]);
			continueButton.setSize(Constants.buttonWidth,
					Constants.buttonHeight);
			continueButton.setPosition(Constants.buttonX, Constants.buttonY
					+ Constants.screenHeight);
			continueButton.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					if (Constants.isMusic) {
						MyAssetsManager.getInstance().buttonClickMusic.play();
					}
					isPlay = true;
					resultGroup.clear();
					Gdx.input.setInputProcessor(gestrueDetector);
					System.out.println("continue");
				}
			});
			Action a3 = Actions.moveTo(Constants.buttonX, Constants.buttonY,
					Constants.actionTime * Gdx.graphics.getDeltaTime());
			continueButton.addAction(a3);
			resultGroup.addActor(continueButton);

			restartButton = new ImageButton(trd[2], trd[3]);
			restartButton
					.setSize(Constants.buttonWidth, Constants.buttonHeight);
			restartButton.setPosition(Constants.buttonX + Constants.buttonAddX,
					Constants.buttonY + Constants.screenHeight);
			restartButton.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					if (Constants.isMusic) {
						MyAssetsManager.getInstance().buttonClickMusic.play();
					}
					game.setScreen(new GameScreen(game, currentLevel,
							currentType));
					System.out.println("restart");
				}
			});
			Action a4 = Actions.moveTo(
					Constants.buttonX + Constants.buttonAddX,
					Constants.buttonY,
					Constants.actionTime * Gdx.graphics.getDeltaTime());
			restartButton.addAction(a4);
			resultGroup.addActor(restartButton);
		} else if (type == 1) { // 胜利时，显示继续按钮
			if (Constants.isMusic) {
				MyAssetsManager.getInstance().winMusic.play();
			}

			// infoLabel.setText("干得漂亮，么么哒");
			infoLabel.setText("Wonderful");
			continueButton = new ImageButton(trd[0], trd[1]);
			continueButton.setSize(Constants.buttonWidth,
					Constants.buttonHeight);
			continueButton.setPosition(Constants.buttonX, Constants.buttonY
					+ Constants.screenHeight);
			continueButton.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					// if(Constants.isMusic){
					// MyAssetsManager.getInstance().buttonClickMusic.play();
					// }

					if (currentLevel == Constants.currentLevel[currentType] + 1) { // 胜利时，继续下一关
						Constants.currentLevel[currentType] += 1;
					}
					++currentLevel;
					MyAssetsManager.getInstance().writeLevel();
					game.setScreen(new GameScreen(game, currentLevel,
							currentType));
					System.out.println("next");
				}
			});
			Action a5 = Actions.moveTo(Constants.buttonX, Constants.buttonY,
					Constants.actionTime * Gdx.graphics.getDeltaTime());
			continueButton.addAction(a5);
			resultGroup.addActor(continueButton);
		} else if (type == 2) { // 失败时，显示重来按钮
			if (Constants.isMusic) {
				MyAssetsManager.getInstance().failMusic.play();
			}

			// infoLabel.setText("亲亲宝宝，加油！");
			infoLabel.setText("Try Again");
			retryButton = new ImageButton(trd[2], trd[3]);
			retryButton.setSize(Constants.buttonWidth, Constants.buttonHeight);
			retryButton.setPosition(Constants.buttonX, Constants.buttonY
					+ Constants.screenHeight);
			retryButton.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) { // 当前关重新开始
					if (Constants.isMusic) {
						MyAssetsManager.getInstance().buttonClickMusic.play();
					}
					game.setScreen(new GameScreen(game, currentLevel,
							currentType));
					System.out.println("retry");
				}
			});
			Action a6 = Actions.moveTo(Constants.buttonX, Constants.buttonY,
					Constants.actionTime * Gdx.graphics.getDeltaTime());
			retryButton.addAction(a6);
			resultGroup.addActor(retryButton);
		}

		// 菜单按钮总是显示
		menuButton = new ImageButton(trd[4], trd[5]);
		menuButton.setSize(Constants.buttonWidth, Constants.buttonHeight);
		menuButton.setPosition(Constants.buttonAddX * 2 + Constants.buttonX,
				Constants.buttonY + Constants.screenHeight);
		menuButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (Constants.isMusic) {
					MyAssetsManager.getInstance().buttonClickMusic.play();
				}

				if (type == 1) {
					Constants.currentLevel[currentType] += 1;
					++currentLevel;
					MyAssetsManager.getInstance().writeLevel();
				}
				game.setScreen(new LevelScreen(game,
						Constants.currentLevel[currentType], currentType));
				System.out.println("return");
			}
		});
		Action a7 = Actions.moveTo(
				Constants.buttonAddX * 2 + Constants.buttonX,
				Constants.buttonY,
				Constants.actionTime * Gdx.graphics.getDeltaTime());
		menuButton.addAction(a7);
		resultGroup.addActor(menuButton);

	}

}
