/**
 * Created with IntelliJ IDEA.
 * User: liliya
 * Date: 11/02/14
 * Time: 13:34
 * To change this template use File | Settings | File Templates.
 */

import org.junit.*;
import static org.junit.Assert.*;
import org.junit.Before;

import java.util.*;

public class ContactManagerTest {
    private ContactManager contactManager;
    private Set<Contact> contacts;
    private Calendar pastDate;
    private Calendar pastDate2;
    private Calendar futureDate;
    private Calendar futureDate2;
    private Calendar today;


    @Before
    public void buildUp(){
        contactManager =new ContactManagerImpl();
        pastDate=Calendar.getInstance();
        pastDate.set(2013, Calendar.DECEMBER, 7);
        futureDate=Calendar.getInstance();
        futureDate.set(2014, Calendar.MARCH, 30);
        futureDate2=Calendar.getInstance();
        futureDate2.set(2014, Calendar.APRIL, 30);
        pastDate2=Calendar.getInstance();
        pastDate2.set(2013, Calendar.DECEMBER, 15);
        today=Calendar.getInstance();
        today.set(2014, Calendar.FEBRUARY, 22);

    }

  //Begin meeting test cases

    @Test
    public void addFutureMeetingNormalTest(){

        int idSteve=contactManager.addNewContact("Steve", "accountant");
        int idJane=contactManager.addNewContact("Jane", "Public relations");
        contacts=contactManager.getContacts(idSteve, idJane);
        int meetingID= contactManager.addFutureMeeting(contacts, futureDate);

        FutureMeeting meeting= contactManager.getFutureMeeting(meetingID);

        assertNotNull(meeting);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addFutureMeetingInThePastTest(){

        contactManager.addFutureMeeting(contacts, pastDate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addFutureMeetingNonExistentContactTest(){
        Contact c=new ContactImpl(1234, "Jim", "non-existent");
        Set<Contact> nonExistentContact=new HashSet<Contact>();
        nonExistentContact.add(c);
        contactManager.addFutureMeeting(nonExistentContact, futureDate);

    }

    @Test
    //difficult to test without return
    public void getPastMeetingNormalTest(){
        String notes="notes";
        contactManager.addNewPastMeeting(contacts,pastDate,notes );
        contactManager.addNewPastMeeting(contacts,pastDate,notes );

    }

    @Test
    //difficult to test without return
    public void getPastMeetingInFutureTest(){
        String notes="notes";
        contactManager.addNewPastMeeting(contacts,futureDate,notes );
    }

    @Test
    public void getFutureMeetingNormalTest(){
        int idSteve=contactManager.addNewContact("Steve", "accountant");
        int idJane=contactManager.addNewContact("Jane", "Public relations");
        contacts=contactManager.getContacts(idSteve, idJane);
        int id=contactManager.addFutureMeeting(contacts, futureDate);

        assertEquals(contactManager.getFutureMeeting(id).getId(), id);

    }

    @Test
    public void getFutureMeetingInPastTest(){
        //difficult to test without new past meeting returning id

    }

    @Test
    public void getMeetingExistingTest(){
        int idSteve=contactManager.addNewContact("Steve", "accountant");
        int idJane=contactManager.addNewContact("Jane", "Public relations");
        contacts=contactManager.getContacts(idSteve, idJane);
        int idFuture=contactManager.addFutureMeeting(contacts, futureDate);

        assertNotNull(contactManager.getMeeting(idFuture));
        assertEquals(idFuture, contactManager.getMeeting(idFuture).getId());

    }

    @Test
    public void getMeetingNonExistentTest(){

    }

    @Test
    public void getFutureMeetingListPerContactNormalTest(){
        Contact c=null;
        int idSteve=contactManager.addNewContact("Steve", "accountant");
        int idJane=contactManager.addNewContact("Jane", "Public relations");
        contacts=contactManager.getContacts(idSteve, idJane);
        int idFuture1=contactManager.addFutureMeeting(contacts, futureDate);
        int idFuture2=contactManager.addFutureMeeting(contacts, futureDate2);

        Set<Contact> contactSteve=contactManager.getContacts(idSteve);
        for(Contact curr:contactSteve){
            c=curr;
        }
        List<Meeting> meetingsSteve=contactManager.getFutureMeetingList(c);
        assertEquals(2, meetingsSteve.size());
        Meeting prev = null;
        for(Meeting curr:meetingsSteve){
            if (prev != null) {
                    assertTrue(curr.getDate().compareTo(prev.getDate())>0)     ;
            }
            prev =curr;
        }

    }

    @Test(expected=IllegalArgumentException.class)
    public void getFutureMeetingListPerContactNonExistingContactTest(){
        Contact c=new ContactImpl(1234, "Fred","tax");
        List<Meeting> meetingsSteve=contactManager.getFutureMeetingList(c);

    }

    @Test
    public void getFutureMeetingListPerContactNoDuplicatesTest(){
        Contact c=null;
        int idSteve=contactManager.addNewContact("Steve", "accountant");
        int idJane=contactManager.addNewContact("Jane", "Public relations");
        contacts=contactManager.getContacts(idSteve, idJane);
        int idFuture1=contactManager.addFutureMeeting(contacts, futureDate);
        int idFuture2=contactManager.addFutureMeeting(contacts, futureDate);

        Set<Contact> contactSteve=contactManager.getContacts(idSteve);
        for(Contact curr:contactSteve){
            c=curr;
        }
        List<Meeting> meetingsSteve=contactManager.getFutureMeetingList(c);
        assertEquals(1, meetingsSteve.size());

    }

    @Test
    public void getFutureMeetingsPerDateTest(){
        int idSteve=contactManager.addNewContact("Steve", "accountant");
        int idJane=contactManager.addNewContact("Jane", "Public relations");
        int idTim=contactManager.addNewContact("Tim", "Public relations");
        contacts=contactManager.getContacts(idSteve, idJane);
        Set<Contact> contacts1=contactManager.getContacts(idSteve, idTim);
        int idFuture=contactManager.addFutureMeeting(contacts, futureDate);
        int idFuture3=contactManager.addFutureMeeting(contacts, futureDate);
        int idFuture2=contactManager.addFutureMeeting(contacts1, futureDate);

        assertEquals(3, contactManager.getFutureMeetingList(futureDate).size());
    }

    @Test
    public void getPastMeetingsPerContactNormalTest(){
        //need to implement the addNewPastMeeting first

        Contact c=null;
        int idSteve=contactManager.addNewContact("Steve", "accountant");
        int idJane=contactManager.addNewContact("Jane", "Public relations");
        contacts=contactManager.getContacts(idSteve, idJane);

        contactManager.addNewPastMeeting(contacts,  pastDate, "notes");
        contactManager.addNewPastMeeting(contacts,  pastDate2, "notes");

        Set<Contact> contactSteve=contactManager.getContacts(idSteve);
        for(Contact curr:contactSteve){
            c=curr;
        }
        assertEquals(2, contactManager.getPastMeetingList(c).size());

    }

    @Test
    public void getPastMeetingsPerContactDuplicatesTest(){

    }

    @Test
    public void getPastMeetingsPerContactSortedTest(){

    }

    @Test(expected=IllegalArgumentException.class)
    public void getPastMeetingsPerNonExistentContactTest(){

    }

    @Test
    public void addNewPastMeetingNormalTest(){
        //how can this be tested if it does not return ID
        Contact c=null;
        int idSteve=contactManager.addNewContact("Steve", "accountant");
        int idJane=contactManager.addNewContact("Jane", "Public relations");
        contacts=contactManager.getContacts(idSteve, idJane);
        String notes="Organisational set up";

        contactManager.addNewPastMeeting(contacts,pastDate,notes);
        Set<Contact> contactSteve=contactManager.getContacts(idSteve);
        for(Contact curr:contactSteve){
            c=curr;
        }
        List<PastMeeting> testPastMeetingsList=contactManager.getPastMeetingList(c);
        for(PastMeeting curr:testPastMeetingsList){
            assertEquals("Organisational set up",curr.getNotes());
            assertEquals(pastDate, curr.getDate());
        }

    }

    @Test
    public void addMeetingNotesNormalTest(){
        int idSteve=contactManager.addNewContact("Steve", "accountant");
        int idJane=contactManager.addNewContact("Jane", "Public relations");
        contacts=contactManager.getContacts(idSteve, idJane);
        Contact c=null;
        Set<Contact> contactSteve=contactManager.getContacts(idSteve);
        for(Contact curr:contactSteve){
            c=curr;
        }
        int id=contactManager.addFutureMeeting(contacts,today);
        String text="test";
        contactManager.addMeetingNotes(id, text);

        List<PastMeeting> newPastMeeting=contactManager.getPastMeetingList(c);
        for(PastMeeting curr:newPastMeeting){
            assertEquals(text, curr.getNotes());
        }

    }

    @Test(expected=IllegalArgumentException.class)
    public void addMeetingNotesNonExistentMeetingTest(){

    }

    @Test(expected=IllegalArgumentException.class)
    public void addMeetingNotesFutureMeetingTest(){

    }

    @Test(expected =NullPointerException.class)
    public void addMeetingNotesNullTest(){

    }

    //may need another test case here for overriding notes functionality


  //End Meeting test cases

  //Begin Contact test cases

    @Test
    public void addNewContactNormalTest(){

       //difficult to test without any returns!
       int id =contactManager.addNewContact("Liliya", "test");

        assertNotNull(contactManager.getContacts(id));

    }

    @Test(expected = NullPointerException.class)
    public void addNewContactNameNullTest(){

        contactManager.addNewContact(null, "test");

    }

    @Test(expected = NullPointerException.class)
    public void addNewContactNotesNullTest(){

        contactManager.addNewContact("Liliya",null);

    }

    @Test
    public void getContactsNormalTest(){
        int idTom=contactManager.addNewContact("Tom", "sponsor");
        int idJane=contactManager.addNewContact("Jane", "sponsor");
        int idTIm=contactManager.addNewContact("Tim", "sponsor");
        int id=1234;

        Set<Contact> testContacts=contactManager.getContacts(idTom, idJane, idTIm);
        assertEquals(3, testContacts.size());

        Contact tom=null;
        Contact jane=null;
        Contact tim=null;

        for(Contact curr:testContacts){
            if(curr.getId()==idTom){
                tom=curr;
            }
            else if(curr.getId()==idJane){
                jane=curr;
            }
            else if(curr.getId()==idTIm){
                tim=curr;
            }
            else{
                throw new RuntimeException("unexpected contact ID");
            }
        }

        assertNotNull(tom);
        assertNotNull(jane);
        assertNotNull(tim);

        assertEquals("Tom", tom.getName());
        assertEquals("Jane", jane.getName());
        assertEquals("Tim", tim.getName());

    }

    @Test(expected = IllegalArgumentException.class)
    public void getContactsNonExistentContact(){
        int idTom=contactManager.addNewContact("Tom", "sponsor");
        int idJane=contactManager.addNewContact("Jane", "sponsor");
        int idTIm=contactManager.addNewContact("Tim", "sponsor");
        int idUnexpected=1234;

        Set<Contact> testContacts=contactManager.getContacts(idTom, idJane, idTIm, idUnexpected);
        assertEquals(3, testContacts.size());

        Contact tom=null;
        Contact jane=null;
        Contact tim=null;

        for(Contact curr:testContacts){
            if(curr.getId()==idTom){
                tom=curr;
            }
            else if(curr.getId()==idJane){
                jane=curr;
            }
            else if(curr.getId()==idTIm){
                tim=curr;
            }
            else{
                throw new RuntimeException("unexpected contact ID");
            }
        }

        assertNotNull(tom);
        assertNotNull(jane);
        assertNotNull(tim);

        assertEquals("Tom", tom.getName());
        assertEquals("Jane", jane.getName());
        assertEquals("Tim", tim.getName());

    }


    @Test
    public void getContactsNormal(){
        int idTom=contactManager.addNewContact("Tom Jones", "sponsor");
        int idJane=contactManager.addNewContact("Jane Smith", "sponsor");
        int idTim=contactManager.addNewContact("Tim Jones", "sponsor");
        contacts=contactManager.getContacts(idTom, idJane, idTim);

        assertEquals(2,contactManager.getContacts("Jones").size());

    }

    @Test(expected = NullPointerException.class)
    public void getContactsNullParam(){

        int idTom=contactManager.addNewContact("Tom Jones", "sponsor");
        int idJane=contactManager.addNewContact("Jane Smith", "sponsor");
        int idTim=contactManager.addNewContact("Tim Jones", "sponsor");
        contacts=contactManager.getContacts(idTom, idJane, idTim);

        String name=null;
       contactManager.getContacts(name);

    }

  //End Contact related test cases

  //Begin I/O related test cases












}
