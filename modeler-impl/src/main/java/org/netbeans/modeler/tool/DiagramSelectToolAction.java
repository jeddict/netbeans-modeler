package org.netbeans.modeler.tool;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import org.netbeans.modeler.resource.toolbar.ImageUtil;
import org.netbeans.modeler.specification.model.document.IModelerScene;

public class DiagramSelectToolAction extends AbstractAction {

    private IModelerScene scene;
    private Cursor cursor;
    private String tool;

    public DiagramSelectToolAction(IModelerScene scene) {
        this(scene, DesignerTools.SELECT, ImageUtil.getInstance().getIcon("selection-arrow.png"), "SelectToolAction", Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR), null, null);
    }

    public DiagramSelectToolAction(IModelerScene scene,
            String tool,
            Icon icon,
            String tooltip,
            Cursor cursor,
            KeyStroke accelerator,
            KeyStroke macAccelerator) {
        this.scene = scene;
        putValue(Action.SMALL_ICON, icon);
        putValue(Action.SHORT_DESCRIPTION, tooltip);
        this.cursor = cursor;
        this.tool = tool;

        putValue(Action.ACCELERATOR_KEY, accelerator);
        // putValue(DiagramInputkeyMapper.MAC_ACCELERATOR, macAccelerator);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        Object eventSource = evt.getSource();

        if (eventSource instanceof JToggleButton) {
            JToggleButton button = (JToggleButton) eventSource;
            if (button.isSelected()) {
                scene.setActiveTool(tool);
                scene.setCursor(cursor);
            } else {
                scene.setActiveTool(DesignerTools.SELECT);
                scene.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        }
    }

}
