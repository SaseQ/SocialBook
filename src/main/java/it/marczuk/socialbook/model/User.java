package it.marczuk.socialbook.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.marczuk.socialbook.model.enums.Gender;
import it.marczuk.socialbook.model.enums.Role;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Column(unique=true)
    private String email;

    @JsonIgnore
    private String password;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birtdate;

    private Gender gender;

    private String userImage;

    @OneToOne(mappedBy = "user")
    @Cascade(value = org.hibernate.annotations.CascadeType.ALL)
    private Biography biography;

    private LocalDateTime createAccountDate;

    private Role role;

    private boolean accountNonLocked;

    private boolean isEnabled;

    @OneToMany(mappedBy = "user")
    @Cascade(value = org.hibernate.annotations.CascadeType.ALL)
    private List<Post> posts;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role.toString()));
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }


}
