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
        Meeting meeting=null;
        for(Meeting curr: allMeetings){
            if(curr.getId()==id){
                meeting=curr;
            }
        }
        return meeting;
    }

    @Override
    public List<Meeting> getFutureMeetingList(Contact contact) {
        if(!allContacts.contains(contact)){
            throw new IllegalArgumentException("Contact does not exist");
        }
        List<Meeting> futureMeetingsPerContact=new ArrayList<Meeting>();
        //all meetings for a contact will be put in a tree set to sort and remove duplicates
        Set<Meeting> interim=new TreeSet<Meeting>(new Comparator<Meeting>() {
            @Override
            public int compare(Meeting o1, Meeting o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });

        for(Meeting curr: allMeetings){
            //only add future meetings to the list
            if(curr instanceof FutureMeeting){
                if(curr.getContacts().contains(contact)){
                        interim.add(curr);
                }
            }
        }
        //converting the tree to list
        for(Meeting curr:interim){
            if(curr!=null){
            futureMeetingsPerContact.add(curr);
            }
        }
        return futureMeetingsPerContact;
    }

    @Override
    public List<Meeting> getFutureMeetingList(Calendar date) {
        //how can the list be sorted if they are all on the same date?

        List<Meeting> futureMeetingsPerDate=new ArrayList<Meeting>();
        Set<Meeting> interim=new TreeSet<Meeting>(new Comparator<Meeting>() {
            @Override
            public int compare(Meeting o1, Meeting o2) {
                //compares items by date to check they are the same
                int rcode = o1.getDate().compareTo(o2.getDate());
                if (rcode == 0) {
                    //if so, it checks the ids to confirm if they are duplicates on the same day
                    //or different meetings
                    return new Integer(o1.getId()).compareTo(o2.getId());
                } else {
                    return rcode;
                }
            }
        });
        for(Meeting curr: allMeetings){
            //only add future meetings to the list
            if(curr instanceof FutureMeeting){
                if(curr.getDate().equals(date)){
                    interim.add(curr);
                }
            }
        }
        //converting the tree to list
        for(Meeting curr:interim){
            if(curr!=null){
                futureMeetingsPerDate.add(curr);
            }
        }
        return futureMeetingsPerDate;
    }

    @Override
    public List<PastMeeting> getPastMeetingList(Contact contact) {
        if(!allContacts.contains(contact)){
            throw new IllegalArgumentException("Contact does not exist");
        }
        List<PastMeeting> pastMeetingsPerContact=new ArrayList<PastMeeting>();
        //all meetings for a contact will be put in a tree set to sort and remove duplicates
        Set<PastMeeting> interim=new TreeSet<PastMeeting>(new Comparator<Meeting>() {
            @Override
            public int compare(Meeting o1, Meeting o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });

        for(Meeting curr: allMeetings){
            //only add future meetings to the list
            if(curr instanceof PastMeeting){
                if(curr.getContacts().contains(contact)){
                    interim.add((PastMeeting)curr);
                }
            }
        }
        //converting the tree to list
        for(PastMeeting curr:interim){
            if(curr!=null){
                pastMeetingsPerContact.add(curr);
            }
        }
        return pastMeetingsPerContact;

    }


    @Override
    public void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text) {
        //need to add a check here to ensure the id does not exist already
        Calendar now=Calendar.getInstance();
        //check this is right
        if(date.getTime().compareTo(now.getTime())>0){
            throw new IllegalArgumentException("Date specified in the future");
        }
        else if(date==null||text==null||contacts==null){
            throw new IllegalArgumentException("One of the parameters is missing");
        }
        else if(contacts.isEmpty()){
            throw new IllegalArgumentException("The list of contacts is empty");

        }
        else{
            for(Contact curr:contacts){
                if(!allContacts.contains(curr)){
                    throw  new IllegalArgumentException("Contact "+ curr.getName()+ " does not exist");
                }
            }
        }
        int id=generateMeetingID();
        Meeting newPastMeeting=new PastMeetingImpl(id, date, contacts, text);
        allMeetings.add(newPastMeeting);

    }

    @Override
    public void addMeetingNotes(int id, String text) {
        Calendar now=Calendar.getInstance();
        Meeting convertedToPast=null;
        boolean meetingExists=false;
        if(text==null){
            throw new NullPointerException("Notes are not specified");
        }
        for(Meeting curr:allMeetings){
            if(curr.getId()==id){
                if(curr.getDate().getTime().compareTo(now.getTime())>0)
                {
                    throw new IllegalStateException("Meeting is set for date in the future");
                }
                convertedToPast=new PastMeetingImpl(curr.getId(), curr.getDate(), curr.getContacts(), text);
                allMeetings.remove(curr);
                allMeetings.add(convertedToPast);
            }

        }

    }

    @Override
    //changed interface to return int, to allow for testing to be performed
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
        Set<Contact> contactsContainingName=new HashSet<Contact>();
        CharSequence s=name;
        if(name==null){
            throw new NullPointerException("Name provided is null");
        }
        else{
            for(Contact curr: allContacts){
                if(curr.getName().contains(s)){
                    contactsContainingName.add(curr);
                }
            }
        }
        return contactsContainingName;
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


    public static void main(String [] args){

        ContactManager cm=new ContactManagerImpl();


    }
}
