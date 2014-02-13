/** Copyright [2014] Gaurav Gupta
   *
   *Licensed under the Apache License, Version 2.0 (the "License");
   *you may not use this file except in compliance with the License.
   *You may obtain a copy of the License at
   *
   *    http://www.apache.org/licenses/LICENSE-2.0
   *
   *Unless required by applicable law or agreed to in writing, software
   *distributed under the License is distributed on an "AS IS" BASIS,
   *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   *See the License for the specific language governing permissions and
   *limitations under the License.
   */
package org.netbeans.modeler.widget.action.doubleclick;

import org.netbeans.api.visual.widget.Widget;
import org.netbeans.api.visual.action.WidgetAction;

import java.awt.event.MouseEvent;
import java.awt.*;


public final class DoubleClickAction extends WidgetAction.LockedAdapter {

    private boolean aiming = false;
    private boolean invertSelection;
    private DoubleClickProvider provider;
    
    public DoubleClickAction (DoubleClickProvider provider) {
        this.provider = provider ;
    }
  

    @Override
    protected boolean isLocked () {
        return aiming;
    }

    @Override
    public State mousePressed (Widget widget, WidgetMouseEvent event) {
        if (isLocked()) {
            return State.createLocked(widget, this);
        }
        Point localLocation = event.getPoint();
        if (event.getButton() == MouseEvent.BUTTON1  && event.getClickCount() == 2) {
            invertSelection = (event.getModifiersEx() & MouseEvent.CTRL_DOWN_MASK) != 0;
            provider.onDoubleClick(widget, localLocation, invertSelection);
                    return State.CHAIN_ONLY;
         } 
        return State.REJECTED;
    }


  
}
