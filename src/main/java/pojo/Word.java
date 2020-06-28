package pojo;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

@Entity
@Table(name = "dictionary")
public class Word {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE,
            generator = "dictionary_id_Sequence")
    @SequenceGenerator(name = "dictionary_id_Sequence",
            sequenceName = "dictionary_sequence",
            initialValue = 0,
            allocationSize = 1)
    private int ID;

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
