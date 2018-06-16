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
		String[] wordSplit=queryString.toLowerCase().split(" ");
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
	
	/*	
	 * Logic -- First split the string on from(keyword). Take the 2nd part after
	 * from  i.e ipl.csv group by city. Now split the second string on " " and check
	 * for each string which contains .csv in it. Return the string.
     */
		
		String[] wordSplit=queryString.toLowerCase().split(" from ");
		String afterFrom=wordSplit[1];
		String[] splitAfterFrom=afterFrom.split(" ");
		String fileName="";
		for(int i=0;i<splitAfterFrom.length;i++) {
			if(splitAfterFrom[i].contains(".csv"))
				fileName=fileName+splitAfterFrom[i];
		}
		return fileName;
	}

	/*
	 * This method is used to extract the baseQuery from the query string. BaseQuery
	 * contains from the beginning of the query till the where clause
	 * 
	   Note: ------- 1. The query might not contain where clause but contain order
	 * by or group by clause 2. The query might not contain where, order by or group
	 * by clause 3. The query might not contain where, but can contain both group by
	 * and order by clause
	 */
	
	public String getBaseQuery(String queryString) {
	
	 /*
	  * Logic -- First get the indexof where. if where is present in the string
	  * then split on where and return the 0th string of the string array.
	  * If where is not present then check for index of groupby and orderby in else
	  * case. If groupby orderby both index is -1 return the queryString as it cause
	  * it has no where no groupby and no orderby. If one of the groupby and orderby 
	  * present then split on it and return the 0th string of the string array.
	  * If both groupby and orderby is present then split on value whose index is small
	  * (means split on that which comes first) and return the 0th string from the string array.
	  */
		int indexOfWhere=queryString.toLowerCase().indexOf(" where ");
		if(indexOfWhere!=-1) {
			String[] splitOnWhere=queryString.toLowerCase().split(" where ");
			return splitOnWhere[0].trim();
		}
		else {
			int indexOfGroupBy=queryString.toLowerCase().indexOf(" group by ");
			int indexOfOrderBy=queryString.toLowerCase().indexOf(" order by ");
			if(indexOfGroupBy ==-1 && indexOfGroupBy==-1) {
				return queryString.toLowerCase();
			}
			else if(indexOfGroupBy == -1 && indexOfOrderBy !=-1) {
				String[] splitOnOrderBy=queryString.toLowerCase().split(" order by ");
				return splitOnOrderBy[0];
			}
			else if(indexOfGroupBy != -1 && indexOfGroupBy ==-1) {
				String[] splitOnGroupBy=queryString.toLowerCase().split(" group by ");
				return splitOnGroupBy[0];
			}
			else {
				if(indexOfGroupBy<indexOfOrderBy) {
					String[] splitOnGroupBy=queryString.toLowerCase().split(" group by ");
					return splitOnGroupBy[0];
				}
				else {
					String[] splitOnGroupBy=queryString.toLowerCase().split(" group by ");
					return splitOnGroupBy[0];
				}
			}
		}
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

    /*	Logic -- To get all fields names, first split on from keyword. Take the first part
     *  of the string array and then split on select keyword. Take the second part of string array
     *  which is the actual string of fields. Then finally split on commas, trim the string and return 
     *  the string array.
     */
		String[] splitOnFrom=queryString.toLowerCase().split(" from ");
		String[] splitOnSelect=splitOnFrom[0].trim().split("select ");
		String[] splitOnComma=splitOnSelect[1].trim().split(",");
		for(int i=0;i<splitOnComma.length;i++) {
			splitOnComma[i]=splitOnComma[i].trim();
		}
		return splitOnComma;
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

	public String getConditionsPartQuery(String queryString) {
		
    /*
     * Logic-- First get the indexOf where if where is not there then no conditions will be
     * there so return null. If where keyword is there than split on it and take the 2nd part
     * of it cause condition will be there in second part.Now if group by order by keyword is
     * not there in 2nd half of string then simply returns the 2nd half. If group by order by 
     * is there than based on the index split the string and take the first part of it that will
     * be our conditional string, return it.
     */			
		int indexOfWhere=queryString.toLowerCase().indexOf(" where ");
		if(indexOfWhere==-1) {
			return null;
		}
		else {
			String[] whereSplit=queryString.split("where");
			String afterWhere=whereSplit[1].trim();
			int indexOfGroupBy=afterWhere.indexOf("group by");
			int indexOfOrderBy=afterWhere.indexOf("order by");
			if(indexOfGroupBy==-1 && indexOfOrderBy==-1) {
				return afterWhere.toLowerCase();
			}
			else if(indexOfGroupBy == -1 && indexOfOrderBy !=-1) {
				String[] splitOnOrderBy=afterWhere.toLowerCase().split(" order by ");
				return splitOnOrderBy[0];
			}
			else if(indexOfGroupBy != -1 && indexOfGroupBy ==-1) {
				String[] splitOnGroupBy=afterWhere.toLowerCase().split(" group by ");
				return splitOnGroupBy[0];
			}
			else {
				if(indexOfGroupBy<indexOfOrderBy) {
					String[] splitOnGroupBy=afterWhere.toLowerCase().split(" group by ");
					return splitOnGroupBy[0];
				}
				else {
					String[] splitOnGroupBy=afterWhere.toLowerCase().split(" group by ");
					return splitOnGroupBy[0];
				}
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
		
     /*
      *  Logic -- Pass the queryString to the getConditionPartQuery to get our condition as a String.
      * If the where keyword is not there then condition string will be null hence return null. else
      * split the string on (and or keyword). The main idea here is we are spliting on (space and space) 
      * not just (and) because some field name may also contain ...and... as there substring.
      */
		String conditions=getConditionsPartQuery(queryString);
		if(conditions==null) {
			return null;
		}
		else {
			String[] conditionArray=conditions.toLowerCase().split(" and | or ");
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
      
		/*
        *  Logic -- First check the indexOf where if index is -1 then no where no conditions
        * hence no operators return null. If where is there then split on where and take the
        * 2nd part of the string which contains our Logical operator. Split the second part
        * of the string on space and check for each string of the array whether it matches 
        * with the and ,or ,not . If it does put it in arrayList of string and finally copy
        * the value from arrayList to string array.
        * WHY CHOOSE ARRAYLIST -- as we don't know the length of the string array hence it is
        * better to take dynamic arrayList and proceed.
        * */
		int indexOfWhere= queryString.toLowerCase().indexOf(" where ");
		if(indexOfWhere==-1) {
			return null;
		}
		else {
			String lowerQueryString=queryString.toLowerCase();
			String[] afterWhere=lowerQueryString.split(" where ");
			String[] stringArray=afterWhere[1].split(" ");
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
		
	/* Logic -- First check the index of order by if it is -1 than return null.
	 * Else split the string on order by and take the 2nd part. Split the second 
	 * part on comma and trim every string in it and then return the string array.
	 */
		String lowerCaseQueryString=queryString.toLowerCase();
		int orderByIndex=lowerCaseQueryString.indexOf(" order by ");
		if(orderByIndex==-1) {
			return null;
		}
		else {
			String[] orderBySplit=lowerCaseQueryString.split(" order by ");
			String afterOrderBy=orderBySplit[1].trim();
			String[] afterOrderByFields=afterOrderBy.split(",");
			for(int i=0;i<afterOrderByFields.length;i++) {
				afterOrderByFields[i]=afterOrderByFields[i].trim();
			}
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
		
	/* Logic -- First check the index of order by if it is -1 than return null.
	 * Else split the string on order by and take the 2nd part. Split the second 
	 * part on comma and trim every string in it and then return the string array.
	 */
		String lowerCaseQueryString=queryString.toLowerCase();
		int groupByIndex=lowerCaseQueryString.indexOf(" group by ");
		if(groupByIndex==-1) {
			return null;
		}
		else {
			String[] groupBySplit=lowerCaseQueryString.split(" group by ");
			String afterGroupBy=groupBySplit[1].trim();
			String[] afterGroupByFields=afterGroupBy.split(",");
			for(int i=0;i<afterGroupByFields.length;i++) {
				afterGroupByFields[i]=afterGroupByFields[i].trim();
			}
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
	/* Logic -- Our aggregate function will be between select and from keyword so 
	 * first split on the basis of from and take first string. Split the first half 
	 * on select keyword. Hence our aggregate function may lie on aggregateAssume string
	 * now split it on comma and for each string array check if it start with "sum("...
	 * If it does add it to arrayList. If arrayList is empty means no aggregate function
	 * return null else copy from arrayList to string array and return the array */
		String[] splitOnWhere=queryString.toLowerCase().split(" from ");
		String[] splitOnSelect=splitOnWhere[0].trim().split("select ");
		String aggregateAssume=splitOnSelect[1];
		String[] aggregateAssumeCommaSeparate=aggregateAssume.trim().split(",");
		ArrayList<String> al=new ArrayList<String>();
		for(int i=0;i<aggregateAssumeCommaSeparate.length;i++) {
			if(aggregateAssumeCommaSeparate[i].startsWith("sum(") || aggregateAssumeCommaSeparate[i].startsWith("count(")
					|| aggregateAssumeCommaSeparate[i].startsWith("min(") || aggregateAssumeCommaSeparate[i].startsWith("max(")
					|| aggregateAssumeCommaSeparate[i].contains("avg("))
			{
				al.add(aggregateAssumeCommaSeparate[i].trim());
			}
		}
		if(al.size()==0) {
			return null;
		}
		else {
			String[] returnAggregate=new String[al.size()];
			for(int i=0;i<al.size();i++) {
				returnAggregate[i]=al.get(i);
			}
			return returnAggregate;
		}

	}
	
  
}