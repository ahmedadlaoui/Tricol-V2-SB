package com.example.tricolv2sb.Repository;

import com.example.tricolv2sb.Entity.GoodsIssueLine;
import com.example.tricolv2sb.Entity.GoodsIssue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsIssueLineRepository extends JpaRepository<GoodsIssueLine, Long> {
    
    List<GoodsIssueLine> findByGoodsIssue(GoodsIssue goodsIssue);
}