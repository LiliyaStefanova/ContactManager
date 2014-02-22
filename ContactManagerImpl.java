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
        //need to add a check here to ensure the id does not exist already
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
        Meeting meeting=null;
        PastMeeting pastMeeting=null;
        for(Meeting curr:allMeetings){
            if(curr.getId()==id){
                meeting=curr;
            }
        }
        if(meeting instanceof PastMeeting){
            pastMeeting=(PastMeeting) meeting;

        }
        else if(meeting instanceof FutureMeeting){
            throw new IllegalArgumentException("This meeting is happening in the future");
        }
        return pastMeeting;
    }

    @Override
    public FutureMeeting getFutureMeeting(int id) {

        Meeting meeting=null;
        FutureMeeting futureMeeting=null;
        for(Meeting curr:allMeetings){
            if(curr.getId()==id){
                meeting=curr;
            }
        }
        if(meeting instanceof PastMeeting){
            throw new IllegalArgumentException("This meeting is happening in the past");
        }
        else if(meeting instanceof FutureMeeting){
            futureMeeting=(FutureMeeting) meeting;
        }

        return futureMeeting;
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
    //changed signature to return int, to allow for testing to be performed
    public int addNewContact(String name, String notes) {

        int id=generateContactID();
        Contact newContact=new ContactImpl(id, name, notes);
        allContacts.add(newContact);

        return newContact.getId();
    }

    @Override
    public Set<Contact> getContacts(int... ids) {
        Set<Contact> contacts=new HashSet<Contact>();
        for (int id : ids) {
            for (Contact curr : allContacts) {
                if (curr.getId() == id) {
                    contacts.add(curr);
                }
            }

        }
        if(contacts.size()!=ids.length) {
            throw new IllegalArgumentException("ID provided is a for a non-existent contact");
        }

        return contacts;
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
