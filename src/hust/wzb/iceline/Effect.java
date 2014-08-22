package hust.wzb.iceline;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Effect extends Actor {
	private Animation mAnimation;
	private TextureRegion[] keyFrames;
	private float stateTime = 0f;

	public Effect() {

	}

	public Effect(int type) {
		switch (type) {
		case 0: // 横向消去
			keyFrames = MyAssetsManager.getInstance().singleSprite;
			break;
		case 1: // 竖向消去
			keyFrames = MyAssetsManager.getInstance().singleSprite2;
			break;
		case 2: // 十字消去横向
			keyFrames = MyAssetsManager.getInstance().tenSprite;
			break;
		case 3: // 十字消去竖向
			keyFrames = MyAssetsManager.getInstance().tenSprite2;
			break;
		case 4: // 炸弹
			keyFrames = MyAssetsManager.getInstance().bombSprite;
			break;
		}
		mAnimation = new Animation((float) 0.2, keyFrames);
	}

	public void setType(int type) {
		switch (type) {
		case 0: // 横向消去
			keyFrames = MyAssetsManager.getInstance().singleSprite;
			break;
		case 1: // 竖向消去
			keyFrames = MyAssetsManager.getInstance().singleSprite2;
			break;
		case 2: // 十字消去横向
			keyFrames = MyAssetsManager.getInstance().tenSprite;
			break;
		case 3: // 十字消去竖向
			keyFrames = MyAssetsManager.getInstance().tenSprite2;
			break;
		case 4: // 炸弹
			keyFrames = MyAssetsManager.getInstance().bombSprite;
			break;
		}
		mAnimation = new Animation((float) 0.2, keyFrames);
	}

	@Override
	public void setColor(Color color) {
		super.setColor(color);
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		stateTime += Gdx.graphics.getDeltaTime();

		TextureRegion textureRegion = mAnimation.getKeyFrame(stateTime, true);
		// 这里要注意，我们添加的action只是改变actor的属性值，绘制的时候并没有
		// 自动给我们处理这些逻辑， 我们要做的就是取得这些值，然后自己处理
		batch.draw(textureRegion, getX(), getY(), getWidth(), getHeight());

		if (stateTime > Gdx.graphics.getDeltaTime() * Constants.actionTime) { // 动画播放完毕,去掉
			this.remove();
		}
	}
}
