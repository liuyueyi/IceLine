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
	private Sprite sprite; // ��Ԫ�񱳾�
	private Sprite effectSprite; // ����ʮ�֡�ը�������ݵ�ͼƬ
	private int value; // �洢��ֵ
	private boolean selected; // true ��ʾѡ�У� false��ʾ δѡ��
	private int type = -1;

	public int addScore = 0; // ��ʾ����ȥ�����Ƿ�ӷ�,��ʾ�ӷ�

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
	 * ��ӱ�ʾը���ȵ�Сͼ��
	 * 
	 * @param type
	 *            : 0 �������� 1 �������� 2 ʮ�� 3 ը�� 4 ���� -1 ʱ��ʾ����Ĭ����ȥ��������ը��; -2
	 *            �����ö�������ʾ������or��ͳһ��ȥ�� -3 ��ʾ������ȥ�� -4��ʾ������ȥ�� -5 ������ȥ�����ڵ��У�
	 *            -6������ȥ�����ڵ���
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
		// move to ��tox, toy) �����ƶ�����
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
