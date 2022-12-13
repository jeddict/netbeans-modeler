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

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

public class PinTextDesignValidator extends XmlAdapter<PinTextDesign, PinTextDesign> {

    @Override
    public PinTextDesign marshal(PinTextDesign textDesign) throws Exception {
        if (textDesign != null && !textDesign.isChanged()) {
            return null;
        }
        return textDesign;
    }

    @Override
    public PinTextDesign unmarshal(PinTextDesign textDesign) throws Exception {
        return textDesign;
    }

}
