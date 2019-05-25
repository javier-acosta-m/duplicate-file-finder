package utilities;

//-Libraries
import java.util.ArrayList;
import java.util.Random;

/**
 * Ancillary String functions
 */
public class StringFunctions {
    
    /*Constants*/
    public static final byte PAD_LEFT  = -1;
    public static final byte PAD_RIGHT =  1;

    /**
     * Get Random string of
     * @param _length Number of characters in the return string
     * @return
     */
    public static String randomString(int _length) {
        StringBuilder buffer = new StringBuilder();
        Random random = new Random();
        char[] chars = new char[]{'a', 'b', 'c', 'd', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        for (int i = 0; i < _length; i++) {
            buffer.append(chars[random.nextInt(chars.length)]);
        }
        return buffer.toString();
    }

    /**
     * Converts String[] to List<String>
     * @param _list The String[] list
     * @return List<Strig> containing the '_list'
     */
    public static ArrayList<String> toList(String[] _list) {
        ArrayList<String> res = new ArrayList<>();
        if (_list != null) {
            for (int i = 0; i < _list.length; ++i) {
                res.add(_list[i]);
            }
        }
        return res;
    }

    /**
     * Pad a string on the right (spaces)
     * @param s
     * @param n
     * @return
     */
    public static String padRight(String s, int n) {
        return String.format("%1$-" + n + "s", s);
    }

    /**
     * Pad a string on the left (spaces)
     * @param s
     * @param n
     * @return
     */
    public static String padLeft(String s, int n) {
        return String.format("%1$#" + n + "s", s);
    }

    /**
     * Pad a string 
     * @param _source Input string
     * @param _size Size of the string
     * @param _char Character tp pad with
     * @param _padding (-1 = left & 1 = right)
     * @return Padded string
     */
    public static String padding(String _source, int _size, char _char, byte _padding) {
        StringBuilder str = new StringBuilder(_source);
        int strLength = str.length();
        if (_size > 0 && _size > strLength) {
            for (int i = 0; i <= _size; i++) {
                switch (_padding) {
                    case -1:
                        if (i < _size - strLength) {
                            str.insert(0, _char);
                        }
                        break;
                    case 1:
                        if (i > strLength) {
                            str.append(_char);
                        }
                        break;
                }
            }
        }
        return str.toString();
    }
    
    /**
     * Pad a string with a given chacater to become multiple of n
     * @param _source
     * @param _multipleOf
     * @param _character
     * @return
     */
    public static String pad(String _source, int _multipleOf, char _character){
        String padding = "";        
        int numChars = (_multipleOf - (_source.length() % _multipleOf));
        for (int i=0; i< numChars; ++i)
            padding += _character;
        return _source + padding;        
    }
    
}