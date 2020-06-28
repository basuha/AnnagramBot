package pojo;

import javax.persistence.*;

@Entity
@Table(name = "dictionary")
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int wordID;

    private String value;

    public Word(String value) {
        this.value = value;
    }
}
