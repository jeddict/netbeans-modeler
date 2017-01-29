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
package org.netbeans.modeler.core;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.lang.model.element.TypeElement;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import org.netbeans.api.java.source.ClassIndex;
import org.netbeans.api.java.source.ClasspathInfo;
import org.netbeans.api.java.source.ElementHandle;
import org.netbeans.api.java.source.ui.TypeElementFinder;
import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modeler.resource.toolbar.ImageUtil;
import org.netbeans.modeler.shape.ShapeDesign;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.specification.model.document.INModelerScene;
import org.netbeans.modeler.widget.connection.relation.IRelationProxy;
import org.netbeans.modeler.widget.connection.relation.IRelationValidator;
import org.netbeans.modeler.widget.connection.relation.RelationProxy;
import org.netbeans.modeler.widget.edge.IEdgeWidget;
import org.netbeans.modeler.widget.node.INodeWidget;
import org.netbeans.modeler.widget.node.IWidget;
import org.netbeans.modeler.widget.node.NodeWidgetStatus;
import org.netbeans.modeler.widget.pin.IPinWidget;
import org.openide.util.Exceptions;
import org.openide.util.Pair;
import org.netbeans.editor.Utilities;

public class NBModelerUtil {

    public static void loadModelerFile(ModelerFile file) throws Exception {
        file.getModelerUtil().loadModelerFile(file);
    }

    public static void init(ModelerFile file) {
        file.getModelerUtil().init();
    }

    /**
     * BUG FIX Method : Remove all resize border
     *
     * @param file
     */
    public static void hideAllResizeBorder(ModelerFile file) {
        /* BUG : On Save if any widget is Selected with resize border then [NodeWidget + border] width is calculated as bound */
 /*BUG Fix Start : Hide Resize border of all selected NodeWidget*/
        IModelerScene scene = file.getModelerScene();
        boolean validate = false;
        for (Object o : scene.getSelectedObjects()) {
            if (scene.isNode(o)) {
                Widget w = scene.findWidget(o);
                if (w instanceof INodeWidget) {
                    INodeWidget nodeWidget = (INodeWidget) w;
                    if (nodeWidget.getWidgetBorder() instanceof org.netbeans.modeler.border.ResizeBorder) {
                        nodeWidget.hideResizeBorder();
                        validate = true;
                    }
                }
            }
        }
        if (validate) {
            scene.validate();
        }
        /*BUG Fix End*/

    }

    public static void saveModelerFile(ModelerFile file) {
        if (file.getModelerScene() instanceof INModelerScene) {//not required for IPModelerScene because no manual resize border
            NBModelerUtil.hideAllResizeBorder(file);
        }
        file.getModelerUtil().saveModelerFile(file);
    }

    public static INodeWidget updateNodeWidgetDesign(ShapeDesign shapeDesign, INodeWidget nodeWidget) {
        return nodeWidget.getModelerScene().getModelerFile().getModelerUtil().updateNodeWidgetDesign(shapeDesign, nodeWidget);
    }

    public static void dettachEdgeSourceAnchor(IModelerScene scene, IEdgeWidget edgeWidget, INodeWidget sourceNodeWidget) {
        scene.getModelerFile().getNModelerUtil().dettachEdgeSourceAnchor(scene, edgeWidget, sourceNodeWidget);
    }

    public static void dettachEdgeTargetAnchor(IModelerScene scene, IEdgeWidget edgeWidget, INodeWidget targetNodeWidget) {
        scene.getModelerFile().getNModelerUtil().dettachEdgeTargetAnchor(scene, edgeWidget, targetNodeWidget);
    }

    public static void attachEdgeSourceAnchor(IModelerScene scene, IEdgeWidget edgeWidget, INodeWidget sourceNodeWidget) {
//        if (sourceNodeWidget instanceof INNodeWidget) {
//            scene.getModelerFile().getNModelerUtil().attachEdgeSourceAnchor(scene, edgeWidget, sourceNodeWidget);
//        } else if (sourceNodeWidget instanceof IPNodeWidget) {
//            scene.getModelerFile().getPModelerUtil().attachEdgeSourceAnchor(scene, edgeWidget, sourceNodeWidget);
//        }
        scene.getModelerFile().getModelerUtil().attachEdgeSourceAnchor(scene, edgeWidget, sourceNodeWidget);
    }

    public static void attachEdgeTargetAnchor(IModelerScene scene, IEdgeWidget edgeWidget, INodeWidget targetNodeWidget) {
//        if (targetNodeWidget instanceof INNodeWidget) {
//            scene.getModelerFile().getNModelerUtil().attachEdgeTargetAnchor(scene, edgeWidget, targetNodeWidget);
//        } else if (targetNodeWidget instanceof IPNodeWidget) {
//            scene.getModelerFile().getPModelerUtil().attachEdgeTargetAnchor(scene, edgeWidget, targetNodeWidget);
//        }
        scene.getModelerFile().getModelerUtil().attachEdgeTargetAnchor(scene, edgeWidget, targetNodeWidget);
    }

    public static void dettachEdgeSourceAnchor(IModelerScene scene, IEdgeWidget edgeWidget, IPinWidget sourcePinWidget) {
        scene.getModelerFile().getPModelerUtil().dettachEdgeSourceAnchor(scene, edgeWidget, sourcePinWidget);
    }

    public static void dettachEdgeTargetAnchor(IModelerScene scene, IEdgeWidget edgeWidget, IPinWidget targetPinWidget) {
        scene.getModelerFile().getPModelerUtil().dettachEdgeTargetAnchor(scene, edgeWidget, targetPinWidget);
    }

    public static void attachEdgeSourceAnchor(IModelerScene scene, IEdgeWidget edgeWidget, IPinWidget sourcePinWidget) {
        scene.getModelerFile().getPModelerUtil().attachEdgeSourceAnchor(scene, edgeWidget, sourcePinWidget);
    }

    public static void attachEdgeTargetAnchor(IModelerScene scene, IEdgeWidget edgeWidget, IPinWidget targetPinWidget) {
        scene.getModelerFile().getPModelerUtil().attachEdgeTargetAnchor(scene, edgeWidget, targetPinWidget);
    }

    public static Anchor getAnchor(INodeWidget nodeWidget) {
        return nodeWidget.getModelerScene().getModelerFile().getModelerUtil().getAnchor(nodeWidget);
    }

    public static String getEdgeType(INodeWidget sourceNodeWidget, INodeWidget targetNodeWidget, String connectionContextToolId) {
        return sourceNodeWidget.getModelerScene().getModelerFile().getModelerUtil().getEdgeType(sourceNodeWidget, targetNodeWidget, connectionContextToolId);
    }

    public static Long getAutoGeneratedId() {
        return new Date().getTime();
    }
    private static int sequence = 0;

    public static String getAutoGeneratedStringId() {
        return "_" + new Date().getTime() + ++sequence;
    }

//
    public static void hideContextPalette(IModelerScene scene) {
        if (scene.getContextPaletteManager() != null) {
            scene.getContextPaletteManager().cancelPalette();
        }
    }

    public static void showContextPalette(IModelerScene scene, IWidget widget) {
        if (scene.getContextPaletteManager() != null) {
            scene.getContextPaletteManager().selectionChanged(widget, ((Widget) widget).convertLocalToScene(((Widget) widget).getLocation()));
        }
    }

    public static boolean isValidRelationship(IRelationValidator relationValidator, INodeWidget from, INodeWidget to, String edgeType, Boolean changeStatus) {//gg2

        IRelationProxy relationshipProxy = new RelationProxy();
        relationshipProxy.setSource(from);
        relationshipProxy.setTarget(to);
        relationshipProxy.setEdgeType(edgeType);
//        IRelationValidator validator = new RelationValidator();

        boolean retVal = relationValidator.validateRelation(relationshipProxy);
        if (changeStatus) {
            if (retVal) {
                relationshipProxy.getTarget().setStatus(NodeWidgetStatus.VALID);
            } else {
                relationshipProxy.getTarget().setStatus(NodeWidgetStatus.INVALID);
            }

            JComponent comp = (JComponent) relationshipProxy.getTarget().getModelerScene().getModelerPanelTopComponent();
            if (retVal) {
                if (comp.getCursor() != NBModelerUtil.VALID_CONNECTION) {
                    comp.setCursor(NBModelerUtil.VALID_CONNECTION);
                }
            } else if (comp.getCursor() != NBModelerUtil.INVALID_CONNECTION) {
                comp.setCursor(NBModelerUtil.INVALID_CONNECTION);
            }

        }
        return retVal;
    }

    public static Collection<ConnectionWidget> getAllContainedEdges(Widget widget) {
        HashSet<ConnectionWidget> set = new HashSet<ConnectionWidget>();
        Scene scene = widget.getScene();
        if (scene instanceof GraphScene) {
            GraphScene gs = (GraphScene) scene;

            List<Object> nodeChildren = getAllNodeChildren(widget);
            for (Object obj : nodeChildren) {
                Collection edges = gs.findNodeEdges(obj, true, true);
                for (Object e : edges) {
                    Object source = gs.getEdgeSource(e);
                    Object target = gs.getEdgeTarget(e);
                    if (nodeChildren.contains(source) && nodeChildren.contains(target)) {
                        set.add((ConnectionWidget) gs.findWidget(e));
                    }
                }
            }
        }
        return set;
    }

    public static List<Object> getAllNodeChildren(Widget widget) {
        if (!(widget.getScene() instanceof GraphScene)) {
            return new ArrayList<Object>();
        }

        return getAllNodeChildrenRecursive(new ArrayList<Object>(), widget);
    }

    private static List<Object> getAllNodeChildrenRecursive(List<Object> list, Widget widget) {
        for (Widget child : widget.getChildren()) {
            Object pe = ((GraphScene) widget.getScene()).findObject(child);
            if (((GraphScene) widget.getScene()).isNode(pe)) {
                list.add(pe);
            }

            list = getAllNodeChildrenRecursive(list, child);
        }
        return list;
    }
    public static final Cursor VALID_CONNECTION;
    public static final Cursor INVALID_CONNECTION;

    static {
        VALID_CONNECTION = getValidConnectCursor();
        INVALID_CONNECTION = getInvalidConnectCursor();
    }

    public static Cursor getValidConnectCursor() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = ImageUtil.getInstance().getImage("valid_connect.gif");
        Point hotspot = new Point(0, 0);
        return toolkit.createCustomCursor(image, hotspot, "VALID_CONNECTION");
    }

    public static Cursor getInvalidConnectCursor() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = ImageUtil.getInstance().getImage("invalid_connect.gif");
        Point hotspot = new Point(0, 0);
        return toolkit.createCustomCursor(image, hotspot, "INVALID_CONNECTION");
    }

    /*
     * get align path between two point using horizontal or vetical distance by buffer
     */

    public static Point getComputedPoint(Point oppositeLocation, Point relatedLocation, int buffer) {
        Point point_A = oppositeLocation;
        Point point_B = relatedLocation;
        int x = point_B.x, y = point_B.y;
        if (Math.abs(point_A.x - point_B.x) < buffer) {
            x = point_A.x;
        }
        if (Math.abs(point_A.y - point_B.y) < buffer) {
            y = point_A.y;
        }
        return new Point(x, y);
    }

    public static Pair<JScrollPane, JEditorPane> getJavaSingleLineEditor(JComponent comp, String className, String tooltipText) {
        JComponent [] editorComponents = Utilities.createSingleLineEditor("text/x-java-nbdebug-class");
        JScrollPane sle = (JScrollPane) editorComponents[0];
        JEditorPane epClassName = (JEditorPane) editorComponents[1];
        epClassName.setText(className);
        if(comp!=null){
            comp.removeAll();
            comp.setLayout(new java.awt.GridLayout());
            comp.add(sle);
        }
        sle.setToolTipText(tooltipText);
        epClassName.setToolTipText(tooltipText);
        return Pair.<JScrollPane, JEditorPane>of(sle, epClassName);
    }
    
    public static String browseClass(ModelerFile modelerFile) {
        return browseClass(modelerFile, null);
    }
    
    public static String browseClass(ModelerFile modelerFile, String defaultClass) {
    ElementHandle<TypeElement> handle = TypeElementFinder.find(ClasspathInfo.create(modelerFile.getFileObject()), defaultClass, new TypeElementFinder.Customizer() {
            @Override
            public Set<ElementHandle<TypeElement>> query(ClasspathInfo classpathInfo, String textForQuery, ClassIndex.NameKind nameKind, Set<ClassIndex.SearchScope> searchScopes) {
                return classpathInfo.getClassIndex().getDeclaredTypes(textForQuery, nameKind, searchScopes);
            }

            @Override
            public boolean accept(ElementHandle<TypeElement> typeHandle) {
                return true;
            }
        });
        return handle != null ? handle.getQualifiedName() : "";
    }

    public static boolean isEmptyObject(Object obj) {
        boolean empty = true;
        if (obj != null) {
            for (Field f : obj.getClass().getDeclaredFields()) {
                try {
                    f.setAccessible(true);
                    if (f.get(obj) != null) {
                        empty = false;
                    }
                    f.setAccessible(false);
                } catch (IllegalArgumentException ex) {
                    Exceptions.printStackTrace(ex);
                } catch (IllegalAccessException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }
        return empty;
    }

}
