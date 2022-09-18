package RegexParser;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserImplementation {
	public static void main(String[] args) {
		System.out.println("Recursive Descent Parser");

		Tokenizer t = new Tokenizer();
		String inputString = "";

		try {

			
			Scanner scannedInput = new Scanner(System.in);
			System.out.println("Please enter input string : ");
			

				inputString =scannedInput.nextLine();
				scannedInput.close();
			
			System.out.println("Input to process =" + inputString);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		ArrayList<Token> tokenlist = t.tokenizeGrammer(inputString);

		// ArrayList<Token> tokenlist=t.tokenizeGrammer("{22,[6...8] = 33,54, [12 ...
		// 14] = { 44, 33, [4] = { 99, }, },}");//complex out -F
		// ArrayList<Token> tokenlist=t.tokenizeGrammer("{ 22, [7] = 99, 44,
		// [10...12]=33, { 44, [3] = 73, 7 }, 12 }");// mixed inits -F
		// ArrayList<Token> tokenlist=t.tokenizeGrammer("{ 33, 11, 23, 55 }");//
		// multielement -P
		// ArrayList<Token> tokenlist=t.tokenizeGrammer("{}"); // empty init -P
		// ArrayList<Token> tokenlist=t.tokenizeGrammer("42"); //int -P
		// ArrayList<Token> tokenlist=t.tokenizeGrammer("{{{42}}}");// nested single
		// element -P
		// ArrayList<Token>
		// tokenlist=t.tokenizeGrammer("{42,33,{44},88,{99}}");//positional inits -P
		// ArrayList<Token> tokenlist=t.tokenizeGrammer("{42}"); -P
		// ArrayList<Token> tokenlist=t.tokenizeGrammer("{22,{33,},44}"); // trailing
		// comma -P
		// ArrayList<Token> tokenlist=t.tokenizeGrammer("{ [2] = 99, [2]=45, [2]= 33
		// }"); // overwrite -F
		// ArrayList<Token> tokenlist=t.tokenizeGrammer("{ [2 ... 5] = 99, [8 ... 10] ={ 44, 33 } }"); // range inits -F
		//{ [2 ... 5] = 99, [8 ... 10] =77}
		// ArrayList<Token> tokenlist=t.tokenizeGrammer(" { [2] = 99, [5] = 44 }"); //
		// simple design inits -P
		// ArrayList<Token> tokenlist=t.tokenizeGrammer(" { [2] = 33, [4] = 55,[9]=89
		// }"); //-P

		// ArrayList<Token> tokenlist=t.tokenizeGrammer("{ 22, 33, {44}, } }"); // too
		// many braces -P
		// ArrayList<Token> tokenlist=t.tokenizeGrammer("{ 22, 99"); // unbalanced -P
		// ArrayList<Token> tokenlist=t.tokenizeGrammer(" { 88, , }"); //extra comma -F
		// ArrayList<Token> tokenlist=t.tokenizeGrammer("{ 22, 33, x4 }");//garbage -P
		// ArrayList<Token> tokenlist=t.tokenizeGrammer("{ 22, { 44 }");//nested
		// unbalance -P
		// ArrayList<Token> tokenlist=t.tokenizeGrammer(" { 22 } { 33 }");//multi top
		// levels -F
		// ArrayList<Token> tokenlist=t.tokenizeGrammer(" { 22 33, }"); //missing comma
		// -F
		// ArrayList<Token> tokenlist=t.tokenizeGrammer(" { 44, [33 = 77 }");//bad
		// design error -P
		// ArrayList<Token> tokenlist=t.tokenizeGrammer("{ 21, [33 .. 44] = 2 }");//bad
		// range -P

		// ArrayList<Token> tokenlist=t.tokenizeGrammer("$?");//non zero status -P
		// ArrayList<Token> tokenlist=t.tokenizeGrammer("{"); //missing } -P
		// ArrayList<Token> tokenlist=t.tokenizeGrammer("{ [3..5]= 99}");//expecting .
		// -F
		// ArrayList<Token> tokenlist=t.tokenizeGrammer("{ 99, ,}");//expecting } but
		// got , -F
		// ArrayList<Token> tokenlist=t.tokenizeGrammer("{ 1, 2, [99 = 33 ,}");
		// //missing ] -P
		// ArrayList<Token> tokenlist=t.tokenizeGrammer("{ 1, 2, }}");//expecting EOF
		// but found }-P
		// ArrayList<Token> tokenlist=t.tokenizeGrammer("{22, {44, 99}, [6...8]=33,}");
		// //-F
		// ArrayList<Token> tokenlist=t.tokenizeGrammer("{22, {44, 99}, [6...8]={33,
		// [3]=4, 77}, [7...8]=99,}");//-F
		// ArrayList<Token> tokenlist=t.tokenizeGrammer("{22, {44, 99}, [6...8]={33,
		// [3]=4, 77}, 99,}");//-F
		// ArrayList<Token> tokenlist=t.tokenizeGrammer("{22, {44, 99}, 33,}");//-P
		// ArrayList<Token> tokenlist=t.tokenizeGrammer("{22, 33}");//-P
		// ArrayList<Token> tokenlist=t.tokenizeGrammer("{}");//-P
		// ArrayList<Token> tokenlist=t.tokenizeGrammer("42");//-p

		String tokenListString = "";
		for (int i = 0; i < tokenlist.size(); i++) {
			tokenListString = tokenListString + tokenlist.get(i).getLexeme();
		}
		Parser parserObj = new Parser(tokenlist);
		String returnedParsedValue = parserObj.parse();
		
		if (!(returnedParsedValue.matches("unable to parse Grammer"))) {
			String output = "";
			validateinput(tokenListString);

			if (!returnedParsedValue.contains("[") || !returnedParsedValue.contains("]")) {
				String finalOutput = parserObj.getJsonString(tokenlist);
				System.out.println("Output === " + finalOutput);
			} else {
				String finalOutput = parserObj.getJsonString(tokenlist);
				System.out.println("Output === " + finalOutput);

			}

		}
	}

	public static boolean validateinput(String input) {

		int countOpenSquareBrackets = countOccurrences(input, Constants.TERMINAL_OPENED_SQUARE_BRACKET);
		int countClosedSquareBrackets = countOccurrences(input, Constants.TERMINAL_CLOSED_SQUARE_BRACKET);
		

		int countOpencurlyBrackets = countOccurrences(input, Constants.TERMINAL_OPENED_CURLY_BRACKET);
		int countClosedCurlyBrackets = countOccurrences(input, Constants.TERMINAL_CLOSED_CURLY_BRACKET);
		
		boolean commaAfterComma = false;
		for (int i = 0; i < input.length(); i++) {
			String currString = "" + input.charAt(i);
			if (i <= input.length() - 2) {
				String nexString = "" + input.charAt(i + 1);

				if (currString == "," && nexString == ",") {
					commaAfterComma = true;
				}
			}
		}
		if (countOpencurlyBrackets != countClosedCurlyBrackets) {
			Utils.showError(Constants.ERR_INVALID_CURLY_BRACKETS_STRING);
			System.exit(1);
			return false;
		} else if (countOpenSquareBrackets != countClosedSquareBrackets) {
			Utils.showError(Constants.ERR_INVALID_SQUARE_BRACKETS_STRING);
			System.exit(1);
			return false;
		} else if (commaAfterComma) {
			Utils.showError(Constants.ERR_INVALID_GRAMMER_EXTRA_COMMA);
			System.exit(1);
			return false;

		} else if (input.contains("....")) {// ||input.contains("..")||input.contains(".")) {
			Utils.showError(Constants.ERR_INVALID_GRAMMER_THREE_DOTS);
			System.exit(1);
			return false;
		} else {
			return true;
		}

	}

	private static int countOccurrences(String str, String ch) {
		try {

			int counter = 0;

			for (int i = 0; i < str.length(); i++) {
				String currentChar = "" + str.charAt(i);
				if (currentChar.endsWith(ch)) {
					counter++;
				}
			}

			return counter;
		} catch (Exception e) {

		}

		return 0;
	}
	
	
	public class Constants {
	    
		//Constants for Error messages
	    public static String ERR_NULL_INPUT_STRING = "Please input valid grammer, could not process empty grammer";
	    public static String ERR_INVALID_CURLY_BRACKETS_STRING = "Unbalanced number of curly brackets found input, Please enter valid input";
	    public static String ERR_INVALID_SQUARE_BRACKETS_STRING = "Unbalanced number of square brackets found input, Please enter valid input";
	    public static String ERR_INVALID_GRAMMER_EXTRA_COMMA = "Extra Comma found , Please enter valid input";
	    public static String ERR_INVALID_GRAMMER_THREE_DOTS = "Invalid sign for ... , Please enter valid input";



	    //Constants for Terminals
	    public static String TERMINAL_COMMA=",";
	    public static String TERMINAL_EQUALS="=";
	    public static String TERMINAL_THREE_DOTS="...";
	    public static String TERMINAL_INT="INT";
	    public static String TERMINAL_OPENED_SQUARE_BRACKET="[";
	    public static String TERMINAL_CLOSED_SQUARE_BRACKET="]";
	    public static String TERMINAL_OPENED_CURLY_BRACKET="{";
	    public static String TERMINAL_CLOSED_CURLY_BRACKET="}";
	    public static String TERMINAL_EXTRA="EXTRA";
	    
	    

	}
	/**
	 * Grammar Parser: Recursive descent parser
	 */
	public static class Parser {

		Token lookahead;
		int nextlookahead;
		int index = 0;
		StringBuilder jsonString;
		ArrayList<Token> tokens;

		public Parser(ArrayList<Token> tokens) {
			this.tokens = tokens;
			this.index = 0;
			this.lookahead = this.nextToken();
			jsonString = new StringBuilder();
		}

		public String parse() {
			try {
				parseGrammer(tokens);

				return jsonString.toString();
			} catch (Exception e) {
				e.printStackTrace();
				return "unable to parse Grammer";
			}

		}

		public Token nextToken() {
			if (this.index < this.tokens.size()) {
				return this.tokens.get(this.index++);
			} else {
				return new Token(Constants.TERMINAL_EXTRA, "EOF");
			}
		}

		// Added nextToNextToken to handle optional comma condition in the grammar
		public Token nextToNextToken() {
			if (this.index < (this.tokens.size() - 1)) {
				return this.tokens.get(this.index + 1);
			} else {
				return new Token(Constants.TERMINAL_EXTRA, "EOF");
			}
		}

		public boolean check(String kind) {
			if (this.lookahead.kind == kind) {
				jsonString.append(this.lookahead.lexeme);
				return true;
			} else {
				if (this.lookahead.lexeme == "EOF") {
					System.out.println("EOF");
				}
				return false;
			}
		}

		public void match(String kind) {

			if (this.check(kind)) {
				this.lookahead = nextToken();
			} else if (this.lookahead.lexeme == "EOF") {
				System.err.println("INVALID GRAMMER");
				System.exit(1);
			} else {

				String errMsg = "expecting " + kind + " at " + this.lookahead.getLexeme();
				Utils.showError(errMsg);
				System.exit(1);

			}

		}


		public void parseGrammer(ArrayList<Token> tokensList) {
			this.val();
		}

	//val
	//  : INT
	//  | '{' initializers '}'
	//  ;
		public void val() {

			if (this.index == this.tokens.size() + 1) {
				System.out.println("done with parsing");

			} else if (this.lookahead.kind == Constants.TERMINAL_INT) {
				this.match(Constants.TERMINAL_INT);

			} else if (this.lookahead.kind == Constants.TERMINAL_OPENED_CURLY_BRACKET) {
				this.match(Constants.TERMINAL_OPENED_CURLY_BRACKET);
				initializers();

				this.match(Constants.TERMINAL_CLOSED_CURLY_BRACKET);

			} else if (this.lookahead.lexeme == "EOF") {
				System.err.println("Reached EOF");
				System.exit(1);
			} else if (this.lookahead.kind == Constants.TERMINAL_EXTRA) {
				System.err.println("INVALID GRAMMER FOUND");
				System.exit(1);
			}

		}

	//initializers
	//: initializer ( ',' initializer )* ','? //optional comma after last init
	//| //empty
	//;

		public void initializers() {

			initializer();

			while (this.lookahead.kind == Constants.TERMINAL_COMMA) {
				
				this.match(Constants.TERMINAL_COMMA);

				if (this.lookahead.kind == Constants.TERMINAL_INT
						|| this.lookahead.kind == Constants.TERMINAL_OPENED_CURLY_BRACKET) {
					val();
				}

				if (this.lookahead.kind == Constants.TERMINAL_OPENED_SQUARE_BRACKET) {
					initializer();
				}
			}

		}

	//initializer
	// : '[' INT '] '=' val              //simple designated initializer
	//  | '[' INT '...' INT ']' '=' val   //range designated initializer
	//  | val                             //positional initializer
	//  ;

		public boolean initializer() {
			boolean isInitializer = false;
			if (this.lookahead.kind == Constants.TERMINAL_OPENED_SQUARE_BRACKET) {
				isInitializer = true;
				match(Constants.TERMINAL_OPENED_SQUARE_BRACKET);
				match(Constants.TERMINAL_INT);
				if (this.lookahead.kind == Constants.TERMINAL_THREE_DOTS) {
					if (lookahead.getLexeme() == ".." || lookahead.getLexeme() == "." || lookahead.getLexeme() == "....") {
						Utils.showError(Constants.ERR_INVALID_GRAMMER_THREE_DOTS);
						System.exit(1);
					} else {
						match(Constants.TERMINAL_THREE_DOTS);// TODO:
						match(Constants.TERMINAL_INT);
					}
				}
				match(Constants.TERMINAL_CLOSED_SQUARE_BRACKET);
				match(Constants.TERMINAL_EQUALS);
				val();
			} else {

				isInitializer = true;

				val();

			}
			return isInitializer;
		}

		public String getJsonString(ArrayList<Token> tokenList) {
			int counter = 0;
			String JSONString = "";
			
			for (int i = 0; i < tokenList.size(); i++) {
				if (tokenList.get(i).kind == Constants.TERMINAL_OPENED_CURLY_BRACKET) {
							JSONString = JSONString + "[";
				} else if (tokenList.get(i).kind == Constants.TERMINAL_CLOSED_CURLY_BRACKET) {
							JSONString = JSONString + "]";
				} else if (tokenList.get(i).kind == Constants.TERMINAL_COMMA) {
					String lastElement = "" + JSONString.charAt(JSONString.length() - 1);
					if (lastElement != ",") {
						JSONString = JSONString + ",";
					} else {
						JSONString = JSONString + "";
					}
				} else if (tokenList.get(i).kind == Constants.TERMINAL_INT) {

					JSONString = JSONString + tokenList.get(i).lexeme;
					counter++;

				} else if (tokenList.get(i).kind == Constants.TERMINAL_OPENED_SQUARE_BRACKET) {
					int firstNo = Integer.parseInt(tokenList.get(i + 1).lexeme);
					int secondNo = 999;
					String equalNo = "";
					boolean isThreeDot = false;

					if (tokenList.get(i + 2).getLexeme() == ".." || tokenList.get(i + 2).getLexeme() == "."
							|| tokenList.get(i + 2).getLexeme() == "....") {
						System.out.println(Constants.ERR_INVALID_GRAMMER_THREE_DOTS);
						System.exit(1);
					} else if (tokenList.get(i + 2).kind == Constants.TERMINAL_THREE_DOTS) {
						isThreeDot = true;
						secondNo = Integer.parseInt(tokenList.get(i + 3).lexeme);
						if ((tokenList.get(i + 6).lexeme) != Constants.TERMINAL_OPENED_CURLY_BRACKET) {
							equalNo = (tokenList.get(i + 6).lexeme);
						} else {
							int j = i + 7;
							equalNo = equalNo + "[";
							while (tokenList.get(j).getKind() != Constants.TERMINAL_CLOSED_CURLY_BRACKET) {

								equalNo = equalNo + tokenList.get(j).lexeme;
								j++;
							}
							equalNo = equalNo + "]";
						}
					} else {
						isThreeDot = false;
						if ((tokenList.get(i + 4).lexeme) != Constants.TERMINAL_OPENED_CURLY_BRACKET) {
							equalNo = tokenList.get(i + 4).lexeme;
						} else {

							equalNo = tokenList.get(i + 5).lexeme;
							
						}
					}
					if (!isThreeDot) {

						if (counter == firstNo) {
							counter = 0;
							JSONString = "";
						}

						while (counter <= firstNo) {
							if (counter == firstNo) {

								JSONString = JSONString + equalNo;
								counter++;
							} else {
								JSONString = JSONString + "0,";
								counter++;
							}

						}
						

						i = i + 4;
					} else {
						while (counter <= firstNo) {
							if (counter == firstNo) {

								String lastElement = "" + JSONString.charAt(JSONString.length()-1);
								if (lastElement != ",") {
									JSONString = JSONString + equalNo + ",";
								} else {
									JSONString = JSONString + equalNo;
								}

								counter++;
								while (counter != secondNo) {

									String lastElement1 = "" + JSONString.charAt(JSONString.length()-1);
									if (lastElement1 != ",") {
										JSONString = JSONString + equalNo + ",";
									} else {
										JSONString = JSONString + equalNo;
									}
									counter++;
								}

							} else {
								JSONString = JSONString + "0,";
								counter++;
							}

						}

						i = i + 6;
					}

				} else {
					//
				}
			}

			return JSONString;
		}


		public String processNestedGrammer(ArrayList<Token> tokenList) {
			ArrayList<Token> Tlist = tokenList;
			String firstKind = "";
			for (int i = 0; i < tokenList.size(); i++) {

				if (tokenList.get(i).kind == Constants.TERMINAL_OPENED_SQUARE_BRACKET) {
					
					int firstNo = Integer.parseInt(tokenList.get(i + 1).lexeme);
					int secondNo = 999, equalNo;

					if (tokenList.get(i + 2).kind == Constants.TERMINAL_THREE_DOTS) {
						secondNo = Integer.parseInt(tokenList.get(i + 3).lexeme);
						equalNo = Integer.parseInt("" + (tokenList.get(i + 5).lexeme));
					}
					equalNo = Integer.parseInt("" + (tokenList.get(i + 4).lexeme));
					
					int firstNoCopy = firstNo;
					ArrayList<Token> toklist = Tlist;
					for (int k = i; k < firstNo; k++) {
						toklist.remove(k);
					}
					
					for (int j = i - 1; j <= firstNoCopy; j++) {
						if (secondNo == 999) {

							if (j != firstNoCopy) {

								Token t1 = new Token(Constants.TERMINAL_INT, "0,");

								tokenList.add(j, t1);

								firstKind = firstKind + "0,";

							} else {
								Token t2 = new Token(Constants.TERMINAL_INT, "" + equalNo);
								tokenList.add(j, t2);
								firstKind = firstKind + equalNo;
							}
						} else {
//							System.out.println(" logic to implement");

						}
					}
				}
			}

			return firstKind;
		}

	}

	
	/*POJO class for Token which stores kind and lexeme required for perocessing grammer
	 * **/
	public static class Token {
		
		String kind;
		String lexeme;

	    Token(){
	        kind=null;
	        lexeme=null;
	    }
		
		public Token(String kind, String lexeme) {
			this.kind = kind;
			this.lexeme = lexeme;
		}
		
		public void setToken(String kindArg, String lexemeArg) {
			kind = kindArg;
			lexeme = lexemeArg;
		}

		public String getKind() {
			return kind;
		}

		public void setKind(String kind) {
			this.kind = kind;
		}

		public String getLexeme() {
			return lexeme;
		}

		public void setLexeme(String lexeme) {
			this.lexeme = lexeme;
		}

	    @Override
	    public String toString() {
	        return "Token [kind:" + kind + ", lexeme:" + lexeme + "]";
	    }

	    
		
	}
	
	public static class Tokenizer {

	    ArrayList<Token> tokenList;
	    int grammerLength;

	    public  Tokenizer(){
	        tokenList=new ArrayList<>();
	    }

	    public ArrayList<Token> tokenizeGrammer(String grammerIn){

	        //Remove all the whitespaces in the input
	        grammerIn= grammerIn.replaceAll(" ", "");
	        grammerIn=grammerIn.replaceAll("\\n", "");
	        
	        //getting length of a grammer to process
	        grammerLength= grammerIn.length();
	        tokenList= getTokenList(grammerIn,grammerLength);

	        return tokenList;
	    }

	    public ArrayList<Token> getTokenList(String inpuString,int inputLength){
	        int lookahead=0;
	        //int nextlookahead=lookahead+1;
	        String originalInputString= inpuString;
	        int originalStringLength = originalInputString.length();
	       try{
	        for(int i=lookahead;i<=originalStringLength;i++){
	        if(Character.isDigit(inpuString.charAt(0))){
	            Pattern patt1 = Pattern.compile("^[0-9]*");
	                Matcher matcher = patt1.matcher(inpuString);
	                if(matcher.find()){
	                    int startIndex= matcher.start();
	                    int lastIndex=matcher.end();
	                                                     
	                    String value= inpuString.substring(startIndex,lastIndex); // you can get it from desired index as well
	                    lookahead=lookahead+lastIndex;

	                    inpuString=originalInputString.substring(lookahead,originalStringLength);
	                    inputLength=inpuString.length();
	                    tokenList.add(ProcessTokens(value, true));

	                }   
	                }else if(inpuString.startsWith(Constants.TERMINAL_COMMA)){
	                   
	                	tokenList.add(ProcessTokens(Constants.TERMINAL_COMMA,false));
	                	lookahead=lookahead+1;
	                    
	                    inpuString=originalInputString.substring(lookahead,originalStringLength);
	                    inputLength=inpuString.length();

	                }
	                else if(inpuString.startsWith(Constants.TERMINAL_EQUALS)){
	                    tokenList.add(ProcessTokens(Constants.TERMINAL_EQUALS,false));
	                    lookahead=lookahead+1;
	                    
	                    inpuString=originalInputString.substring(lookahead,originalStringLength);
	                    inputLength=inpuString.length();                   

	                }else if(inpuString.startsWith(Constants.TERMINAL_THREE_DOTS)){
	                    tokenList.add(ProcessTokens(Constants.TERMINAL_THREE_DOTS,false));
	                    lookahead=lookahead+3;
	                    
	                    inpuString=originalInputString.substring(lookahead,originalStringLength);
	                    inputLength=inpuString.length();             


	                }else if(inpuString.startsWith(Constants.TERMINAL_OPENED_SQUARE_BRACKET)){
	                    tokenList.add(ProcessTokens(Constants.TERMINAL_OPENED_SQUARE_BRACKET,false));
	                    lookahead=lookahead+1;
	                    
	                    inpuString=originalInputString.substring(lookahead,originalStringLength);
	                    inputLength=inpuString.length();                    

	                }else if(inpuString.startsWith(Constants.TERMINAL_CLOSED_SQUARE_BRACKET)){
	                    tokenList.add(ProcessTokens(Constants.TERMINAL_CLOSED_SQUARE_BRACKET,false));
	                    lookahead=lookahead+1;
	                    
	                    inpuString=originalInputString.substring(lookahead,originalStringLength);
	                    inputLength=inpuString.length();


	                }else if(inpuString.startsWith(Constants.TERMINAL_OPENED_CURLY_BRACKET)){
	                    tokenList.add(ProcessTokens(Constants.TERMINAL_OPENED_CURLY_BRACKET,false));
	                    lookahead=lookahead+1;
	                    
	                    inpuString=originalInputString.substring(lookahead,originalStringLength);
	                    inputLength=inpuString.length();                    


	                }else if(inpuString.startsWith(Constants.TERMINAL_CLOSED_CURLY_BRACKET)){
	                    tokenList.add(ProcessTokens(Constants.TERMINAL_CLOSED_CURLY_BRACKET,false));
	                    lookahead=lookahead+1;
	                    

	                    inpuString=originalInputString.substring(lookahead,originalStringLength);
	                    inputLength=inpuString.length();

	                }else{
	                    tokenList.add(ProcessTokens(inpuString.substring(i, i+1),false));
	                    lookahead=lookahead+1;
	                    
	                    inpuString=originalInputString.substring(lookahead,originalStringLength);
	                    inputLength=inpuString.length();

	                }

	                if(i==originalStringLength)
	                break;

	            }
	        }catch(java.lang.StringIndexOutOfBoundsException e){
	        }
	            return tokenList;
	        }
	    

	    
	    /*ProcessTokens method adds 
	     * **/
	    public Token ProcessTokens(String tok,boolean isInt){
	        Token t= new Token();
	        if(isInt){
	            t.kind="INT";
	            t.lexeme=tok;
	        }else if(tok.equals(Constants.TERMINAL_THREE_DOTS)){
	            t.kind=Constants.TERMINAL_THREE_DOTS;
	            t.lexeme=tok;
	        }else if(tok.equals(Constants.TERMINAL_COMMA)){
	            t.kind=Constants.TERMINAL_COMMA;
	            t.lexeme=Constants.TERMINAL_COMMA;
	        }else if(tok.equals(Constants.TERMINAL_EQUALS)){
	            t.kind=Constants.TERMINAL_EQUALS;
	            t.lexeme=Constants.TERMINAL_EQUALS;
	        }else if(tok.equals(Constants.TERMINAL_OPENED_SQUARE_BRACKET)){
	            t.kind=Constants.TERMINAL_OPENED_SQUARE_BRACKET;
	            t.lexeme=Constants.TERMINAL_OPENED_SQUARE_BRACKET;
	        }else if(tok.equals(Constants.TERMINAL_CLOSED_SQUARE_BRACKET)){
	            t.kind=Constants.TERMINAL_CLOSED_SQUARE_BRACKET;
	            t.lexeme=Constants.TERMINAL_CLOSED_SQUARE_BRACKET;
	        }else if(tok.equals(Constants.TERMINAL_OPENED_CURLY_BRACKET)){
	            t.kind=Constants.TERMINAL_OPENED_CURLY_BRACKET;
	            t.lexeme=Constants.TERMINAL_OPENED_CURLY_BRACKET;
	        }else if(tok.equals(Constants.TERMINAL_CLOSED_CURLY_BRACKET)){
	            t.kind=Constants.TERMINAL_CLOSED_CURLY_BRACKET;
	            t.lexeme=Constants.TERMINAL_CLOSED_CURLY_BRACKET;
	        }else{
	            t.kind=Constants.TERMINAL_EXTRA;
	            t.lexeme=tok;
	        }
	        return t;      
	    }
	    
	}

	public class Utils {

		/*showError method shows the error*/
	     public static void showError(String errMsg){
	        System.err.println(errMsg);
	     }
	}
	
	
}