package com.bittle.studycards.utils;

import com.bittle.studycards.Card;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

// build the final pdf from a list of cards
public class PDFBuilder {
    private List<Card> cards;
    private int maxCardRows;
    private boolean centerText = false;
    private double fontSize = 12;
    private String outputDir = System.getProperty("user.home");

    public PDFBuilder(List<Card> cards) {
        if (cards.size() % 2 != 0)
            cards.add(new Card("", ""));
        this.cards = cards;
        maxCardRows = 9;
    }

    // options for user
    public PDFBuilder maxCardRows(int num) {
        maxCardRows = num;
        if (maxCardRows > 10) {
            System.out.println("For styling purposes, restrain from using an integer larger than 10 for the rows.");
        }
        return this;
    }

    public PDFBuilder centerText(boolean bool){
        centerText = bool;
        return this;
    }

    public PDFBuilder setFontSize(double size){
        fontSize = size;
        return this;
    }

    public PDFBuilder setOutputDir(String outputDir){
        this.outputDir = outputDir;
        return this;
    }

    public void create() {
        List<HtmlBuilder> pages = mesh(getPage('q'), getPage('c'));

        if (!pages.isEmpty() && !pages.get(pages.size() - 1).isClosed()) {
            pages.get(pages.size() - 1).close();
        }

        List<File> files = new ArrayList<>();
        for (int x = 0; x < pages.size(); x++) {
            files.add(writeXHtmlToPDF(
                    new File(outputDir),
                    pages.get(x).getXHtml(), x));
        }

        mergePdfFiles(files, outputDir+File.separator+"result.pdf");
    }

    // get questions or answers
    private List<HtmlBuilder> getPage(char desc) {
        List<HtmlBuilder> pages = new ArrayList<>();
        double height = (HtmlBuilder.MAX_PDF_HEIGHT_PX / maxCardRows);
        pages.add(new HtmlBuilder(height, centerText, fontSize));
        int current = 0;
        for (int x = 0; x < cards.size(); x++) {
            if (x > 0 && x % (maxCardRows * 2) == 0) {
                pages.get(current).close();
                current++;
                pages.add(new HtmlBuilder(height, centerText, fontSize));
            }
            if (desc == 'q')
                pages.get(current).append(cards.get(x).getQuestion());
            else
                pages.get(current).append(cards.get(x).getAnswer());
        }

        return pages;
    }

    // combine both lists
    private List<HtmlBuilder> mesh(List<HtmlBuilder> a, List<HtmlBuilder> b) {
        List<HtmlBuilder> parent = new ArrayList<>();
        int swap = 0;
        while (!a.isEmpty()) {
            if (swap == 0) {
                HtmlBuilder temp = a.get(0);
                a.remove(0);
                parent.add(temp);
                swap++;
            } else {
                HtmlBuilder temp = b.get(0);
                b.remove(0);
                parent.add(temp);
                swap = 0;
            }
        }

        while (!b.isEmpty()) {
            HtmlBuilder temp = b.get(0);
            b.remove(0);
            parent.add(temp);
        }

        return parent;
    }

    private File writeXHtmlToPDF(File outputFileFolder, String xhtmlContent, int current) {
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(xhtmlContent);
        renderer.layout();

        try {
            String path = outputFileFolder.getAbsolutePath() + File.separator + ("study_cards_" + current + ".pdf");
            FileOutputStream fos = new FileOutputStream(path);
            renderer.createPDF(fos);
            fos.close();
            System.out.println("File: \'" + path + "\' created.");
            return new File(path);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // helper of mergePdfFiles
    private void mergePDFs(List<InputStream> list, OutputStream outputStream)
            throws DocumentException, IOException {
        com.itextpdf.text.Document document = new com.itextpdf.text.Document();
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        document.open();
        PdfContentByte cb = writer.getDirectContent();

        for (InputStream in : list) {
            PdfReader reader = new PdfReader(in);
            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                document.newPage();
                PdfImportedPage page = writer.getImportedPage(reader, i);
                cb.addTemplate(page, 0, 0);
            }
        }

        outputStream.flush();
        document.close();
        outputStream.close();
    }

    private void mergePdfFiles(List<File> inputFiles, String outputDir) {
        List<InputStream> list = new ArrayList<>();
        try {
            // Source pdfs
            for (File f : inputFiles) {
                list.add(new FileInputStream(f));
            }

            // Resulting pdf
            OutputStream out = new FileOutputStream(
                    new File(outputDir));

            mergePDFs(list, out);

            for (File file : inputFiles) {
                if (file.delete()) {
                    System.out.println("Deleted: " + file.getName());
                } else {
                    System.out.println("Failed to delete: " + file.getName());
                }
            }

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }
}