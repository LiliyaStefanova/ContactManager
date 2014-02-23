/**
 * Created with IntelliJ IDEA.
 * User: liliya
 * Date: 11/02/14
 * Time: 13:30
 * To change this template use File | Settings | File Templates.
 */
import org.junit.*;
import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public class MeetingTest {

    Meeting testMeeting;
    Set<Contact> testContact;

    @Before
    public void buildUp(){
        testContact=new HashSet<Contact>();
        Contact contact1=new ContactImpl(1234, "Jane", "venture capital");
        Contact contact2=new ContactImpl(3456, "Henry", "investor");
        testContact.add(contact1);
        testContact.add(contact2);
        Calendar meetingDate=Calendar.getInstance();
        meetingDate.set(2012,Calendar.DECEMBER,11);

        testMeeting=new MeetingImpl(4567,meetingDate, testContact);
    }

    /**
     * Tests meeting ID is returned correctly
     */
    @Test
    public void getMeetingIdTest(){

        assertTrue(testMeeting.getId()==4567);
    }

    /**
     * Tests meeting date is returned correctly
     */
    @Test
    public void getMeetingDateTest(){
        Calendar expectedDate=Calendar.getInstance();
        expectedDate.set(2012,Calendar.DECEMBER,11);
        assertTrue(expectedDate.equals(testMeeting.getDate()));

    }

    /**
     * Tests the list of contact that attended the meeting is returned correctly
     */
    @Test
    public void getMeetingContactsTest(){
        Set<Contact> expectedContacts=new HashSet<Contact>();
        Contact contact1=new ContactImpl(1234, "Jane", "venture capital");
        Contact contact2=new ContactImpl(3456, "Henry", "investor");
        expectedContacts.add(contact1);
        expectedContacts.add(contact2);

        assertEquals(expectedContacts.size(),testMeeting.getContacts().size());
        //need to fix this one does not work
        assertTrue(expectedContacts.equals(testMeeting.getContacts()));

    }


}
