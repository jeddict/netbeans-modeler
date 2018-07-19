package org.netbeans.modeler.tool;

import java.util.HashMap;
import javax.swing.JToggleButton;

public interface DesignerTools {

    public static final String SELECT = "SELECT";
    public static final String PAN = "PAN";
    public static final String CONTEXT_PALETTE = "CONTEXT PALETTE";
    public static final String PALETTE = "PALETTE";
    public static final String MARQUEE_ZOOM = "MARQUEE_ZOOM";
    public static final String INTERACTIVE_ZOOM = "INTERACTIVE_ZOOM";
    public static final String NAVIGATE_LINK = "NAVIGATE_LINK";
    public static final String READ_ONLY = "READ_ONLY";
    public HashMap<String, JToggleButton> mapToolToButton = new HashMap<String, JToggleButton>();
}
