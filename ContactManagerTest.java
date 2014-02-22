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
    private Set<Contact> contacts, emptyContacts;
    private Calendar pastDate;
    private Calendar futureDate;


    @Before
    public void buildUp(){
        contactManager =new ContactManagerImpl();
        contacts=new HashSet<Contact>();
        Contact contact1=new ContactImpl(123, "Steve", "accountant");
        Contact contact2=new ContactImpl(124, "Jane", "Public relations");
        contacts.add(contact1);
        contacts.add(contact2);
      //  emptyContacts.add(new ContactImpl(0, null, null));
        pastDate=Calendar.getInstance();
        pastDate.set(2013, Calendar.DECEMBER, 7);
        futureDate=Calendar.getInstance();
        futureDate.set(2014, Calendar.MARCH, 30);


    }

  //Begin meeting test cases

    @Test
    public void addFutureMeetingNormalTest(){

        int meetingID= contactManager.addFutureMeeting(contacts, futureDate);

        FutureMeeting meeting= contactManager.getFutureMeeting(meetingID);

        assertNotNull(meeting);


    }

    @Test(expected = IllegalArgumentException.class)
    public void addFutureMeetingInThePastTest(){
        contactManager.addFutureMeeting(contacts, pastDate);
    }

    @Test
    public void addFutureMeetingNonExistentContactTest(){
        contactManager.addFutureMeeting(contacts, futureDate);

    }

    @Test
    public void getPastMeetingNormalTest(){
        String notes="notes";
        contactManager.addNewPastMeeting(contacts,pastDate,notes );
        contactManager.addNewPastMeeting(contacts,pastDate,notes );

    }

    @Test
    public void getPastMeetingInFutureTest(){

    }

    @Test
    public void getFutureMeetingNormalTest(){

        int id=contactManager.addFutureMeeting(contacts, futureDate);

        assertEquals(contactManager.getFutureMeeting(id).getId(), id);

    }

    @Test
    public void getFutureMeetingInPastTest(){

    }

    @Test
    public void getMeetingExistingTest(){

    }

    @Test
    public void getMeetingNonExistentTest(){

    }

    @Test
    public void getFutureMeetingListPerContactNormalTest(){

    }

    @Test
    public void getFutureMeetingListPerContactEmptyListTest(){

    }

    @Test(expected=IllegalArgumentException.class)
    public void getFutureMeetingListPerContactNonExistingContactTest(){

    }

    @Test
    public void getFutureMeetingListPerContactNoDuplicatesTest(){

    }

    @Test
    public void getFutureMeetingListPerContactSortedTest(){

    }

    @Test
    public void getFutureMeetingsPerDateNormalTest(){

    }

    @Test
    public void getFutureMeetingsPerDateEmptyTest(){

    }

    @Test
    public void getFutureMeetingsPerDateDuplicatesTest(){

    }

    @Test
    public void getFutureMeetingsPerDateSortedTest(){

    }

    @Test
    public void getPastMeetingsPerContactNormalTest(){

    }

    @Test
    public void getPastMeetingsPerContactEmptyTest(){

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

    }

    @Test(expected=IllegalArgumentException.class)
    public void addNewPastMeetingEmptyContactListTest(){

    }

    @Test(expected=IllegalArgumentException.class)
    public void addNewPastMeetingNonExistentContactTest(){

    }

    @Test(expected = NullPointerException.class)
    public void addNewPastMeetingNullArgumentTest(){

    }

    @Test
    public void addMeetingNotesNormalTest(){

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

    }

    @Test(expected = NullPointerException.class)
    public void getContactsNullParam(){

    }

  //End Contact related test cases

  //Begin I/O related test cases












}
