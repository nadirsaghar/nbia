package gov.nih.nci.nbia.util;

import gov.nih.nci.nbia.util.StringEncrypter;
import junit.framework.TestCase;

public class StringEncrypterTestCase extends TestCase {

	public void testEncryptHappyPathDESede() throws Exception {
		String origText = "this is plain text";
		
		StringEncrypter stringEncrypter = new StringEncrypter();
		String encryptedText = stringEncrypter.encrypt(origText);
		
		//not really a great assertion, but it shoudl be true :)
		assertFalse(encryptedText.equals(origText));
		
	
		stringEncrypter = new StringEncrypter("DESede");
		String decryptedText = stringEncrypter.decrypt(encryptedText);
		
		assertTrue(decryptedText.equals(origText));
	}
	
	public void testEncryptHappyPathDES() throws Exception {
		String origText = "this is plain text";
		
		StringEncrypter stringEncrypter = new StringEncrypter("DES");
		String encryptedText = stringEncrypter.encrypt(origText);
		
		//not really a great assertion, but it shoudl be true :)
		assertFalse(encryptedText.equals(origText));
		
	
		stringEncrypter = new StringEncrypter("DES");
		String decryptedText = stringEncrypter.decrypt(encryptedText);
		
		assertTrue(decryptedText.equals(origText));
	}	

	
	public void testEncryptErrors() throws Exception {		
		try {
			new StringEncrypter("DES", null);
			fail("null key shouldnt be here");
		}
		catch(Exception ex) {
			//should be here - null key
		}
		try {
			new StringEncrypter("DES", "012345789");
			fail("shorty key shouldnt be here");
		}
		catch(Exception ex) {
			//should be here - null key
		}	
		try {
			new StringEncrypter("fake_encryption_scheme", "012345789012345789012345789012345789");
			fail("non DES shouldnt be here");
		}
		catch(Exception ex) {
			//should be here - null key
		}	
		
		try {
			StringEncrypter stringEncrypter = new StringEncrypter();
			stringEncrypter.encrypt(null);
			fail("null key shouldnt be here");
		}
		catch(Exception ex) {
			//should be here - null string
		}
		
		try {
			StringEncrypter stringEncrypter = new StringEncrypter();
			stringEncrypter.decrypt(null);
			fail("null key shouldnt be here");
		}
		catch(Exception ex) {
			//should be here - null string
		}		
	}	
}
