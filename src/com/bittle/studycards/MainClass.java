package com.bittle.studycards;
import com.bittle.studycards.utils.PDFBuilder;

import java.util.ArrayList;

public class MainClass {

    public static void main(String[] args) {

        // this is a hardcoded example, will by better soon
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(new Card("What is the minimum age for serving in the House of Representatives?", "25"));

        cards.add(new Card("Which of the following states has only one “at-large” member in the House\n" +
                "of Representatives?", "Alaska"));
        cards.add(new Card("Which portion of the Constitution gives Congress the power to make laws?",
                "The Necessary and Proper Clause"));
        cards.add(new Card("Necessary and Proper Clause", "Also known as the Elastic Clause, the portion\n" +
                "of Article I, §8 of the Constitution that grants Congress the authority to do\n" +
                "whatever is required to execute its enumerated powers."));
        cards.add(new Card("implied powers", "General powers suggested by the Constitution rather than\n" +
                "specifically enumerated within it."));
        cards.add(new Card("Which of the following members of Congress would represent the greatest\n" +
                "number of constituents?", "A senator from California, given the state’s large population."));
        cards.add(new Card("What are the term lengths for the members of each house of Congress?",
                "Senate members serve six-year terms, while House members serve two-year\n" +
                        "terms."));
        cards.add(new Card("Which of the following powers is unique to the House?",
                "he power to originate revenue bills by introducing tax legislation is\n" +
                        "unique to the House."));
        cards.add(new Card("Which of the following can initiate impeachment proceedings?",
                "Any impeachment proceedings must begin in the House of Representatives."));
        cards.add(new Card("issue network",
                "A complex set of cooperative relationships between groups\n" +
                        "of citizens affected by a particular set of policies and the bureaucratic\n" +
                        "agency and congressional committee with jurisdiction over those policies."));

        // this is how you make the pdf
        PDFBuilder pdfBuilder = new PDFBuilder(cards);
        pdfBuilder.maxCardRows(3).centerText(true).setFontSize(14).
                setOutputDir("/Users/oscartorres/Desktop/studyCards").create();
    }
}
