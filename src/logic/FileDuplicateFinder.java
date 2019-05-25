package logic;

//-Libraries
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JProgressBar;
import utilities.FolderFunctions;

/**
 * Given a folder, it will find all duplicate files
 */
public class FileDuplicateFinder extends Thread {

    /*Fields*/
    private final File mSource;
    private volatile boolean mTerminated;
    private final boolean mProcessHiddenFiles;
    private final Task mTaskDone;
    private final JProgressBar mProgressBar;
    private final Map<String, List<String>> mFileLists = new HashMap<>();
    private MessageDigest mMessageDigest;
    
    //-Constant
    static final int DIRECT_READ_FILE_SIZE_LIMIT_BYTES = 512 * 1024 * 1024; //-512 MB 
            
    /**
     * Constructor
     * @param _source
     * @param process_hidden_files
     * @param _progressBar
     * @param _task_done 
     */
    public FileDuplicateFinder(File _source, boolean process_hidden_files, JProgressBar _progressBar, Task _task_done) {
        mTerminated = false;
        mProcessHiddenFiles = process_hidden_files;
        mSource = _source;
        mProgressBar = _progressBar;
        mTaskDone = _task_done;
    }

    /**
     * Terminate execution
     */
    public void terminate() {
        mTerminated = true;
    }

    /**
     * Returns map containing the has associates with the files
     * @return 'mFileLists'
     */
    public Map<String, List<String>> getFileLists(){
        return mFileLists;
    }
    
    /**
     * Run
     */
    @Override
    public void run() {
        try {
            mMessageDigest =  MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(FileDuplicateFinder.class.getName()).log(Level.SEVERE, null, ex);
            terminate();
            mTaskDone.execute();
            return;
        }
        
        //-Scannear archivos para processar
        LinkedList<File> listFiles = (LinkedList<File>) FolderFunctions.scanFolder(mSource, mProcessHiddenFiles);

        //-Setup user feedback
        setupProgress(listFiles.size());

        //-Process each file
        for (File sourceFile : listFiles) {
            //-Check for termination
            if (mTerminated) {
                break;
            }
            //-Increment progress
            incrementProgress();
            
            //-Check the file is not a directory
            if (!sourceFile.isDirectory()) {
                String hash;
                try {
                     hash = calculateHash(sourceFile);
                } catch (IOException ex) {
                    hash = ex.getMessage();
                    Logger.getLogger(FileDuplicateFinder.class.getName()).log(Level.SEVERE, null, ex);
                }
                //-Add the <hash, file> pair
                List<String> list = mFileLists.get(hash);
                if (list == null) {
                    list = new LinkedList<>();
                    mFileLists.put(hash, list);
                }
                list.add(sourceFile.getAbsolutePath());
            }
        }
        mTaskDone.execute();
    }

    /**
     * Setup the progress bar size
     * @param _size The maximum value of the progress bar 
     */
    private void setupProgress(int _size) {
        if (mProgressBar != null) {
            mProgressBar.setMaximum(_size);
            mProgressBar.setValue(0);
        }
    }

    /**
     * Advance progress in the progress bar
     */
    private void incrementProgress() {
        if (mProgressBar != null) {
            int value = mProgressBar.getValue();
            mProgressBar.setValue(++value);
        }
    }

  /**
     * Calculate SHA-1
     * @param sourceFile The source file
     * @return String containing SHA-1 hash
     * @throws java.io.IOException
     */
    public String calculateHash(File sourceFile) throws IOException{
        String hash;
        FileInputStream fileInputStream = new FileInputStream(sourceFile);
        mMessageDigest.reset();
        
        //-Check the length to select the processing
        if (sourceFile.length() > DIRECT_READ_FILE_SIZE_LIMIT_BYTES) {
            int buffSize = 1 * 1024 * 1024; //-MBB buffer
            byte[] buffer = new byte[buffSize];
            long read = 0;
            //-Calculate the hash of the whole file for the test
           long offset = sourceFile.length();
            int unitsize;
            while (read < offset) {
                unitsize = (int) (((offset - read) >= buffSize) ? buffSize : (offset - read));
                fileInputStream.read(buffer, 0, unitsize);
                mMessageDigest.update(buffer, 0, unitsize);
                read += unitsize;
            }
            hash = new BigInteger(1, mMessageDigest.digest()).toString(16);
        } else {
            byte[] data;
            data = new byte[(int) sourceFile.length()];
            fileInputStream.read(data);
            hash = new BigInteger(1, mMessageDigest.digest(data)).toString(16);
        }
        //-Close the input stream
        fileInputStream.close();
        return hash;
    }

}
