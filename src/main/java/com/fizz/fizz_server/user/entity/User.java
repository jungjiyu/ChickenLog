package com.fizz.fizz_server.user.entity;

import com.fizz.fizz_server.memo.entity.Memo;
import com.fizz.fizz_server.user.dto.request.UserRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Memo> memos = new ArrayList<>();

    public void update(UserRequestDto dto) {
        if (dto.getUsername() != null) {
            this.username = dto.getUsername();
        }
        if (dto.getPassword() != null) {
            this.password = dto.getPassword();
        }
    }

}
