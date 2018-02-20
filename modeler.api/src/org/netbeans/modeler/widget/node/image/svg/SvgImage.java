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
package org.netbeans.modeler.widget.node.image.svg;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.batik.util.XMLResourceDescriptor;
import org.openide.util.Exceptions;
import org.w3c.dom.svg.SVGDocument;
import org.xml.sax.SAXException;

/**
 * Immutable class to get the Image representation of a svg resource.
 */
public final class SvgImage {

    // GraphicsNode rootSvgNode;
    SVGDocument svgDocument;
    // BridgeContext bridgeContext = null;

    public SvgImage(InputStream inputStream)
            throws IOException, ParserConfigurationException, SAXException {
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(parser);
        svgDocument = (SVGDocument) factory.createDocument(null, inputStream);
//        svgDocument.getElementById(parser)
    }

    public SvgImage(SVGDocument svgDocument)
            throws IOException {
        this.svgDocument = svgDocument;
    }

//    private GraphicsNode getRootNode(SVGDocument document) {
//        // Build the tree and get the document dimensions
//        UserAgentAdapter userAgentAdapter = new UserAgentAdapter();
//        bridgeContext = new BridgeContext(userAgentAdapter);
//        GVTBuilder builder = new GVTBuilder();
//
//        return builder.build(bridgeContext, document);
//    }
//
//    public GraphicsNode getRootSvgNode() {
//        return rootSvgNode;
//    }
    public SVGDocument getSvgDocument() {
        return svgDocument;
    }

    public Image getImage(double width, double height) throws IOException {

        PNGTranscoder t = new PNGTranscoder();
        t.addTranscodingHint(PNGTranscoder.KEY_WIDTH, (float) width);
        t.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, (float) height);

        t.addTranscodingHint(PNGTranscoder.KEY_FORCE_TRANSPARENT_WHITE, true);

        TranscoderInput input = new TranscoderInput(svgDocument);
        ByteArrayOutputStream ostream = new ByteArrayOutputStream(1000);
        TranscoderOutput output2 = new TranscoderOutput(ostream);
        try {
            // Save the image.
            t.transcode(input, output2);
        } catch (TranscoderException ex) {
            Exceptions.printStackTrace(ex);
        }

        BufferedImage imag = ImageIO.read(new ByteArrayInputStream(ostream.toByteArray()));
        // ImageIO.write(imag, "png", new File(new Date().getTime()+".png"));
        ostream.flush();
        ostream.close();

        return imag;
    }
}
