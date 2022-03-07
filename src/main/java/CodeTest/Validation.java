package CodeTest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Validation {
	
	public static boolean isValidDate(String date) {
        boolean valid = false;
        try {
            LocalDate.parse(date,
                    DateTimeFormatter.ofPattern("uuuu-M-d")
                            .withResolverStyle(ResolverStyle.STRICT)
            );
            valid = true;
        } catch (DateTimeParseException e) {
        	System.out.println(e.getMessage());
            valid = false;
        }
        return valid;
    }
	
	public static boolean isValidLuhn(String pnbr) {
	    int sum = 0;
	    for (int i = 0; i < pnbr.length(); i++){
	      char temp = pnbr.charAt(i);
	      int num = temp - '0';
	      int product = 0;
	      if (i % 2 != 0){
	        product = num * 1;
	      } else{
	        product = num * 2;
	      }
	      if (product > 9)
	        product -= 9;
	      sum+= product;              
	    }
	    if (!(sum % 10 == 0)) {
	    	System.out.println(pnbr + ": Incorrect control number");
	    	return false;
	    } else {
	    	return true;
	    }
	}
	
	public static boolean isNumeric(String s) {
	    if (s == null) {
	        return false;
	    }
	    try {
	        double d = Double.parseDouble(s);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}
	
	//validates personummer for a person born more than 100 years ago
	//does not check leap years
	public static void validOldPnbr(String s) {
		String pnbr = s.replace("+", "");		
		List<Integer> longMonths = new ArrayList<>();
		Collections.addAll(longMonths, 1,3,5,7,8,10,12);
		List<Integer> shortMonths = new ArrayList<>();
		Collections.addAll(shortMonths, 4,6,9,11);
		
		int year = Integer.parseInt(pnbr.substring(0, 2));
		int month = Integer.parseInt(pnbr.substring(2, 4));
		int day = Integer.parseInt(pnbr.substring(4, 6));
		
		boolean validDate = false;
		if ((0 <= year && year < 100) && (0 < month && month < 13)) {
			if (month == 2 && (0 < day && day <= 29)) {
				validDate = true;
			} else if (longMonths.contains(month) && (0 < day && day < 32)) {
				validDate = true;
			} else if (shortMonths.contains(month) && (0 < day && day < 31)) {
				validDate = true;
			} else {
				validDate = false;
			}
		} else {
			System.out.println("Not a valid date: " + pnbr);
			return;
		}	
		if (!validDate) {
			System.out.println(day + "/" + month + " is not a valid day of month.");
			return;
		}		
		if (isValidLuhn(pnbr)) {
			System.out.println("Valid");
		}	
	}
	
	//method to validate organisationsnummer
	public static void checkValidOrg(String pnbr) {
		if (isValidLuhn(pnbr)) {
			System.out.println("Valid");
		}
	}
	
	//method to validate regular personummer and samordningsnummer
	public static void checkValid(String pnbr){
		LocalDate dt = LocalDate.now();
        int year = Integer.parseInt(Integer.toString(dt.getYear()).substring(2, 3));
        
		int date = 0;
		if (pnbr.length() == 12) {
			date = Integer.parseInt(pnbr.substring(6, 8));
		} else {
			date = Integer.parseInt(pnbr.substring(4, 6));
			if (Integer.parseInt(pnbr.substring(0, 2)) > year) {
				pnbr = "19" + pnbr;
			} else {
				pnbr = "20" + pnbr;
			}
		}	
		//handle samordningsnummer
		if (date > 60 && date < 92) {
			date = date - 60;
		}
		boolean validDate = isValidDate(pnbr.substring(0, 4) + "-" + pnbr.substring(4, 6) + "-" + Integer.toString(date));
		if (!validDate) {
			return;
		}
		if (isValidLuhn(pnbr.substring(2))) {
			System.out.println("Valid");
		}
	}
	
	//method to parse and validate input
	public static void validate(String s) {
		if (s.contains("+")) {
			validOldPnbr(s);
		} else {
			String pnbr = s.replace("-", "");
			if (isNumeric(pnbr)) {				
				//check validation for an organisationsnumber
				if (pnbr.length() == 12) {
					int middle = Integer.parseInt(pnbr.substring(4, 6));
					if ((pnbr.substring(0, 2).equals("16")) && (middle >= 20)) {
						checkValidOrg(pnbr.substring(2));
						return;
					}
				} else if (pnbr.length() == 10){
					int middle = Integer.parseInt(pnbr.substring(2, 4));
					if (middle >= 20) {
						checkValidOrg(pnbr);
						return;
					}
				} else {
					System.out.println("Wrong length of input.");
					return;
				}
				//check validation for personummer/samordningsnummer
				checkValid(pnbr);
			} else {
				System.out.println("Input must be of numbers.");
			}
		}
	}
	
	
	public static void main(String[] args) {
		//Validation v = new Validation();
		Scanner scanner = new Scanner(System.in);
		while(true) {
			System.out.println("Please write a swedish 'personnummer'/'samordningsnummer'/'organisationsnummer' "
					+ "to check if valid:");
			String line = scanner.nextLine().trim();
			validate(line);
		}

	}

}