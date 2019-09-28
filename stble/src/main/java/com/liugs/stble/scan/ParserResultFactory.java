package com.liugs.stble.scan;

public class ParserResultFactory {
    public static <Result> BaseParser<Result> buildParser(ScanLocal<Result> local){
        return new DefaultParserScanResult<>();
    }
}
