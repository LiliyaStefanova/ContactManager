/**
 * Created with IntelliJ IDEA.
 * User: liliya
 * Date: 11/02/14
 * Time: 15:01
 * To change this template use File | Settings | File Templates.
 */
public class ContactImpl implements Contact {

    private int contactID;
    private String contactName;
    private String contactNotes;

    public ContactImpl(int id, String name, String notes){
        if(name==null){
            throw new NullPointerException("Name cannot be null");
        }
        else if(notes==null){
            throw new NullPointerException("Notes cannot be null");
        }
        this.contactID=id;
        this.contactName=name;
        this.contactNotes=notes;
    }

    @Override
    public int getId() {

        return this.contactID;
    }

    @Override
    public String getName() {

        return this.contactName;
    }

    @Override
    public String getNotes() {

        return this.contactNotes;
    }

    @Override
    public void addNotes(String note) {
        //refactoring needed here-overriding existing notes scenario
        this.contactNotes=note;
    }
}
