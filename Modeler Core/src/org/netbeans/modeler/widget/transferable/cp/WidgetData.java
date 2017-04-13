/**
 * Copyright [2017] Gaurav Gupta
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
package org.netbeans.modeler.widget.transferable.cp;

import java.awt.Rectangle;
import java.util.List;
import java.util.Map;
import org.netbeans.modeler.specification.model.document.core.IBaseElement;

/**
 *
 * @author jGauravGupta
 */
public class WidgetData {
    
    private Map<IBaseElement, Rectangle> data;

    public WidgetData(Map<IBaseElement, Rectangle> data) {
        this.data = data;
    }

    /**
     * @return the data
     */
    public Map<IBaseElement, Rectangle> getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(Map<IBaseElement, Rectangle> data) {
        this.data = data;
    }
    
}
