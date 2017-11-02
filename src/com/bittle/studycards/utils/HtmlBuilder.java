package com.bittle.studycards.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

class HtmlBuilder {
    private Document mainHtml = Jsoup.parse("<html><head></head><body><table></table></body></html>");
    static final int MAX_PDF_HEIGHT_PX = 900;

    private Document newRow;
    private int currentRow = 0;

    // for calculating font size
    private double boxHeight;

    HtmlBuilder(double boxHeight, boolean doCenter, double fontSize) {
        this.boxHeight = boxHeight;
        // to put divs side by side with least space
        //double width = 49.43535;
        double width = 50;
        String border = "border: 1px solid black;";
        String center1 = "text-align: center; display: table;";
        String center2 = "display: table-cell; vertical-align: middle;";
        String font = "font-size: " + fontSize + "pt;";

        String nr = "<tr>\n" +
                "<div style=\"width: " + width + "%; height: " + boxHeight + "px; float:left;" + border +
                (doCenter ? center1 : "") + font + "\"><div class=\"left\" style=\"" + (doCenter ? center2 : "")
                + "\"></div></div>\n" +

                "<div style=\"width: " + width + "%; height: " + boxHeight + "px; float:right;" + border +
                (doCenter ? center1 : "") + font + "\"><div class=\"right\" style=\"" + (doCenter ? center2 : "")
                + "\"></div></div></tr>\n";

        newRow = Jsoup.parse(nr, "", Parser.xmlParser());
    }

    // append on the first column first, how questions are shown
    void appendLeftToRight(String entry) {
        if (currentRow == 0) {
            newRow.select(".left").first().text(entry);
            currentRow++;
        } else {
            newRow.select(".right").first().text(entry);
            mainHtml.select("body").first().append(newRow.html());
            currentRow = 0;
        }

    }

    // append the second column first, how answers are shown (since pages are flipped this way when printed)
    void appendRightToLeft(String entry) {
        if (currentRow == 0) {
            newRow.select(".right").first().text(entry);
            currentRow++;
        } else {
            newRow.select(".left").first().text(entry);
            mainHtml.select("body").first().append(newRow.html());
            currentRow = 0;
        }
    }


    String getHtml() {
        return mainHtml.html();
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
        return mainHtml.toString();
    }
}