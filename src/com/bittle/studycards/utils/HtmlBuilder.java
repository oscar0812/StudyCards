package com.bittle.studycards.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

class HtmlBuilder {
    private final StringBuilder builder = new StringBuilder("<html>\n" + "<head></head>\n" + "<body>\n" + "<table>\n");
    static final int MAX_PDF_HEIGHT_PX = 900;

    private String newRow;
    private String currentRow = "";
    private final String close = "</table>\n" + "</body>\n" + "</html>\n";
    private boolean isClosed = false;
    private final String leftCard = "$(LEFT_CARD)$";
    private final String rightCard = "$(RIGHT_CARD)$";

    HtmlBuilder(double boxHeight, boolean doCenter, double fontSize) {
        // to put divs side by side with least space
        //double width = 49.43535;
        double width = 49.71;
        String border = "border: 1px solid black;";
        String center = "text-align: center;";
        String font = "font-size: "+fontSize+"pt;";

        newRow = "<tr>\n" +
                "<div style=\"width: " + width + "%; height: " + boxHeight + "px; float:left;" + border +
                "" + (doCenter ? center : "") +font+ "\">" + leftCard + "</div>\n" +
                "<div style=\"width: " + width + "%; height: " + boxHeight + "px; float:right;" + border +
                "" + (doCenter ? center : "") + font+"\">" + rightCard + "</div></tr>\n";
    }

    void append(String entry) {
        if (currentRow.isEmpty()) {
            currentRow = newRow;
            currentRow = currentRow.replace(leftCard, entry);
        } else {
            currentRow = currentRow.replace(rightCard, entry);
            builder.append(currentRow);
            currentRow = "";
        }
    }

    void close() {
        builder.append(close);
        isClosed = true;
    }

    boolean isClosed() {
        return isClosed;
    }

    String getHtml() {
        return builder.toString();
    }

    String getXHtml() {
        return htmlToXhtml(getHtml());
    }

    private String htmlToXhtml(final String html) {
        final Document document = Jsoup.parse(html);
        document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
        return document.html();
    }

    @Override
    public String toString() {
        return builder.toString();
    }
}