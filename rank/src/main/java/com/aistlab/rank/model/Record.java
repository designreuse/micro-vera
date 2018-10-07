package com.aistlab.rank.model;

import com.vera.shared.model.Rest;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity(name = "ms_rank_record")
@Data
public class Record {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private Long userId;

    private Float score;

    @ElementCollection
    private List<String> evidences;

    private LocalDate createTime;

    private LocalDate updateTime;

    public static void main(String[] args) {
        Rest rest = Rest.create();
    }
}
