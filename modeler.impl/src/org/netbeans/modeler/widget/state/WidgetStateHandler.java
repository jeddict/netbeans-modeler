/**
 * Copyright [2015] Gaurav Gupta
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
package org.netbeans.modeler.widget.state;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import org.netbeans.modeler.widget.node.IWidgetStateHandler;
import org.netbeans.modeler.widget.node.vmd.internal.AdvanceImageWidget;

/**
 *
 * @author Gaurav Gupta
 */
public class WidgetStateHandler implements IWidgetStateHandler {

    private final Set<StateType> stateTypes = new HashSet<>();
    private final AdvanceImageWidget imageWidget;

    public WidgetStateHandler(AdvanceImageWidget imageWidget) {
        this.imageWidget = imageWidget;
    }

    @Override
    public Set<StateType> getState() {
        return stateTypes;
    }

    @Override
    public void applyState(StateType state) {
        stateTypes.add(state);
        imageWidget.setStateType(getCurrentStatus());
    }

    @Override
    public boolean hasState(StateType state) {
        return stateTypes.contains(state);
    }

    @Override
    public void clearState(StateType state) {
        stateTypes.remove(state);
        imageWidget.setStateType(getCurrentStatus());
    }

    private StateType getCurrentStatus() {
        if (stateTypes.isEmpty()) {
            return null;
        } else {
            return Collections.max(stateTypes, Comparator.comparing(s -> s.getPriority()));
        }
    }
}
