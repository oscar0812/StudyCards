package com.bittle.studycards;
import com.bittle.studycards.utils.PDFBuilder;

import java.io.File;

public class MainClass {

    public static void main(String[] args) {

        // this is how you make the pdf

        // Constructor Params: Input file, what the question line start with, what the answer line start with
        PDFBuilder pdfBuilder = new PDFBuilder(
                new File("/Users/oscartorres/Desktop/chapter5.txt"), "--", "=>");
        pdfBuilder.maxCardRows(8)
                .centerText(true)
                .setOutputDir("/Users/oscartorres/Desktop/studyCards").create();
    }
}
