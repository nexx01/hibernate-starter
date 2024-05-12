package com.dmdev.experiments.syncBidirection;

import com.dmdev.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Post")
@Table(name = "post")
@Setter
@Getter
public class Post implements BaseEntity<Long> {

    @Id
    @GeneratedValue
    private Long id;

    private String title;


    @OneToMany
            (mappedBy = "post",
                    cascade = CascadeType.ALL,
                    orphanRemoval = true)
    private List<PostComment> comments = new ArrayList<>();


    public void asddComment(PostComment comment) {
        comments.add(comment);
        comment.setPost(this);
    }

    public void removeComment(PostComment comment) {
        comments.remove(comment);
        comment.setPost(null);
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<PostComment> getComments() {
        return comments;
    }

    public void setComments(List<PostComment> comments) {
        this.comments = comments;
    }
}
