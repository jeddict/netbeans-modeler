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
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import javax.swing.SwingUtilities;
import org.netbeans.modeler.component.IModelerPanel;
import org.netbeans.modeler.component.ModelerPanelTopComponent;
import org.netbeans.modeler.core.IModelerDiagramEngine;
import org.netbeans.modeler.core.ModelerCore;
import org.netbeans.modeler.core.ModelerFile;
import org.netbeans.modeler.core.NBModelerUtil;
import org.netbeans.modeler.core.engine.ModelerDiagramEngine;
import org.netbeans.modeler.file.IModelerFileDataObject;
import org.netbeans.modeler.specification.Vendor;
import org.netbeans.modeler.specification.annotaton.ModelerConfig;
import org.netbeans.modeler.specification.export.IExportManager;
import org.netbeans.modeler.specification.model.DiagramModel;
import org.netbeans.modeler.specification.model.document.IModelerScene;
import org.netbeans.modeler.specification.model.document.IRootElement;
import org.netbeans.modeler.specification.model.util.IModelerUtil;
import org.netbeans.modeler.widget.connection.relation.IRelationValidator;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;

public abstract class ModelerFileActionListener implements ActionListener {

    protected IModelerFileDataObject context;

    public ModelerFileActionListener(IModelerFileDataObject context) {
        this.context = context;
    }

    public ModelerFileActionListener() {
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        SwingUtilities.invokeLater(() -> {
            long st = new Date().getTime();
            openModelerFile();
            System.out.println("Total time : " + (new Date().getTime() - st) + " sec");
        });
    }

    public void openModelerFile() {
        openModelerFile(null, null, null);
    }

    public void openModelerFile(ModelerFile parentFile) {
        openModelerFile(null, null, null, parentFile);
    }

    public void openModelerFile(String id, String name, String tooltip) { //id :=> if file contains multiple modeler file then each modeler file dom has own that represent it as an single modeler file
        openModelerFile(id, name, tooltip, null);
    }

    public void openModelerFile(String id, String name, String tooltip, ModelerFile parentFile) { //id :=> if file contains multiple modeler file then each modeler file dom has own that represent it as an single modeler file
        long st = new Date().getTime();
        if (context == null) {
            if (parentFile == null) {
                throw new IllegalStateException("ModelerFileDataObject(context) and Parent Modeler file does not exist");
            }
            context = parentFile.getModelerFileDataObject();
        }
        FileObject fileObject = context.getPrimaryFile();
        String path = fileObject.getPath();
        String absolutePath;
        if (id == null) {
            absolutePath = path;
        } else {
            absolutePath = path + "#" + id;
        }
        final ModelerFile modelerFile = ModelerCore.getModelerFile(absolutePath) == null ? new ModelerFile() : ModelerCore.getModelerFile(absolutePath);

        if (modelerFile.getPath() == null) { // if new modeler file
            try {
                CountDownLatch latch = new CountDownLatch(5);

                modelerFile.setId(id);
                modelerFile.setParentFile(parentFile);
                if (parentFile != null) {
                    parentFile.addChildrenFile(modelerFile);
                }
                modelerFile.setModelerFileDataObject(context);
                modelerFile.setTooltip(path);
                modelerFile.setPath(absolutePath);
                if (name != null) {
                    modelerFile.setName(name);
                }
                if (tooltip != null) {
                    modelerFile.setTooltip(tooltip);
                }
                ModelerCore.addModelerFile(absolutePath, modelerFile);

                System.out.println("TLTIP Total time : " + (new Date().getTime() - st) + " sec");
                st = new Date().getTime();
                //VendorSpecification,ModelerDiagramSpecification

                Class _class = this.getClass();
                final ModelerConfig modelerConfig = (ModelerConfig) _class.getAnnotation(ModelerConfig.class);
                final org.netbeans.modeler.specification.annotaton.Vendor vendorConfig = (org.netbeans.modeler.specification.annotaton.Vendor) _class.getAnnotation(org.netbeans.modeler.specification.annotaton.Vendor.class);
                final org.netbeans.modeler.specification.annotaton.DiagramModel diagramModelConfig = (org.netbeans.modeler.specification.annotaton.DiagramModel) _class.getAnnotation(org.netbeans.modeler.specification.annotaton.DiagramModel.class);

                Class<? extends IModelerScene> modelerScene = diagramModelConfig.modelerScene();//ModelerScene
                IModelerScene scene = modelerScene.newInstance();
                scene.setModelerFile(modelerFile);
                modelerFile.getVendorSpecification().getModelerDiagramModel().setModelerScene(scene);

                System.out.println("InSpec I Total time : " + (new Date().getTime() - st) + " sec");
                st = new Date().getTime();
                new InitExecuter(latch, modelerFile, modelerConfig, vendorConfig, diagramModelConfig).start();
                new ModelerUtilExecuter(latch, modelerFile, modelerConfig, vendorConfig, diagramModelConfig).start();
                new PaletteConfigExecuter(latch, modelerFile, modelerConfig, vendorConfig, diagramModelConfig).start();
                new InstanceExecuter(latch, modelerFile, modelerConfig, vendorConfig, diagramModelConfig).start();//Top Component
                new DiagramEngineExecuter(latch, modelerFile, modelerConfig, vendorConfig, diagramModelConfig).start();
                //1    260,   428      304
                //2    4180,  3314     3206
                //3    290,   364      348
                //4    1186,  2043     1076
                //5    3921,  2966     3022
                //===========================
                //final 4192,  3326     3214

                latch.await();
                System.out.println("CountDownLatch Total time : " + (new Date().getTime() - st) + " sec");
                initSpecification(modelerFile);
                st = new Date().getTime();
                scene.getModelerPanelTopComponent().init(modelerFile);
                scene.getModelerPanelTopComponent().open();
                scene.getModelerPanelTopComponent().requestActive();
                System.out.println("TC RA Total time : " + (new Date().getTime() - st) + " sec");
                st = new Date().getTime();
                NBModelerUtil.loadModelerFile(modelerFile);

                modelerFile.getModelerScene().init(); //color scehme depends on entitymapping
                System.out.println("lmf Total time : " + (new Date().getTime() - st) + " sec");
                
                modelerFile.loaded();
            } catch (InstantiationException | IllegalAccessException | InterruptedException ex) {
                Exceptions.printStackTrace(ex);
            }

        } else {
            modelerFile.getModelerScene().getModelerPanelTopComponent().requestActive();
        }

    }

    protected abstract void initSpecification(ModelerFile modelerFile);

    class InstanceExecuter extends Thread {

        private CountDownLatch latch;
        private ModelerFile modelerFile;
        private ModelerConfig modelerConfig;
        private org.netbeans.modeler.specification.annotaton.Vendor vendorConfig;
        private org.netbeans.modeler.specification.annotaton.DiagramModel diagramModelConfig;

        public InstanceExecuter(CountDownLatch latch, ModelerFile modelerFile, ModelerConfig modelerConfig,
                org.netbeans.modeler.specification.annotaton.Vendor vendorConfig, org.netbeans.modeler.specification.annotaton.DiagramModel diagramModelConfig) {
            this.latch = latch;
            this.modelerFile = modelerFile;
            this.modelerConfig = modelerConfig;
            this.vendorConfig = vendorConfig;
            this.diagramModelConfig = diagramModelConfig;
        }

        @Override
        public void run() {

            try {
                long st = new Date().getTime();

                modelerFile.getVendorSpecification().setVendor(new Vendor(vendorConfig.id(), vendorConfig.version(), vendorConfig.name(), vendorConfig.displayName()));
                modelerFile.getVendorSpecification().getModelerDiagramModel().setDiagramModel(new DiagramModel(diagramModelConfig.id(), diagramModelConfig.name()));

                Class<? extends IModelerPanel> modelerPanel = diagramModelConfig.modelerPanel();
                if (modelerPanel != IModelerPanel.class) {
                    modelerFile.getModelerScene().setModelerPanelTopComponent(modelerPanel.newInstance());
                } else {
                    modelerFile.getModelerScene().setModelerPanelTopComponent(new ModelerPanelTopComponent());
                }
                Class<? extends IRelationValidator> relationValidator = diagramModelConfig.relationValidator();
                modelerFile.getVendorSpecification().getModelerDiagramModel().setRelationValidator(relationValidator.newInstance());

                Class<? extends IExportManager> exportManager = diagramModelConfig.exportManager();
                modelerFile.getVendorSpecification().getModelerDiagramModel().setExportManager(exportManager.newInstance());

                System.out.println("E1 B3B Total time : " + (new Date().getTime() - st) + " sec");
            } catch (InstantiationException | IllegalAccessException ex) {
                Exceptions.printStackTrace(ex);
            } finally {
                long st = new Date().getTime();
                latch.countDown();
                System.out.println("E1 B3A Total time : " + (new Date().getTime() - st) + " sec");
            }

        }
    }

    class InitExecuter extends Thread {

        private CountDownLatch latch;
        private ModelerFile modelerFile;
        private ModelerConfig modelerConfig;
        private org.netbeans.modeler.specification.annotaton.Vendor vendorConfig;
        private org.netbeans.modeler.specification.annotaton.DiagramModel diagramModelConfig;

        public InitExecuter(CountDownLatch latch, ModelerFile modelerFile, ModelerConfig modelerConfig,
                org.netbeans.modeler.specification.annotaton.Vendor vendorConfig, org.netbeans.modeler.specification.annotaton.DiagramModel diagramModelConfig) {
            this.latch = latch;
            this.modelerFile = modelerFile;
            this.modelerConfig = modelerConfig;
            this.vendorConfig = vendorConfig;
            this.diagramModelConfig = diagramModelConfig;
        }

        @Override
        public void run() {
            try {
                long st = new Date().getTime();
                // #A
                modelerFile.getVendorSpecification().createElementConfig(vendorConfig.id(), modelerConfig.element());//130 sec
//both lines A & B are parallel now
                // #B
//                modelerFile.getVendorSpecification().getModelerDiagramModel().init(modelerFile);//load empty configuration //override it in loadModelerFile() if already have //depends on ModelerScene,ElementConfigFactory
                System.out.println("E2 B3B Total time : " + (new Date().getTime() - st) + " sec");

            } finally {
                long st = new Date().getTime();
                latch.countDown();
                System.out.println("E2 B3A Total time : " + (new Date().getTime() - st) + " sec");
            }
        }
    }

    class DiagramEngineExecuter extends Thread {

        private CountDownLatch latch;

        private ModelerFile modelerFile;
        private ModelerConfig modelerConfig;
        private org.netbeans.modeler.specification.annotaton.Vendor vendorConfig;
        private org.netbeans.modeler.specification.annotaton.DiagramModel diagramModelConfig;

        public DiagramEngineExecuter(CountDownLatch latch, ModelerFile modelerFile, ModelerConfig modelerConfig,
                org.netbeans.modeler.specification.annotaton.Vendor vendorConfig, org.netbeans.modeler.specification.annotaton.DiagramModel diagramModelConfig) {
            this.latch = latch;
            this.modelerFile = modelerFile;
            this.modelerConfig = modelerConfig;
            this.vendorConfig = vendorConfig;
            this.diagramModelConfig = diagramModelConfig;
        }

        @Override
        public void run() {
            try {
                long st = new Date().getTime();

                Class<? extends IModelerDiagramEngine> modelerDiagramEngine = diagramModelConfig.modelerDiagramEngine();
                if (modelerDiagramEngine != IModelerDiagramEngine.class) {
                    modelerFile.getVendorSpecification().getModelerDiagramModel().setModelerDiagramEngine(modelerDiagramEngine.newInstance());
                } else {
                    modelerFile.getVendorSpecification().getModelerDiagramModel().setModelerDiagramEngine(new ModelerDiagramEngine());
                }

                modelerFile.getModelerDiagramEngine().init(modelerFile);
                modelerFile.getModelerDiagramEngine().setModelerSceneAction();

                System.out.println("E3 B3B Total time : " + (new Date().getTime() - st) + " sec");

            } catch (InstantiationException | IllegalAccessException ex) {
                Exceptions.printStackTrace(ex);
            } finally {
                long st = new Date().getTime();
                latch.countDown();
                System.out.println("E3 B3A Total time : " + (new Date().getTime() - st) + " sec");
            }

        }
    }

    class PaletteConfigExecuter extends Thread {

        private CountDownLatch latch;

        private ModelerFile modelerFile;
        private ModelerConfig modelerConfig;
        private org.netbeans.modeler.specification.annotaton.Vendor vendorConfig;
        private org.netbeans.modeler.specification.annotaton.DiagramModel diagramModelConfig;

        public PaletteConfigExecuter(CountDownLatch latch, ModelerFile modelerFile, ModelerConfig modelerConfig,
                org.netbeans.modeler.specification.annotaton.Vendor vendorConfig, org.netbeans.modeler.specification.annotaton.DiagramModel diagramModelConfig) {
            this.latch = latch;
            this.modelerFile = modelerFile;
            this.modelerConfig = modelerConfig;
            this.vendorConfig = vendorConfig;
            this.diagramModelConfig = diagramModelConfig;
        }

        @Override
        public void run() {
            try {
                long st = new Date().getTime();

                modelerFile.getVendorSpecification().createModelerDocumentConfig(vendorConfig.id(), modelerConfig.document());//141 sec
                modelerFile.getVendorSpecification().createPaletteConfig(vendorConfig.id(), diagramModelConfig.id(), modelerConfig.palette());//67 sec //depends on docFac

                System.out.println("E4 B3B Total time : " + (new Date().getTime() - st) + " sec");

            } finally {
                long st = new Date().getTime();
                latch.countDown();
                System.out.println("E4 B3A Total time : " + (new Date().getTime() - st) + " sec");
            }

        }
    }

    class ModelerUtilExecuter extends Thread {

        private CountDownLatch latch;

        private ModelerFile modelerFile;
        private ModelerConfig modelerConfig;
        private org.netbeans.modeler.specification.annotaton.Vendor vendorConfig;
        private org.netbeans.modeler.specification.annotaton.DiagramModel diagramModelConfig;

        public ModelerUtilExecuter(CountDownLatch latch, ModelerFile modelerFile, ModelerConfig modelerConfig,
                org.netbeans.modeler.specification.annotaton.Vendor vendorConfig, org.netbeans.modeler.specification.annotaton.DiagramModel diagramModelConfig) {
            this.latch = latch;
            this.modelerFile = modelerFile;
            this.modelerConfig = modelerConfig;
            this.vendorConfig = vendorConfig;
            this.diagramModelConfig = diagramModelConfig;
        }

        @Override
        public void run() {
            try {
                long st = new Date().getTime();
                Class<? extends IModelerUtil> modelerUtil = diagramModelConfig.modelerUtil();
                modelerFile.getVendorSpecification().getModelerDiagramModel().setModelerUtil(modelerUtil.newInstance());
                NBModelerUtil.init(modelerFile);

                System.out.println("E5 B3B Total time : " + (new Date().getTime() - st) + " sec");
            } catch (InstantiationException | IllegalAccessException ex) {
                Exceptions.printStackTrace(ex);
            } finally {
                long st = new Date().getTime();
                latch.countDown();
                System.out.println("E5 B3A Total time : " + (new Date().getTime() - st) + " sec");
            }

        }
    }

}
