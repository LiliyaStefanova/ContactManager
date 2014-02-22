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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MeetingImpl)) return false;

        MeetingImpl meeting = (MeetingImpl) o;

        if (meetingID != meeting.meetingID) return false;
        if (!meetingAttendees.equals(meeting.meetingAttendees)) return false;
        if (!scheduledDate.equals(meeting.scheduledDate)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = meetingID;
        result = 31 * result + scheduledDate.hashCode();
        result = 31 * result + meetingAttendees.hashCode();
        return result;
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

}
