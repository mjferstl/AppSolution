package mfdevelopement.appsolution.helper;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class InternalStorageFileManager {

    // see examples at https://developer.android.com/training/data-storage/files/internal
    private Context context;
    private final String LOG_TAG = "InternalStorageFileMana";

    public InternalStorageFileManager(Context context) {
        this.context = context;
    }

    public void writeContentToFile(String filename, String fileContent) {

        FileOutputStream outputStream;

        try {
            Log.d(LOG_TAG,"satrted to write the file " + filename);
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(outputStream);
            outputWriter.write(fileContent);
            outputWriter.close();
            Log.d(LOG_TAG,"writing the file " + filename + " successfully");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOG_TAG,"writing the file " + filename + " produced an Exception");
        }
    }

    public String readFile(String filename) {

        String content;

        try {
            FileInputStream fileInputStream = context.openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            fileInputStream.close();
            bufferedReader.close();
            isr.close();

            content = sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(LOG_TAG,"FileNotFoundException: The file " + filename + " does not exist. Returning empty string as content");
            content = "";
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOG_TAG,"IOException: while reading " + filename + ". Returning empty string instead");
            content = "";
        }

        return content;
    }
}
