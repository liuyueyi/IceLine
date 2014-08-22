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
	public BitmapFont cfont; // ��������

	public Sprite background; // ����
	public Sprite background2; // ����
	public Sprite background3; // ����
	public Sprite menuButtons[]; // �˵����水ť
	public Sprite exitButton;
	public Sprite sprites[]; // title��������������

	// level ����
	public Sprite lockbg; // ���еı���
	public Sprite unlockbg; // δ���еı���
	public Sprite circle; // ԲȦ��������ʾ��ǰ�Ĺؿ���������һ��
	public Sprite circleSelected;

	// game ����
	public Sprite iceCells[]; // ��ŵ����ͼƬ
	public Sprite iceSelected; // ѡ�п��ͼƬ
	public Sprite iceCellbg[]; // ����ı���
	public Sprite iceBg[]; // ���е�������̱���

	public Sprite infoBg;// ʱ������ȱ���
	public Sprite timeSprite; // ʱ��
	public Sprite timebar; // ʱ���
	public Sprite timebarfill; // ʱ������

	public Sprite pauseButtonSprite; // ��ͣ��ť����

	public Sprite toolSprite[]; // 5������
	public TextureAtlas atlas2; // ��ȥ��Ķ���ͼ
	public Sprite bombSprite[]; // ը������ȥ�Ÿ�
	public Sprite bombSprite2[]; // ը������ȥһ��
	public Sprite tenSprite[]; // ʮ����ȥ��
	public Sprite tenSprite2[]; // ʮ����ȥ��
	public Sprite singleSprite[]; // ��ȥ��
	public Sprite singleSprite2[]; // ��ȥ��

	// �����
	public Sprite resultbg;
	public Sprite resultButtons[];
	public Sprite resultInfo[];

	public FileHandle file;

	// music
	public Music bgMusic;
	public Music buttonClickMusic;
	public Music failMusic;
	public Music winMusic;
	public Music panMusic; // ����ʱ����
	public Music removeMusic; // ��ȥiceʱ������
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

		// ���˵�����
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

		// level ����
		lockbg = atlas.createSprite("lockbg");
		unlockbg = atlas.createSprite("unlockbg");
		circle = atlas.createSprite("circle1");
		circleSelected = atlas.createSprite("circle2");

		// game ����
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

		// ����
		toolSprite = new Sprite[5];
		toolSprite[0] = atlas.createSprite("shandian"); // ������
		toolSprite[1] = atlas.createSprite("shandian"); // ������
		toolSprite[2] = atlas.createSprite("shizi"); // ʮ��
		toolSprite[3] = atlas.createSprite("zhadan"); // ը��
		toolSprite[4] = atlas.createSprite("bubble"); // ����
		// ����Ч��
		atlas2 = new TextureAtlas(Gdx.files.internal("gfx/effect/effect.pack"));
		bombSprite = new Sprite[5]; // ը��ȥ��9��
		bombSprite[0] = atlas2.createSprite("ebomb1");
		bombSprite[1] = atlas2.createSprite("ebomb2");
		bombSprite[2] = atlas2.createSprite("ebomb3");
		bombSprite[3] = atlas2.createSprite("ebomb4");
		bombSprite[4] = atlas2.createSprite("ebomb5");
		bombSprite2 = new Sprite[5]; // ը��ȥ��1��
		bombSprite2[0] = atlas2.createSprite("bomb1");
		bombSprite2[1] = atlas2.createSprite("bomb2");
		bombSprite2[2] = atlas2.createSprite("bomb3");
		bombSprite2[3] = atlas2.createSprite("bomb4");
		bombSprite2[4] = atlas2.createSprite("bomb5");

		tenSprite = new Sprite[5]; // ʮ����
		tenSprite[0] = atlas2.createSprite("laserw1");
		tenSprite[1] = atlas2.createSprite("laserw2");
		tenSprite[2] = atlas2.createSprite("laserw3");
		tenSprite[3] = atlas2.createSprite("laserw4");
		tenSprite[4] = atlas2.createSprite("laserw5");
		tenSprite2 = new Sprite[5]; // ʮ����
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

		// �����
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
		file.writeString(temp, false); // ����д
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
