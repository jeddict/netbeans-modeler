/**
 * Copyright [2016] Gaurav Gupta
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
package org.netbeans.modeler.widget.design;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class TextDesignValidator extends XmlAdapter<TextDesign, TextDesign> {

    @Override
    public TextDesign marshal(TextDesign textDesign) throws Exception {
        if (textDesign != null && !textDesign.isChanged()) {
            return null;
        }
        return textDesign;
    }

    @Override
    public TextDesign unmarshal(TextDesign textDesign) throws Exception {
        return textDesign;
    }

}
