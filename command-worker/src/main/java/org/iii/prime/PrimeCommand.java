package org.iii.prime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import org.iii.common.Command;

import static org.iii.prime.PrimeCommand.COMMAND_NAME;
import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Component(COMMAND_NAME)
@Scope(SCOPE_PROTOTYPE)
public class PrimeCommand implements Command<Boolean, Long> {
    public static final String COMMAND_NAME = "PrimeCommand";

    @Autowired
    private PrimeService service;

    @Override
    public Boolean execute(Long param) {
        System.out.printf("cal prime: %d...%n", param);
        return service.isPrime(param);
    }
}
