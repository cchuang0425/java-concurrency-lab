package org.iii.common;

public interface Command<R, P> {
    R execute(P param);
}
