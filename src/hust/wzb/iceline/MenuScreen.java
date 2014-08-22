package hust.wzb.iceline;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class MenuScreen implements Screen {
	private MyGdxGame game;

	private SpriteBatch spriteBatch;
	private Sprite backgroud;
	private Image title;
	private Image decorate;
	private Image decorate2;

	private Stage stage;
	private Button easyButton;
	private Button normalButton;
	private Button hardButton;

	private Image musicButton;
	private Image exitButton;

	public MenuScreen(MyGdxGame game) {
		this.game = game;
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(1, 1, 1, 1);// 设置背景为白色
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);// 清屏

		spriteBatch.begin();
		backgroud.draw(spriteBatch);
		spriteBatch.end();

		stage.draw();
		stage.act();

		// if (Gdx.input.isKeyPressed(Input.Keys.A)
		// || Gdx.input.isKeyPressed(Input.Keys.BACK)) {
		// // System.out.println("exit");
		// }
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		init();

		decorate = new Image(MyAssetsManager.getInstance().sprites[0]);
		decorate.setBounds(Constants.decorateX + Constants.screenWidth,
				Constants.decorateY, Constants.decorateWidth,
				Constants.decorateHeight);
		Action a1 = Actions.moveTo(Constants.decorateX, Constants.decorateY,
				Gdx.graphics.getDeltaTime() * 10f);
		Action a2 = Actions.fadeIn(Gdx.graphics.getDeltaTime() * 10f);
		Action a3 = Actions.parallel(a1, a2);
		decorate.addAction(a3);

		decorate2 = new Image(MyAssetsManager.getInstance().sprites[1]);
		decorate2.setBounds(
				Constants.screenWidth * 2 - Constants.decorateWidth,
				Constants.decorateY, Constants.decorateWidth,
				Constants.decorateHeight);
		Action a4 = Actions.moveTo(Constants.screenWidth
				- Constants.decorateWidth, Constants.decorateY,
				Gdx.graphics.getDeltaTime() * 10f);
		Action a5 = Actions.fadeIn(Gdx.graphics.getDeltaTime() * 10f);
		Action a6 = Actions.parallel(a4, a5);
		decorate2.addAction(a6);

		title = new Image(MyAssetsManager.getInstance().sprites[4]);
		title.setBounds(Constants.titleX, Constants.screenHeight,
				Constants.titleWidth, Constants.titleHeight);
		Action a7 = Actions.moveTo(Constants.titleX, Constants.screenHeight,
				Gdx.graphics.getDeltaTime() * 10);
		Action a8 = Actions.moveTo(Constants.titleX, Constants.titleY,
				Gdx.graphics.getDeltaTime() * 10);
		Action a9 = Actions.sequence(a7, a8);
		title.addAction(a9);

		stage.addActor(decorate);
		stage.addActor(decorate2);
		stage.addActor(title);

		MyAssetsManager.getInstance().bgMusic.setLooping(true);
		MyAssetsManager.getInstance().bgMusic.play();
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		spriteBatch.dispose();
		stage.dispose();
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
		spriteBatch.dispose();
		stage.dispose();
	}

	public void init() {
		spriteBatch = new SpriteBatch();
		backgroud = MyAssetsManager.getInstance().background3;
		backgroud
				.setBounds(0, 0, Constants.screenWidth, Constants.screenHeight);

		stage = new Stage(Constants.screenWidth, Constants.screenHeight, true);
		Gdx.input.setInputProcessor(stage);

		final TextureRegionDrawable[] trd = new TextureRegionDrawable[8];
		for (int i = 0; i < 8; i++) {
			trd[i] = new TextureRegionDrawable(
					MyAssetsManager.getInstance().menuButtons[i]);
		}

		easyButton = new ImageButton(trd[0], trd[1]);
		easyButton.setSize(Constants.menuButtonWidth,
				Constants.menuButtonHeight);
		easyButton.setPosition(Constants.menuButtonX, Constants.easyButtonY);
		easyButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (Constants.isMusic) {
					MyAssetsManager.getInstance().buttonClickMusic.play();
				}
				// game.levelScreen.setType(0);
				// game.setScreen(game.levelScreen);
				game.setScreen(new LevelScreen(game, 0));
			}
		});

		normalButton = new ImageButton(trd[2], trd[3]);
		normalButton.setSize(Constants.menuButtonWidth,
				Constants.menuButtonHeight);
		normalButton
				.setPosition(Constants.menuButtonX, Constants.normalButtonY);
		normalButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (Constants.isMusic) {
					MyAssetsManager.getInstance().buttonClickMusic.play();
				}
				// game.levelScreen.setType(1);
				// game.setScreen(game.levelScreen);
				game.setScreen(new LevelScreen(game, 1));
			}
		});

		hardButton = new ImageButton(trd[4], trd[5]);
		hardButton.setSize(Constants.menuButtonWidth,
				Constants.menuButtonHeight);
		hardButton.setPosition(Constants.menuButtonX, Constants.hardButtonY);
		hardButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (Constants.isMusic) {
					MyAssetsManager.getInstance().buttonClickMusic.play();
				}
				// game.levelScreen.setType(2);
				// game.setScreen(game.levelScreen);
//				game.setScreen(new LevelScreen(game, 2));
				game.main.showAdStatic(Constants.MORE);
			}
		});

		exitButton = new Image(MyAssetsManager.getInstance().exitButton);
		exitButton.setBounds(Constants.exitButtonX, Constants.exitButtonY,
				Constants.exitButtonWidth, Constants.exitButtonHeight);
		exitButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (Constants.isMusic) {
					MyAssetsManager.getInstance().buttonClickMusic.play();
				}
				
				game.main.showAdStatic(Constants.CHAPIN);
				game.main.showAdStatic(Constants.EXIT);
			}
		});

		musicButton = new Image(trd[6]);
		musicButton.setBounds(Constants.musicButtonX, Constants.musicButtonY,
				Constants.musicButtonWidth, Constants.musicButtonHeight);
		musicButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (Constants.isMusic) {
					// MyAssetsManager.getInstance().buttonClickMusic.play();
					musicButton.setDrawable(trd[7]);
					Constants.isMusic = false;
					MyAssetsManager.getInstance().bgMusic.stop();
				} else {
					Constants.isMusic = true;
					musicButton.setDrawable(trd[6]);
					MyAssetsManager.getInstance().bgMusic.play();
				}
			}
		});

		stage.addActor(easyButton);
		stage.addActor(normalButton);
		stage.addActor(hardButton);
		stage.addActor(exitButton);
		stage.addActor(musicButton);
		Gdx.input.setInputProcessor(stage);
	}
}
