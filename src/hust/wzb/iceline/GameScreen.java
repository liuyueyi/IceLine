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
	private int currentLevel; // ��ǰ��ˣ�ĵȼ�
	private int currentType; // ��ǰ��ˣ���Ѷ�

	// ����
	private SpriteBatch batch;
	private Sprite background; // ��Ϸ����
	private Sprite iceBg; // ������е���ı���
	private Sprite iceCellBg; // ��������ı���

	private Stage stage;
	private GestureDetector gestrueDetector;

	// ��Ϸ������
	private int count = 0;
	private Group group;
	private IceActor[][] cells;
	private boolean ifSelected = false; // ������ʾ��ǰ�����ϴ治�����Ѿ�ѡ�еĿ�
	private Sprite selectedSprite; // ѡ�е�ͼ��
	private int lastSelectedRow, lastSelectedColumn; // ��һ��ѡ�е����꣬����
	private boolean isRemoved = false; // �ж��Ƿ���ڿ�����ȥ�ĵ��⣬ture��ʾ����
	private Image paopao; // ���ݱ��

	private boolean notTouched = false; // true��ʾ��ǰ�����ܵ������������ִ�л���or��ȥʱ����Ϊtrue
	private boolean locked = false; // �������������ⲻ������ʱ��locked����Ϊtrue����ʱ�ٹ�һ��ʱ�����������notTouchedΪfalse
	private int locktime = 0;

	// ��������ز���
	private float firstX, firstY; // ���ڻ���ʱ��¼��һλ�õ�����
	private boolean paned = false; // false ��ʾ��ʼ����

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

	// result ��
	private Group resultGroup; // ���resultԪ�ص�group
	private boolean isPlay = true;

	public GameScreen(MyGdxGame game, int level, int type) {
		this.game = game;
		this.currentLevel = level;
		this.currentType = type;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);// ���ñ���Ϊ��ɫ
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);// ����

		// TODO Auto-generated method stub
		if (isPlay) { // ��Ϸ��ʱ��ʱ����ȼ���
			leftTime -= Gdx.graphics.getDeltaTime();
			if (leftTime <= 0) { // ʧ�ܵ���ʧ�ܿ�
				showResultDialog(2);
			}
		}
		// ���Ʊ���
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

		// ����time
		timeSprite.draw(batch);
		timebar.draw(batch);
		timebarfill.draw(batch);
		timebarfill.setSize(Constants.timeBarWidth * (leftTime / totalTime),
				Constants.timeBarHeight);

		// ѡ��ʱ������ѡ�п�ͼ��
		if (ifSelected) {
			selectedSprite.draw(batch);
		}
		batch.end();

		// // ����
		// if (Gdx.input.isKeyPressed(Input.Keys.A)
		// || Gdx.input.isKeyPressed(Input.Keys.BACK)) {
		// game.setScreen(new LevelScreen(game, currentType));
		// }

		// game ����
		if (isRemoved) { // ִ�н�������ʱ
			if (++count == Constants.actionTime) { // ��������������ִ����ȥ����action
				removeIce();
				if (isPlay && score >= target) {
					showResultDialog(1); // ʤ��
					isPlay = false;
				}
			}
			if (count == 2 * Constants.actionTime + Constants.actionTime) { // ��ȥ��������
				isRemoved = judge(); // �ж�����ӷ�����Ƿ���ڿ���ȥ����
				if (isRemoved) { // ���� �������´ν����ֱ����ȥ
					count = Constants.actionTime / 2;
				} else { // �����ڿ�ֱ����ȥ�ķ��飬��Ԥ��
					if (!preJudge()) {
						// Ԥ�в�����, �����Ų������´ν����ֱ���ж��Ƿ����ȥ
						addPaopao();
					} else {
						count = 0;
					}
					notTouched = false; // ����Ϊ���Խ��ܵ��
				}
			}
		}

		if (locked) { // ��ס�󣬿�ʼ��ʱ
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
		updateCells(); // ��ӵ��⵽������

		// infoBack
		infoBack = MyAssetsManager.getInstance().infoBg;
		infoBack.setBounds(Constants.infoBackX, Constants.infoBackY,
				Constants.infoBackWidth, Constants.infoBackHeight);

		// time ����
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

		// ��ʼ����ʱ������Ƿ���ڿ���ȥ�ĵ���
		isRemoved = true;
		count = Constants.actionTime / 2; // 10

		// �����
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
		// �����ͣ��ťʱ
		if (x > pauseButton.getX()
				&& x < pauseButton.getX() + pauseButton.getWidth()
				&& Constants.screenHeight - y > pauseButton.getY()
				&& Constants.screenHeight - y < pauseButton.getY()
						+ pauseButton.getHeight()) {
			showResultDialog(0);
			return true;
		}

		if (!inBox(x, Constants.screenHeight - y) || notTouched) {
			// ���������߲�׼���ʱ
		} else {
			int column = getColumn(x);
			int row = getRow(Constants.screenHeight - y);
			System.out.println(column + " out: " + row);
			if (cells[row][column].getEffectType() == 4) { // ��ʾ��������ݣ�ֱ����ʾ��ȥ����
				isRemoved = true;
				count = Constants.actionTime - 1;
				paopao.remove();
				return false;
			}

			if (ifSelected
					&& (lastSelectedColumn == column
							&& Math.abs(lastSelectedRow - row) == 1 || (lastSelectedRow == row && Math
							.abs(lastSelectedColumn - column) == 1))) {// ��ʾ��ǰѡ�еĺ�֮ǰ�����ڣ��򻥻�λ��
				// ���󻥻�
				IceActor tem = cells[row][column];
				cells[row][column] = cells[lastSelectedRow][lastSelectedColumn];
				cells[lastSelectedRow][lastSelectedColumn] = tem;

				isRemoved = judgeRowColumn(row, column, lastSelectedRow,
						lastSelectedColumn); // �жϻ������Ƿ���ڿ���ȥ�ĵ���

				notTouched = true; // ��ס���ô���
				if (Constants.isMusic) {
					MyAssetsManager.getInstance().panMusic.play();
				}

				if (isRemoved) { // �������жϴ��ڿ���ȥ�Ķ���ʱ
					cells[row][column].setChangeAction(
							cells[lastSelectedRow][lastSelectedColumn].getX(),
							cells[lastSelectedRow][lastSelectedColumn].getY());
					cells[lastSelectedRow][lastSelectedColumn].setChangeAction(
							cells[row][column].getX(),
							cells[row][column].getY());
				} else { // �����󲻴��ڿ���������
					locked = true; // ��ס��locktime�����
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

			} else { // ����ǰ����ĵ�������Ϊѡ��
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
		if (notTouched) {// ��ʾ����ִ�ж���
			return false;
		}

		y = Constants.screenHeight - y;
		firstY = Constants.screenHeight - firstY;
		if (!(inBox(firstX, firstY) && inBox(x, y))) { // ��һ����������ʱ
			return false;
		}

		// ������ת��Ϊ����
		int column = getColumn(x);
		int row = getRow(y);

		int column2 = getColumn(firstX);
		int row2 = getRow(firstY);

		if (!paned
				&& ((column == column2 && Math.abs(row - row2) == 1) || (row == row2 && Math
						.abs(column - column2) == 1))) { // ���������򻥓Q

			paned = true; // ��ʾ�Ѿ����й�һ�λ����ˣ�����Ļ�����ִ�л���
			// ���󻥻�
			IceActor tem = cells[row][column];
			cells[row][column] = cells[row2][column2];
			cells[row2][column2] = tem;
			isRemoved = judgeRowColumn(row, column, row2, column2); // �жϻ������Ƿ���ڿ���ȥ�ĵ���

			notTouched = true;
			if (Constants.isMusic) {
				MyAssetsManager.getInstance().panMusic.play();
			}

			if (isRemoved) { // �����󣬴��ڿ���ȥ�Ķ���ʱ
				cells[row][column].setChangeAction(cells[row2][column2].getX(),
						cells[row2][column2].getY());
				cells[row2][column2].setChangeAction(cells[row][column].getX(),
						cells[row][column].getY());
			} else { // �����󲻴��ڿ���������
				locked = true; // ��ס��locktime�����not touch
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
	 * ���Ű��÷���Ķ�ά����
	 */
	public void updateCells() {
		Random rand = new Random();
		if (cells == null) {
			group = new Group();
			cells = new IceActor[Constants.cellRow[currentType]][Constants.cellColumn[currentType]];
			stage.addActor(group);
			group.clear();
		} else {
			// System.out.println("===����====");
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
		// ������ʱ���������
		paopao = new Image(MyAssetsManager.getInstance().toolSprite[4]);
		int temp = new Random().nextInt(Constants.cellRow[currentType]
				* Constants.cellColumn[currentType]);
		int tempC = temp / Constants.cellRow[currentType];
		int tempR = temp % Constants.cellColumn[currentType];
		setBomb(tempR, tempC, 4);// ���øõ��ⱻ���ݸ���
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
	 * �����ж��Ƿ���ڿ��Ա������Ķ���
	 * 
	 * @return
	 */
	public boolean judge() {
		boolean ans = false; // ��ʾ�Ƿ���ڿ���ȥ�ĵ���

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
	 * �ж�(row, column)�Ƿ���Ա���ȥ
	 * 
	 * @param row
	 *            ����ȥ�������
	 * @param column
	 *            ����ȥ�������
	 * @return true ��ʾ����
	 */
	public boolean judgeRowColumn(int row, int column) {
		boolean rans = judgeRow(row, column);
		boolean cans = judgeColumn(row, column);
		if (rans && cans && cells[row][column].getEffectType() < 0) {
			cells[row][column]
					.setValue(Math.abs(cells[row][column].getValue()));
			cells[row][column].setEffect(3); // ���ը��
		}

		return rans || cans;
	}

	/**
	 * �ж�(row, column),(row2,column2) �Ƿ���Ա���ȥ
	 * 
	 * @param row
	 * @param column
	 * @param row2
	 * @param column2
	 * @return
	 */
	public boolean judgeRowColumn(int row, int column, int row2, int column2) {
		boolean tag = false; // ��ǣ�true��ʾ(row,column)��ը���Ǹռ���ȥ�ģ���ô�ڼ��(row2,column2)����ʱ����Ҫ��(row,column)�ָ�Ϊ������
		boolean rans = judgeRow(row, column);
		boolean cans = judgeColumn(row, column);
		if (rans && cans) { // ��ʾ�������ж���������
			cells[row][column]
					.setValue(Math.abs(cells[row][column].getValue()));
			cells[row][column].setEffect(3); // ���ը��
			tag = true;
		}

		boolean rans2 = judgeRow(row2, column2);
		boolean cans2 = judgeColumn(row2, column2);
		if (rans2 && cans2 && cells[row][column].getEffectType() != 3) {
			cells[row2][column2].setValue(Math.abs(cells[row2][column2]
					.getValue()));
			cells[row2][column2].setEffect(3); // ���ը��
		}

		if (tag == true) { // ��Ϊ�ڼ��(row2,column2)ʱ���Ὣ(row,column)����Ϊ��ȥ������ָ�Ϊ������
			cells[row][column]
					.setValue(Math.abs(cells[row][column].getValue()));
		}

		return rans || cans || rans2 || cans2;
	}

	/**
	 * �ж�row���Ƿ���ڿ�����ȥ�ĵ���
	 * 
	 * @param row
	 *            Ҫ�жϵ���
	 * @param tj
	 *            Ϊ��ʱ����ʾ��Ҫ���tool������ӵ�(row, tj)�ĵ����ϣ�������ӵ����ĸ���ͬ�ĵ�����
	 * @return true ��ʾ���ڿ���ȥ�ĵ���
	 */
	public boolean judgeRow(int row, int tj) {
		int count = 0; // ����һ��orһ�м�¼�����ȥ�ĸ���
		boolean tag = false;
		boolean ans = false; // ��ʾ�Ƿ���ڿ���ȥ����
		boolean effected = false; // ��ʾ�Ƿ������tool

		int scoreColumn = -1; // ���ڼ�¼��ǰ����ʾ�ӷֵĵ�����

		for (int j = 0; j < Constants.cellColumn[currentType]; j++) {
			if (tag) {
				if (Math.abs(cells[row][j].getValue()) == Math
						.abs(cells[row][j - 1].getValue())) { // ����ɫ��������
					++count; // ��������ʾ���п�����ȥ�ĸ���
					if (count == Constants.cellColumn[currentType]) { // ��ʾ��ȥһ��ʱ,���ʮ�ֱ��
						cells[row][tj].setEffect(-1); // ȥ��ԭ���ı��
						cells[row][tj].setValue(-Math.abs(cells[row][j]
								.getValue()));
						cells[row][tj].addScore += 10; // ����ȥһ������10��

						cells[row][count / 2].setEffect(2); // ���м����ʮ�ֱ��
						cells[row][count / 2].setValue(Math
								.abs(cells[row][count / 2].getValue()));

						cells[row][count / 2].addScore = (count - 2) * 10;
						break;
					}

					if (!effected) {// û����ӱ��ʱ, ���������
						effected = true; // ����˱�ǣ��ٴ��ж�ʱֻ�����������
						if (tj < 0) { // ��ʾû��ָ����tool��Ƿ����ĸ�ȷ���ĵ�����ʱ
							tj = j;
						} else {
							cells[row][j].setValue(-Math.abs(cells[row][j]
									.getValue()));
						}
						if (cells[row][tj].getEffectType() >= 0) { // �������б��ʱ��������ӱ��
							cells[row][tj].setValue(Math.abs(cells[row][tj]
									.getValue()));
						} else {
							cells[row][tj].setEffect(0); // ��ʾΪ���ĸ�or�����һ������Ӻ�������
							cells[row][tj].setValue(Math.abs(cells[row][tj]
									.getValue()));
						}
					} else { // �Ѿ���ӱ��ʱ���õ���ֱ����ȥ
						cells[row][scoreColumn].addScore += 10; // ����ȥһ������10��
						cells[row][j].setValue(-Math.abs(cells[row][j]
								.getValue()));
					}

				} else { // ����ɫ��֮ǰ�Ĳ���ͬ, ���ò���
					tj = -1;
					effected = false;
					count = 0;
					tag = false;
				}

			} else if (row >= 0
					&& j < Constants.cellColumn[currentType] - 2
					&& Math.abs(cells[row][j].getValue()) == Math
							.abs(cells[row][j + 1].getValue())) { // �ж�ʱ������������ֹ

				if (Math.abs(cells[row][j + 1].getValue()) == Math
						.abs(cells[row][j + 2].getValue())) {
					// ��ʾ�ҵ�������ͬ��
					cells[row][j].setValue(-Math.abs(cells[row][j].getValue()));
					cells[row][j + 1].setValue(-Math.abs(cells[row][j + 1]
							.getValue()));
					cells[row][j + 2].setValue(-Math.abs(cells[row][j + 2]
							.getValue()));

					// ���������ʾ�ӷ���ĵ���
					if (tj == j + 1) {
						scoreColumn = j;
					} else {
						scoreColumn = j + 1;
					}
					cells[row][scoreColumn].addScore += 30;

					tag = true;
					ans = true;
					j += 2;
					count = 3; // �������Ѿ�ȥ������
				} else {
					j += 1;
				}
			}
		}
		return ans;
	}

	/**
	 * �ж�column���Ƿ���ڿ�����ȥ�ĵ���
	 * 
	 * @param ti
	 *            Ϊ��ʱ����ʾ��Ҫ���tool������ӵ�(ti, column)�ĵ����ϣ�������ӵ����ĸ���ͬ�ĵ�����
	 * @param column
	 *            Ҫ�жϵ���
	 * @return true ��ʾ���ڿ���ȥ�ĵ���
	 */
	public boolean judgeColumn(int ti, int column) {
		int count = 0; // ����һ��orһ�м�¼�����ȥ�ĸ���
		boolean tag = false;
		boolean ans = false; // ��ʾ�Ƿ���ڿ���ȥ����
		boolean effected = false; // ��ʾ�Ƿ������tool

		int scoreRow = -1;

		for (int i = 0; i < Constants.cellRow[currentType]; i++) {
			if (tag) {
				if (Math.abs(cells[i][column].getValue()) == Math
						.abs(cells[i - 1][column].getValue())) { // �@�����Ա�������
					++count;
					if (count == Constants.cellRow[currentType]) {
						// ��ʾ��ȥһ����
						cells[ti][column].setEffect(-1);
						cells[ti][column].setValue(-Math.abs(cells[i][column]
								.getValue()));
						cells[ti][column].addScore += 10;

						cells[count / 2][column].setEffect(2);
						cells[count / 2][column].setValue(Math
								.abs(cells[count / 2][column].getValue()));

						cells[count / 2][column].addScore = (count - 2) * 10;
						// ���ò�����������һ���ж�
						continue;
					}

					if (!effected) { // û����ӱ��ʱ
						if (ti < 0) { // ����û�н������ӵ�ָ���ĵ�����ʱ��ѡ����ĸ���Ϊ��ӱ�������ѡ�еĵ�����Ϊ�����
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

				} else { // �@�����ܲ�����
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
						.abs(cells[i + 2][column].getValue())) { // ������������

					cells[i][column].setValue(-Math.abs(cells[i][column]
							.getValue()));
					cells[i + 1][column].setValue(-Math
							.abs(cells[i + 1][column].getValue()));
					cells[i + 2][column].setValue(-Math
							.abs(cells[i + 2][column].getValue()));

					// �����ʾ�ӷ���ĵ���,�õ��ⲻ��Ϊ���ĵ���
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
	 * ��ʾ������ȥ�ĵ���ʱ�����ȸ�����ȥ�ĵ����е�Ч���������������еĵ���
	 */
	public void resetCells() {
		// System.out.println("reset cells!");
		for (int i = 0; i < Constants.cellRow[currentType]; i++) {
			for (int j = 0; j < Constants.cellColumn[currentType]; j++) {
				if (cells[i][j].getValue() < 0) { // ����ȥʱ
					switch (cells[i][j].getEffectType()) {
					case 0: // ������
						cells[i][j].addScore += (Constants.cellColumn[currentType] - 3) * 20;
						setRow(i, -3); // ������ȥ
						if (Constants.isMusic) {
							MyAssetsManager.getInstance().shandianMusic.play();
						}
						break;
					case 1: // ������
						cells[i][j].addScore += (Constants.cellRow[currentType] - 3) * 20;
						setColumn(j, -4); // ������ȥ
						if (Constants.isMusic) {
							MyAssetsManager.getInstance().shandianMusic.play();
						}
						break;
					case 2: // ʮ��
						cells[i][j].addScore += (Constants.cellColumn[currentType] * 2 - 4) * 20;
						setRow(i, -5);
						setColumn(j, -6);
						if (Constants.isMusic) {
							MyAssetsManager.getInstance().shiziMusic.play();
						}
						break;
					case 3: // ը��
						cells[i][j].addScore += 100;
						setArround(i, j);
						if (Constants.isMusic) {
							MyAssetsManager.getInstance().bombMusic.play();
						}
						break;
					case 4: // ����
						cells[i][j].addScore += Constants.cellColumn[currentType]
								* (Constants.cellRow[currentType] / 2 + (j + 1) % 2)
								* 10;
						setPaopao(j);
						if (Constants.isMusic) {
							MyAssetsManager.getInstance().shandianMusic.play();
						}
						break;
					default: // ����
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

	// ��row��column����������Ϊ��ȥ����Ч��Ϊeffect
	public void setBomb(int row, int column, int effect) {
		cells[row][column].setValue(-Math.abs(cells[row][column].getValue()));
		cells[row][column].setEffect(effect);
	}

	/**
	 * ��������or��������ͬɫ�ķ��飬���������µķ���
	 */
	public void removeIce() {
		int tempScore = 0;
		resetCells();
		// System.out.println("remove ice");

		Random rand = new Random();
		for (int i = 0; i < Constants.cellRow[currentType]; i++) {
			for (int j = 0; j < Constants.cellColumn[currentType]; j++) {
				if (cells[i][j] == null || cells[i][j].getValue() < 0) { // ��ȥ��ǰԪ��ʱ
					if (cells[i][j] != null) { // ����ǰԪ�طǿ������move out��Ϊ
						if (cells[i][j].addScore > 0) { // ��ǰ���������ȥ������ȥ�ӷ�ʱ
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
						// �����ȥ����
						addEffect(cells[i][j].getEffectType(), i, j);
					}
					int tem = 1;
					while (i + tem < Constants.cellRow[currentType]
							&& (cells[i + tem][j] == null || cells[i + tem][j]
									.getValue() < 0)) { // Ѱ�Ҹ�Ԫ���ϲ���������Ԫ��
						tem++;
					}

					if (i + tem == Constants.cellRow[currentType]) {// ��ʾ��Ԫ���϶��ǿյģ�������һ����ӵ���λ��
						// System.out.println("add ice");
						int index = rand
								.nextInt(Constants.cellNum[currentType]);
						// �޸�
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

					} else { // ����ķ���ȥԪ������
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
	 * �Д��Ƿ���ڿ��Ա���ȥ�Č���
	 * 
	 * @return true��ʾ���ڿɱ���ȥ�Č���
	 */
	public boolean preJudge() {
		for (int i = 0; i < Constants.cellRow[currentType]; i++) {
			for (int j = 0; j < Constants.cellColumn[currentType]; j++) {
				// ���½���
				if (i + 1 < Constants.cellRow[currentType]) {
					// �ж�������
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

					// �������ж�
					if (j > 0) {
						// ��Ϊ��ȥ���м��
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
						// ��Ϊ��ȥ���ұ߿�
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
					// ��Ϊ��ȥ����߿�
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

				// ���ҽ���
				if (j + 1 < Constants.cellColumn[currentType]) {
					// �ж�������
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

					// �ж�������
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
	 * �ж�(x,y)�Ƿ�����Ϸ���� �Ƿ���true
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
	 * ������yת��Ϊ����Ϸ�����ϵ���
	 * 
	 * @param y
	 * @return
	 */
	public int getRow(float y) {
		return (int) ((y - Constants.cellY) / Constants.cellHeight[currentType]);
	}

	/**
	 * ������xת��Ϊ��Ϸ�����ϵ���
	 * 
	 * @param x
	 * @return
	 */
	public int getColumn(float x) {
		return (int) ((x - Constants.cellX) / Constants.cellWidth[currentType]);
	}

	/**
	 * ����effectType�������ȥЧ��
	 * 
	 * @param effectType
	 *            ��ʾ��ȥЧ���Ĳ���
	 * @param i
	 *            �����ȥЧ������ ��
	 * @param j
	 *            �����ȥЧ������ ��
	 */
	public void addEffect(int effectType, int i, int j) {
		Effect effect = new Effect();
		if (effectType == -2) {
			return;
		} else if (effectType == -3) { // ������ȥ
			effect.setType(0);
			effect.setBounds(cells[i][j].getX(), cells[i][j].getY(),
					Constants.iceBgWidth - Constants.iceBgAdd,
					cells[i][j].getHeight());
		} else if (effectType == -4) { // ������ȥ
			effect.setType(1);
			effect.setBounds(cells[i][j].getX(), cells[i][j].getY(),
					cells[i][j].getWidth(), Constants.iceBgHeight
							- Constants.iceBgAdd);
		} else if (effectType == -5) { // ʮ����ȥ
			effect.setType(2);
			effect.setBounds(cells[i][j].getX(), cells[i][j].getY(),
					Constants.iceBgWidth - Constants.iceBgAdd,
					cells[i][j].getHeight());
		} else if (effectType == -6) { // ʮ����ȥ
			effect.setType(3);
			effect.setBounds(cells[i][j].getX(), cells[i][j].getY(),
					cells[i][j].getWidth(), Constants.iceBgHeight
							- Constants.iceBgAdd);
		} else { // ����ʱ������Ĭ�ϵ�ը������
			effect.setType(4);
			effect.setBounds(cells[i][j].getX(), cells[i][j].getY(),
					cells[i][j].getWidth(), cells[i][j].getHeight());
		}
		group.addActor(effect);
	}

	/**
	 * type: 0 ��ʾ��ͣ�� 1��ʾʤ���� 2��ʾʧ��
	 */
	public synchronized void showResultDialog(final int type) {
		isPlay = false; // ��Ϸ��ͣ
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

		if (type == 0) { // ��ͣʱ����ʾ������ť
		// infoLabel.setText("��Ϣһ�£�������");
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
		} else if (type == 1) { // ʤ��ʱ����ʾ������ť
			if (Constants.isMusic) {
				MyAssetsManager.getInstance().winMusic.play();
			}

			// infoLabel.setText("�ɵ�Ư����ôô��");
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

					if (currentLevel == Constants.currentLevel[currentType] + 1) { // ʤ��ʱ��������һ��
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
		} else if (type == 2) { // ʧ��ʱ����ʾ������ť
			if (Constants.isMusic) {
				MyAssetsManager.getInstance().failMusic.play();
			}

			// infoLabel.setText("���ױ��������ͣ�");
			infoLabel.setText("Try Again");
			retryButton = new ImageButton(trd[2], trd[3]);
			retryButton.setSize(Constants.buttonWidth, Constants.buttonHeight);
			retryButton.setPosition(Constants.buttonX, Constants.buttonY
					+ Constants.screenHeight);
			retryButton.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) { // ��ǰ�����¿�ʼ
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

		// �˵���ť������ʾ
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
