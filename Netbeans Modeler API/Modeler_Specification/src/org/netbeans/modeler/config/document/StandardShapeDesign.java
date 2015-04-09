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
package org.netbeans.modeler.config.document;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.netbeans.modeler.shape.InnerShapeContext;
import org.netbeans.modeler.shape.OuterShapeContext;

@XmlRootElement(name = "shape-design", namespace = "http://nbmodeler.java.net")
@XmlType(name = "shape-design")

@XmlAccessorType(XmlAccessType.FIELD)
public class StandardShapeDesign /*implements Unmarshaller , Marshaller*/ {

    @XmlElement(name = "outer-shape")

    private OuterShapeContext outerShapeContext;

    @XmlElement(name = "inner-shape")
    private InnerShapeContext innerShapeContext;

//    @XmlValue
//    protected String value;
//
//    class Shape {
//        public OuterShapeContext outer;
//        public InnerShapeContext inner;
//    }
//
//
//
//    void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {//XML to Object
//        Gson json = new Gson();
//        Shape shape = json.fromJson(value, Shape.class);
//        this.innerShapeContext = shape.inner;
//        this.outerShapeContext = shape.outer;
//        System.out.println("afterUnmarshal value : " + value);
//    }
//
//    void beforeMarshal(Marshaller marshaller, Object parent) {
////        Gson json = new Gson();
//       final ColorAdapter colorAdapter = new ColorAdapter();
//        JsonSerializer<Color> ser = new JsonSerializer<Color>() {
//            @Override
//            public JsonElement serialize(Color src, Type typeOfSrc, JsonSerializationContext context) {
//                return src == null ? null : new JsonPrimitive(colorAdapter.marshal(src));
//            }
//        };
//
//        JsonDeserializer<Color> deser = new JsonDeserializer<Color>() {
//            @Override
//            public Color deserialize(JsonElement json, Type typeOfT,
//                    JsonDeserializationContext context) throws JsonParseException {
//                return json == null ? null : colorAdapter.unmarshal(value);
//            }
//        };
//
//Gson json = new GsonBuilder()
//   .registerTypeAdapter(Color.class, ser)
//   .registerTypeAdapter(Color.class, deser).create();
//
//
//
//
//        Shape shape = new Shape();
//        shape.inner = this.innerShapeContext;
//        shape.outer = this.outerShapeContext;
//        value = json.toJson(shape);
//        System.out.println("beforeMarshal value : " + value);
//    }
//
//     public void beforeMarshal() {
//       beforeMarshal(null ,null );
//     }
//
//       void afterMarshal(Unmarshaller unmarshaller, Object parent) {//XML to Object
//
//        System.out.println("afterUnmarshal value : " + value);
//    }
//
//    void beforeUnmarshal(Marshaller marshaller, Object parent) {
//
//        System.out.println("beforeMarshal value : " + value);
//    }
//
    /**
     * @return the outerShapeContext
     */
    public OuterShapeContext getOuterShapeContext() {
        return outerShapeContext;
    }

    /**
     * @param outerShapeContext the outerShapeContext to set
     */
    public void setOuterShapeContext(OuterShapeContext outerShapeContext) {
        this.outerShapeContext = outerShapeContext;
    }

    /**
     * @return the innerShapeContext
     */
    public InnerShapeContext getInnerShapeContext() {
        return innerShapeContext;
    }

    /**
     * @param innerShapeContext the innerShapeContext to set
     */
    public void setInnerShapeContext(InnerShapeContext innerShapeContext) {
        this.innerShapeContext = innerShapeContext;
    }

}
