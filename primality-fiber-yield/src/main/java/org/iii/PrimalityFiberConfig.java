package org.iii;

import co.paralleluniverse.common.monitoring.MonitorType;
import co.paralleluniverse.fibers.FiberForkJoinScheduler;
import co.paralleluniverse.fibers.FiberScheduler;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PrimalityFiberConfig {
    public static final String WORKER_POOL_NAME = "workerPool";

    @Bean(WORKER_POOL_NAME)
    public FiberScheduler fiberPool() {
        return new FiberForkJoinScheduler("math-", 10, MonitorType.NONE, false);
    }

    @Bean
    public ApplicationContextAware contextProvider() {
        return new ApplicationContextProvider();
    }

    public static class ApplicationContextProvider implements ApplicationContextAware {
        private static ApplicationContext context = null;

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            context = applicationContext;
        }

        public static ApplicationContext getApplicationContext() {
            return context;
        }
    }
}
