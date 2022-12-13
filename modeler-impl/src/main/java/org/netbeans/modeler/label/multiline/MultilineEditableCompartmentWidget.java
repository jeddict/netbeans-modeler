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
package org.netbeans.modeler.label.multiline;

import java.awt.Insets;
import java.awt.Rectangle;
import java.util.EnumSet;
import java.util.Set;
import org.netbeans.api.visual.action.InplaceEditorProvider;
import org.netbeans.api.visual.action.InplaceEditorProvider.EditorController;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modeler.scene.AbstractModelerScene;

/**
 *
 *
 */
public class MultilineEditableCompartmentWidget extends ModelerMultilineLabelWidget {

    private InplaceEditorProvider.EditorController edcAction;

    /**
     * Creates empty label EC will use border of this widget and with model
     * element derived from this widget
     *
     *
     * @param scene
     */
//    public MultilineEditableCompartmentWidget(Scene scene,
//            String propId, String propDisplayName)
//    {
//        this(scene, "", null, (Widget) null, propId, propDisplayName);
//    }
//
//    /**
//     * Creates label with text
//     * EC will use border of this widget and with model element derived from this widget
//     *
//     *
//     * @param scene
//     * @param text - text to label
//     */
//    public MultilineEditableCompartmentWidget(Scene scene, String text,
//            String propId, String propDisplayName)
//    {
//        this(scene, text, null, (Widget) null, propId, propDisplayName);
//    }
//
//    public MultilineEditableCompartmentWidget(Scene scene, IElement modelElement,
//            String propId, String propDisplayName)
//    {
//        this(scene, "", null, modelElement, propId, propDisplayName);
//    }
    /**
     * @param baseGraphWidget border of toFit widget will be considered as
     * bounds for edit control
     * @param basModelWidget will be used to get presentation element (for
     * example name will take class widget and appropriate presentation when
     * attribute will take attribute widget with attribute presentation)
     * @param text - text to label
     */
    public MultilineEditableCompartmentWidget(Scene scene,
            String text,
            Widget baseGraphWidget,
            Widget baseModelWidget,
            String propId,
            String propDisplayName) {
        super(scene, text, propId, propDisplayName);
        setAlignment(Alignment.CENTER);
//         EditControlEditorProvider provider = new EditControlEditorProvider(baseGraphWidget,baseModelWidget);
//        WidgetAction action = ActionFactory.createInplaceEditorAction(provider);

//         final WidgetAction action = ActionFactory.createInplaceEditorAction (new TextFieldInplaceEditor() {
//            public boolean isEnabled (Widget widget) {
//                return true;
//            }
//            public String getText (Widget widget) {
//                return ((LabelWidget) widget).getLabel ();
//            }
//
//            public void setText (Widget widget, String text) {
//                ((LabelWidget) widget).setLabel (text);
//            }
//        });
        // label.getActions ().addAction (inplaceEditorAction);
//        if (action instanceof InplaceEditorProvider.EditorController) {
//            edcAction = (InplaceEditorProvider.EditorController) action;
//        }
//
//        createActions(DesignerTools.SELECT).addAction(action);//TBD need to add lock edit support
    }

    /**
     * @param baseGraphWidget border of toFit widget will be considered as
     * bounds for edit control
     * @param basModelWidget will be used to get presentation element (for
     * example name will take class widget and appropriate presentation when
     * attribute will take attribute widget with attribute presentation)
     * @param text - text to label
     */
//    public MultilineEditableCompartmentWidget(Scene scene,
//            String text,
//            Widget baseGraphWidget,
//            IElement element,
//            String propId,
//            String propDisplayName)
//    {
//        super(scene, text, propId, propDisplayName);
//        edcAction = (InplaceEditorProvider.EditorController) ActionFactory.createInplaceEditorAction(new EditControlEditorProvider(baseGraphWidget, element));
//        createActions(DesignerTools.SELECT).addAction((WidgetAction) edcAction);//TBD need to add lock edit support
//    }
    public void switchToEditMode() {
        //    edcAction.openEditor(this);
    }

    private class EditControlEditorProvider implements InplaceEditorProvider<EditControlImpl> {

        private Widget baseFitWidget;
        private Widget basePresentationWidget;
        //  private IElement modelElement;

        public EditControlEditorProvider() {

        }

        /**
         * @param toFit border of toFit widget will be considered as bounds for
         * edit control
         * @param presentationWidget will be used to get presentation element
         */
        public EditControlEditorProvider(Widget toFit, Widget presentationWidget) {
            baseFitWidget = toFit;
            basePresentationWidget = presentationWidget;
        }

        /**
         * @param toFit border of toFit widget will be considered as bounds for
         * edit control
         * @param element specification of corresponding model element
         */
//        public EditControlEditorProvider(Widget toFit, IElement element)
//        {
//            baseFitWidget = toFit;
//            modelElement = element;
//        }
        @Override
        public void notifyOpened(final EditorController controller, Widget widget, EditControlImpl editor) {
            editor.setVisible(true);
            editor.setAssociatedParent(controller);
        }

        @Override
        public void notifyClosing(EditorController controller,
                Widget widget,
                EditControlImpl editor,
                boolean commit) {
            if ((editor.getModified() == true) && (commit == true)) {
                editor.handleSave();
            }
            editor.setVisible(false);

            // In the case of only allowing the edit control grow down, I want
            // to make sure that the widget is the same hieght after the edit.
//            setPreferredSize(editor.getSize());
//            revalidate();
            if (widget != null) {
                Scene scene = widget.getScene();
                scene.validate();

                //Fix #138735. Reselect the object when finishing editing to update the property sheet.
                if (scene instanceof AbstractModelerScene) {
                    AbstractModelerScene dScene = (AbstractModelerScene) scene;
                    Set<?> selectedObjs = (Set<?>) dScene.getSelectedObjects();
                    if (selectedObjs != null && selectedObjs.size() == 1) {
                        dScene.userSelectionSuggested(selectedObjs, false);
                    }
                }
            }
        }

        @Override
        public EditControlImpl createEditorComponent(EditorController controller, Widget widget) {
            AbstractModelerScene scene = (AbstractModelerScene) widget.getScene();
            Widget toFit = widget;
            if (baseFitWidget != null) {
                toFit = baseFitWidget;
            }
            //DiagramEditControl ret = new DiagramEditControl(toFit, true, controller);
            EditControlImpl ret = new EditControlImpl(controller, true);
            ret.setVisible(true);

            // IElement el = modelElement;
            Widget presW = widget;
//            if (el == null)
//            {
//                if (basePresentationWidget != null)
//                    presW = basePresentationWidget;
//
//                el = ((IPresentationElement) scene.findObject(presW)).getFirstSubject();
//            }
//            ret.setElement(el);
            ret.setFont(getFont());
            ret.setForeColor(widget.getForeground());
            return ret;
        }

        @Override
        public Rectangle getInitialEditorComponentBounds(EditorController controller,
                Widget widget,
                EditControlImpl editor,
                Rectangle viewBounds) {
            Widget toFit = widget;
            if (baseFitWidget != null) {
                toFit = baseFitWidget;
            }

            Rectangle tmp = toFit.getBounds();
            if (getBorder() != null) {
                Insets insets = getBorder().getInsets();

                // I need to adjust by 1 each side, so that I am not on top of the border.
                tmp.x += (insets.left > 0 ? insets.left : 1);
                tmp.y += (insets.top > 0 ? insets.top : 1);
                int deltaLen = insets.right + insets.left;
                tmp.width -= (deltaLen > 0 ? deltaLen : 2);
                deltaLen = insets.bottom + insets.top;
                tmp.height -= (deltaLen > 0 ? deltaLen : 2);
            }
            tmp = toFit.convertLocalToScene(tmp);
            tmp = widget.getScene().convertSceneToView(tmp);

            editor.setMinimumSize(tmp.getSize());
            return tmp;
        }

        @Override
        public EnumSet<ExpansionDirection> getExpansionDirections(EditorController controller,
                Widget widget,
                EditControlImpl editor) {
//            return EnumSet.of(ExpansionDirection.RIGHT, ExpansionDirection.BOTTOM);
//            return null;
            return EnumSet.of(ExpansionDirection.BOTTOM);
        }
    }
}
