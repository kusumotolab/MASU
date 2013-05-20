package sdl.ist.osaka_u.newmasu.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sdl.ist.osaka_u.newmasu.JDTParser;

public class TotalTest {
    @Before
    public void setUp() throws Exception {
        final JDTParser viewer = new JDTParser();

        viewer.addLibraries(null);
        viewer.addTargetFiles();
        viewer.parseTargetFiles();
        viewer.writeMetrics();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testAll() throws Exception {

    }
}
