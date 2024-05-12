package com.dmdev.experiments.syncBidirection;

import com.dmdev.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Post2")
@Table(name = "post2")
@Setter
@Getter
public class Post2 implements BaseEntity<Long> {

    @Id
    @GeneratedValue
    private Long id;

    private String title;


    @OneToOne
            (mappedBy = "post2",
                    cascade = CascadeType.ALL,
                    orphanRemoval = true, fetch = FetchType.LAZY)
    private PostDetails details;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDetails(PostDetails details) {
        if (details == null) {
            if (this.details != null) {
                this.details.setPost2(null);
            }
        } else {
            details.setPost2(this);
        }
        this.details = details;
    }

}
