package com.doudou.dispatch.trip.commons;

public class TwoTuple<A,B> {

    public A first;

    public B second;

    public TwoTuple(A a, B b){
        first = a;
        second = b;
    }

    public String toString(){
        return "(" + first + ", " + second + ")";
    }
}
