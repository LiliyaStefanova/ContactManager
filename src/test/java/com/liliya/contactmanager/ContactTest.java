package com.liliya.contactmanager; /**
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

    testContact=new ContactImpl(1234, "Steve");
    testContact.addNotes("potential sponsor");
    testContact2=new ContactImpl(2345, "John");
     testContact2.addNotes("");//no notes for this contact

    }

    @Test
    public void getIdTest(){

        assertTrue(testContact.getId()==1234);

    }

    @Test
    public void getNameTest(){

        assertTrue(testContact.getName().equals("Steve"));

    }

    @Test
    public void getNotesFullTest(){

        assertTrue(testContact.getNotes().equals("potential sponsor"));

    }

    @Test
    public void getNotesEmptyTest(){

        assertTrue(testContact2.getNotes().equals(""));

    }

    @Test
    public void addNotesTest(){

        testContact2.addNotes("abcd");

        assertTrue(testContact2.getNotes().equals("abcd"));

    }

}
