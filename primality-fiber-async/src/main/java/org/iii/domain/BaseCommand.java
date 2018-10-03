package org.iii.domain;

import java.io.IOException;
import java.util.UUID;

import org.iii.util.JsonUtils;

public abstract class BaseCommand<P, R> implements Command<P, R> {

    protected CommandMessage<P> commandMessage;
    protected ResultMessage<R> resultMessage;

    @Override
    public void exec(CommandMessage<P> commandMsg) {
        this.commandMessage = commandMsg;
        execute();
    }

    protected String convertCommendMessage() {
        try {
            return JsonUtils.convertJsonFromObject(commandMessage);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    protected abstract void execute();

    @Override
    public P commandParam() {
        return commandMessage.getParam();
    }

    @Override
    public UUID commandId() {
        return commandMessage.getId();
    }

    @Override
    public void success(ResultMessage<R> resultMsg) {
        this.resultMessage = resultMsg;
        next();
    }

    protected abstract void next();

    @Override
    public R resultValue() {
        return resultMessage.getResult();
    }
}
