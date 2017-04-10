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
package org.netbeans.modeler.widget.info;

import org.netbeans.modeler.specification.model.document.core.IBaseElement;

/**
 *
 * @author jGauravGupta
 */
public interface WidgetInfo {

    IBaseElement getBaseElementSpec();

    String getId();

    String getName();

    Boolean isExist();

    void setBaseElementSpec(IBaseElement baseElementSpec);

    void setExist(Boolean exist);

    void setId(String id);

    void setName(String title);

}
