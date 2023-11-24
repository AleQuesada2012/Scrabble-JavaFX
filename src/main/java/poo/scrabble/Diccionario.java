import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Diccionario {

    private Trie palabras;


    public Diccionario(){
        palabras = new Trie();
        insertWordsFromFile("DiccionarioLimpio.txt");
    }


    public void insertWordsFromFile(String filePath) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(filePath);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            String line;
            while ((line = br.readLine()) != null) {
                // Assuming words are separated by spaces in the file
                String[] words = line.split("\\s+");

                for (String word : words) {
                    // Remove non-alphabetic characters except special characters like "ñ"
                    word = word.replaceAll("[^a-zA-ZñÑ]", "");

                    // Insert the word into the trie
                    palabras.insert(word.toLowerCase()); // Converting to lowercase for case-insensitive matching
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean esvalida(String palabra){
        return palabras.search(palabra);
    }





}
