package name.nanek.analogtyper;

import com.zeemote.zc.event.ButtonEvent;

public class Move {
	
	public boolean left;
	
	public boolean right;
	
	public boolean up;
	
	public boolean down;
	
	public void setMove(ButtonEvent e, TypingDemoViews view, boolean value) {
		switch (e.getButtonGameAction()) {
		case ButtonEvent.BUTTON_UP:
			if ( null != view ) {
				view.setUp(value);
			}
			up = value;
			break;
		case ButtonEvent.BUTTON_DOWN:
			if ( null != view ) {
				view.setDown(value);
			}
			down = value;
			break;
		case ButtonEvent.BUTTON_LEFT:
			if ( null != view ) {
				view.setLeft(value);
			}
			left = value;
			break;
		case ButtonEvent.BUTTON_RIGHT:
			if ( null != view ) {
				view.setRight(value);
			}
			right = value;
			break;
		}		
	}
	
	public void clearMove() {
		up = false;
		left = false;
		right = false;
		down = false;
	}
	
	public boolean isAllReleased() {
		return !up && !down && !left && !right;
	}
	
	public String triggerKeys() {
		if ( left ) {
			return "a";
		}
		
		if ( up ) {
			return "b";
		}
		
		if ( right ) {
			return "c";
		}
		
		if ( down ) {
			return "d";
		}
		
		return "";
	}

}