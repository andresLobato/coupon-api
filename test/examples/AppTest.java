package examples;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Test 1
     */
    public void testCase1()
    {
    	Map<String, Float> items = new LinkedHashMap<String, Float>();
    	items.put("ML4", 80f);
    	items.put("ML5", 90f);
    	items.put("ML1", 100f);
    	items.put("ML2", 210f);
    	items.put("ML3", 260f);
    	CuponUtils utils = new CuponUtils();
    	List<String> actual = utils.calculate(items, 500f);
        assertEquals(Arrays.asList("ML4", "ML5", "ML1", "ML2"),actual);
    }
    
    /**
     * Test 2
     */
    public void testCase2()
    {
    	Map<String, Float> items = new LinkedHashMap<String, Float>();
    	items.put("ML1", 30f);
    	items.put("ML2", 40f);
    	items.put("ML3", 50f);
    	items.put("ML4", 75f);
    	items.put("ML5", 80f);
    	items.put("ML6", 200f);
    	CuponUtils utils = new CuponUtils();
    	List<String> actual = utils.calculate(items, 220f);
        assertEquals(Arrays.asList("ML3", "ML4", "ML5"),actual);
    }
    
    /**
     * Test 3
     */
    public void testCase3()
    {
    	Map<String, Float> items = new LinkedHashMap<String, Float>();
    	items.put("ML1", 20f);
    	items.put("ML2", 40f);
    	items.put("ML3", 50f);
    	items.put("ML4", 75f);
    	items.put("ML5", 80f);
    	items.put("ML6", 200f);
    	CuponUtils utils = new CuponUtils();
    	List<String> actual = utils.calculate(items, 220f);
        assertEquals(Arrays.asList("ML1", "ML6"),actual);
    }
    
    /**
     * Test 4
     */
    public void testCase4()
    {
    	Map<String, Float> items = new LinkedHashMap<String, Float>();
    	items.put("ML1", 20f);
    	CuponUtils utils = new CuponUtils();
    	List<String> actual = utils.calculate(items, 30f);
        assertEquals(Arrays.asList("ML1"),actual);
    }
    
    /**
     * Test 5
     */
    public void testCase5()
    {
    	Map<String, Float> items = new LinkedHashMap<String, Float>();
    	items.put("ML1", 40f);
    	CuponUtils utils = new CuponUtils();
    	List<String> actual = utils.calculate(items, 30f);
        assertEquals(Arrays.asList(),actual);
    }
}
