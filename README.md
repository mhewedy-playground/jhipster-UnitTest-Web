I cannot unit test the web layer using jhipster:

```
@RunWith(SpringRunner.class)
@WebMvcTest
public class FlightControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FlightService flightService;

    @Test
    public void testCreate() throws Exception {

        FlightDto expected = new FlightDto();
        ReflectionTestUtils.setField(expected, "id", 1L);

        when(flightService.createFlight(any(FlightDto.class))).thenReturn(expected);

        FlightDto flightDto = new FlightDto();
        flightDto.setNumber("CAI-123400");

        this.mockMvc.perform(post("/api/flight")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(flightDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", endsWith("/api/flight/1")));
    }

}
```

The above Unit test success in case of green-field spring boot project, but fails in case of green-field spring boot-based jhipster project:

When run Unit Test in jhispter project (springboot-with-jhipster) from FlightResource I got:
```
java.lang.IllegalStateException: Unable to find a @SpringBootConfiguration, you need to use @ContextConfiguration or @SpringBootTest(classes=...) with your test

	at org.springframework.util.Assert.state(Assert.java:70)
	at org.springframework.boot.test.context.SpringBootTestContextBootstrapper.getOrFindConfigurationClasses(SpringBootTestContextBootstrapper.java:202)
	at org.springframework.boot.test.context.SpringBootTestContextBootstrapper.processMergedContextConfiguration(SpringBootTestContextBootstrapper.java:137)
	at org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTestContextBootstrapper.processMergedContextConfiguration(WebMvcTestContextBootstrapper.java:35)
	at org.springframework.test.context.support.AbstractTestContextBootstrapper.buildMergedContextConfiguration(AbstractTestContextBootstrapper.java:409)
	at org.springframework.test.context.support.AbstractTestContextBootstrapper.buildDefaultMergedContextConfiguration(AbstractTestContextBootstrapper.java:323)
	at org.springframework.test.context.support.AbstractTestContextBootstrapper.buildMergedContextConfiguration(AbstractTestContextBootstrapper.java:277)
	at org.springframework.test.context.support.AbstractTestContextBootstrapper.buildTestContext(AbstractTestContextBootstrapper.java:112)
	at org.springframework.boot.test.context.SpringBootTestContextBootstrapper.buildTestContext(SpringBootTestContextBootstrapper.java:82)
	at org.springframework.test.context.TestContextManager.<init>(TestContextManager.java:120)
	at org.springframework.test.context.TestContextManager.<init>(TestContextManager.java:105)
	at org.springframework.test.context.junit4.SpringJUnit4ClassRunner.createTestContextManager(SpringJUnit4ClassRunner.java:152)
	at org.springframework.test.context.junit4.SpringJUnit4ClassRunner.<init>(SpringJUnit4ClassRunner.java:143)
	at org.springframework.test.context.junit4.SpringRunner.<init>(SpringRunner.java:49)
	at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)
	at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62)
	at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45)
	at java.lang.reflect.Constructor.newInstance(Constructor.java:423)
	at org.junit.internal.builders.AnnotatedBuilder.buildRunner(AnnotatedBuilder.java:104)
	at org.junit.internal.builders.AnnotatedBuilder.runnerForClass(AnnotatedBuilder.java:86)
	at org.junit.runners.model.RunnerBuilder.safeRunnerForClass(RunnerBuilder.java:59)
	at org.junit.internal.builders.AllDefaultPossibilitiesBuilder.runnerForClass(AllDefaultPossibilitiesBuilder.java:26)
	at org.junit.runners.model.RunnerBuilder.safeRunnerForClass(RunnerBuilder.java:59)
	at org.junit.internal.requests.ClassRequest.getRunner(ClassRequest.java:33)
	at com.intellij.junit4.JUnit4IdeaTestRunner.startRunnerWithArgs(JUnit4IdeaTestRunner.java:49)
	at com.intellij.rt.execution.junit.IdeaTestRunner$Repeater.startRunnerWithArgs(IdeaTestRunner.java:47)
	at com.intellij.rt.execution.junit.JUnitStarter.prepareStreamsAndStart(JUnitStarter.java:242)
	at com.intellij.rt.execution.junit.JUnitStarter.main(JUnitStarter.java:70)
```

