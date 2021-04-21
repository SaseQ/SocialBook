package it.marczuk.socialbook.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "users_bios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Biography {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bio;

    @OneToOne
    @JsonIgnore
    private User user;

    public Biography(String bio, User user) {
        this.bio = bio;
        this.user = user;
    }
}
