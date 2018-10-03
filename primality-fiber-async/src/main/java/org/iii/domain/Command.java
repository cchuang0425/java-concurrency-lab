package org.iii.domain;

import java.util.UUID;

public interface Command<P, R> {

    void exec(CommandMessage<P> commandMsg);

    P commandParam();

    UUID commandId();

    void success(ResultMessage<R> resultMsg);

    R resultValue();
}
