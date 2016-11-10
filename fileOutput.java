package com.sjsu.crawler.parsergrammar;
public class fileOutput {
	public static StringBuffer finalTokens = new StringBuffer();
	public static int newInteger;
	public void printIt(String tempData) {
		//System.out.println("Printing temp data from class new"+ tempData);
		
		finalTokens.append(tempData);
		//System.out.println("am printing final");
		
		//System.out.println("printing final tokens"+ finalTokens.toString());
	}
	
	public String printNum(String dataValue){
		newInteger++;
		String replaceNum= Integer.toString(newInteger);
		//System.out.println(newInteger);
		return replaceNum;
		
	}
	
	
}
