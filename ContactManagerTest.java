
import org.junit.*;
import static org.junit.Assert.*;
import org.junit.Before;

import java.util.*;

public class ContactManagerTest {

    private ContactManager contactManager;
    private Set<Contact> contacts;
    private Calendar december72013;
    private Calendar december152013;
    private Calendar march302014;
    private Calendar april302014;
    private Calendar futureDate3;
    private Calendar today;


    @Before
    public void buildUp(){
        contactManager =new ContactManagerImpl();
        december72013 =Calendar.getInstance();
        december72013.set(2013, Calendar.DECEMBER, 7);
        march302014 =Calendar.getInstance();
        march302014.set(2014, Calendar.MARCH, 30, 10, 30);
        april302014 =Calendar.getInstance();
        april302014.set(2014, Calendar.APRIL, 30);
        december152013 =Calendar.getInstance();
        december152013.set(2013, Calendar.DECEMBER, 15);
        today=Calendar.getInstance();

    }

  //Begin meeting test cases

    @Test
    public void addFutureMeetingNormalTest(){

        int idSteve=contactManager.addNewContact("Steve", "accountant");
        int idJane=contactManager.addNewContact("Jane", "Public relations");
        contacts=contactManager.getContacts(idSteve, idJane);
        int meetingID= contactManager.addFutureMeeting(contacts, march302014);

        FutureMeeting meeting= contactManager.getFutureMeeting(meetingID);

        assertNotNull(meeting);

        assertEquals(march302014, contactManager.getMeeting(meetingID).getDate());
    }

    @Test(expected = IllegalArgumentException.class)
    public void addFutureMeetingInThePastTest(){

        contactManager.addFutureMeeting(contacts, december72013);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addFutureMeetingNonExistentContactTest(){
        Contact c=new ContactImpl(1234, "Jim", "non-existent");
        Set<Contact> nonExistentContact=new HashSet<Contact>();
        nonExistentContact.add(c);
        contactManager.addFutureMeeting(nonExistentContact, march302014);

    }

    @Test(expected = IllegalArgumentException.class)
    public void addDuplicateFutureMeetingTest(){

        int idSteve=contactManager.addNewContact("Steve", "accountant");
        int idJane=contactManager.addNewContact("Jane", "Public relations");
        contacts=contactManager.getContacts(idSteve, idJane);
        contactManager.addFutureMeeting(contacts, march302014);
        contactManager.addFutureMeeting(contacts, march302014);
   }

    @Test
    public void getPastMeetingNormalTest(){
        Contact contactObject=null;
        PastMeeting meetTest=null;
        int idSteve=contactManager.addNewContact("Steve", "accountant");
        contacts=contactManager.getContacts(idSteve);
        String notes="notes";
        contactManager.addNewPastMeeting(contacts, december72013,notes);

        for(Contact c:contacts){
            contactObject=c;
        }

        List<PastMeeting> pastMeetings=contactManager.getPastMeetingList(contactObject);
        for(PastMeeting meet:pastMeetings){
            meetTest=meet;
        }
        assertEquals(meetTest, contactManager.getPastMeeting(meetTest.getId()));

    }

    @Test(expected = IllegalArgumentException.class)
    public void getPastMeetingInFutureTest(){
        int idSteve=contactManager.addNewContact("Steve", "accountant");
        contacts=contactManager.getContacts(idSteve);
        String notes="notes";
        contactManager.addNewPastMeeting(contacts, march302014,notes );
    }

    @Test
    public void getPastMeetingReturnsNullTest(){
        int idSteve=contactManager.addNewContact("Steve", "accountant");
        contacts=contactManager.getContacts(idSteve);
        String notes="notes";
        //no past meeting is added to the list
        assertEquals(null, contactManager.getPastMeeting(123456));
    }

    @Test
    public void getFutureMeetingNormalTest(){
        int idSteve=contactManager.addNewContact("Steve", "accountant");
        int idJane=contactManager.addNewContact("Jane", "Public relations");
        contacts=contactManager.getContacts(idSteve, idJane);
        int id=contactManager.addFutureMeeting(contacts, march302014);

        assertEquals(contactManager.getFutureMeeting(id).getId(), id);

    }

    @Test(expected=IllegalArgumentException.class)
    public void getFutureMeetingInPastTest(){
        Contact contactObject=null;
        PastMeeting meetTest=null;
        int idSteve=contactManager.addNewContact("Steve", "accountant");
        contacts=contactManager.getContacts(idSteve);
        String notes="notes";
        contactManager.addNewPastMeeting(contacts, december72013,notes);

        for(Contact c:contacts){
            contactObject=c;
        }
        List<PastMeeting> pastMeetings=contactManager.getPastMeetingList(contactObject);
        for(PastMeeting meet:pastMeetings){
            meetTest=meet;
        }
        contactManager.getFutureMeeting(meetTest.getId());
    }
    @Test
    public void getFutureMeetingReturnsNullTest(){

       assertEquals(null, contactManager.getFutureMeeting(12345)) ;
    }

    @Test
    public void getMeetingExistingTest(){
        int idSteve=contactManager.addNewContact("Steve", "accountant");
        int idJane=contactManager.addNewContact("Jane", "Public relations");
        contacts=contactManager.getContacts(idSteve, idJane);
        int idFuture=contactManager.addFutureMeeting(contacts, march302014);

        assertNotNull(contactManager.getMeeting(idFuture));
        assertEquals(idFuture, contactManager.getMeeting(idFuture).getId());

    }
    @Test
    public void getMeetingNonExistentTest(){
          assertNull(contactManager.getMeeting(12345));
    }

    @Test
    public void getFutureMeetingListPerContactNormalTest(){
        Contact c=null;
        int idSteve=contactManager.addNewContact("Steve", "accountant");
        int idJane=contactManager.addNewContact("Jane", "Public relations");
        contacts=contactManager.getContacts(idSteve, idJane);
        int idFuture1=contactManager.addFutureMeeting(contacts, march302014);
        int idFuture2=contactManager.addFutureMeeting(contacts, april302014);

        Set<Contact> contactSteve=contactManager.getContacts(idSteve);
        for(Contact curr:contactSteve){
            c=curr;
        }
        List<Meeting> meetingsSteve=contactManager.getFutureMeetingList(c);
        assertEquals(2, meetingsSteve.size());
        //test if meetings are chronologically ordered in list
        Meeting prev = null;
        for(Meeting curr:meetingsSteve){
            if (prev != null) {
                    assertTrue(curr.getDate().compareTo(prev.getDate())>0);
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
        int idFuture1=contactManager.addFutureMeeting(contacts, march302014);
        int idFuture2=contactManager.addFutureMeeting(contacts, march302014);

        Set<Contact> contactSteve=contactManager.getContacts(idSteve);
        for(Contact curr:contactSteve){
            c=curr;
        }
        List<Meeting> meetingsSteve=contactManager.getFutureMeetingList(c);
        assertEquals(1, meetingsSteve.size());

    }

    @Test
    public void getFutureMeetingListDoesNotContainPastMeetingsTest(){
        Contact c=null;
        int idSteve=contactManager.addNewContact("Steve", "accountant");
        int idJane=contactManager.addNewContact("Jane", "Public relations");
        contacts=contactManager.getContacts(idSteve, idJane);
        int idFuture1=contactManager.addFutureMeeting(contacts, march302014);
        int idFuture2=contactManager.addFutureMeeting(contacts, april302014);
        contactManager.addNewPastMeeting(contacts, december72013, "notes");

        Set<Contact> contactSteve=contactManager.getContacts(idSteve);
        for(Contact curr:contactSteve){
            c=curr;
        }
        List<Meeting> meetingsSteve=contactManager.getFutureMeetingList(c);

        assertEquals(2, meetingsSteve.size());

        for(Meeting curr:meetingsSteve){
            assertFalse(curr instanceof PastMeeting);
        }
    }
    @Test
    public void getFutureMeetingsPerDateTest(){
        int idSteve=contactManager.addNewContact("Steve", "accountant");
        int idJane=contactManager.addNewContact("Jane", "Public relations");
        int idTim=contactManager.addNewContact("Tim", "Public relations");
        contacts=contactManager.getContacts(idSteve, idJane);
        Set<Contact> contacts1=contactManager.getContacts(idSteve, idTim);

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

        int idFuture=contactManager.addFutureMeeting(contacts, march30_5oclock);
        int idFuture3=contactManager.addFutureMeeting(contacts, march30_12oclock);
        int idFuture2=contactManager.addFutureMeeting(contacts1, march30_2oclock);
        int idFuture4=contactManager.addFutureMeeting(contacts1, april1st);

        List<Meeting> meetings30March=contactManager.getFutureMeetingList(march30);
        assertEquals(3, meetings30March.size());
        Meeting prev = null;
        for(Meeting curr:meetings30March){
            if (prev != null) {
                assertTrue(curr.getDate().compareTo(prev.getDate()) > 0);
            }
            prev =curr;
        }
    }
    @Test
    public void getPastMeetingsPerContactNormalTest(){

        Contact c=null;
        int idSteve=contactManager.addNewContact("Steve", "accountant");
        int idJane=contactManager.addNewContact("Jane", "Public relations");
        contacts=contactManager.getContacts(idSteve, idJane);

        contactManager.addNewPastMeeting(contacts, december72013, "notes");
        contactManager.addNewPastMeeting(contacts, december152013, "notes");

        Set<Contact> contactSteve=contactManager.getContacts(idSteve);
        for(Contact curr:contactSteve){
            c=curr;
        }
        List<PastMeeting> pastMeetingSteve=contactManager.getPastMeetingList(c);
        assertEquals(2, pastMeetingSteve.size());


        for(PastMeeting curr:pastMeetingSteve){
            assertEquals(contacts, curr.getContacts());
        }
    }

    @Test
    public void getPastMeetingsPerContactDuplicatesTest(){

        Contact c=null;
        int idSteve=contactManager.addNewContact("Steve", "accountant");
        int idJane=contactManager.addNewContact("Jane", "Public relations");
        contacts=contactManager.getContacts(idSteve, idJane);

        contactManager.addNewPastMeeting(contacts, december72013, "notes");
        contactManager.addNewPastMeeting(contacts, december72013, "notes");
        contactManager.addNewPastMeeting(contacts, december152013, "notes");

        Set<Contact> contactSteve=contactManager.getContacts(idSteve);
        for(Contact curr:contactSteve){
            c=curr;
        }
        List<PastMeeting> pastMeetingSteve=contactManager.getPastMeetingList(c);
        assertEquals(2, pastMeetingSteve.size());


        for(PastMeeting curr:pastMeetingSteve){
            assertEquals(contacts, curr.getContacts());
        }

    }

    @Test
    public void getPastMeetingsPerContactSortedTest(){
        Contact c=null;
        int idSteve=contactManager.addNewContact("Steve", "accountant");
        int idJane=contactManager.addNewContact("Jane", "Public relations");
        contacts=contactManager.getContacts(idSteve, idJane);

        contactManager.addNewPastMeeting(contacts, december72013, "notes");
        contactManager.addNewPastMeeting(contacts, december72013, "notes");
        contactManager.addNewPastMeeting(contacts, december152013, "notes");

        Set<Contact> contactSteve=contactManager.getContacts(idSteve);
        for(Contact curr:contactSteve){
            c=curr;
        }
        List<PastMeeting> pastMeetingSteve=contactManager.getPastMeetingList(c);

        Meeting prev = null;
        for(Meeting curr: pastMeetingSteve){
            if (prev != null) {
                assertTrue(curr.getDate().compareTo(prev.getDate())>0);
            }
            prev =curr;
        }
    }

    @Test(expected=IllegalArgumentException.class)
    public void getPastMeetingsPerNonExistentContactTest(){

        Contact c=new ContactImpl(1234, "James", "notes");

        contactManager.getPastMeetingList(c);
    }

    @Test
    public void addNewPastMeetingNormalTest(){
        //how can this be tested if it does not return ID
        Contact c=null;
        int idSteve=contactManager.addNewContact("Steve", "accountant");
        int idJane=contactManager.addNewContact("Jane", "Public relations");
        contacts=contactManager.getContacts(idSteve, idJane);
        String notes="Organisational set up";

        contactManager.addNewPastMeeting(contacts, december72013,notes);
        Set<Contact> contactSteve=contactManager.getContacts(idSteve);
        for(Contact curr:contactSteve){
            c=curr;
        }
        List<PastMeeting> testPastMeetingsList=contactManager.getPastMeetingList(c);
        for(PastMeeting curr:testPastMeetingsList){
            assertEquals("Organisational set up",curr.getNotes());
            assertEquals(december72013, curr.getDate());
        }

    }

    @Test(expected=IllegalArgumentException.class)
    public void addNewPastMeetingDuplicate(){

        int idSteve=contactManager.addNewContact("Steve", "accountant");
        int idJane=contactManager.addNewContact("Jane", "Public relations");
        contacts=contactManager.getContacts(idSteve, idJane);
        contactManager.addNewPastMeeting(contacts, december152013, "test");
        contactManager.addNewPastMeeting(contacts, december152013, "other test");

    }

    @Test(expected=IllegalArgumentException.class)
    public void addNewPastMeetingEmptyNonExistentContactTest(){

        Contact c=new ContactImpl(34567, "Constantine", "development");

        Set<Contact> emptyContactList=new HashSet<Contact>();

        Set<Contact> nonExistentContactList=new HashSet<Contact>();
        nonExistentContactList.add(c);

        contactManager.addNewPastMeeting(emptyContactList, march302014, "notes");
        contactManager.addNewPastMeeting(nonExistentContactList, march302014, "notes");
    }

    @Test(expected=NullPointerException.class)
    public void addNewPastMeetingWithNullArgumentTest(){
        int idSteve=contactManager.addNewContact("Steve", "accountant");
        int idJane=contactManager.addNewContact("Jane", "Public relations");
        contacts=contactManager.getContacts(idSteve, idJane);
        String notes="Organisational set up";

        contactManager.addNewPastMeeting(contacts, december72013,null);

    }

    @Test
    public void addMeetingNotesNormalTest(){
        int idSteve=contactManager.addNewContact("Steve", "accountant");
        int idJane=contactManager.addNewContact("Jane", "Public relations");
        contacts=contactManager.getContacts(idSteve, idJane);
        Calendar february222014=Calendar.getInstance();
        february222014.setTimeInMillis(february222014.getTimeInMillis() + 1000);
        int id=contactManager.addFutureMeeting(contacts, february222014);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Contact c=null;
        Set<Contact> contactSteve=contactManager.getContacts(idSteve);

        for(Contact curr:contactSteve){
            c=curr;
        }
        String text="test";
        contactManager.addMeetingNotes(id, text);
        List<PastMeeting> newPastMeeting=contactManager.getPastMeetingList(c);
        for(PastMeeting curr:newPastMeeting){

            assertEquals(text, curr.getNotes());
            assertEquals(curr,contactManager.getMeeting(id));
        }
    }

    @Test(expected=IllegalArgumentException.class)
    public void addMeetingNotesNonExistentMeetingTest(){
        contactManager.addMeetingNotes(1234, "test");
    }

    @Test(expected=IllegalStateException.class)
    public void addMeetingNotesFutureMeetingTest(){

        int idSteve=contactManager.addNewContact("Steve", "accountant");
        int idJane=contactManager.addNewContact("Jane", "Public relations");
        contacts=contactManager.getContacts(idSteve, idJane);
        int id=contactManager.addFutureMeeting(contacts, march302014);

        contactManager.addMeetingNotes(id,"text");

    }

    @Test(expected =NullPointerException.class)
    public void addMeetingNotesNullTest(){

        int idSteve=contactManager.addNewContact("Steve", "accountant");
        int idJane=contactManager.addNewContact("Jane", "Public relations");
        contacts=contactManager.getContacts(idSteve, idJane);
        int id=contactManager.addFutureMeeting(contacts, march302014);

        String notes=null;
        contactManager.addMeetingNotes(id,null);

    }

    @Test
    public void addMeetingNotesToExistingPastMeetingTest(){
        Contact contactSteve=null;
        int idSteve=contactManager.addNewContact("Steve", "accountant");
        contacts=contactManager.getContacts(idSteve);

        for(Contact c:contacts){
            contactSteve=c;
        }

        contactManager.addNewPastMeeting(contacts, december152013, " ");
        PastMeeting stevePastMeeting=null;

        for(PastMeeting curr:contactManager.getPastMeetingList(contactSteve))
        {
        contactManager.addMeetingNotes(curr.getId(), "test notes");
        }
        for(PastMeeting curr:contactManager.getPastMeetingList(contactSteve)){
            assertEquals("test notes", curr.getNotes());
        }

    }
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
        int idTim=contactManager.addNewContact("Tim", "sponsor");


        Set<Contact> testContacts=contactManager.getContacts(idTom, idJane, idTim);

        //check the size of the returned set matches the number of contacts expected

        assertEquals(3, testContacts.size());

       //checks the contacts in the match the one expected

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
            else if(curr.getId()==idTim){
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

        Contact tom=null;
        Contact tim=null;

        Set<Contact> contactContainingName=contactManager.getContacts("Jones");

        for(Contact curr:contactContainingName){
            if(curr.getId()==idTom){
                tom=curr;
            }
            else if(curr.getId()==idTim){
                tim=curr;
            }
            else{
                throw new RuntimeException("unexpected contact");
            }
        }

        assertEquals("Tom Jones", tom.getName());
        assertEquals("Tim Jones", tim.getName());

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
