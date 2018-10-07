package com.vera.shared.model;

/**
 * @author ychost
 * @date 2018/10/7
 */
@FunctionalInterface
public interface Function3<A,B,C,D> {
   D apply(A a,B b,C c);
}
