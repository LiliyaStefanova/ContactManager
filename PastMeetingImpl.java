import java.util.Calendar;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: liliya
 * Date: 11/02/14
 * Time: 15:19
 * To change this template use File | Settings | File Templates.
 */
public class PastMeetingImpl extends MeetingImpl implements PastMeeting {

    private String meetingNotes;

    public PastMeetingImpl(int id, Calendar date, Set<Contact> attendees, String text){
        super(id, date, attendees);
        if(date.compareTo(getDate())>0){
            throw new IllegalArgumentException("The date is in the future");
        }
        this.meetingNotes=text;
    }

    @Override
    public String getNotes() {

        return this.meetingNotes;

    }
}
