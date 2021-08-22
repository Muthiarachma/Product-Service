package com.MiniProjek.models.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "tbl_product")
@Data
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message="Name harus diisi")
    @Column(name = "product_name", length = 100)
    private String name;

    @NotEmpty(message = "Description harus diisi")
    @Column(name = "product_desc", length = 500)
    private String description;

    private Double price;

    @ManyToOne
    private Category category;


}
