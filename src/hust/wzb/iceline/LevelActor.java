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
	private Sprite bg; // 关卡背景图，分别为锁住状态和解锁状态
	private Label label; // 用于显示当前的关卡数
	private int level;	// 当前的关卡，从0开始，显示时，为level+1
	private boolean locked; // 判断是否锁住
	
	/**
	 * 
	 * @param locked: 表示是否锁住
	 * @param level:  表示当前的关卡
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
		label.setAlignment(Align.center); // 居中显示
		
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
	 * 判断是否锁住
	 * @return true 锁住， false 未锁
	 */
	public boolean isLocked(){
		return locked;
	}
	
	/**
	 * 解锁
	 */
	public void setUnlocked(){ // 解锁
		locked = false;
		bg = MyAssetsManager.getInstance().unlockbg;
	}
	
	/**
	 * 加鎖
	 */
	public void setLocked(){
		locked = true;
		bg = MyAssetsManager.getInstance().lockbg;
	}
	
	/**
	 * type: 1 左边出去， 0 表示右边出去
	 *  出去的动画, type 表示向左还是向右出去
	 */
	public void setMoveOutAction(int type){
		float addx = Constants.screenWidth;
		if(type == 1) addx = - addx; // 左边出去 
		Action action1 = Actions.moveTo(addx + getX(),  getY(),Gdx.graphics.getDeltaTime() * 30);
		Action action2 = Actions.fadeOut(Gdx.graphics.getDeltaTime() * 30);
		Action action3 = Actions.parallel(action1, action2);
		
		this.addAction(action3);
	}
	
	/**
	 * 进去动画，注意进入动画和出去动画效果是相反的
	 * @param type 0 表示右边进去， 1表示左边进去
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
