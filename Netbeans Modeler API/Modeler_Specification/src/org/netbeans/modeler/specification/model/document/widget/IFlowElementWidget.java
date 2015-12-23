/**
 * Copyright [2014] Gaurav Gupta
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
package org.netbeans.modeler.specification.model.document.widget;

import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.specification.model.document.core.IFlowElement;

public interface IFlowElementWidget<E extends IFlowElement> extends IBaseElementWidget<IFlowElement> {

    public Widget getFlowElementsContainer();

    public void setFlowElementsContainer(Widget flowElementsContainer);

    /**
     * @return the scene
     */
    public IModelerScene getModelerScene();

    /**
     * @param scene the scene to set
     */
    public void setModelerScene(IModelerScene scene);

}
