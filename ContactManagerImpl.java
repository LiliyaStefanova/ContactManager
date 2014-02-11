import java.util.Calendar;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: liliya
 * Date: 11/02/14
 * Time: 15:02
 * To change this template use File | Settings | File Templates.
 */
public class ContactManagerImpl implements ContactManager {
    @Override
    public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public PastMeeting getPastMeeting(int id) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public FutureMeeting getFutureMeeting(int id) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Meeting getMeeting(int id) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Meeting> getFutureMeetingList(Contact contact) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Meeting> getFutureMeetingList(Calendar date) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<PastMeeting> getPastMeetingList(Contact contact) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addMeetingNotes(int id, String text) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addNewContact(String name, String notes) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Set<Contact> getContacts(int... ids) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Set<Contact> getContacts(String name) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void flush() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
