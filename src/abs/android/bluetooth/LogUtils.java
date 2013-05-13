package abs.android.bluetooth;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.os.Environment;
import android.util.Log;

public class LogUtils{
	private static boolean DEBUG = true;
	
	public static void d(String TAG, String msg){
		if (DEBUG)Log.d(TAG, msg);
	}
	
	public static void e(String TAG, String msg){
		if (DEBUG)Log.e(TAG, msg);
	}
	
	public static void v(String TAG, String msg){
		if (DEBUG)Log.v(TAG, msg);
	}
	
	public static void i(String TAG, String msg){
		if (DEBUG)Log.i(TAG, msg);
	}
	
	private static final SimpleDateFormat sdf_24 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static void writerLog(String log){   
        try{
        	if (true){
        		return;
        	}
        	String filepath = Environment.getExternalStorageDirectory()+ "/DMC_TEST/";
        	File file = new File(filepath);
        	if (file != null && !file.exists()){
        		boolean ret = file.mkdirs();
        	}
        	
        	filepath = Environment.getExternalStorageDirectory()+ "/DMC_TEST/log.txt";
        	file = new File(filepath);
        	if (file != null && !file.exists()){
        		boolean ret = file.createNewFile();
        	}
        	
            FileWriter info = new FileWriter(file, true);
            PrintWriter printWriter = new PrintWriter(info, true);
            printWriter.println();
            printWriter.println("--------------------------------------------------------------------------");
            printWriter.println("log time: " + sdf_24.format(Calendar.getInstance().getTime()));
            printWriter.print(log);
            info.close();
        }catch (Exception e){
        	e.printStackTrace();
            return;
        } 
    }
}