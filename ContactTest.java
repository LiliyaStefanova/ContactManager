/**
 * Created with IntelliJ IDEA.
 * User: liliya
 * Date: 11/02/14
 * Time: 13:24
 * To change this template use File | Settings | File Templates.
 */

import org.junit.*;
import static org.junit.Assert.*;

public class ContactTest {

    Contact testContact;
    Contact testContact2;

    /**
     * Creating new objects to use for the test
     */
    @Before
    public void buildUp(){

    testContact=new ContactImpl(1234, "Steve", "potential sponsor");
    testContact2=new ContactImpl(2345, "John", ""); //no notes for this contact

    }

    /**
     * Tests if contact ID can be retrieved correctly
     */
    @Test
    public void getIdTest(){

        assertTrue(testContact.getId()==1234);

    }

    /**
     * Tests if contact name can be retrieved correctly
     */
    @Test
    public void getNameTest(){

        assertTrue(testContact.getName().equals("Steve"));

    }

    /**
     * Tests if contact notes are returned correctly
     */
    @Test
    public void getNotesFullTest(){

        assertTrue(testContact.getNotes().equals("potential sponsor"));

    }

    /**
     * Tests that empty notes field is returned correctly
     */

    @Test
    public void getNotesEmptyTest(){

        assertTrue(testContact2.getNotes().equals(""));

    }
    /**
     * Tests if notes are added correctly to an existing record with no notes
     */
    @Test
    public void addNotesTest(){

        testContact2.addNotes("abcd");

        assertTrue(testContact2.getNotes().equals("abcd"));

    }

}
