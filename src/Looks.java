/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Alexis
 */
public class Looks
{    
    // 25 pieces of data
    String[] headers = {  "-Unknown",
                            "Primary Breed",
                            "Default Scale",
                            "Ears",
                            "Head",
                            "Whiskers",
                            "Feet",
                            "Legs",
                            "Tail",
                            "Body",
                            "Coat",
                            "Tongue",
                            "Eye Color", //12
                            "Lid Color", //13
                            "Fur Color 1", //14
                            "Fur Color 2",
                            "Fur Color 3",
                            "Fur Color 4",
                            "Fur Color 5", //18
                            "Marking Factor",
                            "Spot Factor",
                            "Marking 1",
                            "Marking 2",
                            "Leg Extension",
                            "Body Extension"
            };
    
    
    // set by constructor
    private byte[] petData;
    private int start;
    
    boolean allData;
    Allele[] data;
    
    public Looks(byte[] petData, int start, boolean allData)
    {
        this.petData = petData;
        this.start = start;
        this.allData = allData;
    
        int n = Helper.convertByteArrayToInt32(petData, start);
        //System.out.println("Looks: " + n);
        
        if(n != 25) // check to see if N is not equal to then I should abort mission
        {
            System.out.println("Problem in Looks: " + n);
        }
        
        data = new Allele[n];
        
        int allele_size = 15;
        
        for(int col = 0; col < n; col++)
        {
            int pos = (start + 4) + (col * allele_size);
            
            if(headers[col].charAt(0) != '-') // if there's no hyphen, we don't skip it
            {
                data[col] = new Allele(headers[col], pos, petData, false);
                
                data[col].text = data[col].center + ""; // default unless otherwise stated below
                
                
                // time to set some proper data
                if(data[col].range != 0) // then the center is a breed id
                {
                    int index = data[col].center - 100; // use this in the breed array
                    
                    if(0 <= index && index < breedIds.length) // check if the index is in the correct bounds
                    {
                        data[col].text = breedIds[index];
                    }
                }
                else // center is a wildcard of things
                {
                    // use the column variable refer to the variable headers
                    if(col == 12) // eye color
                    {
                        data[col].text = eyecolors[data[col].center];
                    }
                    else if(col == 13) // eyelid color
                    {
                        data[col].text = eyelidColors[data[col].center];
                    }
                    else if(14 <= col && col <= 18) // coat colors
                    {
                        data[col].text = coatColors[data[col].center];
                    }
                    // else everything else is an integer value
                }
            }
            else if(allData)
            {
                data[col] = new Allele(headers[col].substring(1), pos, petData, false);
            }
            // else its just null
        }
    }
    
    public String generateRowOutput(String delim)
    {
        String row = "";
        
        for(Allele a : data)
        {
            if(a != null)
            {
                row += a.output(delim);
            }
        }
        
        return row;
    }
    
    public String generateHeaderRow(String delim)
    {
        String row = "";
        
        for(int col = 0; col < headers.length; col++)
        {
            String header = headers[col];
            
            if(header.charAt(0) != '-')
            {
                if(data[col].isBitmask == false) // then there are two columns
                {
                    row += header + " Center" + delim + header + " Range" + delim;
                }
                else
                {
                    row += header + delim;
                }
            }
        }
        
        return row;
    }

    String[] breedIds = {
            "B+W Shorthair",
            "Calico",
            "Orange Shorthair",
            "Persian",
            "Siamese",
            "Maine Coon",
            "Tabby",
            "Chinchilla Persian",
            "Alley Cat",
            "Russian Blue",
            "Bulldog",
            "Chihuahua",
            "Great Dane",
            "Mutt",
            "Scottie",
            "Dalmatian",
            "Poodle",
            "Sheepdog",
            "Dachshund",
            "Labrador",
            "Pig",
            "Bunny",
            "Japanese Bobtail",
            "Scottish Fold",
            "Unknown",
            "Jack Russell",
            "Golden Retriever",
            "Papillon",
            "Honey Bear",
            "Egyptian Mau",
            "Desert Lynx",
            "German Shepherd",
            "Pug"
        };
        
        String[] eyecolors = {
            "pink (75)",
            "gray (20)",
            "gray (21)",
            "gray (22)",
            "gray (23)",
            "gray (24)",
            "gray (25)",
            "gray (26)",
            "gray (27)",
            "gray (28)",
            "gray (29)",
            "black (244)",
            "blue (171)",
            "blue (172)",
            "silver-blue (173)",
            "silver-blue (174)",
            "blue (175)",
            "blue (176)",
            "blue (177)",
            "blue (178)",
            "teal (6)",
            "steel (110)",
            "steel (111)",
            "steel (112)",
            "steel (113)",
            "steel (114)",
            "steel (115)",
            "steel (116)",
            "steel (117)",
            "steel (118)",
            "steel (119)",
            "green (130)",
            "green (131)",
            "green (132)",
            "green (133)",
            "green (134)",
            "green (135)",
            "green (2)",
            "green (136)",
            "green (137)",
            "green (138)",
            "green (139)",
            "dark teal (203)",
            "dusty (120)",
            "dusty (121)",
            "dusty (122)",
            "dusty (123)",
            "dusty (124)",
            "dusty (125)",
            "dusty (126)",
            "dusty (127)",
            "dusty (128)",
            "dusty (129)",
            "gold (100)",
            "gold (101)",
            "gold (102)",
            "gold (103)",
            "gold (104)",
            "golden brown (105)",
            "golden brown (106)",
            "golden brown (107)",
            "golden brown (108)",
            "golden brown (109)",
            "orange (60)",
            "orange (63)",
            "orange (62)",
            "orange (63)",
            "orange (64)",
            "orange (65)",
            "orange (66)",
            "orange (67)",
            "orange (68)",
            "orange (69)",
            "cherry (50)",
            "cherry (51)",
            "cherry (52)",
            "cherry (53)",
            "cherry (54)",
            "cherry (55)",
            "cherry (56)",
            "cherry (57)",
            "cherry (58)",
            "cherry (59)",
            "brown (90)",
            "brown (91)",
            "brown (92)",
            "brown (93)",
            "brown (94)",
            "brown (95)",
            "brown (96)",
            "brown (97)",
            "brown (98)",
            "brown (99)"
        };
        
        String[] eyelidColors = {
            "white (18)",
            "white (19)",
            "gray (25)",
            "gray (28)",
            "gray (29)",
            "gray (30)",
            "black (35)",
            "black (36)",
            "black (39)",
            "cherry (59)",
            "orange (65)",
            "brown (91)",
            "brown (95)",
            "brown (99)",
            "gold (105)",
            "golden brown (109)",
            "dusty (125)",
            "black (244)"
        };
        
        String[] coatColors = {
            "white (15)",
            "gray (25)",
            "black (35)",
            "cream (45)",
            "cherry (55)",
            "orange (65)",
            "brown (95)",
            "gold (105)",
            "steel (115)",
            "dusty (125)"
    };
}
