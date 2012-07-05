package name.nanek.analogtyper;

import java.util.LinkedList;
import java.util.List;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

public class TypingDemoViews {

	public TextView text;

	public TextView status;
	
	public List<View> leftLabels = new LinkedList<View>();
	
	public List<View> rightLabels = new LinkedList<View>();
	
	public List<View> topLabels = new LinkedList<View>();
	
	public List<View> bottomLabels = new LinkedList<View>();
	
	public List<View> allLabels = new LinkedList<View>();

	public TypingDemoViews(final TypingDemo typingDemo) {
		
		typingDemo.setContentView(R.layout.analogtyper);

		text = (TextView) typingDemo.findViewById(R.id.text);
		
		status = (TextView) typingDemo.findViewById(R.id.zeemoteStatus);
		
		final TableLayout table = (TableLayout) typingDemo.findViewById(R.id.table);
		for(int i = 0; i < table.getChildCount(); i++) {
			final ViewGroup row = (ViewGroup) table.getChildAt(i);
			for(int j = 0; j < row.getChildCount(); j++) {
				final TextView text = (TextView) row.getChildAt(j);		
				text.setTextColor(Color.BLACK);
				text.setGravity(Gravity.CENTER);
				
				if ( i >= 3 && i <= 5 && j >= 0 && j <= 2 ) {
					leftLabels.add(text);
				} else if ( i >= 0 && i <= 2 && j >= 3 && j <= 5 ) {
					topLabels.add(text);
				} else if ( i >= 3 && i <= 5 && j >= 6 && j <= 8 ) {
					rightLabels.add(text);
				} else if ( i >= 6 && i <= 8 && j >= 3 && j <= 5 ) {
					bottomLabels.add(text);
				}
				
				allLabels.add(text);
			}
		}
		
		Button button = (Button) typingDemo.findViewById(R.id.controllerSetupButton);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				typingDemo.onControllerSetupButtonClicked();
			}
		});
	}

	public void setUp(boolean value) {
		clearLabelBackgrounds();
		for(View view : topLabels) {
			view.setBackgroundColor(Color.CYAN);
		}
	}

	public void clearLabelBackgrounds() {
		for(View view : allLabels) {
			view.setBackgroundColor(Color.TRANSPARENT);
		}
	}

	public void setDown(boolean value) {
		clearLabelBackgrounds();
		for(View view : bottomLabels) {
			view.setBackgroundColor(Color.CYAN);
		}
	}

	public void setLeft(boolean value) {
		clearLabelBackgrounds();
		for(View view : leftLabels) {
			view.setBackgroundColor(Color.CYAN);
		}
	}

	public void setRight(boolean value) {
		clearLabelBackgrounds();
		for(View view : rightLabels) {
			view.setBackgroundColor(Color.CYAN);
		}
	}

}
