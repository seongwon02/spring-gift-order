package gift.entity;

import gift.exception.product.option.SoldOutException;
import jakarta.persistence.*;

@Entity
@Table(name = "product_option")
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private Long quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    protected Option() {}

    public Option(String name, Long quantity, Product product) {

        this.name = name;
        this.quantity = quantity;
        this.product = product;
    }

    public void substract(int quantity) {
        long currentQuantity = this.quantity - quantity;
        if (currentQuantity < 0){
            throw new SoldOutException("해당 상품의 재고가 부족합니다.");
        }

        this.quantity = currentQuantity;
    }

    public void update(String name, Long quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
