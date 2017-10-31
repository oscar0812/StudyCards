#Study Cards

Example usage:
```java
// this is how u make the pdf
ArrayList<Card> cards = new ArrayList<>();
cards.add(new Card("What is the minimum age for serving in the House of Representatives?", "25"));

cards.add(...);// and so on


PDFBuilder pdfBuilder = new PDFBuilder(cards);
pdfBuilder.maxCardRows(3).centerText(true).setFontSize(14).create();
```

Example output PDF found [here](https://github.com/oscar0812/StudyCards/blob/master/result.pdf).

##Features
  - Written using iText
  - Customization (Font size, row count, etc)
  - Fast!
