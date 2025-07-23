package gift.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "product")
public class Product extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 15)
    private String name;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false, name = "image_url")
    private String imageUrl;

    @Column(nullable = false)
    private Boolean approved;

    private String description;

    protected Product() {}

    public Product(String name,
                   Long price,
                   String imageUrl,
                   Boolean approved,
                   String description) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.approved = approved;
        this.description = description;
    }

    public void updateProduct(String name, Long price, String imageUrl, Boolean approved, String description) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.approved = approved;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean getApproved() { return approved; }

    public String getDescription() { return description; }
}
