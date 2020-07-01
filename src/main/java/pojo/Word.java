package pojo;

import javax.persistence.*;

@Entity
@Table(name = "dictionary", schema = "public")
public class Word {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy=GenerationType.SEQUENCE,
            generator = "dictionary_id_Sequence")
    @SequenceGenerator(name = "dictionary_id_Sequence",
            sequenceName = "dictionary_sequence",
            allocationSize = 1)
    private int ID;

    @Column(name = "value")
    private String value;

    public Word() {}

    public Word(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
