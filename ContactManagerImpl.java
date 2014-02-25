import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
//need to write java doc comments for all the classes and generate
//need to sort out the IO functions to create a new file
//notes to a past meeting need to be appended and not overwritten

public class ContactManagerImpl implements ContactManager, Serializable {

    private Set<Contact> allContacts;
    private List<Meeting> allMeetings;
    private static final String FILENAME = "contacts.xml";

    public ContactManagerImpl() {
        allContacts = new HashSet<Contact>();
        allMeetings = new ArrayList<Meeting>();
        File f = new File(FILENAME);
        if (f.exists()&&f.length()>0) {
            decodeFile();
        }
        else if(f.exists()&&f.length()>0){

        }
        else if(!f.exists()){
            try {
                f.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
        int id = 0;
        Calendar now = Calendar.getInstance();
        Meeting newFutureMeeting = null;
        //check date against current date to determine if in the past
        if (date.getTime().compareTo(now.getTime()) < 0) {
            throw new IllegalArgumentException("Date specified in the past");
        } else {
            for (Contact curr : contacts) {
                if (!allContacts.contains(curr)) {
                    throw new IllegalArgumentException("Contact " + curr.getName() + " does not exist");
                }
            }
        }
        //keep generating new id's if this random id already exists in the list
        do {
            id = generateRandomMeetingID();
        }
        while (meetingIdExists(id));
        /**
         * Only add the meeting if it does not exist already
         * A meeting is considered a duplicate if it has the same contact list and same date/time
         */
        if (!meetingExists(contacts, date)) {
            newFutureMeeting = new FutureMeetingImpl(id, date, contacts);
            allMeetings.add(newFutureMeeting);
        } else {
            throw new IllegalArgumentException("Meeting already exists");
        }
        return newFutureMeeting.getId();
    }

    @Override
    public PastMeeting getPastMeeting(int id) {
        Meeting meeting = null;
        PastMeeting pastMeeting = null;
        for (Meeting curr : allMeetings) {
            if (curr.getId() == id) {
                meeting = curr;
            }
        }
        if (meeting instanceof PastMeeting) {
            pastMeeting = (PastMeeting) meeting;

        } else if (meeting instanceof FutureMeeting) {
            throw new IllegalArgumentException("This meeting is happening in the future");
        }
        return pastMeeting;
    }

    @Override
    public FutureMeeting getFutureMeeting(int id) {
        Meeting meeting = null;
        FutureMeeting futureMeeting = null;
        for (Meeting curr : allMeetings) {
            if (curr.getId() == id) {
                meeting = curr;
            }
        }
        if (meeting instanceof FutureMeeting) {
            futureMeeting = (FutureMeeting) meeting;

        } else if (meeting instanceof PastMeeting) {
            throw new IllegalArgumentException("This meeting is happening in the past");
        }

        return futureMeeting;
    }

    @Override
    public Meeting getMeeting(int id) {
        Meeting meeting = null;
        for (Meeting curr : allMeetings) {
            if (curr.getId() == id) {
                meeting = curr;
            }
        }
        return meeting;
    }

    @Override
    public List<Meeting> getFutureMeetingList(Contact contact) {
        if (!allContacts.contains(contact)) {
            throw new IllegalArgumentException("Contact does not exist in records");
        }
        List<Meeting> futureMeetingsPerContact = new ArrayList<Meeting>();
        /*
        All meetings for a contact will be put in a TreeSet to sort by date and remove duplicates
         */
        Set<Meeting> interimMeetingList = new TreeSet<Meeting>(new Comparator<Meeting>() {
            @Override
            public int compare(Meeting o1, Meeting o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });

        for (Meeting curr : allMeetings) {
            //only add future meetings to the list
            if (curr instanceof FutureMeeting) {
                if (curr.getContacts().contains(contact)) {
                    interimMeetingList.add(curr);
                }
            }
        }
        //converting the tree to list
        for (Meeting curr : interimMeetingList) {
            if (curr != null) {
                futureMeetingsPerContact.add(curr);
            }
        }
        return futureMeetingsPerContact;
    }

    @Override
    public List<Meeting> getFutureMeetingList(Calendar date) {
        List<Meeting> futureMeetingsPerDate = new ArrayList<Meeting>();
        Set<Meeting> interim = new TreeSet<Meeting>(new Comparator<Meeting>() {
            @Override
            public int compare(Meeting o1, Meeting o2) {
                //compares items by date and time
                int compCode = o1.getDate().compareTo(o2.getDate());
                if (compCode == 0) {
                    //if so, it checks the ids to confirm if they are duplicates on the same day
                    //or different meetings
                    return new Integer(o1.getId()).compareTo(o2.getId());
                } else {
                    return compCode;
                }
            }
        });
        for (Meeting curr : allMeetings) {
            //only add future meetings to the list
            if (curr instanceof FutureMeeting) {
                //populate list with all meetings on the same date, but disregard time
                if (curr.getDate().get(Calendar.YEAR) == date.get(Calendar.YEAR) && curr.getDate().
                        get(Calendar.DAY_OF_MONTH) == date.get(Calendar.DAY_OF_MONTH) && curr.getDate().
                        get(Calendar.MONTH) == date.get(Calendar.MONTH)) {
                    interim.add(curr);
                }
            }
        }
        //converting the tree to list
        for (Meeting curr : interim) {
            if (curr != null) {
                futureMeetingsPerDate.add(curr);
            }
        }
        return futureMeetingsPerDate;
    }

    @Override
    public List<PastMeeting> getPastMeetingList(Contact contact) {
        if (!allContacts.contains(contact)) {
            throw new IllegalArgumentException("Contact does not exist");
        }
        List<PastMeeting> pastMeetingsPerContact = new ArrayList<PastMeeting>();
        //all meetings for a contact will be put in a tree set to sort and remove duplicates
        Set<PastMeeting> interim = new TreeSet<PastMeeting>(new Comparator<Meeting>() {
            @Override
            public int compare(Meeting o1, Meeting o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });

        for (Meeting curr : allMeetings) {
            //only add future meetings to the list
            if (curr instanceof PastMeeting) {
                if (curr.getContacts().contains(contact)) {
                    interim.add((PastMeeting) curr);
                }
            }
        }
        //converting the tree to list
        for (PastMeeting curr : interim) {
            if (curr != null) {
                pastMeetingsPerContact.add(curr);
            }
        }
        return pastMeetingsPerContact;

    }

    @Override
    public void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text) {
        int id = 0;
        Calendar now = Calendar.getInstance();
        //check this is right
        if (date.getTime().compareTo(now.getTime()) > 0) {
            throw new IllegalArgumentException("Date specified in the future");
            //not checking date as Calendar type object indicates it cannot be null
        } else if (text == null || contacts == null) {
            throw new NullPointerException("One of the parameters is missing");
        } else if (contacts.isEmpty()) {
            throw new IllegalArgumentException("The list of contacts is empty");
        } else {
            for (Contact curr : contacts) {
                if (!allContacts.contains(curr)) {
                    throw new IllegalArgumentException("Contact " + curr.getName() + " does not exist");
                }
            }
        }
        /**
         * Check that the same id does not exist already-if yes, regenerate ID
         */
        do {
            id = generateRandomMeetingID();
        }
        while (meetingIdExists(id));

        Meeting newPastMeeting = new PastMeetingImpl(id, date, contacts, text);
        if (!meetingExists(contacts, date)) {
            allMeetings.add(newPastMeeting);
        } else {
            throw new IllegalArgumentException("Meeting already exists");
        }
    }

    @Override
    public void addMeetingNotes(int id, String text) {
        Calendar now = Calendar.getInstance();
        Meeting convertedToPast = null;
        Meeting updatedPast=null;
        if (text == null) {
            throw new NullPointerException("Notes are not specified");
        } else if (!meetingIdExists(id)) {
            throw new IllegalArgumentException("Meeting does not exist");
        }
        for (Meeting curr : allMeetings) {
            if (curr.getId() == id) {
                if (curr.getDate().getTime().compareTo(now.getTime()) > 0) {
                    throw new IllegalStateException("Meeting is set for date in the future");
                }
                else if(curr instanceof PastMeeting){
                    //replace existing past meeting with a new identical past meeting with the new notes
                    updatedPast=new PastMeetingImpl(curr.getId(), curr.getDate(), curr.getContacts(), text);
                    allMeetings.add(convertedToPast);
                    allMeetings.remove(curr);
                }
                else if(curr instanceof FutureMeeting){
                    //replace existing future meeting with an identical past meeting, remove future meeting
                    convertedToPast = new PastMeetingImpl(curr.getId(), curr.getDate(), curr.getContacts(), text);
                    allMeetings.remove(curr);
                    allMeetings.add(convertedToPast);
                 }
            }
        }
    }

    /**
     * The interface of this method has been amended to return the contact ID
     */
    @Override
    public void addNewContact(String name, String notes) {
        int id = 0;
        do {
            id = generateContactID();
        } while (contactIdExists(id));

        //contact deemed unique based on ID only, no checks on duplicate name and notes carried out
        Contact newContact = new ContactImpl(id, name, notes);
        allContacts.add(newContact);
    }

    @Override
    public Set<Contact> getContacts(int... ids) {
        Set<Contact> contacts = new HashSet<Contact>();
        for (int id : ids) {
            for (Contact curr : allContacts) {
                if (curr.getId() == id) {
                    contacts.add(curr);
                }
            }
        }
        if (contacts.size() != ids.length) {
            throw new IllegalArgumentException("ID provided is a for a non-existent contact");
        }

        return contacts;
    }

    @Override
    public Set<Contact> getContacts(String name) {
        Set<Contact> contactsContainingName = new HashSet<Contact>();
        if (name == null) {
            throw new NullPointerException("Name provided is null");
        } else {
            for (Contact curr : allContacts) {
                if (curr.getName().contains(name)) {
                    contactsContainingName.add(curr);
                }
            }
        }
        return contactsContainingName;
    }

    @Override
    public void flush() {
        File contactManager=new File(FILENAME);
        if(contactManager.exists()){
            encodeFile();
        }
        else{
            try{
            contactManager.createNewFile();
            encodeFile();
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        for(Contact c:allContacts){
            System.out.println(c.getName());
        }
    }

    public Set<Contact> getAllContacts() {
        return allContacts;
    }

    public void setAllContacts(Set<Contact> allContacts) {
        this.allContacts = allContacts;
    }

    public List<Meeting> getAllMeetings() {
        return allMeetings;
    }

    public void setAllMeetings(List<Meeting> allMeetings) {
        this.allMeetings = allMeetings;
    }

    //make this just one method-it can be more generic

    private int generateRandomMeetingID() {

        return (int) Math.abs(Math.random() * Integer.MAX_VALUE);
    }

    private int generateContactID() {

        return (int) Math.abs(Math.random() * Integer.MAX_VALUE);
    }

    private boolean meetingExists(Set<Contact> contacts, Calendar date) {
        boolean meetingExists = false;
        for (Meeting curr : allMeetings) {
            if (curr.getContacts().equals(contacts) && curr.getDate().equals(date)) {
                meetingExists = true;
                return meetingExists;
            }
        }
        return meetingExists;
    }

    private boolean meetingIdExists(int id) {
        boolean idExists = false;
        for (Meeting curr : allMeetings) {
            if (curr.getId() == id) {
                idExists = true;
                return idExists;
            }
        }
        return idExists;
    }

    private boolean contactIdExists(int id) {
        boolean idExists = false;
        for (Contact curr : allContacts) {
            if (curr.getId() == id) {
                idExists = true;
                return idExists;
            }
        }
        return idExists;
    }
    @SuppressWarnings("unchecked")
    public void decodeFile() {
        XMLDecoder decode=null;
        try{
            decode=new XMLDecoder(new BufferedInputStream(new FileInputStream(FILENAME)));

        }
        catch(FileNotFoundException ex){
            ex.printStackTrace();
        }
        allMeetings=(List<Meeting>) decode.readObject();
        allContacts=(Set<Contact>) decode.readObject();

        decode.close();

    }

    public void encodeFile() {
        XMLEncoder encode = null;
        try {
            encode = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(FILENAME)));
            encode.writeObject(allMeetings);
            encode.writeObject(allContacts);
        } catch (FileNotFoundException ex) {
            System.err.println(ex);
        } finally {
            if (encode != null) {
                encode.close();
                }
        }
    }
    public static void main(String[] args) {

        ContactManager cm =new ContactManagerImpl();

    }
}
