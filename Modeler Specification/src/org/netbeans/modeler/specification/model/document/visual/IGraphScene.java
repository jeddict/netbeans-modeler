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
package org.netbeans.modeler.specification.model.document.visual;

import java.util.Collection;
import org.netbeans.api.visual.widget.Widget;

public interface IGraphScene<N, E> extends IObjectScene {

    /**
     * Adds an edge.
     *
     * @param edge the edge to be added; the edge must not be null, must not be
     * already in the model, must be unique in the model (means: there is no
     * other node, edge or pin in the model has is equal to this edge) and must
     * not be a Widget
     * @return the widget that is created by attachEdgeWidget; null if the edge
     * is non-visual
     */
    Widget addEdge(E edge);

    /**
     * Adds a node.
     *
     * @param node the node to be added; the node must not be null, must not be
     * already in the model, must be unique in the model (means: there is no
     * other node, edge or pin in the model has is equal to this node) and must
     * not be a Widget
     * @return the widget that is created by attachNodeWidget; null if the node
     * is non-visual
     */
    Widget addNode(N node);

    /**
     * Returns a collection of all edges registered in the graph model.
     *
     * @return the collection of all edges registered in the graph model
     */
    Collection<E> getEdges();

    /**
     * Returns a collection of all nodes registered in the graph model.
     *
     * @return the collection of all nodes registered in the graph model
     */
    Collection<N> getNodes();

    /**
     * Checks whether an object is registered as an edge in the graph model.
     *
     * @param object the object; must not be a Widget
     * @return true, if the object is registered as a edge
     */
    boolean isEdge(Object object);

    /**
     * Checks whether an object is registered as a node in the graph model.
     *
     * @param object the object; must not be a Widget
     * @return true, if the object is registered as a node
     */
    boolean isNode(Object object);

    /**
     * Removes an edge and detaches it from its source and target pins.
     *
     * @param edge the edge to be removed; the edge must not be null and must be
     * already in the model
     */
    void removeEdge(E edge);

    /**
     * Removes a node with all pins that are assigned to the node.
     *
     * @param node the node to be removed; the node must not be null and must be
     * already in the model
     */
    void removeNode(N node);

    /**
     * Removes a node with all pins that are assign to the node and with all
     * edges that are connected to the pins.
     *
     * @param node the node to be removed; the node must not be null and must be
     * already in the model
     */
    void removeNodeWithEdges(N node);

}
