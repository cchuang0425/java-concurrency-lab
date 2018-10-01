package org.iii;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import javax.jms.ConnectionFactory;

import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class PrimalityFiberConfig {

    public static final String COMMAND_QUEUE = "CommandQueue";
    public static final String RESULT_QUEUE = "ResultQueue";
    public static final String WORKER_POOL_NAME = "workerPool";
    public static final String MQ_LISTENER_CONTAINER_FACTORY_NAME = "mqListenerContainerFactory";

    @Bean(WORKER_POOL_NAME)
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(200);
        executor.setKeepAliveSeconds(120);
        executor.setThreadNamePrefix("math-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    @Bean(MQ_LISTENER_CONTAINER_FACTORY_NAME)
    public JmsListenerContainerFactory<?> mqListenerContainerFactory(ConnectionFactory connectionFactory,
            DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, connectionFactory);

        return factory;
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
