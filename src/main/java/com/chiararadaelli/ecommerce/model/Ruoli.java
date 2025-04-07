package com.chiararadaelli.ecommerce.model;

   
import lombok.Data;

import jakarta.persistence.*;

@Data
@Entity
public class Ruoli {



    @Id
    @GeneratedValue(strategy= GenerationType.AUTO,generator="native")
    private Long roleId;

    private String roleName;

}



