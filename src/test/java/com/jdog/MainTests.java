
package com.jdog;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.PrintStream;

import org.junit.Ignore;
import org.junit.Test;

public class MainTests {
    
    private PrintStream out = System.out;
    @Test
    public void test_getProfileEnvKafka() {
        Environment env = mock(Environment.class);
        Main main = new Main(new Server(), env);
        when(env.getCdrProfile()).thenReturn(Environment.CdrProfile.KAFKA);

        assertEquals(KafkaProducingService.class,  main.getService().getClass());
    }

    @Test
    public void test_getProfileEnvNone() {
        Environment env = mock(Environment.class);
        Main main = new Main(new Server(), env);
        when(env.getCdrProfile()).thenReturn(com.jdog.Environment.CdrProfile.NONE);

        assertEquals(DefaultService.class,  main.getService().getClass());
    }


    @Test
    @Ignore
    public void learnEnv() {
        out.println(System.getenv("NEWVAR"));
    }

    
}
