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
package org.netbeans.modeler.widget.node;

import java.util.HashMap;
import java.util.Map;
import org.netbeans.modeler.config.palette.SubCategoryNodeConfig;
import org.netbeans.modeler.specification.model.document.IColorScheme;
import org.netbeans.modeler.widget.pin.IPinSeperatorWidget;
import org.netbeans.modeler.widget.pin.IPinWidget;
import org.netbeans.modeler.widget.pin.info.PinWidgetInfo;

public interface IPNodeWidget extends INodeWidget, IParentPNodeWidget {

    PinWidgetInfo getInternalPinWidgetInfo();

    void setInternalPinWidgetInfo(PinWidgetInfo internalPinWidgetInfo);

    IPinWidget createPinWidget(PinWidgetInfo pinWidgetInfo);

    void deletePinWidget(IPinWidget pinWidget);

    IColorScheme getColorScheme();

    void setColorScheme(IColorScheme colorScheme);

    Map<String, IPinSeperatorWidget> getPinCategoryWidgets();

    void setPinCategoryWidgets(HashMap<String, IPinSeperatorWidget> pinCategoryWidgets);

    int getAnchorGap();

    void setAnchorGap(int gap);

    boolean isHighlightStatus();

    void setHighlightStatus(boolean highlightStatus);
    
    void createPinWidget(SubCategoryNodeConfig subCategoryInfo);
      
    boolean isValidPinWidget(SubCategoryNodeConfig subCategoryInfo);

}
