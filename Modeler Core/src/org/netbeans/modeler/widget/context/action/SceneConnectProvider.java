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
package org.netbeans.modeler.widget.context.action;

import java.awt.Cursor;
import java.awt.Point;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modeler.core.ModelerFile;
import org.netbeans.modeler.core.NBModelerUtil;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.specification.model.document.INModelerScene;
import org.netbeans.modeler.specification.model.document.IPModelerScene;
import org.netbeans.modeler.specification.model.util.PModelerUtil;
import org.netbeans.modeler.widget.connection.relation.IRelationValidator;
import org.netbeans.modeler.widget.context.RelationshipFactory;
import org.netbeans.modeler.widget.edge.IEdgeWidget;
import org.netbeans.modeler.widget.edge.info.EdgeWidgetInfo;
import org.netbeans.modeler.widget.node.INodeWidget;
import org.netbeans.modeler.widget.node.IPNodeWidget;
import org.netbeans.modeler.widget.node.NodeWidgetStatus;
import org.openide.windows.TopComponent;

/**
 *
 *
 */
public class SceneConnectProvider implements ExConnectProvider {

    private RelationshipFactory factory = null;
    private String connectionContextToolId;

    /**
     * Creates a new instance of SceneConnectProvider. The provider will create
     * an edge of type edgeType. If the user drops the edge on the diagram as
     * opposed to a node, the targetType will be used to create a new node.
     *
     * @param edgeType the type of edge to create.
     * @param targetType the type of node to create if the user releases over
     * the diagram.
     */
    public SceneConnectProvider(String connectionContextToolId) {
        this.connectionContextToolId = connectionContextToolId;
    }

    public SceneConnectProvider() {
    }

    @Override
    public boolean isSourceWidget(Widget sourceWidget) {
        return sourceWidget instanceof INodeWidget;
    }

    @Override
    public ConnectorState isTargetWidget(Widget sourceWidget, Widget targetWidget) {
        //ConnectorState retVal = ConnectorState.ACCEPT;
        ConnectorState retVal = ConnectorState.REJECT;
        if (!(targetWidget instanceof IModelerScene)) {
            if (sourceWidget.getScene() instanceof IModelerScene) {
                IModelerScene modelerScene = (IModelerScene) sourceWidget.getScene();
                IRelationValidator relationValidator = modelerScene.getModelerFile().getModelerDiagramModel().getRelationValidator();

                INodeWidget source = null, target = null;

                if (sourceWidget instanceof INodeWidget) {
                    source = (INodeWidget) sourceWidget;
                }
                if (targetWidget instanceof INodeWidget) {
                    target = (INodeWidget) targetWidget;
                }
                if (((source != null) && (target != null)) /*&& (source != target)*/) {
                    INodeWidget from = source;//.getFirstSubject();
                    INodeWidget to = target;//.getFirstSubject();
                    NBModelerUtil.isValidRelationship(relationValidator, from, to, connectionContextToolId, true);

                    retVal = ConnectorState.ACCEPT;

                }

                if (retVal == ConnectorState.ACCEPT) {
                    if (source != target && isSourceParent(sourceWidget, targetWidget) == true) {
                        retVal = ConnectorState.REJECT;
                    }
                }
            }
        } else {
            ((TopComponent) ((IModelerScene) targetWidget).getModelerPanelTopComponent()).setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            if (((IModelerScene) targetWidget).getInvalidNodeWidget() != null) {
                ((IModelerScene) targetWidget).getInvalidNodeWidget().setStatus(NodeWidgetStatus.NONE);
            }
            if (((IModelerScene) targetWidget).getValidNodeWidget() != null) {
                ((IModelerScene) targetWidget).getValidNodeWidget().setStatus(NodeWidgetStatus.NONE);
            }

        }

        return retVal;
    }

    @Override
    public boolean hasCustomTargetWidgetResolver(Scene scene) {
        return false;
    }

    @Override
    public Widget resolveTargetWidget(Scene scene, Point sceneLocation) {
        return null;
    }

    @Override
    public void createConnection(Widget sourceWidget, Widget targetWidget) {
//        if ((validator != null) && (sourceWidget.getScene() instanceof GraphScene))
//        {
        IModelerScene scene = (IModelerScene) sourceWidget.getScene();
        INodeWidget sourceElement = (INodeWidget) sourceWidget;//getElement(sourceWidget);
        INodeWidget targetElement = (INodeWidget) targetWidget;//getElement(targetWidget);
        createConnection(scene, sourceElement, targetElement);
        scene.getModelerPanelTopComponent().changePersistenceState(false);
        //}
    }

    public IEdgeWidget createConnection(IModelerScene scene, INodeWidget source, INodeWidget target) {
        IEdgeWidget edgeWidget = null;
        EdgeWidgetInfo edgeInfo = new EdgeWidgetInfo();
        edgeInfo.setId("_" + NBModelerUtil.getAutoGeneratedId().toString());
        edgeInfo.setName("");
        if (target != null) {
            edgeInfo.setSource(source.getNodeWidgetInfo().getId());
            edgeInfo.setTarget(target.getNodeWidgetInfo().getId());
            edgeInfo.setType(NBModelerUtil.getEdgeType(source, target, connectionContextToolId));

            ModelerFile file = scene.getModelerFile();
            if (!NBModelerUtil.isValidRelationship(file.getModelerDiagramModel().getRelationValidator(), source, target, edgeInfo.getType(), false)) {
                return null;
            }
            edgeWidget = scene.createEdgeWidget(edgeInfo);

            if (scene instanceof INModelerScene) {
                ((INModelerScene) scene).setEdgeWidgetSource(edgeInfo, source.getNodeWidgetInfo());
                ((INModelerScene) scene).setEdgeWidgetTarget(edgeInfo, target.getNodeWidgetInfo());
            } else if (scene instanceof IPModelerScene) {
                if (source instanceof IPNodeWidget && target instanceof IPNodeWidget) {
                    IPNodeWidget pSourceNodeWidget = (IPNodeWidget) source;
                    IPNodeWidget pTargetNodeWidget = (IPNodeWidget) target;
                    PModelerUtil modelerUtil = ((PModelerUtil) scene.getModelerFile().getModelerUtil());
                    ((IPModelerScene) scene).setEdgeWidgetSource(edgeInfo, modelerUtil.getEdgeSourcePinWidget(pSourceNodeWidget, pTargetNodeWidget, edgeWidget));
                    ((IPModelerScene) scene).setEdgeWidgetTarget(edgeInfo, modelerUtil.getEdgeTargetPinWidget(pSourceNodeWidget, pTargetNodeWidget, edgeWidget));
                }
            }
        }
        return edgeWidget;
    }

    public RelationshipFactory getRelationshipFactory() {
        return factory;
    }

    public void setRelationshipFactory(RelationshipFactory factory) {
        this.factory = factory;
    }

//    protected INodeWidget createNodePresentationElement(INodeWidget element)
//    {
//        INodeWidget retVal = null;
//
//        ICreationFactory creationFactory = FactoryRetriever.instance().getCreationFactory();
//        if (creationFactory != null)
//        {
//            Object presentationObj = creationFactory.retrieveMetaType("NodePresentation", null);
//            if (presentationObj instanceof INodeWidget)
//            {
//                retVal = (INodeWidget) presentationObj;
//                retVal.addSubject(element);
//            }
//        }
//
//        return retVal;
//    }
    private INodeWidget getElement(Widget widget) {
        INodeWidget retVal = null;

        Scene widgetScene = widget.getScene();
        if (widgetScene instanceof GraphScene) {
            GraphScene objScene = (GraphScene) widgetScene;
            Object value = objScene.findObject(widget);
            if (value instanceof INodeWidget) {
                retVal = findNode(objScene, (INodeWidget) value);
            }
        }

        return retVal;
    }

//    protected INodeWidget createEdgePresentationElement(INodeWidget element)
//    {
//        INodeWidget retVal = null;
//
//        ICreationFactory creationFactory = FactoryRetriever.instance().getCreationFactory();
//        if (creationFactory != null)
//        {
//            Object presentationObj = creationFactory.retrieveMetaType("NodePresentation", null);
//            if (presentationObj instanceof INodeWidget)
//            {
//                retVal = (INodeWidget) presentationObj;
//                retVal.addSubject(element);
//            }
//        }
//
//        return retVal;
//    }
    protected INodeWidget findNode(GraphScene scene,
            Object target) {
        INodeWidget retVal = null;

        if ((scene != null) && (target != null)) {
            if ((scene.isNode(target) == true)
                    && (target instanceof INodeWidget)) {
                retVal = (INodeWidget) target;
            } else {
                Widget targetWidget = scene.findWidget(target);
                retVal = findNode(scene, scene.findObject(targetWidget.getParentWidget()));
            }
        }

        return retVal;
    }

    private boolean isSourceParent(Widget sourceWidget, Widget targetWidget) {
        boolean retVal = false;

        if ((sourceWidget != null) && (targetWidget != null)) {
            if (sourceWidget.equals(targetWidget) == false) {
                retVal = isSourceParent(sourceWidget.getParentWidget(), targetWidget);
            } else {
                retVal = true;
            }
        }

        return retVal;
    }
}
