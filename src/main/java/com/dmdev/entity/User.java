package com.dmdev.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

import static com.dmdev.util.StringUtils.SPACE;



@NamedEntityGraph(
        name="withCompanyAndChat",
        attributeNodes = {
                @NamedAttributeNode("company"),
                @NamedAttributeNode(value = "userChats",subgraph = "chats")
        },
        subgraphs = {
                @NamedSubgraph(name = "chats",attributeNodes = @NamedAttributeNode("chat"))
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "username")
@ToString(exclude = {"company", "userChats", "payments"})
@Builder
@Entity
@Table(name = "users", schema = "public")
@TypeDef(name = "dmdev", typeClass = JsonBinaryType.class)
@FetchProfile(name = "withCompanyAndPayments", fetchOverrides = {
        @FetchProfile.FetchOverride(
                entity = User.class,association = "company",mode=FetchMode.JOIN
        ),
        @FetchProfile.FetchOverride(
                entity = User.class,association = "payments",mode=FetchMode.JOIN
        )
})
@Audited
public class User implements Comparable<User>, BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotAudited
    @AttributeOverride(name = "birthDate", column = @Column(name = "birth_date"))
    private PersonalInfo personalInfo;

    @Column(unique = true)
    private String username;

    @Type(type = "dmdev")
    private String info;

    @NotAudited
    @Enumerated(EnumType.STRING)
    private Role role;

//    @NotAudited
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id") // company_id
    private Company company;
//
//    @OneToOne(
//            mappedBy = "user",
//            cascade = CascadeType.ALL,
//            fetch = FetchType.LAZY
//    )
//    @NotAudited
//    private Profile profile;

    @Builder.Default
    @OneToMany(mappedBy = "user")
    @NotAudited
    private List<UserChat> userChats = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "receiver")
//    @Fetch(FetchMode.SUBSELECT)
    @NotAudited
    private List<Payment> payments = new ArrayList<>();

    @Override
    public int compareTo(User o) {
        return username.compareTo(o.username);
    }

    public String fullName() {
        return getPersonalInfo().getFirstname() + SPACE + getPersonalInfo().getLastname();
    }
}












