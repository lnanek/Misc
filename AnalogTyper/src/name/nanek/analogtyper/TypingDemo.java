/**
 * Copyright (c) 2006-2011 Zeemote. All rights reserved.
 */
package name.nanek.analogtyper;

import name.nanek.analogtyper.ZeemoteController.StatusReceiver;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;

import com.zeemote.zc.ZeemoteActivity;
import com.zeemote.zc.event.ButtonEvent;
import com.zeemote.zc.event.IButtonListener;

public class TypingDemo extends ZeemoteActivity implements IButtonListener {
	
	private static final String TAG= "TypingDemo";

	private boolean pausingToShowControllerUiActivity;
	
	private ZeemoteController monitor;

	private PowerManager.WakeLock wakeLock;
	
	private Move wasPressed = new Move();

	private Move isPressed = new Move();
	
	private MoveToCharacterMapper chooser;
			
	private boolean needsTrigger;
	
	private TypingDemoViews views;
	
	private Handler handler = new Handler();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);

		monitor = new ZeemoteController(this, new StatusReceiver() {
			@Override
			public void onZeeMoteMessage(String message) {
				views.status.setText(message);			
			}
		}, this);
		
		views = new TypingDemoViews(this);

	}
	
	public void onControllerSetupButtonClicked() {
		monitor.showControllerMenu();
		pausingToShowControllerUiActivity = true;		
	}

	@Override
	protected void onStart() {
		super.onStart();
		final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "zspy");
		wakeLock.acquire();
	}

	@Override
	protected void onStop() {
		super.onStop();
		wakeLock.release();
		wakeLock = null;
	}

	@Override
	public void onResume() {
		Log.d(TAG, "onResume");
		super.onResume();
		if (!pausingToShowControllerUiActivity) {
			monitor.startConnectionProcess();
			pausingToShowControllerUiActivity = true;
		} else {
			// The ControllerAndroidUi activity was hidden.
			pausingToShowControllerUiActivity = false;
		}
	}

	@Override
	public void onPause() {
		Log.d(TAG, "onPause");
		super.onPause();
		if (!pausingToShowControllerUiActivity) {
			// If the ControllerAndroidUi activity is not shown,
			// disconnect the Zeemote controller.
			monitor.disconnect();
		}
	}

	public void buttonPressed(final ButtonEvent e) {
		wasPressed.setMove(e, chooser == null ? views : null, true);
		isPressed.setMove(e, null, true);
		needsTrigger = true;
	}
	
	/**
	 * Called when a controller's joystick is moved by JoystickToButtonAdapter.
	 */
	public void buttonReleased(final ButtonEvent e) {
		
		if ( e.getButtonGameAction() == ButtonEvent.BUTTON_A ) {
			sendCharacter(" ");
			return;
		}
		
		if ( e.getButtonGameAction() == ButtonEvent.BUTTON_B ) {
			sendCharacter("\n");
			return;
		}
		
		if ( e.getButtonGameAction() == ButtonEvent.BUTTON_C ) {
			finish();
			return;
		}
		
		if ( e.getButtonGameAction() == ButtonEvent.BUTTON_D ) {
			// call done method to check score?
			return;
		}

		isPressed.setMove(e, views, false);
		
		if ( needsTrigger && isPressed.isAllReleased() ) {
			
			if ( null == chooser ) {
				chooser = MoveToCharacterMapper.getChooser(wasPressed);
			} else {
			
			
				final String newOutput = chooser.getCharacter(wasPressed);
				sendCharacter(newOutput);
				chooser = null;
			}
			
			wasPressed.clearMove();
			needsTrigger = false;
		}

	}

	private void sendCharacter(final String newOutput) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				views.text.setText(views.text.getText() + newOutput);
				views.clearLabelBackgrounds();
			}
		});
	}
	
}
