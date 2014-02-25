import java.io.Serializable;
import java.util.Calendar;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: liliya
 * Date: 11/02/14
 * Time: 15:24
 * To change this template use File | Settings | File Templates.
 */
public class FutureMeetingImpl extends MeetingImpl implements FutureMeeting, Serializable {

    //parameter-less constructor added as Serializable is implemented
    public FutureMeetingImpl(){}

    public FutureMeetingImpl(int id, Calendar date, Set<Contact> attendees){
        //using super constructor of MeetingImpl
        super(id, date, attendees);
    }
}
