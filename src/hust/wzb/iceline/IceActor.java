package hust.wzb.iceline;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;

public class IceActor extends Actor {
	private Sprite sprite; // 单元格背景
	private Sprite effectSprite; // 保存十字、炸弹、泡泡等图片
	private int value; // 存储的值
	private boolean selected; // true 表示选中， false表示 未选中
	private int type = -1;

	public int addScore = 0; // 表示该消去蛋糕是否加分,表示加分

	public IceActor(Sprite sprite, int value, int type) {
		this.sprite = sprite;
		this.value = value;
		// this.type = type;

		float x = 0;
		if (value % 2 == 0)
			x = -Constants.cellWidth[0];
		else
			x = Constants.cellWidth[0] + Constants.screenWidth;
		this.setPosition(x, Constants.screenHeight / 2);

	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * 添加表示炸弹等的小图标
	 * 
	 * @param type
	 *            : 0 横向闪电 1 竖向闪电 2 十字 3 炸弹 4 泡泡 -1 时表示采用默认消去动画，即炸弹; -2
	 *            不采用动画，表示所在行or列统一消去； -3 表示单行消去； -4表示单列消去； -5 行列消去中所在的行，
	 *            -6行列消去中所在的列
	 */
	public void setEffect(int type) {
		this.type = type;
		if (type >= 0 && type != 4) {
			effectSprite = MyAssetsManager.getInstance().toolSprite[type];
			effectSprite.setSize(getWidth() / 3f, getHeight() / 3f);
			effectSprite.setPosition(getX() + getWidth() * 0.66f, getY());
		}
	}

	public int getEffectType() {
		return type;
	}

	public void setExChangeAction(float toX, float toY) {
		// move to （tox, toy) 后再移动回来
		MoveToAction action = Actions.moveTo(toX, toY,
				Gdx.graphics.getDeltaTime() * Constants.actionTime);
		MoveToAction action2 = Actions.moveTo(getX(), getY(),
				Gdx.graphics.getDeltaTime() * (Constants.actionTime));
		this.addAction(Actions.sequence(action, action2));
	}

	public void setChangeAction(float toX, float toY) {
		MoveToAction action = Actions.moveTo(toX, toY,
				Gdx.graphics.getDeltaTime() * Constants.actionTime);
		this.addAction(action);
	}
	
	public void setMoveDownAction(float toX, float toY){
		MoveToAction action1 = Actions.moveTo(getX(), getY(),
				Gdx.graphics.getDeltaTime() * Constants.actionTime );
		MoveToAction action2 = Actions.moveTo(toX, toY,
				Gdx.graphics.getDeltaTime() * Constants.actionTime);
		this.addAction(Actions.sequence(action1, action2));
	}

	public void setMoveInAction(float toX, float toY) {
		MoveToAction action1 = Actions.moveTo(getX(), getY(),
				Gdx.graphics.getDeltaTime() * Constants.actionTime );
		MoveToAction action2 = Actions.moveTo(toX, toY,
				Gdx.graphics.getDeltaTime() * Constants.actionTime);
		AlphaAction faction = Actions.fadeIn(Gdx.graphics.getDeltaTime()
				* Constants.actionTime);
		ParallelAction paction = Actions.sequence(action1,
				Actions.parallel(action2, faction));

		this.addAction(paction);
	}

	public void setMoveOutAction() {
		AlphaAction faction = Actions.fadeOut(Gdx.graphics.getDeltaTime()
				* Constants.actionTime);
		this.addAction(faction);
	}

	@Override
	public void setPosition(float x, float y) {
		sprite.setPosition(x, y);
		if (type >= 0 && type!= 4)
			effectSprite.setPosition(x + getWidth() * 0.66f, y);
		super.setPosition(x, y);
	}

	@Override
	public void setSize(float width, float height) {
		sprite.setSize(width, height);
		if (type >= 0 && type!= 4) {
			effectSprite.setSize(width * 0.33f, height * 0.33f);
		}
		super.setSize(width, height);
	}

	@Override
	public void setBounds(float x, float y, float width, float height) {
		sprite.setBounds(x + 5, y + 5, width - 10, height - 10);
		if (type >= 0 && type!= 4) {
			effectSprite.setBounds(x + 5 + getWidth() * 0.66f, y + 5,
					(width - 10) * 0.33f, (height - 10) * 0.33f);
		}
		super.setBounds(x, y, width, height);
	}

	@Override
	public void setColor(Color color) {
		sprite.setColor(color);
		if (type >= 0 && type!= 4) {
			effectSprite.setColor(color);
		}
		super.setColor(color);
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		setBounds(getX(), getY(), getWidth(), getHeight());
		setRotation(getRotation());
		setColor(getColor());
		sprite.draw(batch);
		if (type >= 0 && type!= 4) {
			effectSprite.draw(batch);
		}
		super.draw(batch, parentAlpha);
	}
}
