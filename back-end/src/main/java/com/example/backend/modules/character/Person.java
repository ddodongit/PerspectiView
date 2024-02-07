package com.example.backend.modules.character;

import com.example.backend.modules.product.Product;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;


@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String characterName;

    @Column(nullable = true)
    private String characterImageUrl;

    @Column(nullable = true)
    private String characterDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(mappedBy = "person")
    private Set<PersonKeyword> personKeywords = new HashSet<>();

    @Column(nullable = true)
    private double positionX;

    @Column(nullable = true)
    private double positionY;

    @Builder
    public Person(String characterName, Product product) {
        this.characterName = characterName;
        this.product = product;
    }

    @Builder(builderMethodName = "positionBuilder")
    public Person(String characterName, String characterDetail, double positionX, double positionY) {
        this.characterName = characterName;


        this.characterDetail = characterDetail;
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public void changeCharacter(Person person) {
        this.characterName = person.getCharacterName();
        this.characterImageUrl = person.getCharacterImageUrl();
        this.characterDetail = person.getCharacterDetail();
        this.positionX = person.getPositionX();
        this.positionY = person.getPositionY();
    }

    public void addKeyword(PersonKeyword personKeyword) {
        this.personKeywords.add(personKeyword);
    }

    public void removeKeywords(PersonKeyword personKeyword) {
        personKeywords.remove(personKeyword);
    }
}
