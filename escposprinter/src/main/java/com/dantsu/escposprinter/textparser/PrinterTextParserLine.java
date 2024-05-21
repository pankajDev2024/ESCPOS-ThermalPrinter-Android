package com.dantsu.escposprinter.textparser;

import com.dantsu.escposprinter.exceptions.EscPosBarcodeException;
import com.dantsu.escposprinter.exceptions.EscPosEncodingException;
import com.dantsu.escposprinter.exceptions.EscPosParserException;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrinterTextParserLine {
    private PrinterTextParser textParser;
    private int nbrColumns;
    private int nbrCharColumn;
    private int nbrCharForgetted;
    private int nbrCharColumnExceeded;
    private PrinterTextParserColumn[] columns;

    private String textAlignment;
    
    public PrinterTextParserLine(PrinterTextParser textParser, String textLine) throws EscPosParserException, EscPosBarcodeException, EscPosEncodingException {
        this.textParser = textParser;
        int nbrCharactersPerLine = this.getTextParser().getPrinter().getPrinterNbrCharactersPerLine();
    
        Pattern pattern = Pattern.compile(PrinterTextParser.getRegexAlignTags());
        Matcher matcher = pattern.matcher(textLine);
    
        ArrayList<String> columnsList = new ArrayList<String>();
        int lastPosition = 0;
        
        while (matcher.find()) {
            int startPosition = matcher.start();
            if(startPosition > 0) {
                columnsList.add(textLine.substring(lastPosition, startPosition));
            }
            lastPosition = startPosition;
        }
        columnsList.add(textLine.substring(lastPosition));

        this.nbrColumns = columnsList.size();
        this.nbrCharColumn = (int) Math.floor(((float) nbrCharactersPerLine) / ((float) this.nbrColumns));
        this.nbrCharForgetted = nbrCharactersPerLine - (nbrCharColumn * this.nbrColumns);
        this.nbrCharColumnExceeded = 0;

        setTextAlignment(textLine);
        this.columns = new PrinterTextParserColumn[this.nbrColumns];
        
        int i=0;
        for (String column : columnsList) {
            this.columns[i++] = new PrinterTextParserColumn(this, column);
        }
    }

    private void setTextAlignment(String textLine) {
        if(textLine !=null && !textLine.isEmpty() && textLine.length() > 2){
            this.textAlignment = textLine.substring(1, 2).toUpperCase();
        }
    }


    public PrinterTextParser getTextParser() {
        return this.textParser;
    }
    
    public PrinterTextParserColumn[] getColumns() {
        return this.columns;
    }
    
    public int getNbrColumns() {
        return this.nbrColumns;
    }

    public String getTextAlignment() {
        return this.textAlignment;
    }

    public PrinterTextParserLine setNbrCharColumn(int newValue) {
        this.nbrCharColumn = newValue;
        return this;
    }
    public int getNbrCharColumn() {
        return this.nbrCharColumn;
    }
    
    
    public PrinterTextParserLine setNbrCharForgetted(int newValue) {
        this.nbrCharForgetted = newValue;
        return this;
    }
    public int getNbrCharForgetted() {
        return this.nbrCharForgetted;
    }
    
    
    public PrinterTextParserLine setNbrCharColumnExceeded(int newValue) {
        this.nbrCharColumnExceeded = newValue;
        return this;
    }
    public int getNbrCharColumnExceeded() {
        return this.nbrCharColumnExceeded;
    }
}
