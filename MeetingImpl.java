import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: liliya
 * Date: 11/02/14
 * Time: 15:02
 * To change this template use File | Settings | File Templates.
 */
public class MeetingImpl implements Meeting {

    private int meetingID;
    private Calendar scheduledDate;
    private Set<Contact> meetingAttendees;

    public MeetingImpl(int id, Calendar date, Set<Contact> attendees){
        this.meetingID=id;
        this.scheduledDate=date;
        meetingAttendees=new HashSet<Contact>();
        this.meetingAttendees=attendees;
    }

    @Override
    public int getId() {

        return this.meetingID;
    }

    @Override
    public Calendar getDate() {

       return this.scheduledDate;
    }

    @Override
    public Set<Contact> getContacts() {

        return this.meetingAttendees;
    }
    /**
     * Testing purposes only-to be removed
    public static void main(String [] args){
        Set<Contact> testContact=new HashSet<Contact>();
        Contact contact1=new ContactImpl(1234, "Jane", "venture capital");
        Contact contact2=new ContactImpl(3456, "Henry", "investor");
        testContact.add(contact1);
        testContact.add(contact2);
        Calendar meetingDate=Calendar.getInstance();
        meetingDate.set(2012,Calendar.DECEMBER,11);
        Meeting meet=new MeetingImpl(1234,meetingDate, testContact);
        Set<Contact> test1=new HashSet<Contact>();
        Contact contact3=new ContactImpl(1234, "Jane", "venture capital");
        Contact contact4=new ContactImpl(3456, "Henry", "investor");
        test1.add(contact1);
        test1.add(contact2);
        for(Contact current:test1){

            System.out.println(current.getName());
        }
            System.out.println(testContact.equals(test1));

        }
    */

}
