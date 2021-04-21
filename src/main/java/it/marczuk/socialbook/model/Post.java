package it.marczuk.socialbook.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.marczuk.socialbook.model.enums.Category;
import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Category category;

    private String title;

    @Column(length = 2000)
    private String content;

    @OneToMany(mappedBy = "post")
    @Cascade(value = org.hibernate.annotations.CascadeType.ALL)
    private List<Like> like;

    private Integer likesCount;

    private LocalDateTime createDate;

    @ManyToOne
    @JsonIgnore
    private User user;
}
