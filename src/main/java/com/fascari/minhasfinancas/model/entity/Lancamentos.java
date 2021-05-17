package com.fascari.minhasfinancas.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import javax.persistence.*;

import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import com.fascari.minhasfinancas.model.enums.StatusLancamento;
import com.fascari.minhasfinancas.model.enums.TipoLancamento;

@Entity
@Table(name = "lancamentos", schema = "mf_owner")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Lancamentos {
    @Column(name = "ano")
    private Integer ano;

    @Column(name = "data_cadastro")
    @Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
    private LocalDate dataCadastro;

    @Column(name = "descricao")
    private String descricao;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "mes")
    private Integer mes;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private StatusLancamento status;

    @Column(name = "tipo")
    @Enumerated(value = EnumType.STRING)
    private TipoLancamento tipo;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuarios usuarios;

    @Column(name = "valor")
    private BigDecimal valor;

}