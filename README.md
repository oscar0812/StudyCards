# Study Cards

#### Example usage with Card objects:
```java
// this is how you make the pdf
ArrayList<Card> cards = new ArrayList<>();
cards.add(new Card("What is the minimum age for serving in the House of Representatives?", "25"));

cards.add(...);// and so on


PDFBuilder pdfBuilder = new PDFBuilder(cards);
pdfBuilder.maxCardRows(3).centerText(true).setFontSize(14).
                setOutputDir("/Users/oscartorres/Desktop/studyCards").create();
```


#### Example usage from txt file:

```java

// Constructor Params: Input file, what the question line start with, what the answer line start with
PDFBuilder pdfBuilder = new PDFBuilder(
                new File("/Users/oscartorres/Desktop/chapter5.txt"), "--", "=>");
pdfBuilder.maxCardRows(8)
                .centerText(true)
                .setOutputDir("/Users/oscartorres/Desktop/studyCards").create();
```
##### Example file contents:
*Notice that questions start with -- and answer start with =>*
```text
-- What is the minimum age for serving in the House of Representatives?
=> 25

-- Which of the following states has only one “at-large” member in the House
of Representatives?
=> Alaska

-- Which portion of the Constitution gives Congress the power to make laws?
=> The Necessary and Proper Clause
...
```

Example result PDF found [here](https://github.com/oscar0812/StudyCards/blob/master/result.pdf).

The result PDF is meant to be printed out using both sides of paper, therefore, the questions are displayed in all odd pages (pages 1 , 3, 5...), and answers on the even pages (pages 2, 4, 6...)

### Features
  - Written using iText
  - Customization (font size, row count, etc)
  - Fast!
  - Written in Java

Want to contribute? Fork me!
