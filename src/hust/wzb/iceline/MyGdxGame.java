package hust.wzb.iceline;

import com.badlogic.gdx.Game;

public class MyGdxGame extends Game{
	MainActivity main;
	public MenuScreen menuScreen;
	
	public MyGdxGame(MainActivity main){
		this.main = main;
	}
	
	
	@Override
	public void create() {
		// TODO Auto-generated method stub
		menuScreen = new MenuScreen(this);
		MyAssetsManager.getInstance().loadTexture();
		MyAssetsManager.getInstance().loadMusic();
		this.setScreen(menuScreen);
	}
	
	
	@Override
	public void dispose(){
		MyAssetsManager.getInstance().clear();
	}
}
