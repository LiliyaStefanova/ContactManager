
import org.junit.*;

import static org.junit.Assert.*;

import org.junit.Before;

import java.util.*;

//need to write IO test cases
//generate java docs

public class ContactManagerTest {

    private ContactManager contactManager;
    private Calendar december72013;
    private Calendar december152013;
    private Calendar march302014;
    private Calendar april302014;
    Set<Contact> contacts1 = null;
    Set<Contact> contacts2 = null;
    Set<Contact> contacts3 = null;

    @Before
    public void buildUp() {
        contactManager = new ContactManagerImpl(false);
        december72013 = Calendar.getInstance();
        december72013.set(2013, Calendar.DECEMBER, 7, 18, 00);
        december152013 = Calendar.getInstance();
        december152013.set(2013, Calendar.DECEMBER, 15, 11, 30);
        march302014 = Calendar.getInstance();
        march302014.set(2014, Calendar.MARCH, 30, 10, 30);
        april302014 = Calendar.getInstance();
        april302014.set(2014, Calendar.APRIL, 30, 14, 00);
    }

    @After
    public void cleanUp() {
        contactManager = null;
        contacts1 = null;
        contacts2 = null;
        contacts3 = null;
    }

    //Begin meeting test cases

    @Test
    public void addFutureMeetingNormalTest() {
        contacts2 = generateListOfContacts2();
        int meetingID = contactManager.addFutureMeeting(contacts2, march302014);
        FutureMeeting meeting = contactManager.getFutureMeeting(meetingID);

        assertNotNull(meeting);

        assertEquals(march302014, contactManager.getMeeting(meetingID).getDate());
    }

    @Test(expected = IllegalArgumentException.class)
    public void addFutureMeetingInThePastTest() {
        contacts2 = generateListOfContacts2();
        contactManager.addFutureMeeting(contacts2, december72013);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addFutureMeetingNonExistentContactTest() {
        Contact c = new ContactImpl(1234, "Jim", "non-existent");
        Set<Contact> nonExistentContact = new HashSet<Contact>();
        nonExistentContact.add(c);
        contactManager.addFutureMeeting(nonExistentContact, march302014);
    }

    @Test
    public void addDuplicateFutureMeetingTest() {
        contacts2 = generateListOfContacts2();
        contactManager.addFutureMeeting(contacts2, march302014);
        int id2 = contactManager.addFutureMeeting(contacts2, march302014);

        assertNull(contactManager.getMeeting(id2));
    }

    @Test
    public void getPastMeetingNormalTest() {
        contacts1 = generateListOfContacts1();
        Contact contactObject = null;
        PastMeeting meetTest = null;
        String notes = "notes";
        contactManager.addNewPastMeeting(contacts1, december72013, notes);
        for (Contact c : contacts1) {
            contactObject = c;
        }

        List<PastMeeting> pastMeetings = contactManager.getPastMeetingList(contactObject);
        for (PastMeeting meet : pastMeetings) {
            meetTest = meet;
        }
        assertEquals(meetTest, contactManager.getPastMeeting(meetTest.getId()));

    }

    @Test(expected = IllegalArgumentException.class)
    public void getPastMeetingInFutureTest() {
        contacts1 = generateListOfContacts1();
        int idFutureMeeting = contactManager.addFutureMeeting(contacts1, march302014);
        contactManager.getPastMeeting(idFutureMeeting);
    }

    @Test
    public void getPastMeetingReturnsNullTest() {
        //no past meeting is added to the list
        assertEquals(null, contactManager.getPastMeeting(123456));
    }

    @Test
    public void getFutureMeetingNormalTest() {
        contacts2 = generateListOfContacts2();
        int id = contactManager.addFutureMeeting(contacts2, march302014);

        assertEquals(contactManager.getFutureMeeting(id).getId(), id);

    }

    @Test(expected = IllegalArgumentException.class)
    public void getFutureMeetingInPastTest() {
        contacts1 = generateListOfContacts1();
        Contact contactObject = null;
        PastMeeting meetTest = null;
        String notes = "notes";
        contactManager.addNewPastMeeting(contacts1, december72013, notes);

        for (Contact c : contacts1) {
            contactObject = c;
        }
        List<PastMeeting> pastMeetings = contactManager.getPastMeetingList(contactObject);
        for (PastMeeting meet : pastMeetings) {
            meetTest = meet;
        }
        contactManager.getFutureMeeting(meetTest.getId());
    }

    @Test
    public void getFutureMeetingReturnsNullTest() {

        assertEquals(null, contactManager.getFutureMeeting(12345));
    }

    @Test
    public void getMeetingExistingTest() {
        contacts2 = generateListOfContacts2();
        int idFuture = contactManager.addFutureMeeting(contacts2, march302014);

        assertNotNull(contactManager.getMeeting(idFuture));
        assertEquals(idFuture, contactManager.getMeeting(idFuture).getId());

    }

    @Test
    public void getMeetingNonExistentTest() {
        assertNull(contactManager.getMeeting(12345));
    }

    @Test
    public void getFutureMeetingListPerContactNormalTest() {
        contacts2 = generateListOfContacts2();
        Contact c = null;
        contactManager.addFutureMeeting(contacts2, march302014);
        contactManager.addFutureMeeting(contacts2, april302014);

        for (Contact curr : contacts2) {
            if (curr.getName().equals("Jamie Jones")) {
                c = curr;
            }
        }
        List<Meeting> meetingsJamie = contactManager.getFutureMeetingList(c);
        // assertEquals(2, meetingsSteve.size());
        //test if meetings are chronologically ordered in list
        Meeting prev = null;
        for (Meeting curr : meetingsJamie) {
            if (prev != null) {
                assertTrue(curr.getDate().compareTo(prev.getDate()) > 0);
            }
            prev = curr;
        }

    }

    @Test(expected = IllegalArgumentException.class)
    public void getFutureMeetingListPerContactNonExistingContactTest() {
        Contact c = new ContactImpl(1234, "Fred", "tax");
        contactManager.getFutureMeetingList(c);
    }

    @Test
    public void getFutureMeetingListPerContactNoDuplicatesTest() {
        contacts2 = generateListOfContacts2();
        contactManager.addNewContact("Jamie Jones", "accountant");
        Set<Contact> duplicateContact = contactManager.getContacts("Jamie Jones");
        Contact c = null;
        contactManager.addFutureMeeting(contacts2, march302014);
        contactManager.addFutureMeeting(duplicateContact, march302014);

        for (Contact curr : contacts2) {
            if (curr.getName().equals("Jamie Jones")) {
                c = curr;
            }
        }
        List<Meeting> meetingsJamie = contactManager.getFutureMeetingList(c);
        assertEquals(1, meetingsJamie.size());

    }

    @Test
    public void getFutureMeetingListDoesNotContainPastMeetingsTest() {
        contacts2 = generateListOfContacts2();
        Contact c = null;
        contactManager.addFutureMeeting(contacts2, march302014);
        contactManager.addFutureMeeting(contacts2, april302014);
        contactManager.addNewPastMeeting(contacts2, december72013, "notes");

        for (Contact curr : contacts2) {
            if (curr.getName().equals("Jamie Jones")) {
                c = curr;
            }
        }
        List<Meeting> meetingsJamie = contactManager.getFutureMeetingList(c);

        assertEquals(2, meetingsJamie.size());

        for (Meeting curr : meetingsJamie) {
            assertFalse(curr instanceof PastMeeting);
        }
    }

    @Test
    public void getFutureMeetingsPerDateTest() {

        contacts2 = generateListOfContacts2();
        contacts3 = generateListOfContacts3();
        Calendar march30_12oclock = Calendar.getInstance();
        march30_12oclock.set(2014, Calendar.MARCH, 30, 12, 0, 0);

        Calendar march30_2oclock = Calendar.getInstance();
        march30_2oclock.set(2014, Calendar.MARCH, 30, 14, 0, 0);

        Calendar march30_5oclock = Calendar.getInstance();
        march30_5oclock.set(2014, Calendar.MARCH, 30, 17, 0, 0);

        Calendar april1st = Calendar.getInstance();
        april1st.set(2014, Calendar.APRIL, 1, 17, 0, 0);

        Calendar march30 = Calendar.getInstance();
        march30.set(2014, Calendar.MARCH, 30);

        contactManager.addFutureMeeting(contacts2, march30_5oclock);
        contactManager.addFutureMeeting(contacts2, march30_12oclock);
        contactManager.addFutureMeeting(contacts3, march30_2oclock);
        contactManager.addFutureMeeting(contacts3, april1st);

        List<Meeting> meetings30March = contactManager.getFutureMeetingList(march30);
        assertEquals(3, meetings30March.size());
        //check that the meetings are in chronological order
        Meeting prev = null;
        for (Meeting curr : meetings30March) {
            if (prev != null) {
                assertTrue(curr.getDate().compareTo(prev.getDate()) > 0);
            }
            prev = curr;
        }
    }

    @Test
    public void getPastMeetingsPerContactNormalTest() {
        contacts2 = generateListOfContacts2();
        Contact c = null;

        contactManager.addNewPastMeeting(contacts2, december72013, "notes");
        contactManager.addNewPastMeeting(contacts2, december152013, "notes");

        for (Contact curr : contacts2) {
            if (curr.getName().equals("Jamie Jones")) {
                c = curr;
            }
        }
        List<PastMeeting> pastMeetingJamie = contactManager.getPastMeetingList(c);

        assertEquals(2, pastMeetingJamie.size());


        for (PastMeeting curr : pastMeetingJamie) {

            assertEquals(contacts2, curr.getContacts());
        }
    }

    @Test
    public void getPastMeetingsPerContactDuplicatesTest() {
        contacts2 = generateListOfContacts2();
        Contact c = null;
        contactManager.addNewContact("Jamie Jones", "accountant");
        Set<Contact> duplicateContact = contactManager.getContacts("Jamie Jones");
        contactManager.addNewPastMeeting(contacts2, december72013, "notes");
        contactManager.addNewPastMeeting(duplicateContact, december72013, "notes");

        for (Contact curr : contacts2) {
            if (curr.getName().equals("Jamie Jones")) {
                c = curr;
            }
        }
        List<PastMeeting> pastMeetingJamie = contactManager.getPastMeetingList(c);
        assertEquals(1, pastMeetingJamie.size());


        for (PastMeeting curr : pastMeetingJamie) {
            assertEquals("notes", curr.getNotes());
        }

    }

    @Test
    public void getPastMeetingsPerContactSortedTest() {

        contacts2 = generateListOfContacts2();
        Contact c = null;
        contactManager.addNewContact("Jamie Jones", "accountant");
        Set<Contact> duplicateContact = contactManager.getContacts("Jamie Jones");
        contactManager.addNewPastMeeting(contacts2, december72013, "notes");
        contactManager.addNewPastMeeting(duplicateContact, december152013, "notes");

        for (Contact curr : contacts2) {
            if (curr.getName().equals("Jamie Jones")) {
                c = curr;
            }
        }
        List<PastMeeting> pastMeetingJamie = contactManager.getPastMeetingList(c);

        Meeting prev = null;
        for (Meeting curr : pastMeetingJamie) {
            if (prev != null) {
                assertTrue(curr.getDate().compareTo(prev.getDate()) > 0);
            }
            prev = curr;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void getPastMeetingsPerNonExistentContactTest() {

        Contact c = new ContactImpl(1234, "James", "notes");

        contactManager.getPastMeetingList(c);
    }

    @Test
    public void addNewPastMeetingNormalTest() {
        contacts2 = generateListOfContacts2();
        Contact c = null;
        String notes = "Organisational set up";

        contactManager.addNewPastMeeting(contacts2, december72013, notes);
        for (Contact curr : contacts2) {
            if (curr.getName().equals("Jamie Jones")) {
                c = curr;
            }
        }
        List<PastMeeting> testPastMeetingsList = contactManager.getPastMeetingList(c);
        for (PastMeeting curr : testPastMeetingsList) {

            assertEquals("Organisational set up", curr.getNotes());
            assertEquals(december72013, curr.getDate());
        }

    }

    @Test
    public void addNewPastMeetingDuplicate() {
        contacts2 = generateListOfContacts2();
        contactManager.addNewPastMeeting(contacts2, december152013, "test");
        contactManager.addNewPastMeeting(contacts2, december152013, "other test");

    }

    @Test(expected = IllegalArgumentException.class)
    public void addNewPastMeetingEmptyNonExistentContactTest() {

        Contact c = new ContactImpl(34567, "Constantine", "development");

        Set<Contact> emptyContactList = new HashSet<Contact>();

        Set<Contact> nonExistentContactList = new HashSet<Contact>();
        nonExistentContactList.add(c);

        contactManager.addNewPastMeeting(emptyContactList, march302014, "notes");
        contactManager.addNewPastMeeting(nonExistentContactList, march302014, "notes");
    }

    @Test(expected = NullPointerException.class)
    public void addNewPastMeetingWithNullArgumentTest() {
        contacts2 = generateListOfContacts2();
        String notes = "Organisational set up";

        contactManager.addNewPastMeeting(contacts2, null, notes);

    }

    @Test
    public void addMeetingNotesNormalTest() {
        contacts2 = generateListOfContacts2();
        Calendar currentDateTime = Calendar.getInstance();
        currentDateTime.setTimeInMillis(currentDateTime.getTimeInMillis() + 1000);
        //create a meeting for a future meeting today +1000 ms
        int id = contactManager.addFutureMeeting(contacts2, currentDateTime);

        //sleep for 1000 ms-the meeting is now in the past but still future meeting
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Contact c = null;

        for (Contact curr : contacts2) {
            if (curr.getName().equals("Jamie Jones")) {
                c = curr;
            }
        }
        String text = "test";
        contactManager.addMeetingNotes(id, text);
        List<PastMeeting> newPastMeeting = contactManager.getPastMeetingList(c);
        //check that looking for a past meeting retrieves the converted one correctly
        for (PastMeeting curr : newPastMeeting) {

            assertEquals(text, curr.getNotes());
            assertEquals(curr, contactManager.getMeeting(id));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void addMeetingNotesNonExistentMeetingTest() {
        contactManager.addMeetingNotes(1234, "test");
    }

    @Test(expected = IllegalStateException.class)
    public void addMeetingNotesFutureMeetingTest() {
        contacts2 = generateListOfContacts2();
        int id = contactManager.addFutureMeeting(contacts2, march302014);

        contactManager.addMeetingNotes(id, "text");

    }

    @Test(expected = NullPointerException.class)
    public void addMeetingNotesNullTest() {
        contacts2 = generateListOfContacts2();
        int id = contactManager.addFutureMeeting(contacts2, march302014);

        String notes = null;
        contactManager.addMeetingNotes(id, notes);

    }

    @Test
    public void addMeetingNotesToExistingPastMeetingTest() {
        contacts1 = generateListOfContacts1();
        Contact contactShirley = null;

        for (Contact c : contacts1) {
            contactShirley = c;
        }

        contactManager.addNewPastMeeting(contacts1, december152013, "test");

        for (PastMeeting curr : contactManager.getPastMeetingList(contactShirley)) {
            contactManager.addMeetingNotes(curr.getId(), "notes");
        }
        for (PastMeeting curr : contactManager.getPastMeetingList(contactShirley)) {

            assertEquals("test notes", curr.getNotes());
        }

    }
    //End Meeting test cases

    //Begin Contact test cases

    @Test
    public void addNewContactNormalTest() {
        int idNewContact = 0;
        Contact newContact = null;
        contactManager.addNewContact("Liliya Stefanova", "test");

        Set<Contact> newContacts = contactManager.getContacts("Liliya Stefanova");
        for (Contact curr : newContacts) {
            idNewContact = curr.getId();
            newContact = curr;
        }
        assertNotNull(contactManager.getContacts(idNewContact));
        assertEquals("test", newContact.getNotes());

        assertEquals(1, newContacts.size());

    }

    @Test(expected = NullPointerException.class)
    public void addNewContactNameNullTest() {

        contactManager.addNewContact(null, "test");
    }

    @Test(expected = NullPointerException.class)
    public void addNewContactNotesNullTest() {

        contactManager.addNewContact("Liliya", null);
    }

    @Test
    public void getContactsNormalTest() {
        contacts3 = generateListOfContacts3();

        Contact steve = null;
        Contact jane = null;
        Contact tim = null;
        int idSteve = 0;
        int idJane = 0;
        int idTim = 0;

        for (Contact curr : contacts3) {
            if (curr.getName().equals("Steve Austin")) {
                steve = curr;
                idSteve = steve.getId();
            } else if (curr.getName().equals("Jane Smith")) {
                jane = curr;
                idJane = jane.getId();
            } else if (curr.getName().equals("Tim Davies")) {
                tim = curr;
                idTim = tim.getId();
            } else {
                throw new RuntimeException("unexpected name");
            }
        }

        assertNotNull(steve);
        assertNotNull(jane);
        assertNotNull(tim);

        assertEquals(contacts3, contactManager.getContacts(idSteve, idJane, idTim));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getContactsNonExistentContact() {
        contacts1 = generateListOfContacts1();
        int idExpected = 0;
        for (Contact curr : contacts1) {
            idExpected = curr.getId();
        }
        int idUnexpected = 1234;

        Set<Contact> testContacts = contactManager.getContacts(idExpected, idUnexpected);
    }

    @Test
    public void getContactsNormal() {

        contacts3 = generateListOfContacts3();

        Contact steve = null;
        Contact tim = null;

        Set<Contact> contactContainingName = contactManager.getContacts("v");

        assertEquals(2, contactManager.getContacts("v").size());

        for (Contact curr : contactContainingName) {
            if (curr.getName().equals("Steve Austin")) {
                steve = curr;
            } else if (curr.getName().equals("Tim Davies")) {
                tim = curr;
            } else {
                throw new RuntimeException("unexpected contact");
            }
        }

        assertEquals("Steve Austin", steve.getName());
        assertEquals("Tim Davies", tim.getName());

    }

    @Test(expected = NullPointerException.class)
    public void getContactsNullParam() {

        String name = null;
        contactManager.getContacts(name);

    }

    //End Contact related test cases

    //Begin I/O related test cases


    @Test
    public void saveDataToFileTest() {

        //set up a contact register
        contactManager.addNewContact("John", "investor");
        contactManager.addNewContact("Tom", "investor");
        contactManager.addNewContact("Jim", "designer");
        contactManager.addNewContact("Lin", "lawyer");
        contactManager.addNewContact("Tom", "lawyer");

        //retrieve a subset of contacts
        Set<Contact> contacts1 = contactManager.getContacts("o");
        //add a meeting to current instance
        int idRecord = contactManager.addFutureMeeting(contacts1, april302014);
        //flush first instance
        contactManager.flush();


        //create a second instance of contact manager, i.e. re-open program
        ContactManager contactManagerSecondInstance = new ContactManagerImpl();

        Set<Contact> contactsContainI = contactManagerSecondInstance.getContacts("i");
        //add more meetings to the new instance
        contactManagerSecondInstance.addNewPastMeeting(contactsContainI, december152013, "notes");
        //flush second instance
        contactManagerSecondInstance.flush();
        //create a third instance
        ContactManager contactManagerThirdInstance = new ContactManagerImpl();

        //check if meetings and contacts added in the previous instances are accessible in this instance
        int idRetrieve = contactManagerThirdInstance.getFutureMeeting(idRecord).getId();

        contactManagerThirdInstance.addFutureMeeting(contactsContainI, april302014);
        contactManagerThirdInstance.addNewContact("Ben", "marketing exec");

        contactManagerThirdInstance.flush();
        assertEquals(idRecord, idRetrieve);

        for (Contact c : contactManagerThirdInstance.getContacts("Tom")) {
            System.out.println(c.getName());
        }

    }

    @Test
    public void saveDataToFileContactsOnly() {

        //set up a contact register
        contactManager.addNewContact("John", "investor");
        contactManager.addNewContact("Tom", "investor");
        contactManager.addNewContact("Jim", "designer");
        contactManager.addNewContact("Lin", "lawyer");
        contactManager.addNewContact("Tom", "lawyer");

        contactManager.flush();


        //create a second instance of contact manager, i.e. re-open program
        ContactManager contactManagerSecondInstance = new ContactManagerImpl();

        //flush second instance
        contactManagerSecondInstance.flush();
        //create a third instance
        ContactManager contactManagerThirdInstance = new ContactManagerImpl();

        //check if meetings and contacts added in the previous instances are accessible in this instance

        contactManagerThirdInstance.addNewContact("Ben", "marketing exec");

        contactManagerThirdInstance.flush();

        for (Contact c : contactManagerThirdInstance.getContacts("Tom")) {
            System.out.println(c.getName());
        }

    }

    @Test
    public void withoutFlush() {

        contactManager.addNewContact("John", "investor");
        contactManager.addNewContact("Tom", "investor");
        contactManager.addNewContact("Jim", "designer");
        contactManager.addNewContact("Lin", "lawyer");

        Set<Contact> contactsContainingO = contactManager.getContacts("o");

        int id = contactManager.addFutureMeeting(contactsContainingO, april302014);

        ContactManager contactManagerSecondInstance = new ContactManagerImpl();

        assertNull(contactManagerSecondInstance.getFutureMeeting(id));
    }


    //helper method to generate a list with one contact in it
    public Set<Contact> generateListOfContacts1() {

        contactManager.addNewContact("Shirley McLane", "lawyer");
        return contactManager.getContacts("Shirley McLane");
    }

    //helper method to generate a list with two contacts
    public Set<Contact> generateListOfContacts2() {
        contactManager.addNewContact("Jamie Jones", "accountant");
        contactManager.addNewContact("Patricia Roberts", "Public relations");

        Set<Contact> contactsSteve = contactManager.getContacts("Jamie Jones");
        Set<Contact> contactsJane = contactManager.getContacts("Patricia Roberts");

        Set<Contact> contactSteveJane = new HashSet<Contact>();

        for (Contact curr : contactsSteve) {
            contactSteveJane.add(curr);
        }
        for (Contact curr : contactsJane) {
            contactSteveJane.add(curr);
        }

        return contactSteveJane;

    }
    //helper method to generate a list with three

    public Set<Contact> generateListOfContacts3() {
        contactManager.addNewContact("Steve Austin", "accountant");
        contactManager.addNewContact("Jane Smith", "Public relations");
        contactManager.addNewContact("Tim Davies", "venture capital");

        Set<Contact> contactsSteve = contactManager.getContacts("Steve Austin");
        Set<Contact> contactsJane = contactManager.getContacts("Jane Smith");
        Set<Contact> contactsTim = contactManager.getContacts("Tim Davies");


        Set<Contact> contactSteveJaneTim = new HashSet<Contact>();

        assertEquals(1, contactsSteve.size());

        for (Contact curr : contactsSteve) {
            contactSteveJaneTim.add(curr);
        }
        for (Contact curr : contactsJane) {
            contactSteveJaneTim.add(curr);
        }
        for (Contact curr : contactsTim) {
            contactSteveJaneTim.add(curr);
        }

        return contactSteveJaneTim;

    }

}