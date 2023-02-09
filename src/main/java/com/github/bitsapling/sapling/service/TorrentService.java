package com.github.bitsapling.sapling.service;

import com.github.bitsapling.sapling.controller.torrent.dto.request.SearchTorrentRequestDTO;
import com.github.bitsapling.sapling.entity.Category;
import com.github.bitsapling.sapling.entity.Torrent;
import com.github.bitsapling.sapling.repository.TorrentRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TorrentService {
    @Autowired
    private TorrentRepository torrentRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private EntityManager entityManager;

    @Nullable
    public Torrent getTorrent(long id) {
        return torrentRepository.findById(id).orElse(null);
    }

    @Nullable
    public Torrent getTorrent(@NotNull String infoHash) {
        infoHash = infoHash.toLowerCase();
        Optional<Torrent> entity = torrentRepository.findByInfoHashIgnoreCase(infoHash);
        return entity.orElse(null);
    }

    public List<Torrent> getAllTorrents() {
        return new ArrayList<>(torrentRepository.findAll());
    }

    @Nullable
    public List<Torrent> getTorrentWithCategory(@Nullable String categorySlug) {
        List<Torrent> torrents = new ArrayList<>();
        if (categorySlug == null) {
            torrents.addAll(torrentRepository.findAll());
        } else {
            Category category = categoryService.getCategory(categorySlug);
            if (category != null) {
                torrents.addAll(torrentRepository.findAllByCategory(category));
            }
        }
        return torrents;
    }

    @NotNull
    public Torrent save(@NotNull Torrent torrent) {
        torrent.setInfoHash(torrent.getInfoHash());
        return torrentRepository.save(torrent);
    }

    @NotNull
    public Page<Torrent> search(@NotNull SearchTorrentRequestDTO searchRequestDTO) {
        return search(searchRequestDTO.getKeyword(),
                searchRequestDTO.getCategory(),
                searchRequestDTO.getPromotion(),
                searchRequestDTO.getTag(),
                Pageable.ofSize(searchRequestDTO.getEntriesPerPage()).withPage(searchRequestDTO.getPage()));
    }

    @NotNull
    public Page<Torrent> search(@NotNull String keyword, @NotNull List<String> categoriesRequired, @NotNull List<String> promotionRequired, @NotNull List<String> tagRequired, @NotNull Pageable pageable) {
        List<Long> categoriesRequiredId = new ArrayList<>();
        List<Long> promotionRequiredId = new ArrayList<>();
        List<Long> tagRequiredId = new ArrayList<>();
        for (String categorySlug : categoriesRequired) {
            Category category = categoryService.getCategory(categorySlug);
            if (category != null) {
                categoriesRequiredId.add(category.getId());
            }
        }
        for (String promotionSlug : promotionRequired) {
            Category promotion = categoryService.getCategory(promotionSlug);
            if (promotion != null) {
                promotionRequiredId.add(promotion.getId());
            }
        }
        for (String tagSlug : tagRequired) {
            Category tag = categoryService.getCategory(tagSlug);
            if (tag != null) {
                tagRequiredId.add(tag.getId());
            }
        }
        return torrentRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!keyword.isEmpty()) {
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(root.get("title"), "%" + keyword + "%"),
                        criteriaBuilder.like(root.get("subTitle"), "%" + keyword + "%")
                ));
            }
            if (!categoriesRequiredId.isEmpty()) {
                predicates.add(root.get("category").in(categoriesRequiredId));
            }
            if (!promotionRequired.isEmpty()) {
                predicates.add(root.get("promotion").in(promotionRequiredId));
            }
            if (!tagRequired.isEmpty()) {
                predicates.add(root.get("tag").in(tagRequiredId));
            }
            query.orderBy(criteriaBuilder.desc(root.get("id")));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }, pageable);

    }

}
