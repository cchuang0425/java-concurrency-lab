package org.iii.math;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import org.iii.domain.BaseCommand;
import org.iii.mq.CommandSender;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Component
@Scope(SCOPE_PROTOTYPE)
public class PrimeCommand extends BaseCommand<Long, Boolean> {

    @Autowired
    private CommandSender sender;

    @Autowired
    private SystemCache caches;

    @Autowired
    private MathService service;

    @Override
    protected void execute() {
        sender.sendCommand(convertCommendMessage());
        caches.getPrimeCache().create(commandMessage.getId(), this);
    }

    @Override
    protected void next() {
        service.aggregate(resultMessage.getId(), resultValue());
    }
}
