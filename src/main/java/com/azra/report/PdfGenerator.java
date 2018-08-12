package com.azra.report;

import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xml.sax.InputSource;

import java.io.ByteArrayOutputStream;

public class PdfGenerator {

    public static byte[] generatePDF(String url) throws Exception {
        ByteArrayOutputStream pdfByteArrayOutputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocument(url);
        renderer.layout();
        renderer.createPDF(pdfByteArrayOutputStream);
        return pdfByteArrayOutputStream.toByteArray();
    }
}
