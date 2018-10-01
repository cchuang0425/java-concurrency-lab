package org.iii.fib;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import org.iii.common.Command;

import static org.iii.fib.FibCommand.COMMAND_NAME;
import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Component(COMMAND_NAME)
@Scope(SCOPE_PROTOTYPE)
public class FibCommand implements Command<Long, Long> {
    public static final String COMMAND_NAME = "FibCommand";

    @Autowired
    private FibService service;

    @Override
    public Long execute(Long param) {
        System.out.printf("cal fib: %d...%n", param);

        if (param < 0L) {
            return 0L;
        } else {
            return service.calFibRec(param);
        }
    }
}
