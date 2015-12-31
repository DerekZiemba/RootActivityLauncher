package localhost.derekziemba;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.io.*;
import java.util.concurrent.TimeoutException;

import com.stericson.RootShell.RootShell;
import com.stericson.RootShell.exceptions.RootDeniedException;
import com.stericson.RootShell.execution.Command;
import com.stericson.RootShell.execution.Shell;

import de.szalkowski.activitylauncher.R;

/**
 * Created by Derek on 12/30/2015.
 */
public class RootActivityLauncher {

    public static boolean hasRootAccess(){
        return RootShell.isRootAvailable() && RootShell.isAccessGiven();
    }

    public static Intent getActivityIntent(ComponentName activity) {
        Intent intent = new Intent();
        intent.setComponent(activity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    public static void launchActivity(Context context, ComponentName activity) {
        Intent intent = getActivityIntent(activity);

        Toast.makeText(context, String.format(context.getText(R.string.starting_activity).toString(), activity.flattenToShortString()), Toast.LENGTH_LONG).show();
        try {
            context.startActivity(intent);
        }
        catch(Exception e) {
           // Toast.makeText(context, context.getText(R.string.error).toString() + ": " + e.toString() + "\n Attempting with root", Toast.LENGTH_LONG).show();
            launchActivityRoot(context, activity);
        }

    }

    public static void launchActivityRoot(Context context, ComponentName activity) {
        if(hasRootAccess()){
            String name = activity.flattenToShortString();
            String sCommand = "am start -a android.intent.action.MAIN -n " + name;

            try {
                Shell shell = RootShell.getShell(true);
                Command cmd = new Command(0,sCommand);
                shell.add(cmd);
                shell.close();

            } catch (IOException e) {
                Toast.makeText(context, "IOException: " + e.toString(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            } catch (TimeoutException e) {
                Toast.makeText(context, "Root Timeout", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            } catch (RootDeniedException e) {
                Toast.makeText(context, "Root Denied", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        } else{
            Toast.makeText(context, "Root Unavailable/Denied", Toast.LENGTH_LONG).show();
        }
    }



}
