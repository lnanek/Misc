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

import com.htc.lockscreen.fusion.idlescreen.SimpleIdleScreenEngine;
import com.htc.lockscreen.idlescreen.IdleScreenEngine;
import com.htc.lockscreen.idlescreen.IdleScreenService;
import com.htc.sample.phonegaplockscreen.ExampleLockScreen;

/**
 * Support for roms not updated with final lockscreen API   
 */
@Deprecated
public class SampleServiceLegacy extends IdleScreenService {

    @Override
    public IdleScreenEngine onCreateEngine() {
        return new IdleScreenRemoteEngine();
    }

    public class IdleScreenRemoteEngine extends SimpleIdleScreenEngine implements LockScreenListener {

		private ExampleLockScreen lockScreen = new ExampleLockScreen(SampleServiceLegacy.this, this);

		public IdleScreenRemoteEngine() {
            super(SampleServiceLegacy.this);
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
