package hust.wzb.iceline;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

public class LevelActor extends Actor{
	private Sprite bg; // �ؿ�����ͼ���ֱ�Ϊ��ס״̬�ͽ���״̬
	private Label label; // ������ʾ��ǰ�Ĺؿ���
	private int level;	// ��ǰ�Ĺؿ�����0��ʼ����ʾʱ��Ϊlevel+1
	private boolean locked; // �ж��Ƿ���ס
	
	/**
	 * 
	 * @param locked: ��ʾ�Ƿ���ס
	 * @param level:  ��ʾ��ǰ�Ĺؿ�
	 */
	public LevelActor(boolean locked, int level){		
		this.locked = locked;
		if(locked){
			bg = MyAssetsManager.getInstance().lockbg;
		} else {
			bg = MyAssetsManager.getInstance().unlockbg;
		}
		bg.setSize(Constants.levelWidth, Constants.levelHeight);
		
		this.level = level;
		label = new Label((level+1) + "", MyAssetsManager.getInstance().style);
		label.setSize(Constants.levelWidth, Constants.levelHeight); 
		label.setAlignment(Align.center); // ������ʾ
		
		setSize(Constants.levelWidth, Constants.levelHeight);
	}
	
	public void setLevel(int level){
		this.level = level;
		label.setText("" + (level +1));
	}
	
		
	public int getLevel(){
		return level;
	}
	
	/**
	 * �ж��Ƿ���ס
	 * @return true ��ס�� false δ��
	 */
	public boolean isLocked(){
		return locked;
	}
	
	/**
	 * ����
	 */
	public void setUnlocked(){ // ����
		locked = false;
		bg = MyAssetsManager.getInstance().unlockbg;
	}
	
	/**
	 * ���i
	 */
	public void setLocked(){
		locked = true;
		bg = MyAssetsManager.getInstance().lockbg;
	}
	
	/**
	 * type: 1 ��߳�ȥ�� 0 ��ʾ�ұ߳�ȥ
	 *  ��ȥ�Ķ���, type ��ʾ���������ҳ�ȥ
	 */
	public void setMoveOutAction(int type){
		float addx = Constants.screenWidth;
		if(type == 1) addx = - addx; // ��߳�ȥ 
		Action action1 = Actions.moveTo(addx + getX(),  getY(),Gdx.graphics.getDeltaTime() * 30);
		Action action2 = Actions.fadeOut(Gdx.graphics.getDeltaTime() * 30);
		Action action3 = Actions.parallel(action1, action2);
		
		this.addAction(action3);
	}
	
	/**
	 * ��ȥ������ע����붯���ͳ�ȥ����Ч�����෴��
	 * @param type 0 ��ʾ�ұ߽�ȥ�� 1��ʾ��߽�ȥ
	 */
	public void setMoveInAction(int type){
		float addx = Constants.screenWidth; 
		if(type == 1) addx = - Constants.screenWidth;
		this.setPosition(addx + getX(), getY());
		
		Action action1 = Actions.moveTo(getX() - addx,  getY(), Gdx.graphics.getDeltaTime() * 30);
		Action action2 = Actions.fadeIn(Gdx.graphics.getDeltaTime() * 30);
		Action action3 = Actions.parallel(action1, action2);
		this.addAction(action3);
	}
	
	
	@Override
	public void setColor(Color color){
		bg.setColor(color);
		label.setColor(color);
		super.setColor(color);
	}
	
	@Override
	public void setPosition(float x, float y) {
		bg.setPosition(x, y);
		label.setPosition(x, y);
		super.setPosition(x, y);
	}
	
	@Override
	public void setSize(float width, float height){
		bg.setSize(width, height);
		label.setSize(width, height);
		super.setSize(width, height);
	}
	
	@Override
	public void draw (SpriteBatch batch, float parentAlpha) {
		setSize(getWidth(), getHeight());
		setPosition(getX(), getY());		
		bg.draw(batch);
		if(!locked){		
			label.draw(batch, parentAlpha);
		}else{
			label.setVisible(false);
		}
		super.draw(batch, parentAlpha);
	}
}
