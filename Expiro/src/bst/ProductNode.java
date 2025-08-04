package bst;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ProductNode {
	LocalDate data;
	ProductNode left, right;
	String key;
	
	ProductNode (String k, String d){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate dDate = LocalDate.parse(d, formatter);
		key = k;
		data = dDate;
		left = null;
		right = null;
	}
	
	public String getName() {
	    return key;
	}

	public LocalDate getExpirationDate() {
	    return data;
	}

	public String getKey() {
		// TODO Auto-generated method stub
		return key;
	}
}