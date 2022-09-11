
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class sozluk2 {
    public class TrieNode {

    Map<Character, TrieNode> children;
    char c;
    boolean isWord;

    public TrieNode(char c) {
      this.c = c;
      children = new HashMap<>();
    }

    public TrieNode() {
      children = new HashMap<>();
    }

    public void insert(String word) {
      if (word == null || word.isEmpty()) return;
      char firstChar = word.charAt(0);
      TrieNode child = children.get(firstChar);
      if (child == null) {
        child = new TrieNode(firstChar);
        children.put(firstChar, child);
      }

      if (word.length() > 1) child.insert(
        word.substring(1)
      ); else child.isWord = true;
    }
  }

  TrieNode root;

  public sozluk2(List<String> words) {
    root = new TrieNode();
    for (String word : words) root.insert(word);
  }

  public boolean find(String prefix, boolean exact) {
    TrieNode lastNode = root;
    for (char c : prefix.toCharArray()) {
      lastNode = lastNode.children.get(c);
      if (lastNode == null) return false;
    }
    return !exact || lastNode.isWord;
  }

  public boolean find(String prefix) {
    return find(prefix, false);
  }

  public void suggestHelper(
    TrieNode root,
    List<String> list,
    StringBuffer curr
  ) {
    if (root.isWord) {
      list.add(curr.toString());
    }

    if (root.children == null || root.children.isEmpty()) return;

    for (TrieNode child : root.children.values()) {
      suggestHelper(child, list, curr.append(child.c));
      curr.setLength(curr.length() - 1);
    }
  }

  public List<String> suggest(String prefix) {
    List<String> list = new ArrayList<>();
    TrieNode lastNode = root;
    StringBuffer curr = new StringBuffer();
    for (char c : prefix.toCharArray()) {
      lastNode = lastNode.children.get(c);
      if (lastNode == null) return list;
      curr.append(c);
    }
    suggestHelper(lastNode, list, curr);
    return list;
  }

  public static void main(String[] args) throws IOException {
    String filePath = "sozluk.txt";
    List<String> words = new ArrayList<String>();
    String line;
    BufferedReader reader = new BufferedReader(new FileReader(filePath));
    System.out.println("Sözlük Yükleniyor Lütfen Bekleyin...");
    while ((line = reader.readLine()) != null) {
      words.add(line);
    }
    sozluk2 trie = new sozluk2(words);
    System.out.println("Sözlük Yuklendi.");
    while (true) {
      Scanner input = new Scanner(System.in);
      System.out.println("Bir Kelime Yazıp Enter Tuşuna basın.");
      String aranan_kelime = input.nextLine();
      if (trie.suggest(aranan_kelime).size() > 0) {
        System.out.println("Olası Kelimeler:");
        for (String word : trie.suggest(aranan_kelime)) {
          System.out.println(word);
        }
      } else System.out.println("Aranan kelime Bulunamadı.");
    }
  }
}
