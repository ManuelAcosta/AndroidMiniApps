package com.sel2in.an.learn.numbershapes.numbershapres;

/**
 * Created by t on 16-04-2016.
 */
class NumberChecks{
    private double value;
    //cache these so we do less calcs for same call
    private boolean triangular;
    private boolean square;
    public NumberChecks(double d){
        setValue(d);
    }
    public double getValue(){
        return value;
    }

    public void setValue(double v){
        value = v;
        setTriangular(checkIfTriangular());
        setSquare(checkIfSquare());
    }

    public boolean isSquare(){
        return square;
    }

    private void setSquare(boolean v){
        square = v;
        //System.out.println("square :" + square + " " + value);
    }

    public boolean isTriangular(){
        return triangular;
    }

    private void setTriangular(boolean v){
        triangular= v;
    }
    //public so you can refresh it to make sure, redundant
    public boolean checkIfTriangular(){
        int i=1, c= 1;
        while(c < value){
            i++;
            c += i;
        }
        if(c==value && value > 0)return true;
        return false;
    }

    public boolean checkIfSquare(){
        final double sq = Math.sqrt(value);
        return Math.floor(sq) == sq;
    }

}