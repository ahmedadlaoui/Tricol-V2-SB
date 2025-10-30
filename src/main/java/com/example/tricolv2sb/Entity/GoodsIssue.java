package com.example.tricolv2sb.Entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "goods_issues")
@Data
public class GoodsIssue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String issueNumber;
    
    @Column(nullable = false)
    private LocalDate issueDate;
    
    @Column(nullable = false)
    private String destination;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GoodsIssueMotif motif;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GoodsIssueStatus status;
    
    @OneToMany(mappedBy = "goodsIssue", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<GoodsIssueLine> issueLines;
}