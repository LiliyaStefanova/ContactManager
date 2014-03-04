
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PastMeetingTest {

    PastMeeting pastMeetingTest1;
    Set<Contact> testContact;

    @Before
    public void buildUp() {
        Calendar date = Calendar.getInstance();
        date.set(2013, Calendar.JANUARY, 23);
        testContact = new HashSet<Contact>();
        Contact contact1 = new ContactImpl(1234, "Jane", "venture capital");
        Contact contact2 = new ContactImpl(3456, "Henry", "investor");
        testContact.add(contact1);
        testContact.add(contact2);
        pastMeetingTest1 = new PastMeetingImpl(1234, date, testContact, "Very productive");
    }

    @Test
    public void getNotesFullTest() {

        assertTrue(pastMeetingTest1.getNotes().equals("Very productive"));
    }

}
