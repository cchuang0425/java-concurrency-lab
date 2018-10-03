package org.iii.math;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import org.iii.common.ChannelRegistry;
import org.iii.domain.BaseCommand;
import org.iii.domain.CommandMessage;
import org.iii.mq.CommandSender;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Component
@Scope(SCOPE_PROTOTYPE)
public class FibCommand extends BaseCommand<Long, Long> {

    @Autowired
    private CommandSender sender;

    @Autowired
    private SystemCache caches;

    @Autowired
    private PrimeCommand primeCommand;

    @Autowired
    private ChannelRegistry channels;

    @Override
    protected void execute() {
        sender.sendCommand(convertCommendMessage());

        caches.getFibCache().create(commandMessage.getId(), this);
        caches.getFibMap().put(super.commandMessage.getId(), super.commandMessage.getParam());
    }

    @Override
    protected void next() {
        CommandMessage<Long> primeMsg = MathCommands.createPrimeCommand(resultValue());
        primeCommand.exec(primeMsg);
        caches.getPrimeMap().put(primeMsg.getId(), super.commandMessage.getId());
    }
}
