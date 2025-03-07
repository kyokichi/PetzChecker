import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class LNZ
{
    int start;
    byte[] petData;
    
    int pos;
    
    String LNZ;
    
    String face;
    
    private String usesPalette;
    private ArrayList<String> textures;
    
    
    
    public LNZ(byte[] petData, int startingPoint) throws UnsupportedEncodingException
    {
        start = startingPoint+1;
        this.petData = petData;
        
        byte[] nullTerm = {0x00};
        int end = Helper.findSection(petData, start, nullTerm, 1);
        int length = end - start;
        
        //System.out.println(length);
        
        LNZ = Helper.getStringFromLength(start, petData, length);
        
        //System.out.println(LNZ);
        
        
        /* HAS Palettes ? */
        usesPalette = getPiece("[Palette]");
        // could be null?
        
        
        /* Set the TEXTURE LIST */
        String textureSection = getPiece("[Texture List]");
        
        if(textureSection != null)
        {
            Scanner nz = new Scanner(textureSection);
            textures = new ArrayList<String>();
            
            while(nz.hasNextLine())
            {
                String line = nz.nextLine();
                
                String[] sline = line.split(" ");
                String texture = sline[0];

                if(!Helper.containsArray(PFM_Extracted_Textures, texture))
                {
                    textures.add(texture);
                }
            }
        }
        
    
        /* literally only does Head Shot right now */
        byte[] headshot = "[Head Shot]".getBytes("ASCII");
        pos = Helper.findSection(petData, start, headshot, 1);
        if(pos != -1)
        {
            pos++;

            int ending = Helper.findSection(petData, pos, "[".getBytes("ASCII"), 1);
            ending -= 2; // have to go backwards once for the [ and again for the \n
            int size = ending - pos;

            String petHeadshot = Helper.getStringFromLength(pos, petData, size);
            face = "Unknown";


            HashMap<String, String> breedFaces = new HashMap<String, String>();

            // catz
            breedFaces.put("0\n29\n1\n-1\n-28\n7\n9\n46 46\n-30 -30\n0 0", "Alley Cat");
            breedFaces.put("0\n51\n0\n0\n-51\n0\n-8\n29 29\n-15 -15\n0 0", "B+W Shorthair");
            breedFaces.put("0\n51\n0\n0\n-51\n0\n0\n29 29\n17 17\n0 0", "Calico");
            breedFaces.put("0\n46\n0\n-10\n-46\n7\n-7\n23 23\n22 22\n0 0", "Chinchilla Persian");
            breedFaces.put("0\n42\n0\n0\n-42\n-3\n10\n29 29\n17 17\n0 0", "Maine Coon");
            breedFaces.put("0\n51\n0\n0\n-51\n8\n5\n29 29\n17 17\n0 0", "Orange Shorthair");
            breedFaces.put("0\n51\n0\n0\n-51\n8\n-11\n38 38\n17 17\n0 0", "Persian");
            breedFaces.put("0\n19\n0\n4\n-21\n2\n17\n43 43\n23 23\n0 0", "Russian Blue");
            breedFaces.put("0\n51\n0\n0\n-51\n2\n16\n43 43\n-12 -12\n0 0", "Siamese");
            breedFaces.put("0\n51\n0\n0\n-51\n-3\n0\n29 29\n17 17\n0 0", "Tabby");

            // dogz
            breedFaces.put("0\n51\n0\n3\n-51\n10\n0\n46 46\n23 23\n0 0", "Bulldog");
            breedFaces.put("0\n51\n0\n3\n-51\n5\n21\n35 35\n23 23\n0 0", "Chihuahua");
            breedFaces.put("0\n49\n0\n3\n-49\n7\n13\n40 40\n20 20\n0 0", "Dachshund");
            breedFaces.put("0\n51\n0\n3\n-51\n5\n-11\n35 35\n23 23\n0 0", "Dalmatian");
            breedFaces.put("0\n51\n0\n3\n-51\n5\n8\n35 35\n23 23\n0 0", "Great Dane");
            //breedFaces.put("0\n51\n0\n3\n-50\n5\n13\n35 35\n23 23\n0 0", "Labrador");
            breedFaces.put("0\n51\n0\n3\n-50\n5\n13\n35 35\n23 23\n0 0", "Labrador / Mutt");
            breedFaces.put("0\n51\n0\n3\n-51\n1\n11\n50 50\n23 23\n0 0", "Poodle");
            breedFaces.put("0\n51\n0\n3\n-51\n10\n0\n35 35\n15 15\n0 0", "Scottie");
            breedFaces.put("0\n40\n0\n-3\n-40\n7\n7\n45 45\n31 31\n0 0", "Sheepdog");


            if(breedFaces.containsKey(petHeadshot))
            {
                face = breedFaces.get(petHeadshot);
            }
        }
        else
        {
            face = "Bunny"; // I noticed that Bunny has no Headshot
        }
    }
    
    private String getPiece(String section)
    {
        String result = null;
        
        int begin = LNZ.indexOf(section);
        
        if(begin != -1)
        {
            begin += section.length();
            int end = LNZ.substring(begin).indexOf("[");
            if(end != -1)
            {
                result = LNZ.substring(begin + 1, begin + end - 1);
            }
        }
        
        return result;
    }
    
    public String getTextures()
    {
        int n = textures.size();
        
        if(n == 0)
            return "None";
        
        String result = "("+n+") ";
        
        for(int i = 0; i < n; i++)
        {
            if(i > 0)
            {
                result += " | ";
            }
            
            result += textures.get(i);
        }
        
        return result;
    }
    
    String[] PFM_Extracted_Textures = {
            "\\art\\autobuild\\bunnybrown2.bmp",
            "\\art\\autobuild\\bunnygrey.bmp",
            "\\art\\autobuild\\bunnytan.bmp",
            "\\art\\autobuild\\cottonwhite.bmp",
            "\\art\\autobuild\\terrypink.bmp",
            "\\art\\textures\\cali2.bmp",
            "\\art\\textures\\cali5.bmp",
            "\\art\\textures\\cottongrey.bmp",
            "\\art\\textures\\cottonwhite.bmp",
            "\\art\\textures\\hair10.bmp",
            "\\art\\textures\\hair11.bmp",
            "\\art\\textures\\hair20.bmp",
            "\\art\\textures\\hair3.bmp",
            "\\art\\textures\\hair4.bmp",
            "\\art\\textures\\hair6.bmp",
            "\\art\\textures\\jowl1.bmp",
            "\\art\\textures\\jowl2.bmp",
            "\\art\\textures\\mottled2.bmp",
            "\\art\\textures\\mottled5.bmp",
            "\\art\\textures\\Plush.bmp",
            "\\art\\textures\\redrib.bmp",
            "\\art\\textures\\swtr7.bmp",
            "\\ptzfiles\\cat\\mc\\maine4.bmp",
            "\\ptzfiles\\cat\\mc\\maine5.bmp",
            "\\ptzfiles\\cat\\mc\\maine9.bmp",
            "\\ptzfiles\\cat\\ta\\stripe11.bmp",
            "\\ptzfiles\\cat\\ta\\stripe13.bmp",
            "\\ptzfiles\\cat\\ta\\stripe14.bmp",
            "\\ptzfiles\\cat\\ta\\stripe16.bmp",
            "\\ptzfiles\\cat\\ta\\stripe17.bmp",
            "\\ptzfiles\\cat\\ta\\stripe3.bmp",
            "\\ptzfiles\\dog\\pd\\skin1.bmp",
            "\\art\\textures\\ameoba.bmp",
            "\\art\\textures\\argile.bmp",
            "\\art\\textures\\bluerib.bmp",
            "\\art\\textures\\bluesocks.bmp",
            "\\art\\textures\\bunnybrown.bmp",
            "\\art\\textures\\bunnybrown2.bmp",
            "\\art\\textures\\bunnygrey.bmp",
            "\\art\\textures\\bunnytan.bmp",
            "\\art\\textures\\burlapgreen.bmp",
            "\\art\\textures\\camod.bmp",
            "\\art\\textures\\camoj.bmp",
            "\\art\\textures\\chainmail2.bmp",
            "\\art\\textures\\cotton.bmp",
            "\\art\\textures\\cotton2.bmp",
            "\\art\\textures\\cottongreen.bmp",
            "\\art\\textures\\cottonred.bmp",
            "\\art\\textures\\cow.bmp",
            "\\art\\textures\\denim3.bmp",
            "\\art\\textures\\denim4.bmp",
            "\\art\\textures\\dots2.bmp",
            "\\art\\textures\\elephant2.bmp",
            "\\art\\textures\\fleks3.bmp",
            "\\art\\textures\\flower.bmp",
            "\\art\\textures\\giraffe2.bmp",
            "\\art\\textures\\giraffe3.bmp",
            "\\art\\textures\\golf.bmp",
            "\\art\\textures\\golfhat.bmp",
            "\\art\\textures\\greenrib2.bmp",
            "\\art\\textures\\greysocks.bmp",
            "\\art\\textures\\hawaiian.bmp",
            "\\art\\textures\\hearts.bmp",
            "\\art\\textures\\helmet.bmp",
            "\\art\\textures\\hunter.bmp",
            "\\art\\textures\\hunter2.bmp",
            "\\art\\textures\\jewel.bmp",
            "\\art\\textures\\leather2.bmp",
            "\\art\\textures\\leopard.bmp",
            "\\art\\textures\\mexi03.bmp",
            "\\art\\textures\\pjduck.bmp",
            "\\art\\textures\\plaid5.bmp",
            "\\art\\textures\\plaidal.bmp",
            "\\art\\textures\\plaidblue.bmp",
            "\\art\\textures\\plaidred.bmp",
            "\\art\\textures\\polka.bmp",
            "\\art\\textures\\polka2.bmp",
            "\\art\\textures\\polkaplease.bmp",
            "\\art\\textures\\polkaplease4.bmp",
            "\\art\\textures\\purp.bmp",
            "\\art\\textures\\redsocks.bmp",
            "\\art\\textures\\ski2.bmp",
            "\\art\\textures\\stars.bmp",
            "\\art\\textures\\stars2.bmp",
            "\\art\\textures\\stars3.bmp",
            "\\art\\textures\\stitch.bmp",
            "\\art\\textures\\stripes.bmp",
            "\\art\\textures\\terrypink.bmp",
            "\\art\\textures\\tiedye2.bmp",
            "\\art\\textures\\tiedye4.bmp",
            "\\art\\textures\\tiger2.bmp",
            "\\art\\textures\\tights.bmp",
            "\\art\\textures\\tights2.bmp",
            "\\art\\textures\\turb.bmp",
            "\\art\\textures\\whiterib.bmp",
            "\\art\\textures\\wizard.bmp",
            "\\art\\textures\\yellowdiamond.bmp",
            "\\art\\textures\\zebra.bmp",
            "\\art\\textures\\zebra2.bmp",
            "\\art\\textures\\zigzag.bmp",
            "\\art\\textures\\egyptm03.bmp",
            "\\art\\textures\\honey1.bmp",
            "\\art\\textures\\lynx04.bmp",
            "\\art\\textures\\scott1.bmp"
        };
}
