
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
    Behavior behavior;
    
    Sprite inheritedSprite1;
    Behavior inheritedBehavior1;
    Looks inheritedLooks1;
    
    Sprite inheritedSprite2;
    Behavior inheritedBehavior2;
    Looks inheritedLooks2;
    
    public Pet(byte[] data, String name, boolean isPet)
    {
        petData = data;
        this.name = name;
        this.isPet = isPet;
    }
    
    public void generateInheritedData(boolean allData) throws UnsupportedEncodingException
    {
        byte[] pfmstr = "p.f.magicpetzIII".getBytes("ASCII");
        // find the 3rd PFM string
        int start = Helper.findSection(petData, 0, pfmstr, 3);
        start++; // account for the null terminator
        
        
        int allele_size = 23;
        inheritedSprite1 = new Sprite(petData, start, allData);
        start = (start + 4) + (41 * allele_size); // 41 is the number of alleles
        
        inheritedBehavior1 = new Behavior(petData, start);
        start = (start + 4) + (22 * allele_size); // 22 is the number of alleles
        
        allele_size = 15;
        inheritedLooks1 = new Looks(petData, start, allData);
        start = (start + 4) + (25 * allele_size); // 25 is the number of alleles
        
        allele_size = 23;
        inheritedSprite2 = new Sprite(petData, start, allData);
        start = (start + 4) + (41 * allele_size); // 41 is the number of alleles
        
        inheritedBehavior2 = new Behavior(petData, start);
        start = (start + 4) + (22 * allele_size); // 22 is the number of alleles
        
        allele_size = 15;
        inheritedLooks2 = new Looks(petData, start, allData);
    }
    
    public void generatePersonalityData(boolean allData) throws UnsupportedEncodingException
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
        
        
        sprite = new Sprite(petData, start, allData);
        start = (start + 4) + (41 * 23); // 41 is the number of alleles
        
        behavior = new Behavior(petData, start);
    }
}
