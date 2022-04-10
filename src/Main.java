import java.io.*;

public class Main
{
    /**
     * Checks if a is contained in b using the specified starting point in b
     * only checks as many bytes as a.length
     * @param a what I'm searching for in b
     * @param b where I'm searching
     * @param start starting index in b to search for a
     * @return true if a is contained in b at the starting point, false otherwise
     */
    private static boolean checkEqual(byte[] a, byte[] b, int start)
    {
        if(b.length - start < a.length)
        {
            return false;
        }
        
        for(int i = 0; i < a.length; i++)
        {
            if(a[i] != b[i + start])
            {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Creates a string of the preferences based on the 4 bytes passed in
     * @param petPref the 4 bytes read from a pet file
     * @param adjNames all the possible adjectives
     * @return a string formatted how I'd like it to appear
     */
    private static String bytesToNames(byte[] petPref, String[] adjNames)
    {
        String result = "";
        for(int outterByte = 0; outterByte < petPref.length; outterByte++)
        {
            for(int b = 0; b < 8; b++)
            {
                int index = b + (outterByte * 8);
                if( ((petPref[outterByte] & (1 << b)) != 0) && (index < adjNames.length) )
                {
                    result += adjNames[index] + " ";
                    // result += "("+index+") "; REMOVED IN RELEASE
                }
            }
        }
        
        return result;
    }
    
    
    public static void main(String[] args) throws IOException
    {
        // for each file in the directory
        // check the extension, should be .pet for now
        // then process the file
        
        String path = ".";
        File dir = new File(path);
        File[] dirList = dir.listFiles();
        
        //output to a file as tab delimited txt
        FileWriter fw = new FileWriter("petzfavorites.txt");
        
        if(dirList != null)
        {
            for(File f : dirList)
            {
                String name = f.getName();
                if(name.length() > 4 && name.substring(name.length()-4).equals(".pet"))
                {
                    fw.write(processFile(name) + "\n");
                }
            }
        }
        
        fw.close();
    }
    
    
    public static String processFile(String inputFile) throws FileNotFoundException, IOException 
    {
        //next we need to find the name of the file
        //substring to remove the last 4 characters .pet
        String filename = inputFile.substring(0, inputFile.length()-4);
        
        String result = filename + "\t";;
        
        File f = new File(inputFile);
        InputStream inputStream = new FileInputStream(f);

        long fileSize = f.length();
        byte[] allBytes = new byte[(int)fileSize];
        inputStream.read(allBytes);

        // searched for the byte which should be p.f.magicpetzIII
        byte[] searched = {0x70, 0x2E, 0x66, 0x2E, 0x6D, 0x61, 0x67, 0x69, 0x63, //p.f.magic
            0x70, 0x65, 0x74, 0x7A, 0x49, 0x49, 0x49}; //petzIII
        
        int i = 0;
        int found = 0;
        while(i < allBytes.length && found < 4)
        {
            if(allBytes[i] == searched[0])
            {
                if(checkEqual(searched, allBytes, i))
                {
                    found++;
                }
            }
            if(found < 4)
            {
                i++;
            }
        }

        
        // found the 4th  occurance of the PFM string
        if(found == 4)
        {
            i += searched.length; // set i to the next point
            i += 5; // to the first chunk of this block
            i += (23 * 6); // skip foward 6 chunks
            
            
            // favorite color
            byte[] color = new byte[4];
            for(int x = 0; x < color.length && i < allBytes.length; x++)
            {
                color[x] = allBytes[i];
                // result += String.format("0x%x ",allBytes[i]);   REMOVED IN RELEASE
                i++;
            }
            
            result += "\t";
            
            String[] allColors = {"white", "black", "red", "green", "yellow", "blue", "purple", "pink", // 7
                                  "orange", "brown", "gray", "clear" }; // 11
            
            result += bytesToNames(color, allColors);
            
            result += "\t";
            
            // update i
            i+= 19;
            
            byte[] flavor = new byte[4];
            for(int x = 0; x < flavor.length && i < allBytes.length; x++)
            {
                flavor[x] = allBytes[i];
                // result += String.format("0x%x ",allBytes[i]); REMOVED IN RELEASE
                i++;
            }
            
            result += "\t";

            String[] allFlavors = {"chicken", "beef", "fish", "wet", "milk", "candy", "catnip", "cheese", // 7
                                    "shiny", "chewy", "soft", "bone", "wood", "metal", "water", "rocks", // 15
                                    "???", "hair", "???", "???", "???", "???", "???", "chemicals", // 23
                                    "rubbish", "???", "flea", "plants", "cardboard" }; //29
            
            result += bytesToNames(flavor, allFlavors);
        }
        else
        {
           result += "ERROR";
        }
        
        inputStream.close();
        
        return result;
    }
}