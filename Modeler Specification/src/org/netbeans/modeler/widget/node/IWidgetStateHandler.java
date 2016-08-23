/**
 * Copyright [2016] Gaurav Gupta
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
package org.netbeans.modeler.widget.node;

import java.awt.Image;
import java.util.Set;
import org.netbeans.modeler.util.Util;

/**
 *
 * @author Gaurav Gupta
 */
public interface IWidgetStateHandler {

    public Set<StateType> getState();
    public void applyState(StateType state);
    public boolean hasState(StateType state);
    public void clearState(StateType state);
    

    public enum StateType {
        ERROR(1,Util.loadImage("org/netbeans/modeler/resource/error_8.png")),
        WARNING(2,Util.loadImage("org/netbeans/modeler/resource/warning_8.png")),
        TODO(3,Util.loadImage("org/netbeans/modeler/resource/warning_8.png"));
        
        private final int priority;
        public final Image icon;

        private StateType(int priority, Image icon) {
            this.priority = priority;
            this.icon = icon;
        }

        public Image getIcon() {
            return icon;
        }

        public int getPriority() {
            return priority;
        }
    }

}
