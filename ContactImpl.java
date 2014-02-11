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
        this.contactID=id;
        this.contactName=name;
        this.contactNotes=notes;
    }

    @Override
    public int getId() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getName() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getNotes() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addNotes(String note) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
