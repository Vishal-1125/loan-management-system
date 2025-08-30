 	package com.twinline.loan_management_system.entity;

 	import jakarta.persistence.*;
 	import lombok.*;

 	@Entity
 	@Table(name = "role")
 	@Data
 	@NoArgsConstructor
 	@AllArgsConstructor
 	@Builder
 	public class Role {

 	    @Id
 	    @GeneratedValue(strategy = GenerationType.IDENTITY)
 	    private Long roleId;

 	    @Column(nullable = false, unique = true)
 	    private String roleName;

 	    private String description;
 	}
