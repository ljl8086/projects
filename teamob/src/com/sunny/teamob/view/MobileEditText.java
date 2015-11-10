package com.sunny.teamob.view;

import android.content.Context;
import android.graphics.Rect;
import android.text.Editable;
import android.util.AttributeSet;
import android.widget.EditText;

public class MobileEditText extends EditText{

	public MobileEditText(Context context) {
		super(context);
	}

	public MobileEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public String toString() {
		return this.getEditableText().toString().replace("-", "");
	}

	@Override
	protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
		if (!focused) {
			Editable editable = this.getText();
			if (editable.length() > 3 && editable.charAt(3)!='-') {
				editable.insert(3, "-");
			}
			if (editable.length() > 8  && editable.charAt(8)!='-'){
				editable.insert(8, "-");
			}
			this.setText(editable);
		}
		super.onFocusChanged(focused, direction, previouslyFocusedRect);
	}
	
	public String getMobile(){
		return this.getEditableText().toString().replace("-", "");
	}

}
