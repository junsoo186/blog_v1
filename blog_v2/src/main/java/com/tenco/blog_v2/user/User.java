package com.tenco.blog_v2.user;

import com.tenco.blog_v2.board.Board;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
@Table(name="user_tb")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique=true) // 유니크 제약 조건 설정
    private String username;
    private String password;
    private String Email;
    
    @CreationTimestamp // 엔티티 생성시 자동으로 현재 시간 입력
    private Timestamp createdAt;

    // 단방향, 양방향 매핑(mappedBy)
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY) // 지연 로딩 설정
   //@OneToMany(mappedBy = "user", fetch = FetchType.EAGER) // 즉시 로딩 설정
    private List<Board> boards;

    public User(Integer id, String username, String password, String Email, Timestamp createdAt, List<Board> boards) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.Email = Email;
        this.createdAt = createdAt;
        this.boards = boards;
    }
}
