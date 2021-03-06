package com.liliya.contactmanager;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.*;
import java.util.ArrayList;
import java.util.List;

public class ContactManagerImpl implements ContactManager, Serializable {

    private Set<Contact> allContacts;
    private List<Meeting> allMeetings;
    //this implementation uses an xml file to write data to
    private static final String FILENAME = "contacts.xml";

    public ContactManagerImpl() {
        this(true);
    }
    //this constructor will be called if de-serialization is not required for some reason
    public ContactManagerImpl(boolean deserialize) {
        allContacts = new HashSet<Contact>();
        allMeetings = new ArrayList<Meeting>();

        if (deserialize) {
            //file is loaded when the program starts, i.e. when a new Contact manager object is created
            File f = new File("."+File.separator+FILENAME);
            if (f.exists() && f.length() > 0) {
                decodeFile();
            } else if (f.exists() && f.length() == 0) {
                System.out.println("File is empty");
                //warn user if file exists but is empty
                //wait for file to be written to
            } else if (!f.exists()) {
                try {
                    f.createNewFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

    }

    /**
     * Method adds future meeting to the list
     * @param contacts a list of contacts that will participate in the meeting
     * @param date the date on which the meeting will take place
     * @return the id of the meeting
     * @throws IllegalArgumentException if the date is specified in the past or contact does not exist
     * A runtime IllegalArgument exception will also be thrown if a meeting
     * with same date/time and contacts already exists-not specified in javadoc of interface
     */
    @Override
    public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
        int id = 0;
        Calendar now = Calendar.getInstance();
        Meeting newFutureMeeting = null;
        //check date against current date to determine if in the past
        if (date.getTime().compareTo(now.getTime()) < 0) {
            throw new IllegalArgumentException("Date specified is in the past");
        } else {
            for (Contact curr : contacts) {
                if (!allContacts.contains(curr)) {
                    throw new IllegalArgumentException("Contact " + curr.getName() + " does not exist");
                }
            }
        }
        //keep generating new id's if this random id already exists in the list
        do {
            id = generateRandomID();
        }
        while (meetingIdExists(id));

         //A meeting is considered a duplicate if it has the same contact list and same date/time
        if (!meetingExists(contacts, date)) {
            newFutureMeeting = new FutureMeetingImpl(id, date, contacts);
            allMeetings.add(newFutureMeeting);
            id=newFutureMeeting.getId();
        } else {
            //throwing a runtime exception if the meeting already exists
            throw new IllegalArgumentException("Meeting already exists!");
        }
        return id;
    }

    @Override
    public PastMeeting getPastMeeting(int id)  {
        Meeting meeting = null;
        PastMeeting pastMeeting = null;
        for (Meeting curr : allMeetings) {
            if (curr.getId() == id) {
                meeting = curr;
            }
        }
        if (meeting instanceof PastMeeting) {
            //down-casting to past meeting
            pastMeeting = (PastMeeting) meeting;

        } else if (meeting instanceof FutureMeeting) {
            /**
             * It is assumed that if a future meeting has gone into the past but has not been converted to a
             * past meeting using addMeetingNotes() yet it will throw an exception
             */
            throw new IllegalArgumentException("This meeting is happening in the future");
        }
        return pastMeeting;
    }

    @Override
    public FutureMeeting getFutureMeeting(int id)  {
        Meeting meeting = null;
        FutureMeeting futureMeeting = null;
        for (Meeting curr : allMeetings) {
            if (curr.getId() == id) {
                meeting = curr;
            }
        }
        if (meeting instanceof FutureMeeting) {
            //down-casting to FutureMeeting
            futureMeeting = (FutureMeeting) meeting;

        } else if (meeting instanceof PastMeeting) {
            throw new IllegalArgumentException("This meeting has happened in the past");
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
    /**
     * This method retrieves meetings that a contact attended and returns them in a list
     * Meetings are chronologically sorted by date and time
     * A tree set structure is used to eliminate duplicates and allow sorting
     * @param contact
     * @return list of meetings that took place on that day
     */
    @Override
    public List<Meeting> getFutureMeetingList(Contact contact) {
        if (!allContacts.contains(contact)) {
            //contact is not an entry in allContacts
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
            //only adding future meetings that the contact was part of to the list
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

    /**
     * This method retrieves meetings that occurred on a given date and returns them in a list
     * Meetings are chronologically sorted by time
     * A tree set structure is used to eliminate duplicates and allow sorting
     * @param date the date
     * @return list of meetings that took place on that day
     */
    @Override
    public List<Meeting> getFutureMeetingList(Calendar date) {
        List<Meeting> futureMeetingsPerDate = new ArrayList<Meeting>();
        Set<Meeting> interim = new TreeSet<Meeting>(new Comparator<Meeting>() {
            @Override
            public int compare(Meeting o1, Meeting o2) {
                //sorts items by date and time
                int compCode = o1.getDate().compareTo(o2.getDate());
                if (compCode == 0) {
                    //and then by id
                    return Integer.valueOf(o1.getId()).compareTo(o2.getId());
                } else {
                    return compCode;
                }
            }
        });
        for (Meeting curr : allMeetings) {
            //only add future meetings to the list
            if (curr instanceof FutureMeeting) {
                /*
                only date component is considered to decide if a meeting is on the same date
                time component disregarded as there can be multiple meetings on the same day but at different times
                 */
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
        //all meetings for a contact will be put in a tree set to sort by date and remove duplicates
        Set<PastMeeting> interim = new TreeSet<PastMeeting>(new Comparator<Meeting>() {
            @Override
            public int compare(Meeting o1, Meeting o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });

        for (Meeting curr : allMeetings) {
            //only add past meetings to the list
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

    /**
     * This method adds a brand new meeting which was not on record previously but took place in the past
     * The method implementation assumes that some notes need to be specified in the constructor as a past meeting
     * is created, even if empty
     * @param contacts a list of participants
     * @param date the date on which the meeting took place
     * @param text messages to be added about the meeting.
     * @throws IllegalArgumentException if the date is in the future or the list of contacts is empty
     * @throws NullPointerException is one of the params is missing
     * Also throws and illegal argument runtime exception if a meeting with the same date/time and contacts already exists
     */
    @Override
    public void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text) {
        int id = 0;
        Calendar now = Calendar.getInstance();
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
                    throw new IllegalArgumentException("Contact " + curr.getName() + " does not exist in records");
                }
            }
        }
        /**
         * Check that the same id does not exist already-if yes, regenerate ID
         */
        do {
            id = generateRandomID();
        }
        while (meetingIdExists(id));

        Meeting newPastMeeting = new PastMeetingImpl(id, date, contacts, text);
        //check if the meeting is not a duplicate
        //a meeting is considered duplicate if it has the same contacts and date/time as an existing meeting
        if (!meetingExists(contacts, date)) {
            allMeetings.add(newPastMeeting);
        } else {
            //throws a Runtime exception if a meeting with the same date/time and contacts is in the list already
            throw new IllegalArgumentException("Meeting already exists!");
        }
    }

    /**
     * The implementation of this method assumes that a meeting which was added as a future meeting but
     * is now in the past can only be converted to a past meeting with this method being called
     * Implementation also assumes that where a past meeting was already added, this method can be used to
     * update its notes,and the notes will be appended to anything previously in place in the field
     * @param id the ID of the meeting
     * @param text notes to be added about the meeting.
     */
    @Override
    public void addMeetingNotes(int id, String text)  {
        Calendar now = Calendar.getInstance();
        Meeting convertedToPastMeeting = null;
        Meeting updatedPastMeeting = null;
        if (text == null) {
            throw new NullPointerException("Please specify notes for the meeting");
        } else if (!meetingIdExists(id)) {
            throw new IllegalArgumentException("Meeting does not exist");
        }
        for (Meeting curr : allMeetings) {
            if (curr.getId() == id) {
                if (curr.getDate().getTime().compareTo(now.getTime()) > 0) {
                    throw new IllegalStateException("This meeting is set for a date in the future");
                } else if (curr instanceof PastMeeting) {
                    //replace existing past meeting with a new identical past meeting with an addition to the notes
                    updatedPastMeeting = new PastMeetingImpl(curr.getId(), curr.getDate(), curr.getContacts(), ((PastMeeting) curr).getNotes()+" "+text);
                    allMeetings.add(updatedPastMeeting);
                    allMeetings.remove(curr);
                } else if (curr instanceof FutureMeeting) {
                    //replace existing future meeting with an identical past meeting, remove future meeting from records
                    convertedToPastMeeting = new PastMeetingImpl(curr.getId(), curr.getDate(), curr.getContacts(), text);
                    allMeetings.remove(curr);
                    allMeetings.add(convertedToPastMeeting);
                }
            }
        }
    }

    /**
     * Method to add a new contact to the list
     * Contact deemed unique based on ID only, as duplicate name and/or notes are possible
     * @param name the name of the contact.
     * @param notes notes to be added about the contact.
     */
    @Override
    public void addNewContact(String name, String notes) {
        int id = 0;
        //regenerate id until one that does not exist already in contacts is provided
        do {
            id = generateRandomID();
        } while (contactIdExists(id));

        Contact newContact = new ContactImpl(id, name);
        if(notes==null){
            throw new NullPointerException("Notes cannot be null");
        }
        else{
            newContact.addNotes(notes);
        }
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
            throw new IllegalArgumentException("One of the IDs provided is for a non-existent contact");
        }

        return contacts;
    }

    @Override
    public Set<Contact> getContacts(String name)  {
        Set<Contact> contactsContainingName = new HashSet<Contact>();
        if (name == null) {
            throw new NullPointerException("Name provided is not valid");
        } else {
            for (Contact curr : allContacts) {
                if (curr.getName().contains(name)) {
                    contactsContainingName.add(curr);
                }
            }
        }
        return contactsContainingName;
    }

    /**
     *  Writes the contents of the contact manager collections to a file
     */

    @Override
    public void flush() {
        encodeFile();
    }

    /**
     *  generates a random integer value to be used as meeting or contact ID
     * @return random ID
     */
    private int generateRandomID() {
        return (int) Math.abs(Math.random() * Integer.MAX_VALUE);
    }

    /**
     * Determines if a meeting to be added already exists
     * @param contacts
     * @param date
     * @return true or false depending on whether the meeting exists
     */
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
    /**
     * Determines if a meeting to be added already exists
     * @param id
     * @return true or false depending on whether the ID exists
     */
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
    /**
     * Determines if a contact to be added already exists
     * @param id
     * @return true or false depending on whether the ID exists
     */
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

    /**
     * Decoding the xml file back into the meetings and contacts objects
     */

    @SuppressWarnings("unchecked")
    private void decodeFile() {
        XMLDecoder decode = null;
        try {
            decode = new XMLDecoder(new BufferedInputStream(new FileInputStream("."+File.separator+FILENAME)));
            allMeetings = (List<Meeting>) decode.readObject();
            allContacts = (Set<Contact>) decode.readObject();

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        finally {
            if(decode!=null){
                decode.close();
            }
        }
    }

    /**
     * Encoding the data from the meetings list and the contacts set collections to an xml file
     * not checking if the file exists here as this check was made previously as part of the program launch
     */

    private void encodeFile() {
        XMLEncoder encode = null;
        try {
            encode = new XMLEncoder(new BufferedOutputStream(new FileOutputStream("."+File.separator+FILENAME)));
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
    /*
    Getters and setters created as serializable implemented
    Not part of the interface
     */

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

}
