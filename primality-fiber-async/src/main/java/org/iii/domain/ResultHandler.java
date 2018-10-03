package org.iii.domain;

import org.iii.domain.ResultMessage;

public interface ResultHandler<T extends ResultMessage<?>> {
    void onReceive(T resultMessage);
}
