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
package org.netbeans.modeler.specification.model.file.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;
import org.netbeans.modeler.core.ModelerCore;
import org.netbeans.modeler.core.ModelerFile;
import org.netbeans.modeler.core.NBModelerUtil;
import org.netbeans.modeler.file.IModelerFileDataObject;
import org.netbeans.modeler.specification.Vendor;
import org.netbeans.modeler.specification.annotaton.ModelerConfig;
import org.netbeans.modeler.specification.model.DiagramModel;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.openide.filesystems.FileObject;

public abstract class ModelerFileActionListener implements ActionListener {

    private final IModelerFileDataObject context;

    public ModelerFileActionListener(IModelerFileDataObject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                openModelerFile();
            }
        });
    }

//    public static void openModelerFile(FileObject context) {
//        org.openide.loaders.DataObject datObject = null;
//        try {
//            datObject = org.openide.loaders.DataObject.find(context);
//        } catch (DataObjectNotFoundException ex) {
//            Exceptions.printStackTrace(ex);
//        }
//        if (datObject == null || !(datObject instanceof IModelerFileDataObject)) {
//            throw new IllegalStateException("Invalid Modeler File");
//        }
//        ModelerFileActionListener actionListener = new ModelerFileActionListener((IModelerFileDataObject) datObject);
//        actionListener.actionPerformed(null);
//
//    }
    
    public void openModelerFile() {
        openModelerFile(null,null,null);
    }
    public void openModelerFile(String id , String name , String tooltip) { //id :=> if file contains multiple modeler file then each modeler file dom has own that represent it as an single modeler file

        FileObject fileObject = context.getPrimaryFile();
        String path = fileObject.getPath();
        
        String absolutePath;
        if(id==null){
            absolutePath = path;
        } else {
            absolutePath = path + "#" + id;
        }
        ModelerFile modelerFile = ModelerCore.getModelerFile(absolutePath);
        
        if (modelerFile == null) {
            modelerFile = new ModelerFile();
            modelerFile.setId(id);
            modelerFile.setModelerFileDataObject(context);
            modelerFile.setTooltip(path);
            modelerFile.setPath(absolutePath);

            if(name!=null){
                modelerFile.setName(name);
            }
            if(tooltip!=null){
                modelerFile.setTooltip(tooltip);
            }
            
            initSpecification(modelerFile);

            Class _class = this.getClass();
            ModelerConfig modelerConfig = (ModelerConfig) _class.getAnnotation(ModelerConfig.class);
            org.netbeans.modeler.specification.annotaton.Vendor vendorConfig = (org.netbeans.modeler.specification.annotaton.Vendor) _class.getAnnotation(org.netbeans.modeler.specification.annotaton.Vendor.class);
            org.netbeans.modeler.specification.annotaton.DiagramModel diagramModelConfig = (org.netbeans.modeler.specification.annotaton.DiagramModel) _class.getAnnotation(org.netbeans.modeler.specification.annotaton.DiagramModel.class);

            modelerFile.getVendorSpecification().setVendor(new Vendor(vendorConfig.id(), vendorConfig.version(), vendorConfig.name(), vendorConfig.displayName()));
            modelerFile.getVendorSpecification().getModelerDiagramModel().setDiagramModel(new DiagramModel(diagramModelConfig.id(), diagramModelConfig.name()));

            modelerFile.getVendorSpecification().createElementConfig(vendorConfig.id(), modelerConfig.element());
            modelerFile.getVendorSpecification().createModelerDocumentConfig(vendorConfig.id(), modelerConfig.document());
            modelerFile.getVendorSpecification().createPaletteConfig(vendorConfig.id(), diagramModelConfig.id(), modelerConfig.palette());

            modelerFile.getVendorSpecification().getModelerDiagramModel().init(modelerFile);//load empty configuration //override it in loadModelerFile() if already have
            modelerFile.getVendorSpecification().getModelerDiagramModel().getModelerDiagramEngine().init(modelerFile);

            IModelerScene scene = modelerFile.getVendorSpecification().getModelerDiagramModel().getModelerScene();

            scene.setModelerFile(modelerFile);
            scene.setModelerPanelTopComponent(modelerFile.getModelerPanelTopComponent());
            scene.setModelerDiagramEngine(modelerFile.getModelerDiagramEngine());
            modelerFile.getModelerDiagramEngine().setModelerSceneAction();

            modelerFile.getVendorSpecification().getModelerDiagramModel().getModelerPanelTopComponent().init(modelerFile);

            ModelerCore.addModelerFile(absolutePath, modelerFile);
            modelerFile.getVendorSpecification().getModelerDiagramModel().getModelerPanelTopComponent().open();
            modelerFile.getVendorSpecification().getModelerDiagramModel().getModelerPanelTopComponent().requestActive();

            NBModelerUtil.loadModelerFile(modelerFile);
            modelerFile.getVendorSpecification().getModelerDiagramModel().getModelerScene().init();

        } else {
            modelerFile.getVendorSpecification().getModelerDiagramModel().getModelerPanelTopComponent().requestActive();
        }

    }

    protected abstract void initSpecification(ModelerFile modelerFile);

}
