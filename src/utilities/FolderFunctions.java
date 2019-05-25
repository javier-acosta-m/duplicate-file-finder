/*
 * @file    FolderFunctions 
 * @date    Jan 30, 2009
 * @author  JIAM Jan 30, 2009
 * @summary 
 * @notes
 */
package utilities;

//-Libraries
import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


/**
 * Supporting folder functions 
 */
public class FolderFunctions {

    /**
     * Delete directory
     * @param path The target directory
     * @return boolean indicating the success of the operation
     */
    public static boolean delete(File path) {
        if( path.exists() ) {
            File[] files = path.listFiles();
            for(int i=0; i<files.length; i++) {
                if(files[i].isDirectory()) {
                    delete(files[i]);
                } else
                    files[i].delete();
            }
        }
        return( path.delete() );
    }
    
    /**
     * Delete a file
     * @param _filepath The filename
     * @return boolean indicating the success of the operation
     */
    public static boolean delete(String _filepath) {
        if (_filepath != null){
            File f = new File(_filepath);
            return delete(f);
        }
        return true;
    }
        
    /**
     * Make Directories
     * @param _dir
     * @return operation success
     * @See File.mkdir()
     */
    public static boolean mkdir(String _dir){
        File dir = new File (_dir);
        return dir.mkdir();        
    }
    
    /**
     * Make Directories
     * @param _dir
     * @return operation success
     * @See File.mkdirs()
     */
    public static boolean mkdirs(String _dir){
        File dir = new File (_dir);
        return dir.mkdirs();        
    }
    
    /**
     * Creates a temporary folder
     * @return
     */
    public static File createTempFolder(){
        String tempDir = System.getenv("temp");        
        return new File(tempDir,StringFunctions.randomString(8));        
    }
        
    
    /**
     * Scan a Folder for and list all the files and directory under the folder
     * @param _directory File appointing to the entry level
     * @return List<File> containing all files & folders
     */
    private static void scanFolderR(File _directory, List<File> _output, boolean process_hidden){
        _output.addAll(Arrays.asList(_directory.listFiles()));
        File[] fileList = _directory.listFiles();
        for (File f: fileList){
            if (f.isDirectory()){
                if (f.isHidden() && !process_hidden){
                    continue;
                }
                scanFolderR(f,_output, process_hidden);
            }
        }        
    }

    /**
     * Scan a Folder for and list all the files and directory under the folder
     * @param _directory File appointing to the entry level
     * @return List<File> containing all files & folders
     */   
    public static List<File> scanFolder(File _directory, boolean hidden_files){
        LinkedList<File> listFiles = new LinkedList<File>();        
        scanFolderR(_directory, listFiles, hidden_files);
        return listFiles;
    }
    
}