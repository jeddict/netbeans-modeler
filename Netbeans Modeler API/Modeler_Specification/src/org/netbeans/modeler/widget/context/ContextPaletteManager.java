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
 package org.netbeans.modeler.widget.context;

import java.awt.Point;
//import org.netbeans.modeler.widget.context.base.ContextPaletteModel;
import org.netbeans.modeler.widget.node.INodeWidget;

/**
 *
 * 
 */
public interface ContextPaletteManager
{
    /**
     * Notifies the context palette manager that the select element has changed.
     * 
     * @param p point related to corresponding selection event in scene.  If 
     *          null is specified the context palette manager will determine the
     *          best location to place the palette.
     */
    public void selectionChanged(INodeWidget selectedWidget,Point p);
    
    /**
     * Cancel the palette action.  The context palette will also be hidden.
     */
    public void cancelPalette();
    
    /**
     * Retrieves the model used to represent the context palette information.
     * 
     * @return the context palette model.
     */
    public ContextPaletteModel getModel();
    
    /**
     * Request that the context palette recieve input focus.
     */
    public void requestFocus();
}
