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
		case 0: // ������ȥ
			keyFrames = MyAssetsManager.getInstance().singleSprite;
			break;
		case 1: // ������ȥ
			keyFrames = MyAssetsManager.getInstance().singleSprite2;
			break;
		case 2: // ʮ����ȥ����
			keyFrames = MyAssetsManager.getInstance().tenSprite;
			break;
		case 3: // ʮ����ȥ����
			keyFrames = MyAssetsManager.getInstance().tenSprite2;
			break;
		case 4: // ը��
			keyFrames = MyAssetsManager.getInstance().bombSprite;
			break;
		}
		mAnimation = new Animation((float) 0.2, keyFrames);
	}

	public void setType(int type) {
		switch (type) {
		case 0: // ������ȥ
			keyFrames = MyAssetsManager.getInstance().singleSprite;
			break;
		case 1: // ������ȥ
			keyFrames = MyAssetsManager.getInstance().singleSprite2;
			break;
		case 2: // ʮ����ȥ����
			keyFrames = MyAssetsManager.getInstance().tenSprite;
			break;
		case 3: // ʮ����ȥ����
			keyFrames = MyAssetsManager.getInstance().tenSprite2;
			break;
		case 4: // ը��
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
		// ����Ҫע�⣬������ӵ�actionֻ�Ǹı�actor������ֵ�����Ƶ�ʱ��û��
		// �Զ������Ǵ�����Щ�߼��� ����Ҫ���ľ���ȡ����Щֵ��Ȼ���Լ�����
		batch.draw(textureRegion, getX(), getY(), getWidth(), getHeight());

		if (stateTime > Gdx.graphics.getDeltaTime() * Constants.actionTime) { // �����������,ȥ��
			this.remove();
		}
	}
}
