package de.unigoettingen.sub.mongomapper.helper.tei;

import com.mongodb.BasicDBObject;

/**
 * Created by jpanzer on 21.01.14.
 */
public class Paragraph {
    private String id;
    private StringBuffer words = new StringBuffer();

    public Paragraph() {
    }

    public Paragraph(String id) {
        this.id = id;
    }


//    public void setId(String id) {
//        this.id = id;
//    }

    public void addWord(String word) {

        if (word.matches("([\\.,;:?!)])") && words.length()>0)
            words.insert(words.length() - 1, word + " ");
        else if (word.matches("([\\(])"))
            words.append(word);
        else
            words.append(word + " ");
    }


    public String getId() {
        return id;
    }

    public String getWords() {
        return words.toString();
    }

    public BasicDBObject getParagraphAsBasicDBObject() {
        return new BasicDBObject(this.getId(), this.getWords());
    }

    public String toString() {
        return "Paragraph with id: " + this.id;
    }
}
