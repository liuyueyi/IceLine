package hust.wzb.iceline;

import com.badlogic.gdx.Gdx;

public class Constants {
	public static float screenWidth = Gdx.graphics.getWidth();
	public static float screenHeight = Gdx.graphics.getHeight();
	public static float hrate = Gdx.graphics.getHeight() / 800.0f;
	public static float wrate = Gdx.graphics.getWidth() / 480.0f;

	// 菜单界面
	public final static float menuButtonWidth = screenWidth / 3f;
	public final static float menuButtonHeight = menuButtonWidth / 2f;
	public static float menuButtonX = menuButtonWidth;
	public static float normalButtonY = screenHeight * 0.5f - menuButtonHeight;
	public static float hardButtonY = normalButtonY - 1.5f * menuButtonHeight;
	public static float easyButtonY = normalButtonY + 1.5f * menuButtonHeight;

	public static float musicButtonWidth = 60 * wrate;
	public static float musicButtonHeight = 60 * hrate;
	public static float musicButtonX = menuButtonX - 2 * musicButtonWidth;
	public static float musicButtonY = hardButtonY - 1.5f * musicButtonHeight;
	public static boolean isMusic = true;
	
	public static float exitButtonWidth = musicButtonWidth;
	public static float exitButtonHeight = exitButtonWidth;
	public static float exitButtonX = menuButtonX + menuButtonWidth + exitButtonWidth;
	public static float exitButtonY = musicButtonY;
	
	public static float titleWidth = screenWidth * 0.67f;
	public static float titleHeight = titleWidth * 0.35f;
	public static float titleX = (screenWidth - titleWidth) / 2f;
	public static float titleY = easyButtonY + 1.5f * menuButtonHeight;
	
	public static float decorateHeight = titleX + titleX;
	public static float decorateWidth = decorateHeight * 0.67f;
	public static float decorateX = 0f ;
	public static float decorateY = titleY - decorateHeight * 0.25f;

	public final static float baseY = 130 * hrate; // 为广告预留的空地

	// 关卡界面参数
	public final static int LEVEL_ROW = 7;
	public final static int LEVEL_COLUMN = 6;

	public static int currentLevel[] = { 0, 0, 0 }; // 存储不同难度的当前关卡,下标从0开始
	public static float levelWidth = screenWidth / (LEVEL_COLUMN + 2);
	public static float levelHeight = levelWidth;
	public static float levelX = (levelWidth + levelWidth)
			/ (LEVEL_COLUMN + 1f); // 起始坐标 levelx
	public static float levelAddX = levelX + levelWidth;
	public static float levelAddY = (screenHeight - baseY * 2 - LEVEL_ROW
			* levelHeight)
			/ (LEVEL_ROW + 1f) + levelHeight;
	public static float levelY = screenHeight - baseY - levelAddY; // 起始坐标
																	// levely，在左上角

	public final static int circleNum = 4; // 圆框的数目
	public static float circleWidth = levelWidth / 2.0f;
	public static float circleHeight = levelHeight / 2.0f;
	public static float circleAddX = circleWidth / 2f;
	public static float circleX = (screenWidth - circleWidth
			* (circleNum * 2 - 1))
			/ 2f + circleAddX;
	public static float circleY = levelY + (levelAddY + circleAddX);

	// 游戏界面参数
	public static int cellNum[] = {6, 6, 6}; // 当前显示在界面的蛋糕数目
	public static int cellMaxNum = 8; // 蛋糕最多只能有8个
	// 存放所有蛋糕的背景宽、高，及坐标
	public static float iceBgAdd = 40 * hrate;
	public static float iceBgHeight = screenWidth - 40 * wrate;
	public static float iceBgWidth = iceBgHeight;
	public static float iceBgX = (screenWidth - iceBgWidth) / 2f;
	public static float iceBgY = baseY;
	// 蛋糕元素的基本信息
	public static int cellRow[] = { 5, 6, 7 }; // 蛋糕行数
	public static int cellColumn[] = { 5, 6, 7 }; // 蛋糕列数
	public static float cellWidth[] = { (iceBgWidth - iceBgAdd) / cellRow[0],
			(iceBgWidth - iceBgAdd) / cellRow[1],
			(iceBgWidth - iceBgAdd) / cellRow[2] };
	public static float cellHeight[] = cellWidth;
	public static float cellX = iceBgX + iceBgAdd / 2f;
	public static float cellY = iceBgY + iceBgAdd / 2f;

	// time 条目的信息	
	public static float timeSpriteWidth = 40 * wrate;
	public static float timeSpriteHeight = 40 * wrate; // time图标的宽高
	public static float timeSpriteX = cellX;
	public static float timeSpriteY = iceBgY + iceBgHeight + 10 * hrate; // time图标的坐标
	public static float timeBarWidth = iceBgWidth - timeSpriteWidth - iceBgAdd;
	public static float timeBarHeight = timeSpriteHeight / 3f; // time bar的宽高
	public static float timeBarX = timeSpriteX + timeSpriteWidth;
	public static float timeBarY = timeSpriteY + timeSpriteHeight / 3f; // timebar 坐标
	
	public static int totalTime = 400;
	public static int addTime = 3;

	// target、score、pause button的信息
	public static int target = 1520;
	public static int addTarget = 90;
	public static float targetSpriteWidth = 100 * wrate + 10f;
	public static float targetSpriteHeight = 40 * hrate;
	public static float targetSpriteX = timeSpriteX;
	public static float targetSpriteY = timeSpriteY + timeSpriteHeight;
	public static float scoreSpriteWidth = 100 * wrate;
	public static float scoreSpriteHeight = 40 * hrate;
	public static float scoreSpriteX = targetSpriteX + targetSpriteWidth * 2f;
	public static float scoreSpriteY = targetSpriteY;
	
	public static float pauseButtonWidth = 40 * wrate;
	public static float pauseButtonHeight = pauseButtonWidth;
	public static float pauseButtonX = iceBgX + iceBgWidth - pauseButtonWidth;
	public static float pauseButtonY = scoreSpriteY;
	
	// 分数、时间的背景框
	public static float infoBackWidth = iceBgWidth;
	public static float infoBackHeight = targetSpriteHeight;
	public static float infoBackX = iceBgX;
	public static float infoBackY = targetSpriteY;
	
	// other
	public static int actionTime = 12;

	// result dialog
	public static float resultWidth = 0.8f * screenWidth;
	public static float resultHeight = resultWidth;
	public static float resultX = 0.1f * screenWidth;
	public static float resultY = (screenHeight - resultHeight) / 2;
	
	public static float buttonWidth = resultWidth * 0.2f;
	public static float buttonHeight = buttonWidth * 0.5f;
	public static float buttonX = resultX + resultWidth * 0.1f;
	public static float buttonY = resultY + buttonHeight * 2;
	public static float buttonAddX = buttonWidth + resultWidth * 0.1f;
	
	public static float infoLabelWidth = resultWidth;
	public static float infoLabelHeight = resultHeight * 0.5f;
	public static float infoLabelX = resultX;
	public static float infoLabelY = buttonY;
	

	// 标记
	public static final int CHAPIN = 0;
	public static final int MORE = 1;
	public static final int EXIT = 2;
	
}

