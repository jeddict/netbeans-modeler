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
package org.netbeans.modeler.widget.transferable;

import java.awt.datatransfer.DataFlavor;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 *
 */
public class MoveWidgetTransferable {

    public static final DataFlavor FLAVOR = createDataFlavor();

    private Widget widget = null;

    public MoveWidgetTransferable(Widget w) {
        widget = w;
    }

    public Widget getWidget() {
        return widget;
    }

    private static DataFlavor createDataFlavor() {
        try {
            return new DataFlavor("model/move_widget;class=org.netbeans.modeler.widget.transferable.MoveWidgetTransferable", // NOI18N
                    "Modeling Palette Item", // XXX missing I18N!
                    MoveWidgetTransferable.class.getClassLoader());
        } catch (ClassNotFoundException e) {
            throw new AssertionError(e);
        }

    }
}
