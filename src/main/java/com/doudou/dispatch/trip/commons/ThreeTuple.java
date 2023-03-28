package com.doudou.dispatch.trip.commons;

public class ThreeTuple<A,B,C> {

    public A first;

    public B second;

    public C three;

    public ThreeTuple(A a, B b,C c){
        first = a;
        second = b;
        three = c;
    }

    public String toString(){
        return "(" + first + ", " + second + ")";
    }
}
