package com.practice1;
import java.io.*;
import java.security.MessageDigest;

public class Main {
    public static void main(String[] args) {
        Dictionary myBooks = new Dictionary();
        int a = myBooks.getSize();
        String asentance[] = new String[a];// array to hold all sentences
        String hashList[] = new String[a];// array to hold hashes of sentences
        String nsentence[] = new String[a];// array to hold filtered sentences
        int number = 0;
        int out1 = 0;
        int out2 = 0;
        int ns = 0;
        int s = 0;
        String s1 = "";

        boolean result;
        for (int i = 0; i < a; i++) {
            asentance[i] = myBooks.getWord(i); // calls class
            if (asentance[i].matches(".(\\S+\s){1,9}\\S+")) //regex to count amount of whitespace and nonwhitespaces that are under ten
            {
                ns += 1; //adds one for every sentence that fits the regex
            }
        }
        nsentence = new String[ns];// new array with the size of sentences that are under equal or under 10 words
        for (int i = 0; i < a; i++) {
            if (asentance[i].matches(".(\\S+\s){1,9}\\S+"))
            {
                nsentence[s] = asentance[i].substring(1); // removes empty whitespace at the beginning of sentences
                s++;
            }
        }
        for (int i = 0; i < ns; i++) {
            hashList[i] = sha256(nsentence[i]); //converts sentences to hash by calling the sha method
        }
        for (int i = 0; i < ns; i++) {
            for (int j = 0; j < ns; j++) {
                int sim = compare(hashList[i], hashList[j]); //calls method that compares the sentences converted to hash and stores in variable

                // calls the compare method, if true it outputs the highest value  by outputing anything higher or equal to the last highest value
                // and makes sure there under 64 also ignores sentences that dont start with caps
                if (sim >= number && sim < 63 && Character.isUpperCase(nsentence[i].charAt(0)) &&  Character.isUpperCase(nsentence[j].charAt(0)))
                {
                    number = sim; //stores the value of identical Strings
                    out1 = i;//stores i value in for loop
                    out2 = j;//stores i value in for loop
                }
            }
        }
        System.out.println(nsentence[out1]); //prints the sentences with the highest similarity
        System.out.println(hashList[out1]);  //prints the hashes with the highest similarity
        System.out.println("");
        System.out.println(nsentence[out2]);//prints the sentences with the highest similarity
        System.out.println(hashList[out2]); //prints the hashes with the highest similarity
        System.out.println("");
        System.out.println(number); // prints the amount of similar Strings
    }
    public static int compare(String hash1, String hash2) {
        int count = 0;//holds the values of identical Strings
        for (int i = 0; i < 64; i++) {
            if (hash1.charAt(i) == hash2.charAt(i)) //checks if the Strings are identical and also in the same position
            {
                count++;// true it counts how many by add each time its found an identical String
            }
        }
        return count; //returns the value of identical Strings
    }
    public static String sha256(String input) {
        try {
            MessageDigest mDigest = MessageDigest.getInstance("SHA-256");
            byte[] salt = "CS210+".getBytes("UTF-8");
            mDigest.update(salt);
            byte[] data = mDigest.digest(input.getBytes("UTF-8"));
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < data.length; i++) {
                sb.append(Integer.toString((data[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (Exception e) {
            return (e.toString());
        }
    }

    public static class Dictionary {

        private String input[];

        public Dictionary() {
            input = load("/Users/mohamedibrahim/Desktop/Ichi.txt");
        }

        public int getSize() {
            return input.length;
        }

        public String getWord(int n) {
            return input[n];
        }

        private String[] load(String file) {
            File aFile = new File(file);
            StringBuffer contents = new StringBuffer();
            BufferedReader input = null;
            try {
                input = new BufferedReader(new FileReader(aFile));
                String line = null;
                int i = 0;
                while ((line = input.readLine()) != null) {
                    contents.append(line);
                    i++;
                    contents.append(System.getProperty("line.separator"));
                }
            } catch (FileNotFoundException ex) {
                System.out.println("Can't find the file - are you sure the file is in this location: " + file);
                ex.printStackTrace();
            } catch (IOException ex) {
                System.out.println("Input output exception while processing file");
                ex.printStackTrace();
            } finally {
                try {
                    if (input != null) {
                        input.close();
                    }
                } catch (IOException ex) {
                    System.out.println("Input output exception while processing file");
                    ex.printStackTrace();
                }
            }
            String[] array = contents.toString().split("(?<=[.!?])"); // allows you to split sentences while including the fullstop, question mark and exclamation mark
            for (String s : array) {
                s.trim();
            }
            return array;
        }
    }
}
