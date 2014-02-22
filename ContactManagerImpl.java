import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: liliya
 * Date: 11/02/14
 * Time: 15:02
 * To change this template use File | Settings | File Templates.
 */
public class ContactManagerImpl implements ContactManager {

    private Set<Contact> allContacts;
    private List<Meeting> allMeetings;
    private Map<Contact, Meeting> meetingsPerContact;
    private Map<Meeting, Contact> contactMeetings;

    public ContactManagerImpl(){

        allContacts=new HashSet<Contact>();
        allMeetings=new ArrayList<Meeting>();
        meetingsPerContact=new HashMap<Contact, Meeting>();
        contactMeetings=new HashMap<Meeting, Contact>();
    }

    @Override
    public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
        //need to add a check here to ensure the id does not exist alread
        Calendar now=Calendar.getInstance();
        //check this is right
        if(date.getTime().compareTo(now.getTime())<0){
            throw new IllegalArgumentException("Date specified in the past");
        }
        else{
            for(Contact curr:contacts){
                if(!allContacts.contains(curr)){
                    throw  new IllegalArgumentException("Contact "+ curr.getName()+ " does not exist");
                }
            }
        }
        int id=generateMeetingID();
        Meeting newFutureMeeting=new FutureMeetingImpl(id,date, contacts);
        allMeetings.add(newFutureMeeting);
        return newFutureMeeting.getId();
    }

    @Override
    public PastMeeting getPastMeeting(int id) {
        PastMeeting result=null;
        if(allMeetings.get(id)==null){
            return null;
        }
       else if(allMeetings.get(id) instanceof PastMeeting){
            result=(PastMeeting) allMeetings.get(id);
        }
        else if(allMeetings.get(id) instanceof FutureMeeting){
            throw new IllegalArgumentException("This meeting is happening in the future");
        }
        return result;
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

    private int generateMeetingID(){

        int meetingID=(int) Math.abs(Math.random()*Integer.MAX_VALUE);
        return meetingID;
    }

    private int generateContactID(){

        int contactID=(int) Math.abs(Math.random()*Integer.MAX_VALUE);
        return contactID;

    }


}
