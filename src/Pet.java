
import java.io.UnsupportedEncodingException;

public class Pet
{
    enum Version {
        petz2, petz3, petz4, petz5, unknown
    }
    
    enum Species {
        dog, cat, both, unknown
    }
    
    byte[] petData;
    String name;
    boolean isPet;
    
    Version ver;
    Species species;
    
    Sprite sprite;
    
    public Pet(byte[] data, String name, boolean isPet)
    {
        petData = data;
        this.name = name;
        this.isPet = isPet;
    }
    
    public void generatePersonalityData(boolean toTable, String delim, boolean hasNotes) throws UnsupportedEncodingException
    {
        int start;
        if(isPet)
        {
            // searched for the byte which should be p.f.magicpetzIII
            //byte[] searched = {0x70, 0x2E, 0x66, 0x2E, 0x6D, 0x61, 0x67, 0x69, 0x63, //p.f.magic
            //0x70, 0x65, 0x74, 0x7A, 0x49, 0x49, 0x49}; //petzIII
            byte[] pfmstr = "p.f.magicpetzIII".getBytes("ASCII");
            // found the 4th  occurance of the PFM string
            start = Helper.findSection(petData, 0, pfmstr, 4);
            start++; // account for the null terminator
        }
        else
        {
            byte[] pfmstr = "pfmagic".getBytes("ASCII");
            start = Helper.findSection(petData, 0, pfmstr, 4);
            // account for the null terminator
            start += 5;
            // plus something else so I should look that up
        }
        
        
        sprite = new Sprite(petData, start);
        
        // a lot of this should be passed in?
        sprite.read(toTable, delim, hasNotes);
    }
}
