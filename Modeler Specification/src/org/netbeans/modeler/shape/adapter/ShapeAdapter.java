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
package org.netbeans.modeler.shape.adapter;

import com.google.gson.Gson;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class ShapeAdapter extends XmlAdapter<String, ShapeAdapter> {

    Gson json = new Gson();

    @Override
    public ShapeAdapter unmarshal(String outerShape) {
        ShapeAdapter outerShapeContext;
        outerShapeContext = json.fromJson(outerShape, ShapeAdapter.class);
        return outerShapeContext;
    }

    @Override
    public String marshal(ShapeAdapter outerShapeContext) {
        String outerShape;
        outerShape = json.toJson(outerShapeContext);
        return outerShape;
    }
}
