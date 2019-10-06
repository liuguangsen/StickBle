package com.liugs.stble.scan;

public class ParserResultFactory {
    public static <Result> BaseParser<Result> buildParser(ScanLocal<Result> local){
        BaseParser<Result> parserScanResult = new DefaultParserScanResult<>();
        parserScanResult.setScanLocal(local);
        return parserScanResult;
    }
}
