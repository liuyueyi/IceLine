package hust.wzb.iceline;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

public class LevelScreen implements Screen, GestureListener {
	private MyGdxGame game;
	private int currentType; // 当前的难度级别， 0-easy, 1-normal, 2-hard

	// 背景
	private SpriteBatch batch;
	private Sprite background;

	// 界面
	private Stage stage;
	private Group group; // 用于存放所有的level块的容器
	private Array<LevelActor> levels;
	private LevelActor tempLevel;

	private Sprite[] circles;
	private Sprite tempCircle;
	private int currentPage;
	private int currentLevel;

	public LevelScreen(MyGdxGame game) {
		this.game = game;
	}

	public LevelScreen(MyGdxGame game, int type) {
		this.game = game;
		this.currentType = type;
		this.currentLevel = Constants.currentLevel[type];
	}

	public LevelScreen(MyGdxGame game, int level, int type) {
		this.game = game;
		currentType = type;
		currentLevel = level;
	}

	public void setType(int type) {
		this.currentType = type;
		this.currentLevel = Constants.currentLevel[type];
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(1, 1, 1, 1);// 设置背景为白色
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);// 清屏

		batch.begin();
		background.draw(batch);
		for (int i = 0; i < Constants.circleNum; i++) {
			if (i == currentPage) {
				tempCircle = circles[1];
			} else {
				tempCircle = circles[0];
			}
			tempCircle.setPosition(Constants.circleX + i
					* (Constants.circleWidth + Constants.circleAddX),
					Constants.circleY);
			tempCircle.draw(batch);
		}

		batch.end();

		if (Gdx.input.isKeyPressed(Input.Keys.A)
				|| Gdx.input.isKeyPressed(Input.Keys.BACK)) {
			game.main.showAdStatic(Constants.CHAPIN);
			game.setScreen(game.menuScreen);
//			this.dispose();
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
		background = MyAssetsManager.getInstance().background3;

		currentPage = 0;
		circles = new Sprite[2]; // 显示当前页的圆圈
		circles[0] = MyAssetsManager.getInstance().circle;
		circles[1] = MyAssetsManager.getInstance().circleSelected;
		circles[0].setSize(Constants.circleWidth, Constants.circleHeight);
		circles[1].setSize(Constants.circleWidth, Constants.circleHeight);

		stage = new Stage(Constants.screenWidth, Constants.screenHeight, true);
		group = new Group();

		levels = new Array<LevelActor>();
		for (int i = 0; i < Constants.LEVEL_ROW; i++) {
			for (int j = 0; j < Constants.LEVEL_COLUMN; j++) {
				tempLevel = new LevelActor(
						(i * Constants.LEVEL_COLUMN + j > currentLevel), i
								* Constants.LEVEL_COLUMN + j + currentPage
								* Constants.LEVEL_COLUMN * Constants.LEVEL_ROW);

				tempLevel.setPosition(Constants.levelX + j
						* Constants.levelAddX, Constants.levelY - i
						* Constants.levelAddY);
				// tempLevel.setMoveInAction(0); // 从右边进去
				levels.add(tempLevel);
				group.addActor(tempLevel);
			}
		}

		stage.addActor(group);
		Gdx.input.setInputProcessor(new GestureDetector(this));
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		stage.dispose();
		batch.dispose();
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
		stage.dispose();
		batch.dispose();
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		// TODO Auto-generated method stub
		System.out.println("x" + x + "y" + y);
		System.out.println("lx" + Constants.levelX + "ly" + Constants.levelY);
		y = Constants.screenHeight - y;
		int column = (int) ((x - Constants.levelX) / Constants.levelAddX);
		int row = (int) ((Constants.levelY + Constants.levelHeight - y) / Constants.levelAddY);
		if (column < 0
				|| row < 0
				|| row >= Constants.LEVEL_ROW
				|| column >= Constants.LEVEL_COLUMN
				|| (x - Constants.levelX - column
						* (Constants.levelHeight + Constants.levelAddX)) > Constants.levelWidth
				|| (Constants.levelY - y - row
						* (Constants.levelWidth + Constants.levelHeight)) > Constants.levelHeight) {
			// 没有点击到关卡
			return false;
		} else {
			int selectedLevel = row * Constants.LEVEL_COLUMN + column + 1;
			if (!levels.get(selectedLevel - 1).isLocked()) { // 如果解鎖,则进行主游戏界面
				GameScreen gameScreen = new GameScreen(game, levels.get(
						selectedLevel - 1).getLevel() + 1, currentType);
				game.setScreen(gameScreen);
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
		System.out.println("fling" + velocityX + "  " + velocityY);
		if (velocityX < 5f) {
			if (currentPage != 3) {
				group.clear();
				for (LevelActor lac : levels) {
					lac.setLevel(lac.getLevel() + Constants.LEVEL_COLUMN
							* Constants.LEVEL_ROW);
					if (lac.getLevel() <= currentLevel) {
						lac.setUnlocked();
					} else {
						lac.setLocked();
					}
					lac.setMoveInAction(0); // 右边进去
					group.addActor(lac);
				}
				++currentPage;
			}
		} else if (velocityX > -5f) {
			if (currentPage != 0) {
				group.clear();
				for (LevelActor lac : levels) {
					lac.setLevel(lac.getLevel() - Constants.LEVEL_COLUMN
							* Constants.LEVEL_ROW);
					if (lac.getLevel() <= currentLevel) {
						lac.setUnlocked();
					} else {
						lac.setLocked();
					}
					lac.setMoveInAction(1); // 左边进去
					group.addActor(lac);
				}
				--currentPage;
			}
		}
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		// TODO Auto-generated method stub
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

}
