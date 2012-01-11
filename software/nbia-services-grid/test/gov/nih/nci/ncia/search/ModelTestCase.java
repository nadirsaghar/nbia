package gov.nih.nci.ncia.search;

import junit.framework.TestCase;

public class ModelTestCase extends TestCase {

	public void testModel() {
		String[] versions = { "v1", "v2", "v3"};
		Model model = new Model();
		model.setName("foo1");
		model.setVersions(versions);
		assertTrue(model.getName().equals("foo1"));
		assertTrue(model.getVersions().length==3);
		
		model.setVersions(1, "moo");
		assertTrue(model.getVersions(1).equals("moo"));
	}

}
