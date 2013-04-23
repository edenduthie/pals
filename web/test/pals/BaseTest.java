package pals;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

@ContextConfiguration(locations={"file:test/mvc-dispatcher-servlet.xml"})
public class BaseTest extends AbstractTestNGSpringContextTests {}
