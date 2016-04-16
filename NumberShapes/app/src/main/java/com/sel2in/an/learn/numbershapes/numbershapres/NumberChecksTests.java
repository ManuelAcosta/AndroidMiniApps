package com.sel2in.an.learn.numbershapes.numbershapres;



class NumberChecksTests{
	int cnt = 0;
	int errs = 0;
     public static void main(String []args){
     	NumberChecksTests app = new NumberChecksTests();
     	app.triangularTests();
     	app.squareTests();
     	app.jointTests();
     }

    public void  triangularTests(){
        cnt = 0;
        errs = 0;
        triangularTest(1, true);
        triangularTest(2, false);
        triangularTest(4, false);
        triangularTest(3, true);
        triangularTest(6, true);
        triangularTest(0, false);
        triangularTest(28, true);
        triangularTest(16, false);
        triangularTest(19, false);
        triangularTest(45, true);
        System.out.println("Triangular Tests :" + cnt + ", errs :" + errs + ", all good :"+ (cnt > 6 && errs==0) + ".");
    }

    public void triangularTest(double numb, boolean expected){
       	cnt++;
       	try{
	    	NumberChecks nc = new NumberChecks(numb);
	    	final boolean actual = nc.isTriangular();
		final boolean verify = actual == expected;
	    	if(verify == false){
	    		errs++;
	    		System.out.println("triangular Test :" + numb + ", expected:" + expected+ ", actual :" + actual+ ".");
	    	}

    	}catch(Throwable e){
    		System.out.println("triangular Test :" + numb + ", expected:" + expected+ " Err : " + e + ".");
    	}

    }

    public void squareTests(){

	 cnt = 0;
        errs = 0;
        squareTest(1, true);
        squareTest(2, false);
        squareTest(4, true);
        squareTest(3, false);
        squareTest(6, false);
        squareTest(0, true);
        squareTest(28, false);
        squareTest(16, true);
        squareTest(19, false);
        squareTest(41616, true);
        System.out.println("Square Tests :" + cnt + ", errs :" + errs + ", all good :"+ (cnt > 6 && errs==0) + ". ");// + " flr(34d) " +  Math.floor(34d) + ", 99d " + Math.floor(99d) + "  " + (Math.floor(99d) == 99d));
    }

	public void squareTest(double numb, boolean expected){
       	cnt++;
       	try{
	    	NumberChecks nc = new NumberChecks(numb);
	    	final boolean actual = nc.isSquare();
		final boolean verify = actual == expected;
	    	if(verify == false){
	    		errs++;
	    		System.out.println("Square Test :" + numb + ", expected:" + expected+ ", actual :" + actual+ ".");
	    	}

    	}catch(Throwable e){
    		System.out.println("Square Test :" + numb + ", expected:" + expected+ " Err : " + e + ".");
    	}

    }

    public void jointTests(){
    	//100128
	cnt = 0;
        errs = 0;
        int n = 41616;
        jointTest(n, true, true);//square, tri
        jointTest(5, false, false);
        jointTest(36, true, true);
        jointTest(1225, true, true);
        jointTest(6, false, true);
        jointTest(16, true, false);
        jointTest(17, false, false);
        jointTest(25, true, false);
        jointTest(45, false, true);
        jointTest(1227, false, false);
        jointTest(-5, false, false);
        System.out.println("Joint Tests :" + cnt + ", errs :" + errs + ", all good :"+ (cnt > 6 && errs==0) + ".");

    }

     public void jointTest(double numb, boolean expectedS,  boolean expectedT){
       	cnt++;
       	try{
	    	NumberChecks nc = new NumberChecks(numb);
	    	final boolean actualS = nc.isSquare();
		final boolean actualT = nc.isTriangular();
	    	if(!(actualS == expectedS && expectedT == actualT)){
	    		errs++;
	    		System.out.println("Joint Test (Square) :" + numb + ", Triangular expected:" + expectedT+ ", actual :" + actualT
	    		+ ", Square expected:" + expectedS + ", actual :" + actualS + ".");
	    	}

    	}catch(Throwable e){
    		System.out.println(" joint Test :" + numb + ", expectedS:" + expectedS + " Err : " + e + ".");
    	}

    }
}