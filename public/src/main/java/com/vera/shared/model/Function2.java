package com.vera.shared.model;

/**
 * @author ychost
 * @date 2018/10/7
 */
@FunctionalInterface
public interface Function2<A,B,C> {
    C apply(A a,B b);
}
