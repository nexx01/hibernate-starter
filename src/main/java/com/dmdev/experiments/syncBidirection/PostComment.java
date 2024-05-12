package com.dmdev.experiments.syncBidirection;

import com.dmdev.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity(name="PostComment")
@Table(name="post_comment")

@Getter
@Setter
public class PostComment implements BaseEntity<Long> {

    @Id
    @GeneratedValue
    private Long id;

    private String review;

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "post_id")
    private Post post;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        PostComment that = (PostComment) o;

        return id != null && id.equals(((PostComment) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
