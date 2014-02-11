/**
 * Created with IntelliJ IDEA.
 * User: liliya
 * Date: 11/02/14
 * Time: 12:45
 * To change this template use File | Settings | File Templates.
 */
/**
 * A meeting that was held in the past.
 *
 * It includes your notes about what happened and what was agreed.
 */

public interface PastMeeting extends Meeting {

    /**
     * Returns the notes from the meeting.
     *
     * If there are no notes, the empty string is returned.
     *
     * @return the notes from the meeting.
     */
    String getNotes();
}
