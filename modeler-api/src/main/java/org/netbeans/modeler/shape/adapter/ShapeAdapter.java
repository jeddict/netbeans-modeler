/**
 * Copyright 2013-2022 Gaurav Gupta
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

import java.awt.Shape;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;

public class ShapeAdapter extends XmlAdapter<String, Shape> {

    private Jsonb jsonb = JsonbBuilder.newBuilder().build();

    @Override
    public Shape unmarshal(String outerShape) {
        Shape outerShapeContext;
        outerShapeContext = jsonb.fromJson(outerShape, Shape.class);
        return outerShapeContext;
    }

    @Override
    public String marshal(Shape outerShapeContext) {
        String outerShape;
        outerShape = jsonb.toJson(outerShapeContext);
        return outerShape;
    }
}
