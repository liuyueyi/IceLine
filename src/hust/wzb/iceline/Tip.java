package hust.wzb.iceline;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class Tip extends Label {
	public Tip(CharSequence text, LabelStyle style) {		
		super(text, style);
		setSize(50, 40);
	}

	public void setAction() {
		Action action = Actions.moveTo(getX() + 5f, getY() + 5f, Gdx.graphics.getDeltaTime() * 30);
		Action moveAction = Actions.moveTo(Constants.scoreSpriteX
				+ Constants.scoreSpriteWidth / 2, Constants.screenHeight + 20,
				Gdx.graphics.getDeltaTime() * 40);
		Action fadeoutAction = Actions.fadeOut(Gdx.graphics.getDeltaTime() * 40);
		Action paraelAction = Actions.parallel(moveAction, fadeoutAction);
		this.addAction(Actions.sequence(action, paraelAction));
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		if (getY() >= Constants.scoreSpriteY) {
			this.remove();
		}
	}
}
