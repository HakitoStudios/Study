package hakito.autosim.logic;

import hakito.autosim.views.ControllsFragment;

/**
 * Created by Oleg on 12-Nov-15.
 */
public interface MessageListener
{
    void show(String text, ControllsFragment.MessageType type);
}
