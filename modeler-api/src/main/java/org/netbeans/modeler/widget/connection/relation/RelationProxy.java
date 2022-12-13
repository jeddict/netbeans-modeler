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
package org.netbeans.modeler.widget.connection.relation;

import org.netbeans.modeler.widget.node.INodeWidget;

public class RelationProxy implements IRelationProxy {

    private INodeWidget source = null;
    private INodeWidget target = null;
    private String edgeType = null;
    private Boolean relationValidated = null;

    /**
     * @return the source
     */
    @Override
    public INodeWidget getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    @Override
    public void setSource(INodeWidget source) {
        this.source = source;
    }

    /**
     * @return the target
     */
    @Override
    public INodeWidget getTarget() {
        return target;
    }

    /**
     * @param target the target to set
     */
    @Override
    public void setTarget(INodeWidget target) {
        this.target = target;
    }

//    /**
//     * @return the connection
//     */
//    @Override
//    public IEdgeWidget getConnection() {
//        return connection;
//    }
//
//    /**
//     * @param connection the connection to set
//     */
//    @Override
//    public void setConnection(IEdgeWidget connection) {
//        this.connection = connection;
//    }
    /**
     * @param relationValidated the relationValidated to set
     */
    public void setRelationValidated(Boolean relationValidated) {
        this.relationValidated = relationValidated;
    }

    @Override
    public boolean isRelationValidated() {
        return relationValidated;
    }

    /**
     * @return the edgeType
     */
    @Override
    public String getEdgeType() {
        return edgeType;
    }

    /**
     * @param edgeType the edgeType to set
     */
    @Override
    public void setEdgeType(String edgeType) {
        this.edgeType = edgeType;
    }

}
