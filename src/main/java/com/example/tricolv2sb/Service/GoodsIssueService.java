package com.example.tricolv2sb.Service;

import com.example.tricolv2sb.DTO.CreateGoodsIssueDTO;
import com.example.tricolv2sb.DTO.ReadGoodsIssueDTO;
import com.example.tricolv2sb.DTO.UpdateGoodsIssueDTO;
import com.example.tricolv2sb.Entity.GoodsIssue;
import com.example.tricolv2sb.Entity.Enum.GoodsIssueStatus;
import com.example.tricolv2sb.Exception.GoodsIssueNotFoundException;
import com.example.tricolv2sb.Mapper.GoodsIssueMapper;
import com.example.tricolv2sb.Repository.GoodsIssueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GoodsIssueService {

    private final GoodsIssueRepository goodsIssueRepository;
    private final GoodsIssueMapper goodsIssueMapper;

    @Transactional(readOnly = true)
    public List<ReadGoodsIssueDTO> fetchAllGoodsIssues() {
        List<GoodsIssue> goodsIssues = goodsIssueRepository.findAll();
        return goodsIssues.stream()
                .map(goodsIssueMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<ReadGoodsIssueDTO> fetchGoodsIssueById(Long id) {
        return Optional.of(
                goodsIssueRepository.findById(id)
                        .map(goodsIssueMapper::toDto)
                        .orElseThrow(() -> new GoodsIssueNotFoundException(
                                "Goods issue with ID " + id + " not found")));
    }

    @Transactional
    public ReadGoodsIssueDTO createGoodsIssue(CreateGoodsIssueDTO dto) {
        GoodsIssue goodsIssue = goodsIssueMapper.toEntity(dto);

        // Generate unique issue number
        String issueNumber = generateIssueNumber();
        goodsIssue.setIssueNumber(issueNumber);

        // Set initial status to DRAFT
        goodsIssue.setStatus(GoodsIssueStatus.DRAFT);

        GoodsIssue savedGoodsIssue = goodsIssueRepository.save(goodsIssue);
        return goodsIssueMapper.toDto(savedGoodsIssue);
    }

    @Transactional
    public ReadGoodsIssueDTO updateGoodsIssue(Long id, UpdateGoodsIssueDTO dto) {
        GoodsIssue existingGoodsIssue = goodsIssueRepository.findById(id)
                .orElseThrow(() -> new GoodsIssueNotFoundException(
                        "Goods issue with ID " + id + " not found"));

        // Only allow updates if status is DRAFT
        if (existingGoodsIssue.getStatus() != GoodsIssueStatus.DRAFT) {
            throw new IllegalStateException(
                    "Cannot update goods issue with status " + existingGoodsIssue.getStatus());
        }

        goodsIssueMapper.updateFromDto(dto, existingGoodsIssue);
        GoodsIssue savedGoodsIssue = goodsIssueRepository.save(existingGoodsIssue);
        return goodsIssueMapper.toDto(savedGoodsIssue);
    }

    @Transactional
    public void deleteGoodsIssue(Long id) {
        GoodsIssue goodsIssue = goodsIssueRepository.findById(id)
                .orElseThrow(() -> new GoodsIssueNotFoundException(
                        "Goods issue with ID " + id + " not found"));

        // Only allow deletion if status is DRAFT
        if (goodsIssue.getStatus() != GoodsIssueStatus.DRAFT) {
            throw new IllegalStateException(
                    "Cannot delete goods issue with status " + goodsIssue.getStatus() +
                            ". Only DRAFT goods issues can be deleted.");
        }

        goodsIssueRepository.deleteById(id);
    }

    @Transactional
    public void validateGoodsIssue(Long id) {
        GoodsIssue goodsIssue = goodsIssueRepository.findById(id)
                .orElseThrow(() -> new GoodsIssueNotFoundException(
                        "Goods issue with ID " + id + " not found"));

        if (goodsIssue.getStatus() != GoodsIssueStatus.DRAFT) {
            throw new IllegalStateException(
                    "Only DRAFT goods issues can be validated");
        }

        // Change status to VALIDATED
        goodsIssue.setStatus(GoodsIssueStatus.VALIDATED);
        goodsIssueRepository.save(goodsIssue);

        // Note: Stock movements will be created automatically via the StockMovement
        // logic
    }

    @Transactional
    public void cancelGoodsIssue(Long id) {
        GoodsIssue goodsIssue = goodsIssueRepository.findById(id)
                .orElseThrow(() -> new GoodsIssueNotFoundException(
                        "Goods issue with ID " + id + " not found"));

        if (goodsIssue.getStatus() == GoodsIssueStatus.CANCELLED) {
            throw new IllegalStateException(
                    "Goods issue is already cancelled");
        }

        goodsIssue.setStatus(GoodsIssueStatus.CANCELLED);
        goodsIssueRepository.save(goodsIssue);
    }

    /**
     * Generate a unique issue number
     * Format: GI-YYYYMMDD-XXX
     */
    private String generateIssueNumber() {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long count = goodsIssueRepository.count() + 1;
        return String.format("GI-%s-%03d", date, count);
    }
}
