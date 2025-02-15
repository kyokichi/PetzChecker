/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Alexis
 */
public class Behavior
{
    // 22 pieces of data
    String[] headers = {"Liveliness", "Playfulness", "Independence", "Confidence",
        "Naughtiness", "Acrobaticness", "Patience", "Kindness", "Nurturing", "Finickiness",
        "Intelligence", "Messiness", "Quirkiness", "Insanity", "Constitution", "Metabolism",
        "Dogginess", "LoveDestiny", "Fertility", "LoveLoyalty", "Libido", "OffspringSex" };
    
    
    // set by constructor
    private byte[] petData;
    private int start;
    
    Allele[] data;
    
    public Behavior(byte[] petData, int start)
    {
        this.petData = petData;
        this.start = start;
    
        int n = Helper.convertByteArrayToInt32(petData, start);
        
        if(n != 22) // check to see if N is not equal to then I should abort mission
        {
            System.out.println("Problem in behavior: " + n);
        }
        
        data = new Allele[n];
        
        for(int col = 0; col < n; col++)
        {
            int pos = (start + 4) + (col * 23);
            
            data[col] = new Allele(headers[col], pos, petData);
        }
    }
    
    public String generateRowOutput(String delim)
    {
        String row = "";
        
        for(Allele a : data)
        {
            row += a.output(delim);
        }
        
        return row;
    }
    
    public String generateHeaderRow(String delim)
    {
        String row = "";
        
        for(int col = 0; col < headers.length; col++)
        {
            String header = headers[col];
            
            if(data[col].isBitmask == false) // then there are two columns
            {
                row += header + " Center" + delim + header + " Range" + delim;
            }
            else
            {
                row += header + delim;
            }
        }
        
        return row;
    }
}
