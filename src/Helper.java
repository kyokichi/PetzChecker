/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Alexis
 */
public class Helper
{
    public static int convertByteArrayToInt32(byte[] bytes, int i)
    {
        return ((bytes[i+3] & 0xFF) << 24) |
                ((bytes[i+2] & 0xFF) << 16) |
                ((bytes[i+1] & 0xFF) << 8) |
                (bytes[i+0] & 0xFF);
    }
    
    
    
    public static int findSection(byte[] allBytes, int start, byte[] searched, int n)
    {
        int count = 0; // find the nth instance        
        int i = start;
        
        while(i < allBytes.length && count < n)
        {   
            if(allBytes[i] == searched[0])
            {
                if(checkEqual(searched, allBytes, i))
                {
                    count++;
                    i += searched.length; // set i to the next point after the string
                }
            }
            if(count != n)
            {
                i++;
            }
        }

        
        if(n == count) // we found the nth instance
        {
            return i;
        }
        else
        {
           System.out.println("Error: Did not find section");
           
           return -1;
        }
    }
    
    
    
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
}
