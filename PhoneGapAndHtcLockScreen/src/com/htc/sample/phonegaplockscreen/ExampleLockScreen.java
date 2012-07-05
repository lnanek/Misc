/*
 * Copyright (C) 2012 HTC Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.htc.sample.phonegaplockscreen;

import org.apache.cordova.DroidGapLockScreen;

import android.app.Activity;
import android.content.Context;
import android.view.SurfaceHolder;

import com.htc.lockscreen.fusion.idlescreen.SimpleIdleScreenEngine;
import com.htc.lockscreen.fusion.open.SimpleEngine;

/**
 * The lock screen wrapper implementation class. <br>
 * Use this class to target legacy API and emulator support in addition to the current API. <br>
 * See AndroidManifest to toggle support for each of the three cases there.
 * 
 */
public class ExampleLockScreen extends DroidGapLockScreen {
    
	public ExampleLockScreen(Context service, SimpleEngine engine) {
		super(service, engine);
	}

	// support for older deprecated API
	public ExampleLockScreen(Context service, SimpleIdleScreenEngine engine) {
		super(service, engine);
	}

	// support for running in an activity (emulator)
	public ExampleLockScreen(Activity context) {
		super(context);
	}

	@Override
	public void onCreate(SurfaceHolder holder) {
        super.onCreate(holder);
        super.loadUrl("file:///android_asset/www/index.html");
	}
		
	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}
	
	@Override
	public void onStop() {
		super.onStop();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
}
