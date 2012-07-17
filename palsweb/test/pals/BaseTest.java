package pals;


import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author eduthie
 */
@ContextConfiguration(locations={"file:test/applicationContext.xml"})
public class BaseTest extends AbstractTestNGSpringContextTests
{
	public static final String TEST_DATA_DIR = "testdata";
}
