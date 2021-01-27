package net.thevpc.scholar.hadruplot.libraries.calc3d.elements;

import net.thevpc.scholar.hadruplot.libraries.calc3d.core.Globalsettings;
import net.thevpc.scholar.hadruplot.libraries.calc3d.mathparser.Calculable;
import net.thevpc.scholar.hadruplot.libraries.calc3d.mathparser.ExpressionBuilder;

public class PieceFunction implements Comparable<PieceFunction>{
	private String expr;
	public double min,max;
	public boolean leftClosed,rightClosed; 
	private String parseErrorMgs;
	public boolean isPolar=false;
	Calculable calc;
	Globalsettings globalsettings;

	public PieceFunction(Globalsettings globalsettings){
		this.globalsettings=globalsettings;
		min=isPolar?0:globalsettings.minX;
		max=isPolar?Math.PI*2:globalsettings.maxX;
	}

	
	public static PieceFunction parse(String expr, boolean isPolar,Globalsettings globalsettings) throws FunctionParseErrorException {
		//function definition and interval definition are separated by comma
		String[] arr=expr.split(",");
		PieceFunction f=new PieceFunction(globalsettings);
		f.isPolar=isPolar;
		boolean isvalid=false;
		if (arr.length==1){//there is no interval definiion so default interval is used
			isvalid=f.setExpr(arr[0]);
		}else{
			if (isInterval(arr[arr.length-1])){
				System.out.println(expr.substring(0, expr.lastIndexOf(",")));
				isvalid=f.setExpr(expr.substring(0, expr.lastIndexOf(",")));
				isvalid=isvalid & f.setInterval(arr[arr.length-1]);
			}else{
				isvalid=f.setExpr(expr);
			}
		}
		
		if (isvalid) return f;
		else
		{
			throw new FunctionParseErrorException(f.parseErrorMgs);
		}
	}
	
	private static boolean isInterval(String expr){
		if (expr.contains("<")||expr.contains(">")||expr.contains("="))return true;
		return false;
	}
	
    private boolean setInterval(String expr){
    	expr=preProcessInterval(expr);
    	
     	if (expr.length()<3){
    		parseErrorMgs="Error in interval";
    		return false;
    	}
    	
    	//get position of x
    	int i=expr.indexOf(isPolar?"t":"x");
       	if (i==0){
    		try{
    			 parseRightInequality(expr.substring(1));
       		}catch (Exception e){
    			 System.out.println(e.getMessage());
       		  	 parseErrorMgs=e.getMessage();
    			 return false;
    		}
    	
       	}else if (i==expr.length()-1){
       		try{
       			parseLeftIneqaulity(expr.substring(0,i));
     		}catch (Exception e){
    			System.out.println(e.getMessage());
    			parseErrorMgs=e.getMessage();
    			return false;
    		}
      	}else if ((0<i)&&(i<expr.length())){
    		if (expr.length()<5){
        		parseErrorMgs="Error in interval";
        		return false;
        	}
    	    try{
    	       	parseLeftIneqaulity(expr.substring(0,i));	
    	    	parseRightInequality(expr.substring(i+1));
    	    }catch (Exception e){
    	   	    System.out.println(e.getMessage());
    			parseErrorMgs=e.getMessage();
    			return false;
    	    }
    	}else {
    		parseErrorMgs="error in Interval: expected variable "+(isPolar?"t":"x");
    		return false;
    	}
       	
       	if (min>=max){
       		if ((min==max) && leftClosed && rightClosed)return true;
       		parseErrorMgs="Minimum value must be less than maximum value for interval";
       		return false;
       	}
        return true;
    }
	
    private String preProcessInterval(String expr) {
    	expr=expr.toLowerCase();
    	expr=expr.trim();
    	if(expr.contains(">") && expr.contains("<"))return "";
    	if (expr.length()<3)return "";
    	return expr;
   }


	private void parseRightInequality(String  expr) throws FunctionParseErrorException {
    	try{
    		 if (expr.length()<2)throw new Exception();
    	
    		 if (expr.startsWith("<=")||expr.startsWith("=<")){
    			 expr=expr.substring(2);
       			 setMax(Double.parseDouble(expr),true);
    		 }else if(expr.startsWith("<")){
    			 expr=expr.substring(1);
    			 setMax(Double.parseDouble(expr),false);
    		 }else if(expr.startsWith(">=")||expr.startsWith("=>")){
    			 expr=expr.substring(2);
    			 setMin(Double.parseDouble(expr),true);
    		 }else if(expr.startsWith(">")){
    			 expr=expr.substring(1);
    			 setMin(Double.parseDouble(expr),false);
    		 }else if (expr.startsWith("=")){
    			 expr=expr.substring(1);
    			 setMin(Double.parseDouble(expr),true);
    			 setMax(Double.parseDouble(expr),true);
    		 }else{
    			 throw new Exception(); 
    		 }
 		}catch (Exception e){
  			 throw new FunctionParseErrorException("invalid upper limit");
		}
    }
    
    private void parseLeftIneqaulity(String  expr) throws FunctionParseErrorException {
    	try{
    		 int i=expr.length();
    		 if (i<2)throw new Exception();
    	     if (expr.endsWith("<")){
    	    	 expr=expr.substring(0,i-1);
    	    	 setMin(Double.parseDouble(expr),false);
    	     }else if (expr.endsWith("<=")||expr.endsWith("=<")){
    	    	 expr=expr.substring(0,i-2);
    	    	 setMin(Double.parseDouble(expr),true);
    	     }else if (expr.endsWith(">")){
    	    	 expr=expr.substring(0,i-1);
    	    	 setMax(Double.parseDouble(expr),false);
    	     }else if (expr.endsWith(">=")||expr.endsWith("=>")){
    	    	 expr=expr.substring(0,i-2);
    	    	 setMax(Double.parseDouble(expr),true);
    	   	 }else{
    	    	throw new Exception(); 
    	     } 
     	 }catch (Exception e){
			 System.out.println(e.toString());
  			 throw new FunctionParseErrorException("invalid lower limit");
		}
    }
    
    private boolean setExpr(String expr){
		try {
			calc= new ExpressionBuilder(expr).withVariableNames(isPolar?"t":"x").build();
			this.expr=expr;
			return true;
		} catch (Exception e) {
			parseErrorMgs=e.getMessage();
			return false;
		}
	}
	
    public String getExpr(){
    	return expr;
    }
    	
    public void setMax(double d,boolean closed){
 		 System.out.println(d+":lowerlimit");
 		 max=d;
 		 rightClosed=closed;
	     if (!isPolar & (max>globalsettings.maxX))max=globalsettings.maxX;
    }
    
    public void setMin(double d,boolean closed){
        min=d;
        leftClosed=closed;
	    if (!isPolar & (min<globalsettings.minX))min=globalsettings.minX;
    }
    
    public String getInterval(){
    	return (leftClosed?" [ ":" ( ")+min+", "+max+(rightClosed?" ]":" )");
    }
    
	public double evaluate(double x){
		if ((x>=min)&& (x<=max)){
			if (isPolar){
				calc.setVariable("t", x);
			}else{
				calc.setVariable("x", x);
			}
			
			return calc.calculate();
		}
		return Double.NaN;
	}
	
	public boolean isValid(){
		if ((parseErrorMgs==null)||(parseErrorMgs==""))
			return true;
		else 
			return false;
	}


	@Override
	public int compareTo(PieceFunction f) {
		 if (min<f.min) return -1;
	     else if (min>f.min) return 1;
	     else return 0;
	}
	
	@Override
	public String toString(){
		String str;
		str=expr+"   "+(leftClosed?"[":"(")+min+","+max+(rightClosed?"]":")");
		return str;
	}
	
	
 }

 