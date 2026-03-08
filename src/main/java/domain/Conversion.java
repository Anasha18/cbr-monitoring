package domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "conversions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Conversion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "result_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal resultAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_from_id")
    @ToString.Exclude
    private Currency currencyFrom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_to_id")
    @ToString.Exclude
    private Currency currencyTo;
}
