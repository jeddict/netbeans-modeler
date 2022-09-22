/**
 * Copyright 2013-2022 Gaurav Gupta
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.netbeans.modeler.properties.customattr;

import java.awt.Frame;
import org.openide.util.Lookup;
import org.openide.util.Mutex;
import org.openide.windows.WindowManager;

public class Misc {

    private static Frame mainFrame = null;

    public static Frame getMainFrame() {
        if (mainFrame == null) {
            Runnable run = new Runnable() {

                @Override
                public void run() {
                    WindowManager w = Lookup.getDefault().lookup(WindowManager.class);
                    mainFrame = (w == null) ? null : w.getMainWindow();
                }
            };

            Mutex.EVENT.readAccess(run);
        }
        return mainFrame;
    }
}
