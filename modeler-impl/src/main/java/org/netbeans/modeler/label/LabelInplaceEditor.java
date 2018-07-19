/**
 * Copyright 2013-2018 Gaurav Gupta
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
package org.netbeans.modeler.label;

import org.netbeans.api.visual.action.TextFieldInplaceEditor;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modeler.specification.model.document.widget.IFlowEdgeWidget;
import org.netbeans.modeler.specification.model.document.widget.IFlowElementWidget;
import org.netbeans.modeler.specification.model.document.widget.IFlowNodeWidget;
import org.netbeans.modeler.specification.model.document.widget.IFlowPinWidget;
import org.netbeans.modeler.widget.edge.IEdgeWidget;
import org.netbeans.modeler.widget.node.INodeWidget;
import org.netbeans.modeler.widget.pin.IPinWidget;

public class LabelInplaceEditor implements TextFieldInplaceEditor {

    private Widget connector = null;

    public LabelInplaceEditor(Widget connector) {
        this.connector = connector;
    }

    @Override
    public boolean isEnabled(Widget widget) {
        return true;
    }

    @Override
    public String getText(Widget widget) {
        if(widget.getParentWidget() != null && widget.getParentWidget() instanceof IFlowElementWidget){
            return ((IFlowElementWidget)widget.getParentWidget()).getName();
        } else {
            return ((LabelWidget) widget).getLabel();
        }
    }

    @Override
    public void setText(Widget widget, String text) {
        if (connector instanceof INodeWidget) {
            ((INodeWidget) connector).setLabel(text);
            if (connector instanceof IFlowNodeWidget) {
                ((IFlowNodeWidget) connector).setName(text);
            }

            ((INodeWidget) connector).getModelerScene().getModelerPanelTopComponent().changePersistenceState(false);

        } else if (connector instanceof IEdgeWidget) {
            ((IEdgeWidget) connector).setLabel(text);
            if (connector instanceof IFlowEdgeWidget) {
                ((IFlowEdgeWidget) connector).setName(text);
            }
            ((IEdgeWidget) connector).getModelerScene().getModelerPanelTopComponent().changePersistenceState(false);

        } else if (connector instanceof IPinWidget) {
            ((IPinWidget) connector).setLabel(text);
            if (connector instanceof IFlowPinWidget) {
                ((IFlowPinWidget) connector).setName(text);
            }
            ((IPinWidget) connector).getModelerScene().getModelerPanelTopComponent().changePersistenceState(false);

        }
    }
}
