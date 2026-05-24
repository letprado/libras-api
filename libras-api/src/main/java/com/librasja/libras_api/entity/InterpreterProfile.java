package com.librasja.libras_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "interpreter_profile")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterpreterProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User interpreter;

    @Column(nullable = false, length = 255)
    private String especialidades;

    @Column(name = "descricao_curta", length = 500)
    private String descricaoCurta;

    @Column(length = 50)
    private String disponivel;
}
