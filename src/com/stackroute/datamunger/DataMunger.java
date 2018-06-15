package com.stackroute.datamunger;

import java.util.ArrayList;

/*There are total 5 DataMungertest files:
 * 
 * 1)DataMungerTestTask1.java file is for testing following 3 methods
 * a)getSplitStrings()  b) getFileName()  c) getBaseQuery()
 * 
 * Once you implement the above 3 methods,run DataMungerTestTask1.java
 * 
 * 2)DataMungerTestTask2.java file is for testing following 3 methods
 * a)getFields() b) getConditionsPartQuery() c) getConditions()
 * 
 * Once you implement the above 3 methods,run DataMungerTestTask2.java
 * 
 * 3)DataMungerTestTask3.java file is for testing following 2 methods
 * a)getLogicalOperators() b) getOrderByFields()
 * 
 * Once you implement the above 2 methods,run DataMungerTestTask3.java
 * 
 * 4)DataMungerTestTask4.java file is for testing following 2 methods
 * a)getGroupByFields()  b) getAggregateFunctions()
 * 
 * Once you implement the above 2 methods,run DataMungerTestTask4.java
 * 
 * Once you implement all the methods run DataMungerTest.java.This test case consist of all
 * the test cases together.
 */

public class DataMunger {

	/*
	 * This method will split the query string based on space into an array of words
	 * and display it on console
	 */

	public String[] getSplitStrings(String queryString) {
		if(queryString.length()==0) {
			return null;
		}
		String[] wordSplit=queryString.split(" ");
		
		for(int i=0;i<wordSplit.length;i++) {
			wordSplit[i]=wordSplit[i].toLowerCase();
		}
		return wordSplit;
	}

	/*
	 * Extract the name of the file from the query. File name can be found after a
	 * space after "from" clause. Note: ----- CSV file can contain a field that
	 * contains from as a part of the column name. For eg: from_date,from_hrs etc.
	 * 
	 * Please consider this while extracting the file name in this method.
	 */

	public String getFileName(String queryString) {
		String[] wordSplit=queryString.split(" ");
	    int i;
	    int indexFrom=0;
		for(i=0;i<wordSplit.length;i++) {
			//System.out.println(wordSplit[i]);
			if(wordSplit[i].trim().equals("from")) {
				indexFrom=i;
				//System.out.println(indexFrom);
				break;
			}
		}
		return wordSplit[indexFrom+1];
	}

	/*
	 * This method is used to extract the baseQuery from the query string. BaseQuery
	 * contains from the beginning of the query till the where clause
	 * 
	 * Note: ------- 1. The query might not contain where clause but contain order
	 * by or group by clause 2. The query might not contain where, order by or group
	 * by clause 3. The query might not contain where, but can contain both group by
	 * and order by clause
	 */
	
	public String getBaseQuery(String queryString) {
        
		int indexOfWhere=queryString.indexOf("where");
		if(indexOfWhere==-1) {
			return queryString;
		}
		String subStringBeforeWhere=queryString.substring(0, indexOfWhere);
		return subStringBeforeWhere.trim();
	}

	/*
	 * This method will extract the fields to be selected from the query string. The
	 * query string can have multiple fields separated by comma. The extracted
	 * fields will be stored in a String array which is to be printed in console as
	 * well as to be returned by the method
	 * 
	 * Note: 1. The field name or value in the condition can contain keywords
	 * as a substring. For eg: from_city,job_order_no,group_no etc. 2. The field
	 * name can contain '*'
	 * 
	 */
	
	public String[] getFields(String queryString) {
        String[] stringArray=queryString.split(" ");
        String columnString=stringArray[1];
        String[] columns=columnString.split(",");
        
		return columns;
	}

	/*
	 * This method is used to extract the conditions part from the query string. The
	 * conditions part contains starting from where keyword till the next keyword,
	 * which is either group by or order by clause. In case of absence of both group
	 * by and order by clause, it will contain till the end of the query string.
	 * Note:  1. The field name or value in the condition can contain keywords
	 * as a substring. For eg: from_city,job_order_no,group_no etc. 2. The query
	 * might not contain where clause at all.
	 */
//	public String convertToLowerCase(String queryString) {
//		String[] wordSplit=queryString.split(" ");
//		String returnString="";
//		for(int i=0;i<wordSplit.length;i++) {
//			wordSplit[i]=wordSplit[i].toLowerCase();
//			returnString=returnString+" "+wordSplit[i].toLowerCase();
//		}
//		return returnString;
//	}
	public String getConditionsPartQuery(String queryString) {
       String[] whereSplit=queryString.split("where");
       String afterWhere=whereSplit[1].trim();
       int indexOfGroupBy=afterWhere.indexOf("group by");
       int indexOfOrderBy=afterWhere.indexOf("order by");
       if(indexOfGroupBy==-1 && indexOfOrderBy==-1) {
    	   return afterWhere.trim().toLowerCase();
       }
       else {
    	   if(indexOfGroupBy==-1)
    	    return afterWhere.substring(0,indexOfOrderBy).trim().toLowerCase();
    	   else {
    		   return afterWhere.substring(0,indexOfGroupBy).trim().toLowerCase();
    	   }
       }
		
	}

	/*
	 * This method will extract condition(s) from the query string. The query can
	 * contain one or multiple conditions. In case of multiple conditions, the
	 * conditions will be separated by AND/OR keywords. for eg: Input: select
	 * city,winner,player_match from ipl.csv where season > 2014 and city
	 * ='Bangalore'
	 * 
	 * This method will return a string array ["season > 2014","city ='bangalore'"]
	 * and print the array
	 * 
	 * Note: ----- 1. The field name or value in the condition can contain keywords
	 * as a substring. For eg: from_city,job_order_no,group_no etc. 2. The query
	 * might not contain where clause at all.
	 */

	public String[] getConditions(String queryString) {
		String toLowerQueryString=queryString.toLowerCase();
        int indexOfWhere=toLowerQueryString.indexOf("where");
        if(indexOfWhere==-1) {
        	return null;
        }
        else {
        	String conditions=getConditionsPartQuery(queryString);
        	String[] conditionArray=conditions.toLowerCase().split(" AND | and | OR | or ");
        	return conditionArray;
        }
	}

	/*
	 * This method will extract logical operators(AND/OR) from the query string. The
	 * extracted logical operators will be stored in a String array which will be
	 * returned by the method and the same will be printed Note:  1. AND/OR
	 * keyword will exist in the query only if where conditions exists and it
	 * contains multiple conditions. 2. AND/OR can exist as a substring in the
	 * conditions as well. For eg: name='Alexander',color='Red' etc. Please consider
	 * these as well when extracting the logical operators.
	 * 
	 */

	public String[] getLogicalOperators(String queryString) {
        int indexOfWhere= queryString.toLowerCase().indexOf("where");
        if(indexOfWhere==-1) {
        	return null;
        }
        else {
        	String lowerqueryString=queryString.toLowerCase();
        	String[] stringArray=lowerqueryString.split(" ");
        	ArrayList<String> al=new ArrayList<String>();
        	for(int i=0;i<stringArray.length;i++) {
        		if(stringArray[i].equals("and") || stringArray[i].equals("or")|| stringArray[i].equals("not")) {
        			al.add(stringArray[i]);
        		}
        	}
        	String[] returnAndOr=new String[al.size()];
        	for(int i=0;i<al.size();i++) {
        		returnAndOr[i]=al.get(i);
        	}
        	return returnAndOr;
        }
		
	}

	/*
	 * This method extracts the order by fields from the query string. Note: 
	 * 1. The query string can contain more than one order by fields. 2. The query
	 * string might not contain order by clause at all. 3. The field names,condition
	 * values might contain "order" as a substring. For eg:order_number,job_order
	 * Consider this while extracting the order by fields
	 */

	public String[] getOrderByFields(String queryString) {
		String lowerCaseQueryString=queryString.toLowerCase();
        int orderByIndex=lowerCaseQueryString.indexOf(" order by ");
        if(orderByIndex==-1) {
        	return null;
        }
        else {
        	String[] orderBySplit=lowerCaseQueryString.split(" order by ");
        	String afterOrderBy=orderBySplit[1];
        	String[] afterOrderByFields=afterOrderBy.split(",");
        	return afterOrderByFields;
        }
		
	}

	/*
	 * This method extracts the group by fields from the query string. Note:
	 * 1. The query string can contain more than one group by fields. 2. The query
	 * string might not contain group by clause at all. 3. The field names,condition
	 * values might contain "group" as a substring. For eg: newsgroup_name
	 * 
	 * Consider this while extracting the group by fields
	 */

	public String[] getGroupByFields(String queryString) {

		String lowerCaseQueryString=queryString.toLowerCase();
        int groupByIndex=lowerCaseQueryString.indexOf(" group by ");
        if(groupByIndex==-1) {
        	return null;
        }
        else {
        	String[] groupBySplit=lowerCaseQueryString.split(" group by ");
        	String afterGroupBy=groupBySplit[1];
        	String[] afterGroupByFields=afterGroupBy.split(",");
        	return afterGroupByFields;
        }
	}

	/*
	 * This method extracts the aggregate functions from the query string. Note:
	 *  1. aggregate functions will start with "sum"/"count"/"min"/"max"/"avg"
	 * followed by "(" 2. The field names might
	 * contain"sum"/"count"/"min"/"max"/"avg" as a substring. For eg:
	 * account_number,consumed_qty,nominee_name
	 * 
	 * Consider this while extracting the aggregate functions
	 */

	public String[] getAggregateFunctions(String queryString) {
		       String queryLowerString=queryString.toLowerCase();
               String[] aggregateAssume=queryLowerString.split(" ");
               String aggregateAssumeString=aggregateAssume[1];
               String[] commaSeparateAggregate=aggregateAssumeString.split(",");
               ArrayList<String>al=new ArrayList<String>();
               for(int i=0;i<commaSeparateAggregate.length;i++) {
            	   if(commaSeparateAggregate[i].contains("("));{
            		   al.add(commaSeparateAggregate[i]);
            	   }
               }
               String[] returnAggregate=new String[al.size()];
               for(int i=0;i<al.size();i++) {
            	   returnAggregate[i]=al.get(i);
               }
               if(returnAggregate.length==0) {
            	   return null;
               }
               
               else {
               return returnAggregate;
               }
	}
	public static void main(String[] args) {
		DataMunger dm=new DataMunger();
		String[] aggregate=dm.getAggregateFunctions("select count(city),sum(win_by_runs),min(win_by_runs),max(win_by_runs),avg(win_by_runs) from ipl.csv");
		for(int i=0;i<aggregate.length;i++) {
			System.out.println(aggregate[i]);
		}
	}
  
}