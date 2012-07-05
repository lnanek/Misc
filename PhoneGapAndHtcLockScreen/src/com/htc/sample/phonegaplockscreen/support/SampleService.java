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

package com.htc.sample.phonegaplockscreen.support;

import android.view.SurfaceHolder;

import com.htc.lockscreen.fusion.open.SimpleEngine;
import com.htc.lockscreen.fusion.open.SimpleIdleScreenService;
import com.htc.sample.phonegaplockscreen.ExampleLockScreen;

public class SampleService extends SimpleIdleScreenService {

    @Override
    public SimpleEngine onCreateEngine() {
        return new IdleScreenRemoteEngine();
    }

    public class IdleScreenRemoteEngine extends SimpleEngine implements LockScreenListener {

		private ExampleLockScreen lockScreen = new ExampleLockScreen(SampleService.this, this);

        public IdleScreenRemoteEngine() {
        }

        public void onCreate(final SurfaceHolder holder) {
        	lockScreen.onCreate(holder);
        }

        public void onStart() {
    		lockScreen.onStart();
	    }
	
	    public void onResume() {
			lockScreen.onResume();
	    }
	
	    public void onPause() {
			lockScreen.onPause();
	    }
	
	    public void onStop() {
			lockScreen.onStop();
	    }
	
	    public void onDestroy() {
			lockScreen.onDestroy();
	    }
	
    }
}
