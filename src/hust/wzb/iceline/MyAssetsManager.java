package hust.wzb.iceline;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class MyAssetsManager {
	private static MyAssetsManager instance;

	private TextureAtlas atlas;
	public BitmapFont font;
	public BitmapFont cfont; // 中文字体

	public Sprite background; // 背景
	public Sprite background2; // 背景
	public Sprite background3; // 背景
	public Sprite menuButtons[]; // 菜单界面按钮
	public Sprite exitButton;
	public Sprite sprites[]; // title及几个动画人物

	// level 界面
	public Sprite lockbg; // 锁中的背景
	public Sprite unlockbg; // 未锁中的背景
	public Sprite circle; // 圆圈，用于显示当前的关卡界面是哪一面
	public Sprite circleSelected;

	// game 界面
	public Sprite iceCells[]; // 存放蛋糕的图片
	public Sprite iceSelected; // 选中框的图片
	public Sprite iceCellbg[]; // 蛋糕的背景
	public Sprite iceBg[]; // 所有蛋糕的托盘背景

	public Sprite infoBg;// 时间分数等背景
	public Sprite timeSprite; // 时间
	public Sprite timebar; // 时间框
	public Sprite timebarfill; // 时间填充框

	public Sprite pauseButtonSprite; // 暂停按钮背景

	public Sprite toolSprite[]; // 5个道具
	public TextureAtlas atlas2; // 消去后的动画图
	public Sprite bombSprite[]; // 炸弹，消去九个
	public Sprite bombSprite2[]; // 炸弹，消去一个
	public Sprite tenSprite[]; // 十字消去行
	public Sprite tenSprite2[]; // 十字消去列
	public Sprite singleSprite[]; // 消去行
	public Sprite singleSprite2[]; // 消去列

	// 结果框
	public Sprite resultbg;
	public Sprite resultButtons[];
	public Sprite resultInfo[];

	public FileHandle file;

	// music
	public Music bgMusic;
	public Music buttonClickMusic;
	public Music failMusic;
	public Music winMusic;
	public Music panMusic; // 滑动时音乐
	public Music removeMusic; // 消去ice时的音乐
	public Music shiziMusic;
	public Music bombMusic;
	public Music shandianMusic;

	public BitmapFont font2;
	public LabelStyle style;

	private MyAssetsManager() {

	}

	public static MyAssetsManager getInstance() {
		if (instance == null) {
			instance = new MyAssetsManager();
			return instance;
		}
		return instance;
	}

	public void loadTexture() {
		Texture fonTexture = new Texture(Gdx.files.internal("font/font.png"));
		fonTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		font = new BitmapFont(Gdx.files.internal("font/font.fnt"),
				new TextureRegion(fonTexture), false);
		font.setScale(Constants.wrate);
		
		Texture fonTexture2 = new Texture(Gdx.files.internal("font/chinese.png"));
		fonTexture2.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		cfont = new BitmapFont(Gdx.files.internal("font/chinese.fnt"),
				new TextureRegion(fonTexture), false);
		//cfont.setScale(Constants.wrate);

		font2 = new BitmapFont();
		font2.setScale(Constants.screenHeight / 480f);
		style = new LabelStyle(font2, Color.WHITE);

		atlas = new TextureAtlas(Gdx.files.internal("gfx/main/ice.pack"));

		// 主菜单界面
		background = atlas.createSprite("bg1");
		background2 = atlas.createSprite("bg2");
		background3 = atlas.createSprite("bg3");
		menuButtons = new Sprite[10];
		menuButtons[0] = atlas.createSprite("btn_easy");
		menuButtons[1] = atlas.createSprite("btn_easy_click");
		menuButtons[2] = atlas.createSprite("btn_normal");
		menuButtons[3] = atlas.createSprite("btn_normal_click");
		menuButtons[4] = atlas.createSprite("btn_hard");
		menuButtons[5] = atlas.createSprite("btn_hard_click");
		menuButtons[6] = atlas.createSprite("button_sound_on");
		menuButtons[7] = atlas.createSprite("button_sound_off");
		exitButton = atlas.createSprite("exit");

		sprites = new Sprite[5];
		sprites[0] = atlas.createSprite("bingqilin1");
		sprites[1] = atlas.createSprite("bingqilin2");
		sprites[2] = atlas.createSprite("bingqilin3");
		sprites[3] = atlas.createSprite("bingqilin4");
		sprites[4] = atlas.createSprite("title");

		// level 界面
		lockbg = atlas.createSprite("lockbg");
		unlockbg = atlas.createSprite("unlockbg");
		circle = atlas.createSprite("circle1");
		circleSelected = atlas.createSprite("circle2");

		// game 界面
		iceBg = new Sprite[3];
		iceBg[0] = atlas.createSprite("cell1");
		iceBg[1] = atlas.createSprite("cell2");
		iceBg[2] = atlas.createSprite("iceBg");
		iceCellbg = new Sprite[3];
		iceCellbg[0] = atlas.createSprite("cell_bg1");
		iceCellbg[1] = atlas.createSprite("cell_bg2");
		iceCellbg[2] = atlas.createSprite("cell_bg3");
		iceCells = new Sprite[8];
		iceCells[0] = atlas.createSprite("1");
		iceCells[1] = atlas.createSprite("2");
		iceCells[2] = atlas.createSprite("3");
		iceCells[3] = atlas.createSprite("4");
		iceCells[4] = atlas.createSprite("5");
		iceCells[5] = atlas.createSprite("6");
		iceCells[6] = atlas.createSprite("7");
		iceCells[7] = atlas.createSprite("8");
		iceSelected = atlas.createSprite("selected");

		infoBg = atlas.createSprite("infoBg");
		timeSprite = atlas.createSprite("time");
		timebar = atlas.createSprite("bonusbar");
		timebarfill = atlas.createSprite("bonusbar_fill");

		pauseButtonSprite = atlas.createSprite("pauseButton");

		// 道具
		toolSprite = new Sprite[5];
		toolSprite[0] = atlas.createSprite("shandian"); // 横闪电
		toolSprite[1] = atlas.createSprite("shandian"); // 竖闪电
		toolSprite[2] = atlas.createSprite("shizi"); // 十字
		toolSprite[3] = atlas.createSprite("zhadan"); // 炸弹
		toolSprite[4] = atlas.createSprite("bubble"); // 泡泡
		// 道具效果
		atlas2 = new TextureAtlas(Gdx.files.internal("gfx/effect/effect.pack"));
		bombSprite = new Sprite[5]; // 炸弹去掉9个
		bombSprite[0] = atlas2.createSprite("ebomb1");
		bombSprite[1] = atlas2.createSprite("ebomb2");
		bombSprite[2] = atlas2.createSprite("ebomb3");
		bombSprite[3] = atlas2.createSprite("ebomb4");
		bombSprite[4] = atlas2.createSprite("ebomb5");
		bombSprite2 = new Sprite[5]; // 炸弹去掉1个
		bombSprite2[0] = atlas2.createSprite("bomb1");
		bombSprite2[1] = atlas2.createSprite("bomb2");
		bombSprite2[2] = atlas2.createSprite("bomb3");
		bombSprite2[3] = atlas2.createSprite("bomb4");
		bombSprite2[4] = atlas2.createSprite("bomb5");

		tenSprite = new Sprite[5]; // 十字行
		tenSprite[0] = atlas2.createSprite("laserw1");
		tenSprite[1] = atlas2.createSprite("laserw2");
		tenSprite[2] = atlas2.createSprite("laserw3");
		tenSprite[3] = atlas2.createSprite("laserw4");
		tenSprite[4] = atlas2.createSprite("laserw5");
		tenSprite2 = new Sprite[5]; // 十字列
		tenSprite2[0] = atlas2.createSprite("laserh1");
		tenSprite2[1] = atlas2.createSprite("laserh2");
		tenSprite2[2] = atlas2.createSprite("laserh3");
		tenSprite2[3] = atlas2.createSprite("laserh4");
		tenSprite2[4] = atlas2.createSprite("laserh5");

		singleSprite = new Sprite[5];
		singleSprite[0] = atlas2.createSprite("wline1");
		singleSprite[1] = atlas2.createSprite("wline2");
		singleSprite[2] = atlas2.createSprite("wline3");
		singleSprite[3] = atlas2.createSprite("wline4");
		singleSprite[4] = atlas2.createSprite("wline5");
		singleSprite2 = new Sprite[5];
		singleSprite2[0] = atlas2.createSprite("hline1");
		singleSprite2[1] = atlas2.createSprite("hline2");
		singleSprite2[2] = atlas2.createSprite("hline3");
		singleSprite2[3] = atlas2.createSprite("hline4");
		singleSprite2[4] = atlas2.createSprite("hline5");

		// 结果框
		resultbg = atlas.createSprite("result");
		resultButtons = new Sprite[6];
		resultButtons[0] = atlas.createSprite("btn_continue");
		resultButtons[1] = atlas.createSprite("btn_continue_click");
		resultButtons[2] = atlas.createSprite("btn_retry");
		resultButtons[3] = atlas.createSprite("btn_retry_click");
		resultButtons[4] = atlas.createSprite("btn_menu");
		resultButtons[5] = atlas.createSprite("btn_menu_click");

		file = Gdx.files.local("data/level");
		if (file.exists()) {
			System.out.println("here!");
			readLevel();
		} else {
			writeLevel();
			System.out.println("bucunzai");
		}
	}

	public void readLevel() {
		String levels = file.readString();
		String level[] = levels.split("\\$");
		int i = 0;
		for (String l : level) {
			Constants.currentLevel[i++] = Integer.parseInt(l);
		}
	}

	public void writeLevel() {
		String temp = Constants.currentLevel[0] + "$"
				+ Constants.currentLevel[1] + "$" + Constants.currentLevel[2];
		file.writeString(temp, false); // 覆盖写
	}

	public void loadMusic() {
		bgMusic = Gdx.audio.newMusic(Gdx.files.internal("mfx/bgm_game.ogg"));
		buttonClickMusic = Gdx.audio.newMusic(Gdx.files
				.internal("mfx/button_clicked.ogg"));
		failMusic = Gdx.audio.newMusic(Gdx.files.internal("mfx/fail.ogg"));
		winMusic = Gdx.audio.newMusic(Gdx.files.internal("mfx/win.ogg"));
		panMusic = Gdx.audio.newMusic(Gdx.files.internal("mfx/change.ogg"));
		removeMusic = Gdx.audio.newMusic(Gdx.files
				.internal("mfx/removedefault.ogg"));
		shiziMusic = Gdx.audio.newMusic(Gdx.files.internal("mfx/shizhi.ogg"));
		shandianMusic = Gdx.audio.newMusic(Gdx.files
				.internal("mfx/shandian.ogg"));
		bombMusic = Gdx.audio.newMusic(Gdx.files.internal("mfx/bomb.ogg"));
	}

	public void clear() {
		font.dispose();
		cfont.dispose();
		font2.dispose();
		atlas.dispose();
		atlas2.dispose();

		bgMusic.dispose();
		buttonClickMusic.dispose();
		failMusic.dispose();
		winMusic.dispose();
		panMusic.dispose();
		removeMusic.dispose();
		shiziMusic.dispose();
		shandianMusic.dispose();
		bombMusic.dispose();
	}
}
