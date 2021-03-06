package com.liliya.contactmanager;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Set;

public class PastMeetingImpl extends MeetingImpl implements PastMeeting, Serializable {

    private String meetingNotes;

    public PastMeetingImpl() {
    }

    public PastMeetingImpl(int id, Calendar date, Set<Contact> attendees, String text) {
        //using the MeetingImpl constructor
        super(id, date, attendees);
        this.meetingNotes = text;
        //no exceptions at constructor level-for meetings in the future
        //check will be made as part the ContactManager addNewPastMeeting()
    }

    public String getMeetingNotes() {
        return meetingNotes;
    }

    public void setMeetingNotes(String meetingNotes) {
        this.meetingNotes = meetingNotes;
    }

    @Override

    public String getNotes() {

        return this.meetingNotes;

    }
}
