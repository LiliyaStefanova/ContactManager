/**
 * Created with IntelliJ IDEA.
 * User: liliya
 * Date: 11/02/14
 * Time: 13:34
 * To change this template use File | Settings | File Templates.
 */

import org.junit.*;
import org.junit.Assert.*;
import org.junit.Before;

public class ContactManagerTest {


  //Begin meeting test cases

    @Test
    public void addFutureMeetingNormalTest(){

    }

    @Test
    public void addFutureMeetingInThePastTest(){

    }

    @Test
    public void addFutureMeetingNonExistentContactTest(){

    }

    @Test
    public void getPastMeetingNormalTest(){

    }

    @Test
    public void getPastMeetingInFutureTest(){

    }

    @Test
    public void getFutureMeetingNormalTest(){

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

    }

    @Test(expected = NullPointerException.class)
    public void addNewContactNameNullTest(){

    }

    @Test(expected = NullPointerException.class)
    public void addNewContactNotesNullTest(){

    }

    @Test
    public void getContactsNormalTest(){

    }

    @Test(expected = IllegalArgumentException.class)
    public void getContactsNonExistentContact(){

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
