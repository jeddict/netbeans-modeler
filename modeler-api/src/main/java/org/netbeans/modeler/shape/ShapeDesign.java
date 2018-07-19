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
package org.netbeans.modeler.shape;

//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.google.gson.JsonDeserializationContext;
//import com.google.gson.JsonDeserializer;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonParseException;
//import com.google.gson.JsonPrimitive;
//import com.google.gson.JsonSerializationContext;
//import com.google.gson.JsonSerializer;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import org.netbeans.modeler.config.document.StandardShapeDesign;

@XmlRootElement(name = "ShapeDesign", namespace = "http://nbmodeler.java.net")
@XmlType(name = "ShapeDesign")
@XmlAccessorType(XmlAccessType.FIELD)
public class ShapeDesign {

    @XmlTransient
    private OuterShapeContext outerShapeContext;

    @XmlTransient
    private InnerShapeContext innerShapeContext;

    public ShapeDesign() {
    }

    public ShapeDesign(StandardShapeDesign standardShapeDesign) {
        this.innerShapeContext = standardShapeDesign.getInnerShapeContext();
        this.outerShapeContext = standardShapeDesign.getOuterShapeContext();
    }

    @XmlValue
    private String value;

    class Shape {
        public OuterShapeContext outer;
        public InnerShapeContext inner;
    }
    
    void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
        Jsonb jsonb = JsonbBuilder.create();
        Shape shape = jsonb.fromJson(value, Shape.class);
        this.innerShapeContext = shape.inner;
        this.outerShapeContext = shape.outer;
    }

    void beforeMarshal(Marshaller marshaller, Object parent) {
        Jsonb jsonb = JsonbBuilder.create();
        Shape shape = new Shape();
        shape.inner = this.innerShapeContext;
        shape.outer = this.outerShapeContext;
        value = jsonb.toJson(shape);//"<![CDATA[" + json.toJson(shape) + "]]>";
    }

    public void beforeMarshal() {
        beforeMarshal(null, null);
    }

    public void afterUnmarshal() {
        afterUnmarshal(null, null);
    }

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
