package org.iii;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommandWorkerConfigTest {

    @Test
    public void testGetApplicationContext(){
        ApplicationContext context =
                CommandWorkerConfig.ApplicationContextProvider.getApplicationContext();

        Assert.assertNotNull(context);
    }
}
