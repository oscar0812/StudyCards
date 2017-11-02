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
    private double fontSize = -1;
    private String outputDir = System.getProperty("user.home");

    public PDFBuilder(List<Card> cards) {
        if (cards.size() % 2 != 0)
            cards.add(new Card("", ""));
        this.cards = cards;
        maxCardRows = 9;
    }

    public PDFBuilder(File inputFile, String questionStart, String answerStart){
        cards = getCardsFromFile(inputFile, questionStart, answerStart);
    }

    // constructor helper
    private List<Card> getCardsFromFile(File inputFile, String questionStart, String answerStart) {
        try {
            List<Card> cards = new ArrayList<>();
            BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile));
            String line;
            boolean isInQuestion = false;
            boolean isInAnswer = false;

            StringBuilder question = new StringBuilder();
            StringBuilder answer = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                line = line+" ";
                if (line.startsWith(questionStart)) {
                    if (isInAnswer) {
                        // read a question and answer already
                        cards.add(new Card(question.toString().substring(questionStart.length()).trim(),
                                answer.toString().substring(answerStart.length()).trim()));
                        question = new StringBuilder();
                        answer = new StringBuilder();
                    }
                    isInQuestion = true;
                    isInAnswer = false;
                    question.append(line);

                } else if (line.startsWith(answerStart)) {
                    isInAnswer = true;
                    isInQuestion = false;
                    answer.append(line);
                } else if(isInQuestion){
                    question.append(line);
                } else if(isInAnswer){
                    answer.append(line);
                }
            }
            return cards;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    // options for user
    public PDFBuilder maxCardRows(int num) {
        maxCardRows = num;
        if (maxCardRows > 10) {
            System.out.println("For styling purposes, restrain from using an integer larger than 10 for the rows.");
        }
        return this;
    }

    public PDFBuilder centerText(boolean bool) {
        centerText = bool;
        return this;
    }

    public PDFBuilder setFontSize(double size) {
        fontSize = size;
        return this;
    }

    public PDFBuilder setOutputDir(String outputDir) {
        this.outputDir = outputDir;
        return this;
    }

    public void create() {
        List<HtmlBuilder> pages = getPages();

        List<File> files = new ArrayList<>();
        for (int x = 0; x < pages.size(); x++) {
            files.add(writeXHtmlToPDF(
                    new File(outputDir),
                    pages.get(x).getXHtml(), x));
        }

        mergePdfFiles(files, outputDir + File.separator + "result.pdf");
    }

    // get questions and answers
    private List<HtmlBuilder> getPages() {
        List<HtmlBuilder> pages = new ArrayList<>();
        double height = (HtmlBuilder.MAX_PDF_HEIGHT_PX / maxCardRows);

        // add by pairs, one page for questions, one for answers
        pages.add(new HtmlBuilder(height, centerText, fontSize));
        pages.add(new HtmlBuilder(height, centerText, fontSize));

        int current = 0;
        for (int x = 0; x < cards.size(); x++) {
            // two cards per row, so * 2
            if (x > 0 && x % (maxCardRows * 2) == 0) {
                //pages.get(current).close();
                //pages.get(current + 1).close();
                current = current + 2;

                // add by pairs, one page for questions, one for answers
                pages.add(new HtmlBuilder(height, centerText, fontSize));
                pages.add(new HtmlBuilder(height, centerText, fontSize));
            }
            pages.get(current).appendLeftToRight(cards.get(x).getQuestion());
            // append reverse on the answers page since paper is flipped when printed
            pages.get(current + 1).appendRightToLeft(cards.get(x).getAnswer());
        }

        return pages;
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