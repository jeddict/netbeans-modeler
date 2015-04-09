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
package org.netbeans.modeler.label;

import org.netbeans.api.visual.widget.LabelWidget;

/**
 * The LabelManager is used to manage a set of labels that can be displayed.
 *
 *
 */
public interface LabelManager {

    /**
     * LabelType is an enumeration of the types of labels.
     */
    enum LabelType {

        NODE,
        /**
         * The edge label. The edge label will usally be displayed on the center
         * of the connection.
         */
        EDGE,
        /**
         * Labels that will be placed on the source end of the connection.
         */
        SOURCE,
        /**
         * Labels that will be placed on the target end of teh connection.
         */
        TARGET;
    }

//    LabelWidget createLabel(String label);
    void setLabel(String name);

    void showLabel();

    void hideLabel();

    boolean isVisible();

    String getLabel();

    LabelWidget getLabelWidget();

    ILabelConnectionWidget getLabelConnectionWidget();

    void setDefaultPosition();
}
