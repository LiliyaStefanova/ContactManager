import java.io.Serializable;
import java.util.Calendar;
import java.util.Set;

public class FutureMeetingImpl extends MeetingImpl implements FutureMeeting, Serializable {

    //parameter-less constructor added as Serializable is implemented
    public FutureMeetingImpl(){}

    public FutureMeetingImpl(int id, Calendar date, Set<Contact> attendees){
        //using super constructor of MeetingImpl
        super(id, date, attendees);
    }
}
