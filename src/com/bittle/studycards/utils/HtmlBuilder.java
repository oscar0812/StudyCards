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
        double width = 50;
        String border = "border: 1px solid black;";
        String center1 = "text-align: center; display: table;";
        String center2 = "display: table-cell; vertical-align: middle;";
        String font = "font-size: " + fontSize + "pt;";

        newRow = "<tr>\n" +
                "<div style=\"width: " + width + "%; height: " + boxHeight + "px; float:left;" + border +
                (doCenter ? center1 : "") + font + "\"><div style=\"" + (doCenter ? center2 : "") + "\">" +
                leftCard + "</div></div>\n" +

                "<div style=\"width: " + width + "%; height: " + boxHeight + "px; float:right;" + border +
                (doCenter ? center1 : "") + font + "\"><div style=\"" + (doCenter ? center2 : "") + "\">" +
                rightCard + "</div></div></tr>\n";
    }

    // append on the first column first, how questions are shown
    void appendLeftToRight(String entry) {
        if (currentRow.isEmpty()) {
            currentRow = newRow;
            currentRow = currentRow.replace(leftCard, entry);
        } else {
            currentRow = currentRow.replace(rightCard, entry);
            builder.append(currentRow);
            currentRow = "";
        }
    }

    // append the second column first, how answers are shown (since pages are flipped this way when printed)
    void appendRightToLeft(String entry) {
        if (currentRow.isEmpty()) {
            currentRow = newRow;
            currentRow = currentRow.replace(rightCard, entry);
        } else {
            currentRow = currentRow.replace(leftCard, entry);
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