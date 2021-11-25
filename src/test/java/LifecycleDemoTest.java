import lombok.extern.slf4j.Slf4j;
import org.junit.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@Slf4j
@RunWith(JUnit4.class)
public class LifecycleDemoTest {

    @Before
    public void before() { System.out.println("before"); }

    @BeforeClass
    public static void beforeClass() { System.out.println("beforeClass"); }

    @BeforeAll
    public static void beforeAll() { System.out.println("beforeAll"); }

    //@BeforeEach and @BeforeAll are the JUnit 5 equivalents of @Before and @BeforeClass.
    @BeforeEach
    public static void beforeEach() { log.info("beforeEach"); }

    @AfterEach
    public static void afterEach() { log.info("afterEach"); }

    @AfterAll
    public static void afterAll() { log.info("afterAll"); }

    @AfterClass
    public static void afterClass() { System.out.println("afterClass"); }

    @After
    public void after() { System.out.println("after"); }

    @Test
    public void testOne() { log.info("Test One"); }

    @Test
    public void testTwo() { log.info("Test Two"); }
}
