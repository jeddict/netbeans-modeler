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

import org.netbeans.modeler.properties.view.manager.IPropertyManager;
import org.netbeans.modeler.specification.model.document.core.IBaseElement;
import org.netbeans.modeler.specification.model.document.property.ElementPropertySet;
import org.netbeans.modeler.widget.node.IWidget;

public interface IBaseElementWidget<E extends IBaseElement> extends IWidget {

    public String getId();

    public void setId(String id);

    void createPropertySet(ElementPropertySet elementPropertySet);

    void createVisualPropertySet(ElementPropertySet elementPropertySet);

    public void setBaseElementSpec(E baseElementSpec);

    public E getBaseElementSpec();

    public void init();//Base Element spec available
    
    /**
     * After relation is completed/connected with other widget
     */
    public void onConnection();
    

    public void destroy();

    //custom added
    void showProperties();

    void exploreProperties();

    void refreshProperties();
    
    IPropertyManager getPropertyManager();

}
