package name.nanek.analogtyper;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.zeemote.zc.Configuration;
import com.zeemote.zc.Controller;
import com.zeemote.zc.event.BatteryEvent;
import com.zeemote.zc.event.ButtonEvent;
import com.zeemote.zc.event.ControllerEvent;
import com.zeemote.zc.event.DisconnectEvent;
import com.zeemote.zc.event.IButtonListener;
import com.zeemote.zc.event.IJoystickListener;
import com.zeemote.zc.event.IStatusListener;
import com.zeemote.zc.event.JoystickEvent;
import com.zeemote.zc.ui.android.ControllerAndroidUi;
import com.zeemote.zc.util.JoystickToButtonAdapter;

public class ZeemoteController {
	
	public static interface StatusReceiver {
		void onZeeMoteMessage(String message);
	}
	
	private static final String TAG= "ZeeMoteMonitor";
	
	public static final int GUIUPDATEIDENTIFIER = 0x101;
	
	private StatusReceiver receiver;
	
	private Controller controller;

	private ControllerAndroidUi controllerUi;
	
	private JoystickToButtonAdapter adapter;
	
	private Handler ViewUpdateHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GUIUPDATEIDENTIFIER:
				receiver.onZeeMoteMessage((String)msg.obj);
				break;
			}
			super.handleMessage(msg);
		}
	};
	
	public ZeemoteController(final Context context, final StatusReceiver receiver, final IButtonListener buttonListener) {
		this.receiver = receiver;

		controller = new Controller(1, Controller.TYPE_GP1);
		
		controllerUi = new ControllerAndroidUi(context, controller);
		
		adapter = new JoystickToButtonAdapter();
		controller.addJoystickListener(adapter);
		adapter.addButtonListener(new IButtonListener() {

			@Override
			public void buttonPressed(final ButtonEvent arg0) {
				ViewUpdateHandler.post(new Runnable() {
					@Override
					public void run() {
						buttonListener.buttonPressed(arg0);
					}
				});
			}

			@Override
			public void buttonReleased(final ButtonEvent arg0) {
				ViewUpdateHandler.post(new Runnable() {
					@Override
					public void run() {
						buttonListener.buttonReleased(arg0);
					}
				});
			}
			
		});
		controller.addButtonListener(buttonListener);

		
		controller.addStatusListener(new IStatusListener() {
			@Override
			public void batteryUpdate(BatteryEvent event) {
				traceLog("Battery Update: cur=" + event.getCurrentLevel() + ", max="
						+ event.getMaximumLevel() + ", warn=" + event.getWarningLevel()
						+ ", min=" + event.getMinimumLevel());
			}

			@Override
			public void connected(ControllerEvent event) {
				Configuration config = event.getController().getConfiguration();
				traceLog("Connected to controller: "+
					"Num Buttons=" + config.getButtonCount()+
					", Num Joysticks=" + config.getJoystickCount());
			}

			@Override
			public void disconnected(DisconnectEvent event) {
				traceLog("Disconnected from controller: "
						+ (event.isUnexpected() ? "unexpected" : "expected"));

				// when an unexpected disconnect occurs display the disconnected warning
				// dialog and go into the controller menu
				if (event.isUnexpected()) {
					traceLog("unexpected disconnect occurs");
				}

			}			
		});
		controller.addButtonListener(new IButtonListener() {
			@Override
			public void buttonPressed(ButtonEvent e) {
				// A button was pressed
				String buttonName = getButtonName(e);
				traceLog(buttonName + " Pressed");
			}

			@Override
			public void buttonReleased(ButtonEvent e) {
				// A button was released
				String buttonName = getButtonName(e);
				traceLog(buttonName + " Released");
			}
		});
		controller.addJoystickListener(new IJoystickListener() {
			@Override
			public void joystickMoved(JoystickEvent e) {
				// A joystick moved. Scale the values between -100 and 100
				int x = e.getScaledX(-100, 100);
				int y = e.getScaledY(-100, 100);
				traceLog("X=" + x + ",Y=" + y);
			}
		});
	}

	public void showControllerMenu() {
		controllerUi.showControllerMenu();		
	}
	
	protected void traceLog(final String s) {
		Log.d(TAG, s);
		Message m = new Message();
		m.what = GUIUPDATEIDENTIFIER;
		m.obj = s;
		ViewUpdateHandler.sendMessage(m);
	}
	
	protected static String getButtonName(ButtonEvent event) {
		int b = event.getButtonID();
		String label = event.getController().getConfiguration().getButtonLabel(b);
		String action = getActionLabel(event.getButtonGameAction());

		return "button id=" + b + ",label=" + label + ",action=" + action;
	}

	private static String getActionLabel(int action) {
		String label;
		switch (action) {
		case ButtonEvent.BUTTON_A: label = "BUTTON_A"; break;
		case ButtonEvent.BUTTON_B: label = "BUTTON_B"; break;
		case ButtonEvent.BUTTON_C: label = "BUTTON_C"; break;
		case ButtonEvent.BUTTON_D: label = "BUTTON_D"; break;
		case ButtonEvent.BUTTON_E: label = "BUTTON_E"; break;
		case ButtonEvent.BUTTON_F: label = "BUTTON_F"; break;
		case ButtonEvent.BUTTON_G: label = "BUTTON_G"; break;
		case ButtonEvent.BUTTON_H: label = "BUTTON_H"; break;
		case ButtonEvent.BUTTON_UP: label = "BUTTON_UP"; break;
		case ButtonEvent.BUTTON_DOWN: label = "BUTTON_DOWN"; break;
		case ButtonEvent.BUTTON_LEFT: label = "BUTTON_LEFT"; break;
		case ButtonEvent.BUTTON_RIGHT: label = "BUTTON_RIGHT"; break;
		case ButtonEvent.BUTTON_CENTER: label = "BUTTON_CENTER"; break;
		case ButtonEvent.BUTTON_L1: label = "BUTTON_L1"; break;
		case ButtonEvent.BUTTON_R1: label = "BUTTON_R1"; break;
		case ButtonEvent.BUTTON_L2: label = "BUTTON_L2"; break;
		case ButtonEvent.BUTTON_R2: label = "BUTTON_R2"; break;
		case ButtonEvent.BUTTON_THUMBL: label = "BUTTON_THUMBL"; break;
		case ButtonEvent.BUTTON_THUMBR: label = "BUTTON_THUMBR"; break;
		case ButtonEvent.BUTTON_START: label = "BUTTON_START"; break;
		case ButtonEvent.BUTTON_SELECT: label = "BUTTON_SELECT"; break;
		case ButtonEvent.BUTTON_MODE: label = "BUTTON_MODE"; break;
		case ButtonEvent.BUTTON_POWER: label = "BUTTON_POWER"; break;
		case ButtonEvent.BUTTON_1: label = "BUTTON_1"; break;
		case ButtonEvent.BUTTON_2: label = "BUTTON_2"; break;
		case ButtonEvent.BUTTON_3: label = "BUTTON_3"; break;
		case ButtonEvent.BUTTON_4: label = "BUTTON_4"; break;
		case ButtonEvent.BUTTON_5: label = "BUTTON_5"; break;
		case ButtonEvent.BUTTON_6: label = "BUTTON_6"; break;
		case ButtonEvent.BUTTON_7: label = "BUTTON_7"; break;
		case ButtonEvent.BUTTON_8: label = "BUTTON_8"; break;
		case ButtonEvent.BUTTON_9: label = "BUTTON_9"; break;
		case ButtonEvent.BUTTON_10: label = "BUTTON_10"; break;
		case ButtonEvent.BUTTON_11: label = "BUTTON_11"; break;
		case ButtonEvent.BUTTON_12: label = "BUTTON_12"; break;
		case ButtonEvent.BUTTON_13: label = "BUTTON_13"; break;
		case ButtonEvent.BUTTON_14: label = "BUTTON_14"; break;
		case ButtonEvent.BUTTON_15: label = "BUTTON_15"; break;
		case ButtonEvent.BUTTON_16: label = "BUTTON_16"; break;
		default: label = "UNKNOWN"; break;
		}
		return label;
	}

	
	public void startConnectionProcess() {
		// Start the establishing a connection.
		// If the auto connect is ON, this shows the ControllerAndroidUi activity.
		// otherwise, this is no effect.
		controllerUi.startConnectionProcess();
	}

	public void disconnect() {
		try {
			controller.disconnect();
		} catch (Exception e) {
		}
	}
	
}
